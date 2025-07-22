package com.kaalka;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

public class PacketTest {

    @Test
    public void testPacketEncryptionAndDecryption() throws Exception {
        String originalData = "Sample packet data";
        String timeKey = "11:22:33";

        Packet packet = new Packet(originalData, timeKey);
        packet.encrypt();
        String encryptedData = packet.getEncrypted();

        assertNotNull(encryptedData, "Encrypted data should not be null");
        assertNotEquals(originalData, encryptedData, "Encrypted data should differ from original");

        String decryptedData = packet.decrypt();
        assertEquals(originalData, decryptedData, "Decrypted data should match original");
    }
}
