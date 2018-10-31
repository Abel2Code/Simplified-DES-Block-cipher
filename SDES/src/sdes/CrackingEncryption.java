package sdes;

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
                
    }
    
}