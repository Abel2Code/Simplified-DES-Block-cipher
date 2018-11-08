package sdes;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class CrackingEncryption {
    
    public static void main(String[] args) {
                
        String msg1, msg2;
        boolean isLocal = filePathPrompt();
        if ( isLocal ) {
            String[] paths = grabPaths();
            msg1 = paths[0];
            msg2 = paths[1];
        }
        else {
            msg1 = psuedoFileMsg1;
            msg2 = psuedoFileMsg2; 
        }
        
        problem1();
        problem2(isLocal, msg1);
        problem3(isLocal, msg2);
    }
    
    public static void problem1() {
        System.out.println();
        
        System.out.println( problemNum(1) );
        
        byte[] key = {0,1,1,1,0,0,1,1,0,1};
        String word = "CRYPTOGRAPHY";
        byte[] plaintext = CASCII.Convert(word);
        
        byte[] ciphertext = new byte[plaintext.length];
        byte[] section = new byte[8];
        byte[] ciphSect;
        
        System.out.println("\nThis is the plain text in CASCII.");
        System.out.println(word + "\n");
        
        System.out.print("This is the byte array of the plain text.");
        printByteArr(plaintext);
        
        for ( int i = 0; i < plaintext.length; i+=0 ) {            
            for (int x = 0; x < 8; x++, i++) {
                section[x] = plaintext[i];
            }
            ciphSect = SDES.Encrypt(key, section);
            for (int x = 0, y = i-8; x < 8; x++, y++) {
                ciphertext[y] = ciphSect[x];
            }
        }
        
        System.out.print("This is the byte array of the cipher text.");
        printByteArr(ciphertext);
        
        System.out.println("This is the cipher text in CASCII.");
        System.out.println(CASCII.toString(ciphertext) + "\n");
        
    }
    
    public static void problem2(boolean isLocal, String file) {
        
        System.out.println( problemNum(2) );
        
        Show result = askPrompt(1024);
        
        System.out.println(" * This may take a few seconds. Please wait. Thank you * \n");
        
        int answerPosition = 756;  // This value is hard coded because i spent the time to manually search the output for the decrypted message.
                                   // It's used to simmplifiy the code, since it was alreaddy manually found.
        byte[] ciphertext;
        
        if ( isLocal ) {
            ciphertext = parseFile(file);
        }
        else {
            ciphertext = psuedoParse(file);
        }
        
        byte[][] keys = keyPermutations();
        
        ArrayList<String> deciph = new ArrayList<>();
        int attempt = 0;
        
        for ( byte[] key : keys ) {
            deciph.add( sdesDecryptWithKey(key, ciphertext, attempt) );
            attempt++;
        }
        
        performAction(deciph, answerPosition, result);
    }
    
    public static void problem3(boolean isLocal, String file) {
        
        System.out.println( problemNum(3) );
        
        Show result = askPrompt(1024*1024);
        
        int answerPosition = 922979;  // This value is hard coded because i spent the time to manually search the output for the decrypted message.
                                      // It's used to simmplifiy the code, since it was alreaddy manually found.        
        byte[] ciphertext;
        
        if ( isLocal ) {
            ciphertext = parseFile(file);
        }
        else {
            ciphertext = psuedoParse(file);
        }
                
        byte[][] key1s = keyPermutations();
        byte[][] key2s = keyPermutations();
        
        ArrayList<String> deciph = new ArrayList<>();
        int attempt = 0;
        
        System.out.println(" * This may take a couple minutes. Please wait. Thank you * \n");
        
        for ( byte[] key1 : key1s ) {
            for ( byte[] key2 : key2s ) {
                deciph.add( tripledesDecryptWithKeys(key1, key2, ciphertext, attempt) );
                attempt++;                
            }            
        }
        
        performAction(deciph, answerPosition, result);        
    }
    
    private static String sdesDecryptWithKey(byte[] key, byte[] ciphertext, int num) {
        
        byte[] plainSect;
        byte[] section = new byte[8]; 
        byte[] plaintext = new byte[ciphertext.length];
        
        for ( int i = 0; i < plaintext.length; i+=0 ) {            
            for (int x = 0; x < 8; x++, i++) {
                section[x] = ciphertext[i];
            }
            plainSect = SDES.Decrypt(key, section);
            for (int x = 0, y = i-8; x < 8; x++, y++) {
                plaintext[y] = plainSect[x];
            }
        }
        
        String str = keyAndByteArrStr(key, plaintext);
        return num + ". " + str;
    }
    
    private static String tripledesDecryptWithKeys(byte[] key1, byte[] key2, byte[] ciphertext, int num) {
        
        byte[] plainSect;
        byte[] section = new byte[8]; 
        byte[] plaintext = new byte[ciphertext.length];
        
        for ( int i = 0; i < plaintext.length; i+=0 ) {            
            for (int x = 0; x < 8; x++, i++) {
                section[x] = ciphertext[i];
            }
            plainSect = TripleDES.Decrypt(key1, key2, section);
            for (int x = 0, y = i-8; x < 8; x++, y++) {
                plaintext[y] = plainSect[x];
            }
        }
        
        String str = keysAndByteArrStr(key1, key2, plaintext);
        return num + ". " + str;
    }
    
    
    private static byte[][] processPermutes(Object[] arr) {
        byte[][] perms = new byte[arr.length][10];
        String str, sub;
        byte b;
        for ( int i = 0; i < arr.length; i++ ) {
            str = (String) arr[i];
            for ( int j = 0; j < str.length(); j++ ) {
                sub = str.substring(j, j+1);
                b = Byte.parseByte(sub);
                perms[i][j] = b;
            }
        }
        return perms;
    }
        
    private static byte[][] keyPermutations() {
        ArrayList<String> strs = new ArrayList<>();
        permutation(10, "", strs);
        return processPermutes(strs.toArray());
    }

    private static void permutation(int num, String str, ArrayList<String> list) {
        if (num == 0) {
            list.add(str);
            return;
        }
        for (int i = 0; i <= 1; i++) {
            permutation(num - 1, str + Integer.toString(i), list);
        }
    }
    
    private static String keyAndByteArrStr(byte[] key, byte[] cascii) {
        StringBuilder sb = new StringBuilder();
        sb.append("key = { ");
        for ( int i = 0; i < key.length; i++ ) {
            if ( i != key.length - 1 )
                sb.append(key[i]).append(",");
            else
                sb.append(key[i]).append(" } ");
        }
        sb.append("-> ").append(CASCII.toString(cascii));
        
        return sb.toString();
    }
    
    private static String keysAndByteArrStr(byte[] key1, byte[] key2, byte[] cascii) {
        StringBuilder sb = new StringBuilder();
        sb.append("key 1 = { ");
        for ( int i = 0; i < key1.length; i++ ) {
            if ( i != key1.length - 1 )
                sb.append(key1[i]).append(",");
            else
                sb.append(key1[i]).append(" } ");
        }
        sb.append("key 2 = { ");
        for ( int i = 0; i < key2.length; i++ ) {
            if ( i != key2.length - 1 )
                sb.append(key2[i]).append(",");
            else
                sb.append(key2[i]).append(" } ");
        }
        sb.append("-> ").append(CASCII.toString(cascii));
        
        return sb.toString();
    }
    
    private static void printByteArr(byte[] arr) {
        printArrByRows(arr, 32);
    }
    
    private static void printArrByRows(byte[] arr, int rows) {
        System.out.println();
        int len = arr.length;
        for ( int i = 0; i < len; i++ ) {
            if ( i % rows == 0)
                System.out.println();
            else if (i != 0 && i % 4 == 0 ) 
                System.out.print(" ");
            System.out.print(arr[i]);
        }
        System.out.println("\n");
    }
    
    private static Show askPrompt(int possible) {
        String prompt = "What would you like to display for this problem.\n"
                      + "  Choose one of the following commands to determine which choice you decide. \n"
                      + "    all  - Finds all possible values for that CASCII String and prints all of them out. THERE ARE ( " + possible + " ) POSSIBILITIES. \n"
                      + "    sect - Finds all possibilities for that CASCII String, but only prints a section, 5 above & 5 under the answer\n"
                      + "    ans  - Finds all possilities (same as above), but only prints out the answer instead\n"
                      + "    none - Finds all possibilities, but doesn't print anything" ;
        return sayAndAskPrompt(prompt);
    }
    
    private static Show sayAndAskPrompt(String prompt) {
        
        Scanner reader = new Scanner(System.in);
        String input = null;        
        System.out.print("\n" + prompt);
        do {
            System.out.println();
            input = reader.nextLine();
            input = input.trim().toLowerCase();
             
            switch(input) {
                case "all":
                    return Show.ALL;
                case "sect":
                    return Show.SECT;
                case "ans":
                    return Show.ANS;
                case "none":
                    return Show.NONE;
                default:
                    System.out.print(">> Not a valid Input, Try again. <<");
                    break;
            }
                
        } while ( input != null || input.length() > 0 );
            
        reader.close();
        
        return null;
    }
    
    private static void performAction(ArrayList<String> list, int ansPos, Show chosen) {
        
        switch (chosen) {
            
            case ALL:
                System.out.println("These are ALL the possible CASCII decoded strings.\n");
                for ( int i = 0; i < list.size(); i++ ) {
                    if ( i != ansPos )
                        System.out.println("   "+list.get(i));
                    else
                        System.out.println("\n-> "+list.get(i)+"\n");
                }
                System.out.println("\nAfter producing all of the possiblities, one stood out in english. This is what we found: \n\n" + list.get(ansPos) + "\n");
                break;
                
            case SECT:
                System.out.println("This is the SECTION where the answer was located (for easy check-ablility):\n");                
                for (int i = ansPos-5; i < ansPos+5; i++) {
                    if ( i != ansPos )
                        System.out.println("   "+list.get(i));
                    else
                        System.out.println("\n-> "+list.get(i)+"\n");
                }
                System.out.println("\nAfter producing all of the possiblities, one stood out in english. This is what we found: \n\n" + list.get(ansPos) + "\n");
                break;
                
            case ANS:
                System.out.println("\nAfter producing all of the possiblities, one stood out in english. This is the ANSWER we found: \n\n" + list.get(ansPos) + "\n");
                break;
                
            case NONE:
                System.out.println("All the possibilities were found, you decided not to show any.\n");
                break;
                
            default:
                System.out.println("ERR: Something went wrong while deciding on acction. :/");
                break;
        }
        
    }
    
    private static String problemNum(int questionNum) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        sb.append("***************************************************************************\n");
        sb.append("*********                      Problem # " + questionNum + "                      ***********\n");
        sb.append("***************************************************************************");
        return sb.toString();
    }    
        
    public static byte[] parseFile(String relPath) {
        
        try {
            File file = new File(relPath);
                        
            try (Scanner reader = new Scanner(file)) {
                
                byte b;
                String line, sub;
                byte[] bytes;
                // This if statement is assuming that there is only one line, and
                // that all the bytes are written in that single line.
                if ( reader.hasNextLine()) {
                    line = reader.nextLine();
                    bytes = new byte[line.length()];
                    for ( int i = 0; i < line.length(); i++) {
                        sub = line.substring(i, i+1);
                        b = Byte.parseByte(sub);
                        bytes[i] = b;
                    }
                    return bytes;
                }
            }
            return null;
            
        } catch (FileNotFoundException ex) {
            System.out.println("\nERROR: File Not Found \n");
        }
        return null;
    }
    
    private static byte[] psuedoParse(String file) {
        byte b;
        String sub;
        byte[] bytes;
        // This if statement is assuming that there is only one line, and
        // that all the bytes are written in that single line.
        
        bytes = new byte[file.length()];
        for ( int i = 0; i < file.length(); i++) {
            sub = file.substring(i, i+1);
            b = Byte.parseByte(sub);
            bytes[i] = b;
        }
        return bytes;
    }
    
    
    
    private static String[] grabPaths() {
        Scanner sc = new Scanner(System.in);        
        String[] paths = new String[2];
        
        System.out.println("\nWhat is the file path for the msg1.txt file that will be used ?");
        String path1 = sc.nextLine();  
        paths[0] = path1;
        
        System.out.println("\nWhat is the file path for the msg2.txt file that will be used ?");
        String path2 = sc.nextLine();  
        paths[1] = path2;
        
        sc.close();
        
        return paths;
    }
    
    private static boolean filePathPrompt() {
        System.out.println("\n * Before we begin, if you want to use a file that is on your local machine then you"
                       + "\n     are going to have to provide the file paths for both the msg1 and msg2 file.");
        System.out.println(" * Also, the data from each file (msg1, msg2) is already stored in this program"
                         + "\n     for easy access, and no path is required.");
        System.out.println("\n * Do you wish to continue with your own files (file path required) from your local machine"
                         + "\n   or from the msg1 and ms2 file data stored in this program. (it is the exact same data as the files provided on CSNS) ?\n");
        System.out.println("    Type in one of the following choices: ");
        System.out.println("      local  -  I want to use my own ms1 and ms2 files from my local machine (Requires both file paths)\n"
                         + "      stored -  I want to use the data from msg1 and ms2 that is already stored in this program (Exact same data as msg1.txt and ms2.txt from CSNS)");
        Scanner sc = new Scanner(System.in);
        String isLocal;
        do {
            isLocal = sc.nextLine().trim().toLowerCase();
            switch (isLocal) {
                case "local":
                    return true;
                case "stored":
                    return false;
                default:
                    System.out.println(">> Not a valid Input, Try again. <<");
                    break;
            }
        } while ( isLocal != null || isLocal.length() > 0 );
        sc.close();
        return false;
    }
    
    private static enum Show { ALL, SECT, ANS, NONE };
    
    private static final String psuedoFileMsg1 = "1011011001111001001011101111110000111110100000000001110111010001111011111101101100010011000000101101011010101000101111100011101011010111"
            + "100011101001010111101100101110000010010101110001110111011111010101010100001100011000011010101111011111010011110111001001011100101101001000011011111011000010010001011"
            + "101100011011110000000110010111111010000011100011111111000010111010100001100001010011001010101010000110101101111111010010110001001000001111000000011110000011110110010"
            + "010101010100001000011010000100011010101100000010111000000010101110100001000111010010010101110111010010111100011111010101111011101111000101001010001101100101100111001"
            + "110111001100101100011111001100000110100001001100010000100011100000000001001010011101011100101000111011100010001111101011111100000010111110101010000000100110110111111"
            + "000000111110111010100110000010110000111010001111000101011111101011101101010010100010111100011100000001010101110111111101101100101010011100111011110101011011";
    
    private static final String psuedoFileMsg2 = "0001111110011111111001111110110011100000001100101111001010101011000101110100110100000011001101011111111000000000101011111100000101001011"
            + "100111100101010110000011011110001111110101110010010001010100001100110010100000010111101100001001101011110001000100100010000111110010000000100000000110110100000000101"
            + "0111010000001000010011100101111001101111011001001010001100010100000";
}   