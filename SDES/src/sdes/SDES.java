package sdes;

public class SDES {
	
	public static byte[] Encrypt(byte[] rawkey, byte[] plaintext) {
		byte[][] keys = generateKeys(rawkey);
		
		byte[] ciphertext = { plaintext[1], plaintext[5], plaintext[2], plaintext[0], plaintext[3], plaintext[7], plaintext[4], plaintext[6] };
		
		ciphertext = fk(ciphertext, keys[0]);
		
		ciphertext = switchMe(ciphertext);
		
		ciphertext = fk(ciphertext, keys[1]);
		
		byte[] result = { ciphertext[3], ciphertext[0], ciphertext[2], ciphertext[4], ciphertext[6], ciphertext[1], ciphertext[7], ciphertext[5] };
		return result;
	}
	
	public static byte[] Decrypt(byte[] rawkey, byte[] ciphertext) {
		byte[][] keys = generateKeys(rawkey);
		
		byte[] plaintext = { ciphertext[1], ciphertext[5], ciphertext[2], ciphertext[0], ciphertext[3], ciphertext[7], ciphertext[4], ciphertext[6] };

		plaintext = fk(plaintext, keys[1]);
		
		plaintext = switchMe(plaintext);
		
		plaintext = fk(plaintext, keys[0]);
		byte[] result = { plaintext[3], plaintext[0], plaintext[2], plaintext[4], plaintext[6], plaintext[1], plaintext[7], plaintext[5] };
		return result;
	}
	
	private static byte[][] generateKeys(byte[] rawKey){
		byte[] permutedKey = { rawKey[2], rawKey[4], rawKey[1], rawKey[6], rawKey[3], rawKey[9], rawKey[0], rawKey[8], rawKey[7], rawKey[5]};
		byte[][] keys = new byte[2][8];

		permutedKey = shiftLeft(permutedKey, 0, 5);
		permutedKey = shiftLeft(permutedKey, 5, 10);	
		
		byte[] key1 = { permutedKey[5], permutedKey[2], permutedKey[6], permutedKey[3], permutedKey[7], permutedKey[4], permutedKey[9], permutedKey[8] };
		keys[0] = key1;
		
		permutedKey = shiftLeft(permutedKey, 0, 5); permutedKey = shiftLeft(permutedKey, 0, 5);
		permutedKey = shiftLeft(permutedKey, 5, 10); permutedKey = shiftLeft(permutedKey, 5, 10);
		
		byte[] key2 = { permutedKey[5], permutedKey[2], permutedKey[6], permutedKey[3], permutedKey[7], permutedKey[4], permutedKey[9], permutedKey[8] };
		keys[1] = key2;
		
		return keys;
	}
	
	private static byte[] shiftLeft(byte[] key, int start, int end) { // End is exclusive
		byte[] newKey = new byte[key.length];
		for(int i = 0; i < start; i++){
			newKey[i] = key[i];
		}
		
		// Start Shift
		newKey[end - 1] = key[start];
		
		for(int i = start; i < end - 1; i++){
			newKey[i] = key[i+1];
		}
		
		// End Shift
		
		for(int i = end; i < key.length; i++){
			newKey[i] = key[i];
		}
		
		return newKey;
	}
	
	public static byte[] fk(byte[] plaintext, byte[] key){
		byte[] output = new byte[8];
		byte[] right = new byte[4];
		
		for(int i = 4; i < 8; i++){
			output[i] = plaintext[i];
			right[i-4] = plaintext[i];
		}
		
		byte[] toXOR = turnSK(right, key);
		
		for(int i = 0; i < 4; i++){
			// Set to XOR
			output[i] = (byte) (plaintext[i] == toXOR[i] ? 0 : 1);
		}	

		
		return output;
	}
	
	private static byte[] turnSK(byte[] right, byte[] key){
		// E/P
		byte[] expandRight = {right[3], right[0], right[1], right[2], right[1], right[2], right[3], right[0] };
		
		byte[] s0Diagram = {1,0,3,2,3,2,1,0,0,2,1,3,3,1,3,2};
		byte[] s1Diagram = {0,1,2,3,2,0,1,3,3,0,1,0,2,1,0,3};
		
		expandRight[0] = (byte) (key[0] == expandRight[0] ? 0 : 1);
		expandRight[1] = (byte) (key[1] == expandRight[1] ? 0 : 1);
		expandRight[2] = (byte) (key[2] == expandRight[2] ? 0 : 1);
		expandRight[3] = (byte) (key[3] == expandRight[3] ? 0 : 1);
		expandRight[4] = (byte) (key[4] == expandRight[4] ? 0 : 1);
		expandRight[5] = (byte) (key[5] == expandRight[5] ? 0 : 1);
		expandRight[6] = (byte) (key[6] == expandRight[6] ? 0 : 1);
		expandRight[7] = (byte) (key[7] == expandRight[7] ? 0 : 1);
		
		byte[] customBits = findSboxComputation(0, expandRight, s0Diagram);
		byte[] mandatoryBits = findSboxComputation(1, expandRight, s1Diagram);
		
		byte[] output = {customBits[1], mandatoryBits[1], mandatoryBits[0], customBits[0] };
		

		
		return output;
	}
	
	private static byte[] findSboxComputation(int row, byte[] pbox, byte[] diagram){
		int rowIndex;
		int columnIndex;
		if(row == 0){
			rowIndex = twoDigitBinaryToDecimal(pbox[0], pbox[3]);
			columnIndex = twoDigitBinaryToDecimal(pbox[1], pbox[2]);
		} else {
			rowIndex = twoDigitBinaryToDecimal(pbox[4], pbox[7]);
			columnIndex = twoDigitBinaryToDecimal(pbox[5], pbox[6]);
		}
		
		return decimalToTwoDigitBinary(diagram[rowIndex * 4 + columnIndex]);
	}
	
	private static byte[] decimalToTwoDigitBinary(byte decimal){
		byte[] binary = new byte[2];
		binary[0] = (byte) (decimal >= 2 ? 1: 0);
		binary[1] = (byte) (decimal % 2 == 1 ? 1 : 0);
		return binary;
	}
	
	private static int twoDigitBinaryToDecimal(byte b1, byte b2){
		return b1 * 2 + b2;
	}
	
	private static byte[] switchMe(byte[] text){
		byte[] switched = {text[4], text[5], text[6], text[7], text[0], text[1], text[2], text[3]};
		return switched;
	}
	
	private static void printByteArray(byte[] key) {
		for(int i = 0; i < key.length; i++){
			System.out.print(key[i] + " ");
		}
		System.out.println();
	}
	
	protected static String stringifyByteArray(byte[] key) {
		String arr = "";
		for(int i = 0; i < key.length; i++){
			if(i == key.length/2){
				arr += " ";
			}
			arr += key[i];
		}
		return arr;
	}
	
	public static void run() {
		byte[] rawkey1 = {0,0,0,0,0,0,0,0,0,0};
		byte[] plaintext1 = {1, 0, 1, 0, 1, 0, 1, 0};
		
		byte[] rawkey2 = {1, 1, 1, 0, 0, 0, 1, 1, 1, 0};
		byte[] plaintext2 = {1, 0, 1, 0, 1, 0, 1, 0};
		
		byte[] rawkey3  = {1, 1, 1, 0, 0, 0, 1, 1, 1, 0};
		byte[] plaintext3  = {0, 1, 0, 1, 0, 1, 0, 1};

		byte[] rawkey4  = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
		byte[] plaintext4  = {1, 0, 1, 0, 1, 0, 1, 0};

		byte[] rawkey5  = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		byte[] plaintext5  = {0, 0, 0, 0, 0, 0, 0, 0};

		byte[] rawkey6  = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
		byte[] plaintext6  = {1, 1, 1, 1, 1, 1, 1, 1};

		byte[] rawkey7  = {0, 0, 0, 0, 0, 1, 1, 1, 1, 1};
		byte[] plaintext7  = {0, 0, 0, 0, 0, 0, 0, 0};

		byte[] rawkey8  = {0, 0, 0, 0, 0, 1, 1, 1, 1, 1};
		byte[] plaintext8  = {1, 1, 1, 1, 1, 1, 1, 1};
		
		byte[] rawkey9 = {1, 0, 0, 0, 1, 0, 1, 1, 1, 0};
		byte[] ciphertext9 = {0, 0, 0, 1, 1, 1, 0, 0};
		
		byte[] rawkey10 = {1, 0, 0, 0, 1, 0, 1, 1, 1, 0};
		byte[] ciphertext10 = {1, 1, 0, 0, 0, 0, 1, 0};

		byte[] rawkey11 = {0, 0, 1, 0, 0, 1, 1, 1, 1, 1};
		byte[] ciphertext11 = {1, 0, 0, 1, 1, 1, 0, 1};

		byte[] rawkey12 = {0, 0, 1, 0, 0, 1, 1, 1, 1, 1};
		byte[] ciphertext12 = {1, 0, 0, 1, 0, 0, 0, 0};
		
		System.out.println("Raw Key        Plaintext      Ciphertext      DecipheredText");
		System.out.printf ("%-14s %-14s %-14s %-14s\n", stringifyByteArray(rawkey1), stringifyByteArray(plaintext1), stringifyByteArray(Encrypt(rawkey1, plaintext1)), stringifyByteArray(Decrypt(rawkey1, Encrypt(rawkey1, plaintext1))));
		System.out.printf ("%-14s %-14s %-14s %-14s\n", stringifyByteArray(rawkey2), stringifyByteArray(plaintext2), stringifyByteArray(Encrypt(rawkey2, plaintext2)), stringifyByteArray(Decrypt(rawkey2, Encrypt(rawkey2, plaintext2))));
		System.out.printf ("%-14s %-14s %-14s %-14s\n", stringifyByteArray(rawkey3), stringifyByteArray(plaintext3), stringifyByteArray(Encrypt(rawkey3, plaintext3)), stringifyByteArray(Decrypt(rawkey3, Encrypt(rawkey3, plaintext3))));
		System.out.printf ("%-14s %-14s %-14s %-14s\n", stringifyByteArray(rawkey4), stringifyByteArray(plaintext4), stringifyByteArray(Encrypt(rawkey4, plaintext4)), stringifyByteArray(Decrypt(rawkey4, Encrypt(rawkey4, plaintext4))));
		System.out.println();
		System.out.printf ("%-14s %-14s %-14s %-14s\n", stringifyByteArray(rawkey5), stringifyByteArray(plaintext5), stringifyByteArray(Encrypt(rawkey5, plaintext5)), stringifyByteArray(Decrypt(rawkey5, Encrypt(rawkey5, plaintext5))));
		System.out.printf ("%-14s %-14s %-14s %-14s\n", stringifyByteArray(rawkey6), stringifyByteArray(plaintext6), stringifyByteArray(Encrypt(rawkey6, plaintext6)), stringifyByteArray(Decrypt(rawkey6, Encrypt(rawkey6, plaintext6))));
		System.out.printf ("%-14s %-14s %-14s %-14s\n", stringifyByteArray(rawkey7), stringifyByteArray(plaintext7), stringifyByteArray(Encrypt(rawkey7, plaintext7)), stringifyByteArray(Decrypt(rawkey7, Encrypt(rawkey7, plaintext7))));
		System.out.printf ("%-14s %-14s %-14s %-14s\n", stringifyByteArray(rawkey8), stringifyByteArray(plaintext8), stringifyByteArray(Encrypt(rawkey8, plaintext8)), stringifyByteArray(Decrypt(rawkey8, Encrypt(rawkey8, plaintext8))));
		System.out.println();
		System.out.printf ("%-14s %-14s %-14s\n", stringifyByteArray(rawkey9), stringifyByteArray(Decrypt(rawkey9, ciphertext9)), stringifyByteArray(ciphertext9));
		System.out.printf ("%-14s %-14s %-14s\n", stringifyByteArray(rawkey10), stringifyByteArray(Decrypt(rawkey10, ciphertext10)), stringifyByteArray(ciphertext10));
		System.out.printf ("%-14s %-14s %-14s\n", stringifyByteArray(rawkey11), stringifyByteArray(Decrypt(rawkey11, ciphertext11)), stringifyByteArray(ciphertext11));
		System.out.printf ("%-14s %-14s %-14s\n", stringifyByteArray(rawkey12), stringifyByteArray(Decrypt(rawkey12, ciphertext12)), stringifyByteArray(ciphertext12));


	}

}
