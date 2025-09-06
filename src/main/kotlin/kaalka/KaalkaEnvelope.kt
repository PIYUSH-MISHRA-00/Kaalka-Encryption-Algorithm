package kaalka

/**
 * Data class for Kaalka envelope
 * Contains senderId, receiverId, timestamp, window, seq, ciphertext, seal
 */
data class KaalkaEnvelope(
    val senderId: String,
    val receiverId: String,
    val timestamp: String,
    val window: Int = 120,
    val seq: Int = 1,
    val ciphertext: String,
    val seal: String
)
