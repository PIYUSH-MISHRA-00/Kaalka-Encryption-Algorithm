package kaalka;

import java.io.Serializable;

/**
 * KaalkaEnvelope: Canonical envelope for Kaalka protocol
 * Contains senderId, receiverId, timestamp, window, seq, ciphertext, seal
 */
public class KaalkaEnvelope implements Serializable {
    public String senderId;
    public String receiverId;
    public String timestamp;
    public int window;
    public int seq;
    public String ciphertext;
    public String seal;

    public KaalkaEnvelope(String senderId, String receiverId, String timestamp, int window, int seq, String ciphertext, String seal) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.timestamp = timestamp;
        this.window = window;
        this.seq = seq;
        this.ciphertext = ciphertext;
        this.seal = seal;
    }

    @Override
    public String toString() {
        return "KaalkaEnvelope{" +
                "senderId='" + senderId + '\'' +
                ", receiverId='" + receiverId + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", window=" + window +
                ", seq=" + seq +
                ", ciphertext='" + ciphertext + '\'' +
                ", seal='" + seal + '\'' +
                '}';
    }
}
