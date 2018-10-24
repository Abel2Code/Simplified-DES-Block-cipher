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

}
