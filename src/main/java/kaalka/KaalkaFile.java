package kaalka;

import java.util.*;
import java.nio.charset.StandardCharsets;

/**
 * KaalkaFile: Chunked file encryption/decryption
 */
public class KaalkaFile {
    public static List<KaalkaEnvelope> encryptFileChunks(byte[] fileBytes, String senderId, String receiverId, String timestamp) {
        List<KaalkaEnvelope> chunks = new ArrayList<>();
        int chunkSize = 1024; // 1KB chunks
        int totalChunks = (fileBytes.length + chunkSize - 1) / chunkSize;
        for (int i = 0; i < totalChunks; i++) {
            int start = i * chunkSize;
            int end = Math.min(start + chunkSize, fileBytes.length);
            byte[] chunk = Arrays.copyOfRange(fileBytes, start, end);
            String chunkTime = timestamp + ":" + i;
            KaalkaEnvelope env = KaalkaProtocol.encryptEnvelope(new String(chunk, StandardCharsets.ISO_8859_1), senderId, receiverId, chunkTime, 120, i + 1);
            chunks.add(env);
        }
        return chunks;
    }

    public static byte[] decryptFileChunks(List<KaalkaEnvelope> encryptedChunks, String receiverId, String timestamp) throws Exception {
        List<byte[]> outChunks = new ArrayList<>();
        for (int i = 0; i < encryptedChunks.size(); i++) {
            KaalkaEnvelope env = encryptedChunks.get(i);
            String chunkTime = timestamp + ":" + i;
            String pt = new KaalkaProtocol().decryptEnvelope(env, receiverId, chunkTime);
            outChunks.add(pt.getBytes(StandardCharsets.ISO_8859_1));
        }
        int totalLen = outChunks.stream().mapToInt(b -> b.length).sum();
        byte[] fileBytes = new byte[totalLen];
        int pos = 0;
        for (byte[] chunk : outChunks) {
            System.arraycopy(chunk, 0, fileBytes, pos, chunk.length);
            pos += chunk.length;
        }
        return fileBytes;
    }
}
