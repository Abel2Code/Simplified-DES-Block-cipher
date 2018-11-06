package sdes;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class CrackingEncryption {
    
    public static void main(String[] args) {
        
        problem1();
        problem2();
        
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
    
    public static void problem1() {
        System.out.println();
        byte[] key = {0,1,1,1,0,0,1,1,0,1};
        String word = "CRYPTOGRAPHY";
        byte[] plaintext = CASCII.Convert(word);
        
        byte[] ciphertext = new byte[plaintext.length];
        byte[] section = new byte[8];
        byte[] ciphSect;
        
        System.out.println("This is the plain text in CASCII.");
        System.out.println(word + "\n");
        
        System.out.println("This is the byte array of the plain text.");
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
        
        System.out.println("This is the byte array of the cipher text.");
        printByteArr(ciphertext);
        
        System.out.println("This is the cipher text in CASCII.");
        System.out.println(CASCII.toString(ciphertext) + "\n");

    }
    
    public static void problem2() {       
        String msg1 = "../msg1.txt";
        byte[] msg1Bytes = parseFile(msg1);
        String msg2 = "../msg2.txt";
        byte[] msg2Bytes = parseFile(msg2);
        
        printByteArr(msg1Bytes);
        printByteArr(msg2Bytes);
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
    
}