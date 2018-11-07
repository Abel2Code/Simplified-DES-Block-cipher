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
        
        System.out.println(" - - - - - - Problem 2 - - - - - - - -\n");
        
        String msg1 = "../msg1.txt";
        byte[] ciphertext = parseFile(msg1);
                
        byte[] plainSect;
        byte[] section = new byte[8]; 
        byte[] plaintext = new byte[ciphertext.length];
        byte[][] keys = keyPermutations();
        
        ArrayList<String> deciph = new ArrayList<>();
        int attemptNum = 0;
        
        for ( byte[] key : keys ) {
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
            deciph.add(attemptNum + ". " + str);
            attemptNum++;
                                    
        }
        deciph.forEach((s) -> {
            System.out.println(s);
        });
        
        System.out.println("\nAfter producing all of the possiblities, one stood out in english. This is what we found: \n\n" + deciph.get(756) + "\n");
    }
    
    public static void problem3() {
        
        System.out.println(" - - - - - - Problem 3 - - - - - - - -\n");
        
        String msg2 = "../msg2.txt";
        byte[] ciphertext = parseFile(msg2);
        
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

}   