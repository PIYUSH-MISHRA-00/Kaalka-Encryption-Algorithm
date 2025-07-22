package com.kaalka;

import com.kaalka.Kaalka;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

public class KaalkaTest {

    @Test
    public void testEncryptionAndDecryption() {
        Kaalka kaalka = new Kaalka();
        String originalMessage = "Hello, World!";
        String encryptedMessage = kaalka.encrypt(originalMessage, "10:20:30");
        String decryptedMessage = kaalka.decrypt(encryptedMessage, "10:20:30");

        assertEquals(originalMessage, decryptedMessage, "Encryption and decryption should be symmetric.");
    }

    @Test
    public void testInvalidCharacters() {
        Kaalka kaalka = new Kaalka();
        String message = "Invalid Characters: \n\t\r\b";
        String encryptedMessage = kaalka.encrypt(message, "01:02:03");
        String decryptedMessage = kaalka.decrypt(encryptedMessage, "01:02:03");

        assertEquals(message, decryptedMessage, "Encryption and decryption should preserve invalid characters.");
    }

    @Test
    public void testFileEncryptionAndDecryption() throws Exception {
        Kaalka kaalka = new Kaalka();
        String timeKey = "12:34:56";

        // Create a temporary file with some content
        String originalContent = "This is a test file content.";
        String tempFileName = "temp_test_file.txt";
        Files.write(Paths.get(tempFileName), originalContent.getBytes());

        // Encrypt the file
        String encryptedFile = kaalka.encryptFile(tempFileName, timeKey);
        assertTrue(new File(encryptedFile).exists(), "Encrypted file should exist.");

        // Decrypt the file
        String decryptedFile = kaalka.decryptFile(encryptedFile, timeKey);
        assertTrue(new File(decryptedFile).exists(), "Decrypted file should exist.");

        // Verify the decrypted content matches the original
        byte[] decryptedBytes = Files.readAllBytes(Paths.get(decryptedFile));
        String decryptedContent = new String(decryptedBytes);
        assertEquals(originalContent, decryptedContent, "Decrypted file content should match original.");

        // Clean up temporary files
        new File(tempFileName).delete();
        new File(encryptedFile).delete();
        new File(decryptedFile).delete();
    }
}
