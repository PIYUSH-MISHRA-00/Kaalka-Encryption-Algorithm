package main.java.com.kaalka;

import java.util.function.Function;

public class KaalkaNTP {
    public int second;

    public KaalkaNTP() {
        this.second = 0;
        updateTimestamp();
    }

    private void updateTimestamp() {
        long timestamp = System.currentTimeMillis();
        this.second = (int) ((timestamp / 1000) % 60);
    }

    public String encrypt(String data, Function<Integer, Integer> encryptionFunction) {
        StringBuilder encryptedMessage = new StringBuilder();
        for (char c : data.toCharArray()) {
            int asciiValue = (int) c;
            int encryptedValue = encryptionFunction.apply(asciiValue);
            encryptedMessage.append((char) encryptedValue);
        }
        return encryptedMessage.toString();
    }

    public String decrypt(String encryptedMessage, Function<Integer, Integer> decryptionFunction) {
        StringBuilder decryptedMessage = new StringBuilder();
        for (char c : encryptedMessage.toCharArray()) {
            int encryptedValue = (int) c;
            int decryptedValue = decryptionFunction.apply(encryptedValue);
            decryptedMessage.append((char) decryptedValue);
        }
        return decryptedMessage.toString();
    }

    public int determineQuadrant(int second) {
        if (0 <= second && second <= 15) {
            return 1;
        } else if (16 <= second && second <= 30) {
            return 2;
        } else if (31 <= second && second <= 45) {
            return 3;
        } else {
            return 4;
        }
    }
}
