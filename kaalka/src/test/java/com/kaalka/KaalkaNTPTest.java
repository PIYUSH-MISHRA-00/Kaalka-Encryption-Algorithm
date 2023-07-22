package test.java.com.kaalka;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class KaalkaNTPTest {

    @Test
    public void testEncryptionAndDecryption() {
        KaalkaNTP kaalkaNTP = new KaalkaNTP();
        String originalMessage = "Hello, World!";
        String encryptedMessage = kaalkaNTP.encrypt(originalMessage, x -> x + 1);
        String decryptedMessage = kaalkaNTP.decrypt(encryptedMessage, x -> x - 1);

        assertEquals(originalMessage, decryptedMessage, "Encryption and decryption should be symmetric.");
    }

    @Test
    public void testInvalidCharacters() {
        KaalkaNTP kaalkaNTP = new KaalkaNTP();
        String message = "Invalid Characters: \n\t\r\b";
        String encryptedMessage = kaalkaNTP.encrypt(message, x -> x + 1);
        String decryptedMessage = kaalkaNTP.decrypt(encryptedMessage, x -> x - 1);

        assertEquals(message, decryptedMessage, "Encryption and decryption should preserve invalid characters.");
    }
}

