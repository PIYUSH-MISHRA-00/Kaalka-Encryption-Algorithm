package kaalka

/**
 * Chunked file encryption/decryption
 */
object KaalkaFile {
    fun encryptFileChunks(
        fileBytes: ByteArray,
        senderId: String,
        receiverId: String,
        timestamp: String? = null
    ): List<KaalkaEnvelope> {
        val chunkSize = 1024
        val totalChunks = (fileBytes.size + chunkSize - 1) / chunkSize
        return (0 until totalChunks).map { i ->
            val start = i * chunkSize
            val end = minOf(start + chunkSize, fileBytes.size)
            val chunk = fileBytes.sliceArray(start until end)
            val chunkTime = (timestamp ?: KaalkaUtils.utcNow()) + ":$i"
            KaalkaProtocol.encryptEnvelope(chunk.toString(Charsets.ISO_8859_1), senderId, receiverId, chunkTime, 120, i + 1)
        }
    }

    fun decryptFileChunks(
        chunks: List<KaalkaEnvelope>,
        receiverId: String,
        timestamp: String? = null
    ): ByteArray {
        val outChunks = chunks.mapIndexed { i, env ->
            val chunkTime = (timestamp ?: env.timestamp) + ":$i"
            KaalkaProtocol.decryptEnvelope(env, receiverId, chunkTime).toByteArray(Charsets.ISO_8859_1)
        }
        val totalLen = outChunks.sumOf { it.size }
        val fileBytes = ByteArray(totalLen)
        var pos = 0
        for (chunk in outChunks) {
            System.arraycopy(chunk, 0, fileBytes, pos, chunk.size)
            pos += chunk.size
        }
        return fileBytes
    }
}
