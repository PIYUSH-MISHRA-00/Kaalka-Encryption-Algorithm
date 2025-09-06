package kaalka

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class KaalkaProtocolTest {
    @Test
    fun testEnvelopeEncryptDecrypt() {
        val sender = "testSender"
        val receiver = "testReceiver"
        val timestamp = KaalkaUtils.utcNow()
        val plaintext = "Hello Kaalka!"
        val env = KaalkaProtocol.encryptEnvelope(plaintext, sender, receiver, timestamp, 120, 1)
        val decrypted = KaalkaProtocol.decryptEnvelope(env, receiver, timestamp)
        assertEquals(plaintext, decrypted)
    }

    @Test
    fun testReplayProtection() {
        val sender = "testSender"
        val receiver = "testReceiver"
        val timestamp = KaalkaUtils.utcNow()
        val plaintext = "Replay Test"
        val env = KaalkaProtocol.encryptEnvelope(plaintext, sender, receiver, timestamp, 120, 2)
        KaalkaProtocol.decryptEnvelope(env, receiver, timestamp)
        assertFailsWith<Exception> { KaalkaProtocol.decryptEnvelope(env, receiver, timestamp) }
    }

    @Test
    fun testMessageExpiry() {
        val sender = "testSender"
        val receiver = "testReceiver"
        val timestamp = "2000-01-01T00:00:00.000Z"
        val plaintext = "Expired Test"
        val env = KaalkaProtocol.encryptEnvelope(plaintext, sender, receiver, timestamp, 1, 3)
        assertFailsWith<Exception> { KaalkaProtocol.decryptEnvelope(env, receiver, KaalkaUtils.utcNow()) }
    }
}
