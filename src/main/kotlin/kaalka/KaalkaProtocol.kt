package kaalka

import java.util.*
import java.util.Base64

/**
 * Implements time-first protocol, envelope, seal, replay protection, chunked file support
 */
object KaalkaProtocol {
    private val replayLedger = mutableMapOf<String, MutableSet<Int>>()

    /**
     * Derive per-device/session secret from UTC time (ms precision)
     */
    fun keyResonance(deviceId: String, timestamp: String? = null): ByteArray {
        val time = timestamp ?: KaalkaUtils.utcNow()
        val seed = (deviceId + time).toByteArray(Charsets.UTF_8)
        return drum(seed, time)
    }

    /**
     * Drum logic (Kaalka-style, time-based transformation)
     */
    fun drum(bytes: ByteArray, time: String): ByteArray {
        val timeHash = time.toByteArray(Charsets.UTF_8)
        return ByteArray(bytes.size) { i -> (bytes[i].toInt() xor timeHash[i % timeHash.size].toInt()).toByte() }
    }

    /**
     * Create canonical envelope
     */
    fun encryptEnvelope(
        plaintext: String,
        senderId: String,
        receiverId: String,
        timestamp: String? = null,
        windowSeconds: Int = 120,
        seq: Int = 1
    ): KaalkaEnvelope {
        val time = timestamp ?: KaalkaUtils.utcNow()
        val key = keyResonance(senderId, time)
        val ct = drum(plaintext.toByteArray(Charsets.UTF_8), time)
        val ciphertext = Base64.getEncoder().encodeToString(ct)
        val env = KaalkaEnvelope(senderId, receiverId, time, windowSeconds, seq, ciphertext, "")
        val seal = seal(env, key, time)
        return env.copy(seal = seal)
    }

    /**
     * Decrypt and verify envelope
     */
    fun decryptEnvelope(
        envelope: KaalkaEnvelope,
        receiverId: String,
        timestamp: String? = null
    ): String {
        val time = timestamp ?: envelope.timestamp
        val key = keyResonance(envelope.senderId, time)
        val now = Date()
        val msgTime = KaalkaUtils.parseUtc(envelope.timestamp)
        val window = envelope.window
        val seq = envelope.seq
        if (Math.abs((now.time - msgTime.time) / 1000) > window) {
            throw Exception("Message expired")
        }
        val ledgerKey = "${envelope.senderId}:$receiverId"
        replayLedger.putIfAbsent(ledgerKey, mutableSetOf())
        if (replayLedger[ledgerKey]!!.contains(seq)) {
            throw Exception("Replay detected")
        }
        replayLedger[ledgerKey]!!.add(seq)
        val expectedSeal = seal(envelope, key, time)
        if (envelope.seal != expectedSeal) {
            throw Exception("Seal verification failed")
        }
        val ct = Base64.getDecoder().decode(envelope.ciphertext)
        val pt = drum(ct, time)
        return pt.toString(Charsets.UTF_8)
    }

    /**
     * Compute time-based seal (integrity MIC)
     */
    fun seal(envelope: KaalkaEnvelope, key: ByteArray, time: String): String {
        val bytes = (envelope.senderId + envelope.receiverId + envelope.timestamp + envelope.window + envelope.seq + envelope.ciphertext).toByteArray(Charsets.UTF_8)
        val mic = drum(bytes, time)
        return Base64.getEncoder().encodeToString(mic)
    }
}
