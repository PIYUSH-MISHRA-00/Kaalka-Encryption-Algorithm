package main.java.com.kaalka;
public class Packet {
    private String data;
    private String encryptedData;

    public Packet(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public String getEncryptedData() {
        return encryptedData;
    }

    public void encrypt(KaalkaNTP kaalkaNTP, Function<Integer, Integer> encryptionFunction) {
        encryptedData = kaalkaNTP.encrypt(data, encryptionFunction);
    }

    public void decrypt(KaalkaNTP kaalkaNTP, Function<Integer, Integer> decryptionFunction) {
        data = kaalkaNTP.encrypt(encryptedData, decryptionFunction);
        encryptedData = null;
    }
}

