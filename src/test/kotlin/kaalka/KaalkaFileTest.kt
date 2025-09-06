package kaalka

import org.junit.Test
import kotlin.test.assertContentEquals

class KaalkaFileTest {
    @Test
    fun testFileChunkEncryptDecrypt() {
        val sender = "fileSender"
        val receiver = "fileReceiver"
        val timestamp = KaalkaUtils.utcNow()
        val fileBytes = "This is a test file for Kaalka chunking.".toByteArray()
        val chunks = KaalkaFile.encryptFileChunks(fileBytes, sender, receiver, timestamp)
        val decrypted = KaalkaFile.decryptFileChunks(chunks, receiver, timestamp)
        assertContentEquals(fileBytes, decrypted)
    }
}
