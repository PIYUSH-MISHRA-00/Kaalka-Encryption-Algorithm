package main.java.com.kaalka;

public class Kaalka {
    private int second;

    public Kaalka() {
        this.second = 0;
        updateTimestamp();
    }

    private void updateTimestamp() {
        // You can use NTP or System.currentTimeMillis() to get the current time in milliseconds.
        // For simplicity, we will use System.currentTimeMillis() in this example.
        long timestamp = System.currentTimeMillis();
        this.second = (int) ((timestamp / 1000) % 60);
    }

    public String encrypt(String data) {
        StringBuilder encryptedMessage = new StringBuilder();
        for (char c : data.toCharArray()) {
            int asciiValue = (int) c;
            int encryptedValue = applyTrigonometricFunction(asciiValue);
            encryptedMessage.append((char) encryptedValue);
        }
        return encryptedMessage.toString();
    }

    public String decrypt(String encryptedMessage) {
        StringBuilder decryptedMessage = new StringBuilder();
        for (char c : encryptedMessage.toCharArray()) {
            int encryptedValue = (int) c;
            int decryptedValue = applyInverseFunction(encryptedValue);
            decryptedMessage.append((char) decryptedValue);
        }
        return decryptedMessage.toString();
    }

    private int applyTrigonometricFunction(int value) {
        int quadrant = determineQuadrant(second);
        switch (quadrant) {
            case 1:
                return value + (int) Math.round(Math.sin(second));
            case 2:
                return value + (int) Math.round(1 / Math.tan(second));
            case 3:
                return value + (int) Math.round(Math.cos(second));
            case 4:
                return value + (int) Math.round(Math.tan(second));
            default:
                return value; // In case of an invalid quadrant, do not modify the value.
        }
    }

    private int applyInverseFunction(int value) {
        int quadrant = determineQuadrant(second);
        switch (quadrant) {
            case 1:
                return value - (int) Math.round(Math.sin(second));
            case 2:
                return value - (int) Math.round(1 / Math.tan(second));
            case 3:
                return value - (int) Math.round(Math.cos(second));
            case 4:
                return value - (int) Math.round(Math.tan(second));
            default:
                return value; // In case of an invalid quadrant, do not modify the value.
        }
    }

    private int determineQuadrant(int second) {
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
