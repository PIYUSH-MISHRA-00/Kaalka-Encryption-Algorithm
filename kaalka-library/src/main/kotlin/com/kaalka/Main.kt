import com.kaalka.Kaalka
import com.kaalka.KaalkaNTP
import com.kaalka.Packet

fun main() {
    // Example usage of Kaalka
    val kaalka = Kaalka()
    val originalMessage = "Hello, Kaalka!"
    val timestamp = "10:20:30" // Example fixed timestamp for deterministic test
    val encryptedMessage = kaalka.encrypt(originalMessage, timestamp)
    val decryptedMessage = kaalka.decrypt(encryptedMessage, timestamp)

    println("Original Message: $originalMessage")
    println("Encrypted Message: $encryptedMessage")
    println("Decrypted Message: $decryptedMessage")

    // Example usage of KaalkaNTP
    val kaalkaNTP = KaalkaNTP()
    val encryptedNTPMessage = kaalkaNTP.encrypt(originalMessage, timestamp)
    val decryptedNTPMessage = kaalkaNTP.decrypt(encryptedNTPMessage, timestamp)

    println("\nUsing KaalkaNTP:")
    println("Original Message: $originalMessage")
    println("Encrypted NTP Message: $encryptedNTPMessage")
    println("Decrypted NTP Message: $decryptedNTPMessage")

    // Example usage of Packet sending and receiving
    Packet("Hello, Kaalka!").sendAndReceiveData()
}
