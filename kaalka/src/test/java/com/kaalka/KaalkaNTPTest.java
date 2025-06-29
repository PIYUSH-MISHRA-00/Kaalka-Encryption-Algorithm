package test.java.com.kaalka;

import main.java.com.kaalka.KaalkaNTP;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class KaalkaNTPTest {

    @Test
    public void testEncryptionAndDecryption() {
        KaalkaNTP kaalkaNTP = new KaalkaNTP();
        String originalMessage = "Hello, World!";
        String encryptedMessage = kaalkaNTP.encrypt(originalMessage, "10:20:30");
        String decryptedMessage = kaalkaNTP.decrypt(encryptedMessage, "10:20:30");

        assertEquals(originalMessage, decryptedMessage, "Encryption and decryption should be symmetric.");
    }

    @Test
    public void testInvalidCharacters() {
        KaalkaNTP kaalkaNTP = new KaalkaNTP();
        String message = "Invalid Characters: \n\t\r\b";
        String encryptedMessage = kaalkaNTP.encrypt(message, "01:02:03");
        String decryptedMessage = kaalkaNTP.decrypt(encryptedMessage, "01:02:03");

        assertEquals(message, decryptedMessage, "Encryption and decryption should preserve invalid characters.");
    }
}

