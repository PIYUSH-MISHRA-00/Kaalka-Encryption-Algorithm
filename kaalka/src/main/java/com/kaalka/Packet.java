package main.java.com.kaalka;

public class Packet {
    private String data;
    private String encrypted;
    private Object timeKey;

    public Packet(String data, Object timeKey) {
        this.data = data;
        this.timeKey = timeKey;
    }

    public void encrypt() {
        Kaalka k = new Kaalka();
        this.encrypted = k.encrypt(data, timeKey);
    }

    public String decrypt() {
        Kaalka k = new Kaalka();
        return k.decrypt(encrypted, timeKey);
    }

    public String getEncrypted() {
        return encrypted;
    }

    public String getData() {
        return data;
    }
}
