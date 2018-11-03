package sdes;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CrackingEncryption {
    
    public static void main(String[] args) {
                
        String word = "CRYPTOGRAPHY";
        
        byte[] encry = CASCII.Convert(word);
        
        System.out.println();
        for ( int i = 0; i < encry.length; i++ ) {
            if ( i == 32 )
                System.out.println();
            else if (i != 0 && i % 4 == 0 ) 
                System.out.print(" ");
            System.out.print(encry[i]);
        }
        System.out.println("\n");
        System.out.println(CASCII.toString(encry) + "\n");
        
        System.out.println();
        
    }
    
    public static byte[] parseFile(String path) {
        
        try {            
            File file = new File("path");
            
            Scanner reader = new Scanner(file);
            byte b;
            System.out.println();
                       
            while ( reader.hasNextByte()) {
                b = reader.nextByte();
                System.out.print(b);
            }
            System.out.println();
            
        } catch (FileNotFoundException ex) {
            System.out.println("\n ERROR: File Not Found \n");
        }
        
    }
    
}