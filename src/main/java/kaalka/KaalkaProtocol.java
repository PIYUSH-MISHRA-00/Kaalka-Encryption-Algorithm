package kaalka;

import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * KaalkaProtocol: Implements time-first protocol, envelope, seal, replay protection, chunked file support
 */
public class KaalkaProtocol {
    private final Map<String, Set<Integer>> replayLedger = new HashMap<>();

    /**
     * Derive per-device/session secret from UTC time (ms precision)
     */
    public static byte[] keyResonance(String deviceId, String timestamp) {
        String time = (timestamp != null) ? timestamp : KaalkaUtils.utcNow();
        byte[] seed = (deviceId + time).getBytes(StandardCharsets.UTF_8);
        return drum(seed, time);
    }

    /**
     * Drum logic (Kaalka-style, time-based transformation)
     */
    public static byte[] drum(byte[] bytes, String time) {
        byte[] timeHash = time.getBytes(StandardCharsets.UTF_8);
        byte[] out = new byte[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            out[i] = (byte) (bytes[i] ^ timeHash[i % timeHash.length]);
        }
        return out;
    }

    /**
     * Create canonical envelope
     */
    public static KaalkaEnvelope encryptEnvelope(String plaintext, String senderId, String receiverId, String timestamp, int windowSeconds, int seq) {
        String time = (timestamp != null) ? timestamp : KaalkaUtils.utcNow();
        byte[] key = keyResonance(senderId, time);
        byte[] ct = drum(plaintext.getBytes(StandardCharsets.UTF_8), time);
        String ciphertext = Base64.getEncoder().encodeToString(ct);
        KaalkaEnvelope env = new KaalkaEnvelope(senderId, receiverId, time, windowSeconds, seq, ciphertext, null);
        env.seal = seal(env, key, time);
        return env;
    }

    /**
     * Decrypt and verify envelope
     */
    public String decryptEnvelope(KaalkaEnvelope env, String receiverId, String timestamp) throws Exception {
        String time = (timestamp != null) ? timestamp : env.timestamp;
        byte[] key = keyResonance(env.senderId, time);
        Date now = KaalkaUtils.utcNowDate();
        Date msgTime = KaalkaUtils.parseUtc(env.timestamp);
        int window = env.window;
        int seq = env.seq;
        if (Math.abs((now.getTime() - msgTime.getTime()) / 1000) > window) {
            throw new Exception("Message expired");
        }
        String ledgerKey = env.senderId + ":" + receiverId;
        replayLedger.putIfAbsent(ledgerKey, new HashSet<>());
        if (replayLedger.get(ledgerKey).contains(seq)) {
            throw new Exception("Replay detected");
        }
        replayLedger.get(ledgerKey).add(seq);
        String expectedSeal = seal(env, key, time);
        if (!env.seal.equals(expectedSeal)) {
            throw new Exception("Seal verification failed");
        }
        byte[] ct = Base64.getDecoder().decode(env.ciphertext);
        byte[] pt = drum(ct, time);
        return new String(pt, StandardCharsets.UTF_8);
    }

    /**
     * Compute time-based seal (integrity MIC)
     */
    public static String seal(KaalkaEnvelope env, byte[] key, String time) {
        // Canonical order: senderId, receiverId, timestamp, window, seq, ciphertext
        StringBuilder sb = new StringBuilder();
        sb.append(env.senderId).append(env.receiverId).append(env.timestamp).append(env.window).append(env.seq).append(env.ciphertext);
        byte[] bytes = sb.toString().getBytes(StandardCharsets.UTF_8);
        byte[] mic = drum(bytes, time);
        return Base64.getEncoder().encodeToString(mic);
    }
}
