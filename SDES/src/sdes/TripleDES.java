package sdes;

public class TripleDES {

    public static byte[] Encrypt(byte[] rawkey1, byte[] rawkey2, byte[] plaintext) {

        byte[] inner, outer, last;
        inner = SDES.Encrypt(rawkey1, plaintext);
        outer = SDES.Decrypt(rawkey2, inner);
        last = SDES.Encrypt(rawkey1, outer);
        return last;

    }

    public static byte[] Decrypt(byte[] rawkey1, byte[] rawkey2, byte[] ciphertext) {

        byte[] inner, outer, last;
        inner = SDES.Decrypt(rawkey1, ciphertext);
        outer = SDES.Encrypt(rawkey2, inner);
        last = SDES.Decrypt(rawkey1, outer);
        return last;

    }
    
    public static void main(String[] args) {
        
        byte[][] texts = new byte[32][8];
        /*
        byte[] rk1,  rk2,   pt1,  ct1,
               rk3,  rk4,   pt2,  ct2,
               rk5,  rk6,   pt3,  ct3,
               rk7,  rk8,   pt4,  ct4,
               rk9,  rk10,  pt5,  ct5,
               rk11, rk12,  pt6,  ct6,
               rk13, rk14,  pt7,  ct7,
               rk15, rk17,  pt8,  ct8;
        */
        
        byte[] rk1 = {0, 0, 0, 0, 0, 0, 0, 0};   texts[0] = rk1;
        byte[] rk2 = {0, 0, 0, 0, 0, 0, 0, 0};   texts[1] = rk2;
        byte[] pt1 = {0, 0, 0, 0, 0, 0, 0, 0};   texts[2] = pt1;
        byte[] ct1 = new byte[8];                texts[3] = ct1;
        
        byte[] rk3 = {0, 0, 0, 0, 0, 0, 0, 0};   texts[4] = rk3;
        byte[] rk4 = {0, 0, 0, 0, 0, 0, 0, 0};   texts[5] = rk4;
        byte[] pt2 = {0, 0, 0, 0, 0, 0, 0, 0};   texts[6] = pt2;
        byte[] ct2 = new byte[8];                texts[7] = ct2;
        
        byte[] rk5 = {0, 0, 0, 0, 0, 0, 0, 0};   texts[8] = rk5;
        byte[] rk6 = {0, 0, 0, 0, 0, 0, 0, 0};   texts[9] = rk6;
        byte[] pt3 = {0, 0, 0, 0, 0, 0, 0, 0};   texts[10] = pt3;
        byte[] ct3 = new byte[8];                texts[11] = ct3;
        
        byte[] rk7 = {0, 0, 0, 0, 0, 0, 0, 0};   texts[12] = rk7;
        byte[] rk8 = {0, 0, 0, 0, 0, 0, 0, 0};   texts[13] = rk8;
        byte[] pt4 = {0, 0, 0, 0, 0, 0, 0, 0};   texts[14] = pt4;
        byte[] ct4 = new byte[8];                texts[15] = ct4;
        
        byte[] rk9 = {0, 0, 0, 0, 0, 0, 0, 0};   texts[16] = rk9;
        byte[] rk10 = {0, 0, 0, 0, 0, 0, 0, 0};   texts[17] = rk10;
        byte[] pt5 = {0, 0, 0, 0, 0, 0, 0, 0};   texts[18] = pt5;
        byte[] ct5 = new byte[8];                texts[19] = ct5;
        
        byte[] rk11 = {0, 0, 0, 0, 0, 0, 0, 0};   texts[20] = rk11;
        byte[] rk12 = {0, 0, 0, 0, 0, 0, 0, 0};   texts[21] = rk12;
        byte[] pt6 = {0, 0, 0, 0, 0, 0, 0, 0};   texts[22] = pt6;
        byte[] ct6 = new byte[8];                texts[23] = ct6;
        
        byte[] rk13 = {0, 0, 0, 0, 0, 0, 0, 0};   texts[24] = rk13;
        byte[] rk14 = {0, 0, 0, 0, 0, 0, 0, 0};   texts[25] = rk14;
        byte[] pt7 = {0, 0, 0, 0, 0, 0, 0, 0};   texts[26] = pt7;
        byte[] ct7 = new byte[8];                texts[27] = ct7;
        
        byte[] rk15 = {0, 0, 0, 0, 0, 0, 0, 0};   texts[28] = rk15;
        byte[] rk16 = {0, 0, 0, 0, 0, 0, 0, 0};   texts[29] = rk16;
        byte[] pt8 = {0, 0, 0, 0, 0, 0, 0, 0};   texts[30] = pt8;
        byte[] ct8 = new byte[8];                texts[31] = ct8;
    }

}
