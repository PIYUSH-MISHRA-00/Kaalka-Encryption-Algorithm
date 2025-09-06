package kaalka;

import org.junit.jupiter.api.*;
import java.util.*;

public class KaalkaProtocolTest {
    @Test
    public void testEnvelopeEncryptDecrypt() throws Exception {
        String sender = "testSender";
        String receiver = "testReceiver";
        String timestamp = KaalkaUtils.utcNow();
        String plaintext = "Hello Kaalka!";
        KaalkaEnvelope env = KaalkaProtocol.encryptEnvelope(plaintext, sender, receiver, timestamp, 120, 1);
        String decrypted = new KaalkaProtocol().decryptEnvelope(env, receiver, timestamp);
        Assertions.assertEquals(plaintext, decrypted);
    }

    @Test
    public void testReplayProtection() throws Exception {
        String sender = "testSender";
        String receiver = "testReceiver";
        String timestamp = KaalkaUtils.utcNow();
        String plaintext = "Replay Test";
        KaalkaEnvelope env = KaalkaProtocol.encryptEnvelope(plaintext, sender, receiver, timestamp, 120, 2);
        KaalkaProtocol protocol = new KaalkaProtocol();
        protocol.decryptEnvelope(env, receiver, timestamp);
        Assertions.assertThrows(Exception.class, () -> protocol.decryptEnvelope(env, receiver, timestamp));
    }

    @Test
    public void testMessageExpiry() throws Exception {
        String sender = "testSender";
        String receiver = "testReceiver";
        String timestamp = "2000-01-01T00:00:00.000Z";
        String plaintext = "Expired Test";
        KaalkaEnvelope env = KaalkaProtocol.encryptEnvelope(plaintext, sender, receiver, timestamp, 1, 3);
        KaalkaProtocol protocol = new KaalkaProtocol();
        Assertions.assertThrows(Exception.class, () -> protocol.decryptEnvelope(env, receiver, KaalkaUtils.utcNow()));
    }
}
