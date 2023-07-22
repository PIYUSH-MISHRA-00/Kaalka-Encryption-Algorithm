package test.java.com.kaalka;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class KaalkaTest {

    @Test
    public void testEncryptionAndDecryption() {
        Kaalka kaalka = new Kaalka();
        String originalMessage = "Hello, World!";
        String encryptedMessage = kaalka.encrypt(originalMessage);
        String decryptedMessage = kaalka.decrypt(encryptedMessage);

        assertEquals(originalMessage, decryptedMessage, "Encryption and decryption should be symmetric.");
    }

    @Test
    public void testInvalidCharacters() {
        Kaalka kaalka = new Kaalka();
        String message = "Invalid Characters: \n\t\r\b";
        String encryptedMessage = kaalka.encrypt(message);
        String decryptedMessage = kaalka.decrypt(encryptedMessage);

        assertEquals(message, decryptedMessage, "Encryption and decryption should preserve invalid characters.");
    }
}
