import javax.crypto.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class BasicEncryptionDemo {

    public static void main(String[] args) {
        final String input = readLine("Enter a string to encrypt: ");
        System.out.println();

        /**
         * Create key generator
         */
        final KeyGenerator keyGenerator;
        try {
            keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(128);
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Oops, your system does not provide AES encryption!");
            return;
        }

        /**
         * Use generator to create a random key
         */
        final SecretKey ourKey = keyGenerator.generateKey();
        System.out.println("Generated random key: " + toHex(ourKey.getEncoded()));
        System.out.println();

        /**
         * Create cipher instance
         */
        final Cipher cipher;
        try {
            cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, ourKey);
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Oops, your system does not provide AES encryption!");
            return;
        } catch (NoSuchPaddingException e) {
            return;
        } catch (InvalidKeyException e) {
            System.out.println("We generated an invalid key! That shouldn't be possible...");
            return;
        }

        /**
         * Apply cipher to input
         */
        final byte[] cipheredInput;
        try {
            System.out.println("Input: " + input);
            System.out.println("Input bytes: " + toHex(input.getBytes()));
            System.out.println();

            cipheredInput = cipher.doFinal(input.getBytes());

            System.out.println("Ciphered: " + new String(cipheredInput));
            System.out.println("Ciphered bytes: " + toHex(cipheredInput));
            System.out.println();
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
            return;
        }

        /**
         * Switch to decryption mode
         */
        try {
            cipher.init(Cipher.DECRYPT_MODE, ourKey);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }

        /**
         * Decipher what we just ciphered
         */
        try {
            final byte[] decipheredInput = cipher.doFinal(cipheredInput);
            System.out.println("Deciphered: " + new String(decipheredInput));
            System.out.println("Deciphered bytes: " + toHex(decipheredInput));
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
    }

    private static String readLine(final String prompt) {
        System.out.print(prompt);
        System.out.flush();

        final Scanner scanner = new Scanner(System.in);
        final String ln = scanner.nextLine();
        if(ln.trim().equals("")) {
            System.out.println("Input string cannot be empty.");
            return readLine(prompt);
        }
        return ln.trim();
    }

    private static String toHex(final byte[] bytes) {
        if(bytes.length == 0) {
            return "";
        }
        final StringBuilder sb = new StringBuilder();
        for(byte b : bytes) {
            sb.append(String.format("%02X ", b));
        }
        return sb.substring(0, sb.length() - 1).toString();
    }

}