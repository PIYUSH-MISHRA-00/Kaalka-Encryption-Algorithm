import com.example.Kaalka
import com.example.KaalkaNTP
import com.example.Packet

fun main() {
    testKaalka()
    testKaalkaNTP()
    testPacket()
}

fun testKaalka() {
    val kaalka = Kaalka()
    val originalMessage = "Hello, Kaalka!"
    val encryptedMessage = kaalka.encrypt(originalMessage)
    val decryptedMessage = kaalka.decrypt(encryptedMessage)

    println("Test Kaalka:")
    println("Original Message: $originalMessage")
    println("Encrypted Message: $encryptedMessage")
    println("Decrypted Message: $decryptedMessage")
}

fun testKaalkaNTP() {
    val kaalkaNTP = KaalkaNTP()
    val originalMessage = "Hello, KaalkaNTP!"
    val encryptedMessage = kaalkaNTP.encrypt(originalMessage)
    val decryptedMessage = kaalkaNTP.decrypt(encryptedMessage)

    println("Test KaalkaNTP:")
    println("Original Message: $originalMessage")
    println("Encrypted NTP Message: $encryptedMessage")
    println("Decrypted NTP Message: $decryptedMessage")
}

fun testPacket() {
    val packet = Packet("Hello, Packet!")
    packet.sendAndReceiveData()
}
