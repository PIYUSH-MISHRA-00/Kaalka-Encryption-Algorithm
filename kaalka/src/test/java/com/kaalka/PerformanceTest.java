package com.kaalka;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.File;

public class PerformanceTest {

    @Test
    public void testLargeFileEncryptionDecryption() throws Exception {
        Kaalka kaalka = new Kaalka();
        String timeKey = "12:34:56";

        // Create a large temporary file (~10MB)
        String largeFileName = "large_test_file.txt";
        byte[] largeContent = new byte[10 * 1024 * 1024]; // 10 MB
        for (int i = 0; i < largeContent.length; i++) {
            largeContent[i] = (byte) (i % 256);
        }
        Files.write(Paths.get(largeFileName), largeContent);

        // Encrypt the large file
        String encryptedFile = kaalka.encryptFile(largeFileName, timeKey);
        assertTrue(new File(encryptedFile).exists(), "Encrypted file should exist.");

        // Decrypt the large file
        String decryptedFile = kaalka.decryptFile(encryptedFile, timeKey);
        assertTrue(new File(decryptedFile).exists(), "Decrypted file should exist.");

        // Verify the decrypted content matches the original
        byte[] decryptedBytes = Files.readAllBytes(Paths.get(decryptedFile));
        assertArrayEquals(largeContent, decryptedBytes, "Decrypted file content should match original.");

        // Clean up temporary files
        new File(largeFileName).delete();
        new File(encryptedFile).delete();
        new File(decryptedFile).delete();
    }
}
