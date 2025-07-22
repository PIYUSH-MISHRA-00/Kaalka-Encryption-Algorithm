package com.kaalka;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.function.Function;

public class KaalkaNTPTest {

    @Test
    public void testEncryptionAndDecryption() {
        KaalkaNTP kaalkaNTP = new KaalkaNTP();
        String originalMessage = "Hello, World!";
        Function<Integer, Integer> encryptFunc = (Integer c) -> (c + 1) % 256;
        Function<Integer, Integer> decryptFunc = (Integer c) -> (c - 1 + 256) % 256;

        String encryptedMessage = kaalkaNTP.encrypt(originalMessage, encryptFunc);
        String decryptedMessage = kaalkaNTP.decrypt(encryptedMessage, decryptFunc);

        assertEquals(originalMessage, decryptedMessage, "Encryption and decryption should be symmetric.");
    }

    @Test
    public void testInvalidCharacters() {
        KaalkaNTP kaalkaNTP = new KaalkaNTP();
        String message = "Invalid Characters: \n\t\r\b";
        Function<Integer, Integer> encryptFunc = (Integer c) -> (c + 2) % 256;
        Function<Integer, Integer> decryptFunc = (Integer c) -> (c - 2 + 256) % 256;

        String encryptedMessage = kaalkaNTP.encrypt(message, encryptFunc);
        String decryptedMessage = kaalkaNTP.decrypt(encryptedMessage, decryptFunc);

        assertEquals(message, decryptedMessage, "Encryption and decryption should preserve invalid characters.");
    }

    @Test
    public void testDetermineQuadrant() {
        KaalkaNTP kaalkaNTP = new KaalkaNTP();

        assertEquals(1, kaalkaNTP.determineQuadrant(0));
        assertEquals(1, kaalkaNTP.determineQuadrant(15));
        assertEquals(2, kaalkaNTP.determineQuadrant(16));
        assertEquals(2, kaalkaNTP.determineQuadrant(30));
        assertEquals(3, kaalkaNTP.determineQuadrant(31));
        assertEquals(3, kaalkaNTP.determineQuadrant(45));
        assertEquals(4, kaalkaNTP.determineQuadrant(46));
        assertEquals(4, kaalkaNTP.determineQuadrant(59));
    }
}
