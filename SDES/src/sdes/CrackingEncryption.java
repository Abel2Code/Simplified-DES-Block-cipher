package sdes;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class CrackingEncryption {
    
    public static void main(String[] args) {        
        problem1();
        problem2();        
        problem3();
    }
    
    public static void problem1() {
        System.out.println();
        
        System.out.println(" - - - - - - - Problem 1 - - - - - - - - - \n");
        
        byte[] key = {0,1,1,1,0,0,1,1,0,1};
        String word = "CRYPTOGRAPHY";
        byte[] plaintext = CASCII.Convert(word);
        
        byte[] ciphertext = new byte[plaintext.length];
        byte[] section = new byte[8];
        byte[] ciphSect;
        
        System.out.println("This is the plain text in CASCII.");
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
    
    public static void problem2() {     
        
        System.out.println("\n - - - - - - Problem 2 - - - - - - - -");
        
        Show result = askPrompt();
        
        System.out.println(" * This may take a few seconds. Please wait. Thank you * \n");
        
        int answerPosition = 756;
        
        String msg1 = "../msg1.txt";
        byte[] ciphertext = parseFile(msg1);
        
        byte[][] keys = keyPermutations();
        
        ArrayList<String> deciph = new ArrayList<>();
        int attempt = 0;
        
        for ( byte[] key : keys ) {
            deciph.add( sdesDecryptWithKey(key, ciphertext, attempt) );
            attempt++;
        }
        
        performAction(deciph, answerPosition, result);
    }
    
    public static void problem3() {
        
        System.out.println("\n - - - - - - Problem 3 - - - - - - - -");
        
        Show result = askPrompt();
        
        int answerPosition = 922979;  // This value is hard coded because i spent the time to manually search the output for the decrypted message.
                              // It's used to simmplifiy the code, since it was alreaddy manually found.
        
        String msg2 = "../msg2.txt";
        byte[] ciphertext = parseFile(msg2);
        
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
    
    
    public static byte[] parseFile(String relPath) {
        
        try {            
            File file = new File(relPath);
                        
            Scanner reader = new Scanner(file);
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
            reader.close();
            return null;
            
        } catch (FileNotFoundException ex) {
            System.out.println("\nERROR: File Not Found \n");
        }
        return null;
    }

    
    private static enum Show { ALL, SECT, ANS, NONE };
    
    private static Show askPrompt() {
        String prompt = "What would you like to display for this problem.\n"
                      + "  Choose one of the following commands to determine which choice you decide. \n"
                      + "    all  - Finds all possible values for that CASCII String and prints all of them out\n"
                      + "    sect - Finds all possibilities for that CASCII String, but only prints a section, 5 above & 5 under the answer\n"
                      + "    ans  - Finds all possilities (same as above), but only prints out the answer instead\n"
                      + "    none - Finds all possibilities, but doesn't print anything" ;
        return sayAndAskPrompt(prompt);
    }
    
    private static Show sayAndAskPrompt(String prompt) {
        
        Scanner reader = new Scanner(System.in);
        boolean notAnswered = true;
        String input = null;
        
        try {
            do {
                System.out.println("\n" + prompt);
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
                        break;
                }
                
            } while ( notAnswered || input != null || input.length() > 0 );
            
            reader.close();
            
        } catch(NullPointerException e) {
            System.out.println("That was not a valid input. Please try again.");
        }
        
        return null;
    }
    
    private static void performAction(ArrayList<String> list, int ansPos, Show chosen) {
        
        switch (chosen) {
            
            case ALL:
                System.out.println("These are ALL the possible CASCII decode string.\n");
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
    
}   