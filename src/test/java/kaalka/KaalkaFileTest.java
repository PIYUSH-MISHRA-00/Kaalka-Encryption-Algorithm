package kaalka;

import org.junit.jupiter.api.*;
import java.util.*;

public class KaalkaFileTest {
    @Test
    public void testFileChunkEncryptDecrypt() throws Exception {
        String sender = "fileSender";
        String receiver = "fileReceiver";
        String timestamp = KaalkaUtils.utcNow();
        byte[] fileBytes = "This is a test file for Kaalka chunking.".getBytes();
        List<KaalkaEnvelope> chunks = KaalkaFile.encryptFileChunks(fileBytes, sender, receiver, timestamp);
        byte[] decrypted = KaalkaFile.decryptFileChunks(chunks, receiver, timestamp);
        Assertions.assertArrayEquals(fileBytes, decrypted);
    }
}
