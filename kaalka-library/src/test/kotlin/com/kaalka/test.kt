package com.kaalka

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.File

class KaalkaTests {

    private val kaalka = Kaalka()

    @Test
    fun testKaalkaEncryptionDecryption() {
        val originalMessage = "Hello, Kaalka!"
        val timestamp = "10:20:30"
        val encryptedMessage = kaalka.encrypt(originalMessage, timestamp)
        val decryptedMessage = kaalka.decrypt(encryptedMessage, timestamp)
        assertNotEquals(originalMessage, encryptedMessage, "Encrypted message should differ from original")
        assertEquals(originalMessage, decryptedMessage, "Decrypted message should match original")
    }

    @Test
    fun testKaalkaNTPEncryptionDecryption() {
        val kaalkaNTP = KaalkaNTP()
        val originalMessage = "Hello, KaalkaNTP!"
        val timestamp = "10:20:30"
        val encryptedMessage = kaalkaNTP.encrypt(originalMessage, timestamp)
        val decryptedMessage = kaalkaNTP.decrypt(encryptedMessage, timestamp)
        assertNotEquals(originalMessage, encryptedMessage, "Encrypted NTP message should differ from original")
        assertEquals(originalMessage, decryptedMessage, "Decrypted NTP message should match original")
    }

    @Test
    fun testPacketSendReceive() {
        val packet = Packet("Hello, Packet!")
        // Assuming sendAndReceiveData has internal assertions or logs
        packet.sendAndReceiveData()
        // No assertion here as method does not return value
    }

    @Test
    fun testFileEncryptionDecryption() {
        val testText = "Kaalka file encryption test!"
        val inputFile = "test_input.txt"
        val encryptedFile = "encrypted/test_encrypted"
        val decryptedFile = "decrypted/test_decrypted"
        File("encrypted").mkdirs()
        File("decrypted").mkdirs()
        File(inputFile).writeText(testText)
        val timestamp = "11:22:33"
        val encSuccess = kaalka.encryptFile(inputFile, encryptedFile, timestamp)
        val decSuccess = kaalka.decryptFile(encryptedFile + ".kaalka", decryptedFile, timestamp)
        val resultText = File(decryptedFile + ".txt").readText()
        assertTrue(encSuccess, "File encryption should succeed")
        assertTrue(decSuccess, "File decryption should succeed")
        assertEquals(testText, resultText, "Decrypted file content should match original")
        File(inputFile).delete()
        File(encryptedFile + ".kaalka").delete()
        File(decryptedFile + ".txt").delete()
    }

    @Test
    fun testBinaryEncryptionDecryption() {
        val testBytes = byteArrayOf(0x01, 0x7F, 0x00, 0xFF.toByte(), 0x55)
        val inputFile = "test_input.bin"
        val encryptedFile = "encrypted/test_encrypted_bytes"
        val decryptedFile = "decrypted/test_decrypted_bytes"
        File("encrypted").mkdirs()
        File("decrypted").mkdirs()
        File(inputFile).writeBytes(testBytes)
        val timestamp = "12:34:56"
        val encSuccess = kaalka.encryptFile(inputFile, encryptedFile, timestamp)
        val decSuccess = kaalka.decryptFile(encryptedFile + ".kaalka", decryptedFile, timestamp)
        val resultBytes = File(decryptedFile + ".bin").readBytes()
        assertTrue(encSuccess, "Binary file encryption should succeed")
        assertTrue(decSuccess, "Binary file decryption should succeed")
        assertArrayEquals(testBytes, resultBytes, "Decrypted binary content should match original")
        File(inputFile).delete()
        File(encryptedFile + ".kaalka").delete()
        File(decryptedFile + ".bin").delete()
    }

    @Test
    fun testImageLosslessEncryptionDecryption() {
        val imagePath = "test_image.jpg"
        val encryptedFile = "encrypted/test_image"
        val decryptedFile = "decrypted/test_image_restored"
        val timestamp = "12:34:56"
        File("encrypted").mkdirs()
        File("decrypted").mkdirs()
        if (File(imagePath).exists()) {
            val encSuccess = kaalka.encryptFile(imagePath, encryptedFile, timestamp)
            val decSuccess = kaalka.decryptFile(encryptedFile + ".kaalka", decryptedFile, timestamp)
            val origBytes = File(imagePath).readBytes()
            val decBytes = File(decryptedFile + ".jpg").readBytes()
            val lossless = origBytes.size == decBytes.size && origBytes.indices.all { origBytes[it] == decBytes[it] }
            assertTrue(encSuccess, "Image file encryption should succeed")
            assertTrue(decSuccess, "Image file decryption should succeed")
            assertTrue(lossless, "Decrypted image should be lossless")
            // Do not delete files to keep them as requested
        } else {
            fail("test_image.jpg not found in the directory.")
        }
    }

    @Test
    fun testEmptyFileEncryptionDecryption() {
        val inputFile = "test_empty.txt"
        val encryptedFile = "encrypted/test_empty"
        val decryptedFile = "decrypted/test_empty_restored"
        File("encrypted").mkdirs()
        File("decrypted").mkdirs()
        File(inputFile).writeText("")
        val timestamp = "00:00:00"
        val encSuccess = kaalka.encryptFile(inputFile, encryptedFile, timestamp)
        val decSuccess = kaalka.decryptFile(encryptedFile + ".kaalka", decryptedFile, timestamp)
        val resultText = File(decryptedFile + ".txt").readText()
        assertTrue(encSuccess, "Empty file encryption should succeed")
        assertTrue(decSuccess, "Empty file decryption should succeed")
        assertEquals("", resultText, "Decrypted empty file should be empty")
        File(inputFile).delete()
        File(encryptedFile + ".kaalka").delete()
        File(decryptedFile + ".txt").delete()
    }

    @Test
    fun testInvalidTimestampEncryptionDecryption() {
        val originalMessage = "Test invalid timestamp"
        val timestamp = "invalid:timestamp"
        try {
            kaalka.encrypt(originalMessage, timestamp)
            fail("Encryption should throw exception for invalid timestamp")
        } catch (e: Exception) {
            // Expected exception
        }
        try {
            kaalka.decrypt(originalMessage, timestamp)
            fail("Decryption should throw exception for invalid timestamp")
        } catch (e: Exception) {
            // Expected exception
        }
    }

    @Test
    fun testCorruptedFileDecryption() {
        val corruptedFile = "encrypted/corrupted_file.kaalka"
        val decryptedFile = "decrypted/corrupted_file_restored"
        File("encrypted").mkdirs()
        File("decrypted").mkdirs()
        File(corruptedFile).writeBytes(byteArrayOf(0x00, 0x01, 0x02)) // Invalid content
        val timestamp = "12:34:56"
        val decSuccess = kaalka.decryptFile(corruptedFile, decryptedFile, timestamp)
        assertFalse(decSuccess, "Decryption should fail for corrupted file")
        File(corruptedFile).delete()
        if (File(decryptedFile).exists()) {
            File(decryptedFile).delete()
        }
    }
}
