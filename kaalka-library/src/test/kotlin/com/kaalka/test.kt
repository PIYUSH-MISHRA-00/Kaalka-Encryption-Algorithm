import com.kaalka.Kaalka
import com.kaalka.KaalkaNTP
import com.kaalka.Packet

fun main() {
    testKaalka()
    testKaalkaNTP()
    testPacket()
}

fun testKaalka() {
    val kaalka = Kaalka()
    val originalMessage = "Hello, Kaalka!"
    val timestamp = "10:20:30" // Fixed timestamp for deterministic test
    val encryptedMessage = kaalka.encrypt(originalMessage, timestamp)
    val decryptedMessage = kaalka.decrypt(encryptedMessage, timestamp)

    println("Test Kaalka:")
    println("Original Message: $originalMessage")
    println("Encrypted Message: $encryptedMessage")
    println("Decrypted Message: $decryptedMessage")
}

fun testKaalkaNTP() {
    val kaalkaNTP = KaalkaNTP()
    val originalMessage = "Hello, KaalkaNTP!"
    val timestamp = "10:20:30" // Fixed timestamp for deterministic test
    val encryptedMessage = kaalkaNTP.encrypt(originalMessage, timestamp)
    val decryptedMessage = kaalkaNTP.decrypt(encryptedMessage, timestamp)

    println("Test KaalkaNTP:")
    println("Original Message: $originalMessage")
    println("Encrypted NTP Message: $encryptedMessage")
    println("Decrypted NTP Message: $decryptedMessage")
}

fun testPacket() {
    val packet = Packet("Hello, Packet!")
    packet.sendAndReceiveData()
}
