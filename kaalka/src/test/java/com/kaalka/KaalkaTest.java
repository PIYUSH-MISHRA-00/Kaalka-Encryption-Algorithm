package test.java.com.kaalka;

import main.java.com.kaalka.Kaalka;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

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
}
