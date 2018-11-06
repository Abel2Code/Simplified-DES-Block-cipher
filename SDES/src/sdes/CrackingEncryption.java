package sdes;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

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

        String msg1 = "../msg1.txt";
        String msg2 = "../msg2.txt";
        
        byte[] msg1Bytes = parseFile(msg1);
        byte[] msg2Bytes = parseFile(msg2);
        
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