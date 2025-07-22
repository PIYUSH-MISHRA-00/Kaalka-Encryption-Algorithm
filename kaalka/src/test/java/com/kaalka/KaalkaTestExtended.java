package com.kaalka;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class KaalkaTestExtended {

    @Test
    public void testEmptyStringEncryption() {
        Kaalka kaalka = new Kaalka();
        String original = "";
        String encrypted = kaalka.encrypt(original, "00:00:00");
        String decrypted = kaalka.decrypt(encrypted, "00:00:00");
        assertEquals(original, decrypted);
    }

    @Test
    public void testLargeStringEncryption() {
        Kaalka kaalka = new Kaalka();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10000; i++) {
            sb.append((char) (i % 256));
        }
        String original = sb.toString();
        String encrypted = kaalka.encrypt(original, "01:02:03");
        String decrypted = kaalka.decrypt(encrypted, "01:02:03");
        assertEquals(original, decrypted);
    }

    @Test
    public void testUnicodeBeyondBMP() {
        Kaalka kaalka = new Kaalka();
        String original = "𐍈𐍉𐍊𐍋"; // Unicode characters beyond BMP
        String encrypted = kaalka.encrypt(original, "05:06:07");
        String decrypted = kaalka.decrypt(encrypted, "05:06:07");
        assertEquals(original, decrypted);
    }
}
