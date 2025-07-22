import com.kaalka.Kaalka
import com.kaalka.KaalkaNTP
import com.kaalka.Packet

fun main() {
    testKaalka()
    testKaalkaNTP()
    testPacket()

    testFileEncryption()
    testBinaryEncryption()
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

fun testFileEncryption() {
    val kaalka = Kaalka()
    val testText = "Kaalka file encryption test!"
    val inputFile = "test_input.txt"
    val encryptedFile = "test_encrypted.bin"
    val decryptedFile = "test_decrypted.txt"
    java.io.File(inputFile).writeText(testText)
    val timestamp = "11:22:33"
    val encSuccess = kaalka.encryptFile(inputFile, encryptedFile, timestamp)
    val decSuccess = kaalka.decryptFile(encryptedFile, decryptedFile, timestamp)
    val resultText = java.io.File(decryptedFile).readText()
    println("\nTest File Encryption:")
    println("Original: $testText")
    println("Decrypted: $resultText")
    println("Encrypt Success: $encSuccess, Decrypt Success: $decSuccess")
    java.io.File(inputFile).delete(); java.io.File(encryptedFile).delete(); java.io.File(decryptedFile).delete(); java.io.File(encryptedFile + ".ext").delete()
}

fun testBinaryEncryption() {
    val kaalka = Kaalka()
    val testBytes = byteArrayOf(0x01, 0x7F, 0x00, 0xFF.toByte(), 0x55)
    val inputFile = "test_input.bin"
    val encryptedFile = "test_encrypted_bytes.bin"
    val decryptedFile = "test_decrypted_bytes.bin"
    java.io.File(inputFile).writeBytes(testBytes)
    val timestamp = "12:34:56"
    val encSuccess = kaalka.encryptFile(inputFile, encryptedFile, timestamp)
    val decSuccess = kaalka.decryptFile(encryptedFile, decryptedFile, timestamp)
    val resultBytes = java.io.File(decryptedFile).readBytes()
    println("\nTest Binary Encryption:")
    println("Original: ${testBytes.joinToString()}")
    println("Decrypted: ${resultBytes.joinToString()}")
    println("Encrypt Success: $encSuccess, Decrypt Success: $decSuccess")
    java.io.File(inputFile).delete(); java.io.File(encryptedFile).delete(); java.io.File(decryptedFile).delete(); java.io.File(encryptedFile + ".ext").delete()
}
