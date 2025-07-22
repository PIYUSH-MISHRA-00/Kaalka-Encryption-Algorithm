package test.java.com.kaalka;

import com.kaalka.Kaalka;
import java.io.File;

public class KaalkaFileDemo {
    public static void main(String[] args) throws Exception {
        Kaalka kaalka = new Kaalka();
        String timeKey = "12:34:56";
        // Test encryption/decryption of test_image.jpg for lossless restoration
        String imagePath = "test_image.jpg";
        if (new File(imagePath).exists()) {
            System.out.println("Testing file encryption/decryption for " + imagePath + "...");
            String encFile = kaalka.encryptFile(imagePath, timeKey);
            System.out.println("Encrypted file: " + encFile);
            String decFile = kaalka.decryptFile(encFile, timeKey);
            System.out.println("Decrypted file: " + decFile);
            byte[] origBytes = java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(imagePath));
            byte[] decBytes = java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(decFile));
            boolean lossless = origBytes.length == decBytes.length;
            if (lossless) {
                for (int i = 0; i < origBytes.length; i++) {
                    if (origBytes[i] != decBytes[i]) {
                        lossless = false;
                        break;
                    }
                }
            }
            System.out.println("Lossless restoration: " + (lossless ? "PASS" : "FAIL"));
        } else {
            System.out.println("test_image.jpg not found in the directory.");
        }
    }
}
