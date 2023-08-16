import com.kaalka.Kaalka
import com.kaalka.KaalkaNTP
import com.kaalka.Packet

fun main() {
    // Example usage of Kaalka
    val kaalka = Kaalka()
    val originalMessage = "Hello, Kaalka!"
    val encryptedMessage = kaalka.encrypt(originalMessage)
    val decryptedMessage = kaalka.decrypt(encryptedMessage)

    println("Original Message: $originalMessage")
    println("Encrypted Message: $encryptedMessage")
    println("Decrypted Message: $decryptedMessage")

    // Example usage of KaalkaNTP
    val kaalkaNTP = KaalkaNTP()
    val encryptedNTPMessage = kaalkaNTP.encrypt(originalMessage)
    val decryptedNTPMessage = kaalkaNTP.decrypt(encryptedNTPMessage)

    println("\nUsing KaalkaNTP:")
    println("Original Message: $originalMessage")
    println("Encrypted NTP Message: $encryptedNTPMessage")
    println("Decrypted NTP Message: $decryptedNTPMessage")

    // Example usage of Packet sending and receiving
    Packet.sendAndReceiveData()
}
