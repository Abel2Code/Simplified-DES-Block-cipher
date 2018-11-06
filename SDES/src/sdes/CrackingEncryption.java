package sdes;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class CrackingEncryption {
    
    public static void main(String[] args) {
        
        problem1();
        problem2();        
        
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
        byte[] msg1Bytes = parseFile(msg1);
        
        //String msg2 = "../msg2.txt";
        //byte[] msg2Bytes = parseFile(msg2);
        
        //printArrByRows(msg1Bytes, 64);
        //printArrByRows(msg2Bytes, 64);
        
        byte[] plainSect;
        byte[] section = new byte[8];
        byte[] ciphertext = new byte[msg1Bytes.length];
               
        byte[][] keys = permutations(10);
        
        ArrayList<String> deciph = new ArrayList<>();
                
        for ( byte[] key : keys ) {
            for ( int i = 0; i < ciphertext.length; i+=0 ) {            
                for (int x = 0; x < 8; x++, i++) {
                    section[x] = msg1Bytes[i];
                }
                plainSect = SDES.Decrypt(key, section);
                for (int x = 0, y = i-8; x < 8; x++, y++) {
                    ciphertext[y] = plainSect[x];
                }
            }
            
            deciph.add(CASCII.toString(ciphertext));
            
            /*
            
            //if (CASCII.toString(ciphertext).indexOf("D") > 0) {
                String str = CASCII.toString(ciphertext).substring(0,10);                
                
                System.out.print(str + " : ");
                                
                for ( int x = 0; x < 5; x++ ) {
                    System.out.print(ciphertext[x]);
                }
                System.out.print(" | ");
                
                for ( int i = 0; i < str.length(); i++ ) {
                    System.out.print(CASCII.Convert(str.charAt(i)) + ", ");
                }
                System.out.println();
            //}
            */
           
            
        } 
        quickSort(deciph);
        for ( String s : deciph ) {
            System.out.println(s + "\n");
        }
                
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
        
    private static byte[][] permutations(int num) {
        ArrayList<String> strs = new ArrayList<>();
        permutation(num, "", strs);
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
    
    public static <E extends Comparable<E>> void quickSort(ArrayList<E> list) {
        quickSort(list, 0, list.size()-1);
    }
    
    private static <E extends Comparable<E>> void quickSort(ArrayList<E> list,int low, int high) {
        if(low < high) {
            int p = partitian(list, low, high);
            quickSort(list, low, p-1);
            quickSort(list, p+1, high);
        }
    }
    
    private static <E extends Comparable<E>> int partitian(ArrayList<E> list,int low, int high) {
        E pivot = list.get(high);
        int i = low - 1;
        
        for(int j = low; j < high; j++) {
            E temp1 = list.get(j);
            if(temp1.compareTo(pivot) < 0 || temp1.equals(pivot)) {
                i++;
                list.set(j, list.get(i));
                list.set(i, temp1);
            }
        }
        
        E temp2 = list.get(i+1);
        list.set(i+1, pivot);
        list.set(high, temp2);
        return i+1;
    }
}   