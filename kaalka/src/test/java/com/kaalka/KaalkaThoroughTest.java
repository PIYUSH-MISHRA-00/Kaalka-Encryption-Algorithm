package com.kaalka;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class KaalkaThoroughTest {
    public static void main(String[] args) throws Exception {
        Kaalka kaalka = new Kaalka();
        String timeKey = "14:35:22";
        new File("encrypted").mkdir();
        new File("decrypted").mkdir();

        // Create dummy files
        Files.write(Paths.get("test.txt"), "Hello Java file!".getBytes());
        Files.write(Paths.get("test.csv"), "a,b,c\n1,2,3".getBytes());
        Files.write(Paths.get("test.json"), "{\"key\":\"value\"}".getBytes());
        Files.write(Paths.get("test.xml"), "<root><item>value</item></root>".getBytes());
        byte[] bin = new byte[256];
        for (int i = 0; i < 256; i++) bin[i] = (byte) i;
        Files.write(Paths.get("test.bin"), bin);
        Files.write(Paths.get("test_large.txt"), "A".repeat(5000).getBytes());

        String[] files = {"test.txt", "test.csv", "test.json", "test.xml", "test.bin", "test_large.txt"};
        // Encrypt all files
        for (String path : files) {
            String encFile = kaalka.encryptFile(path, timeKey);
            Files.copy(Paths.get(encFile), Paths.get("encrypted/" + new File(encFile).getName()), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Encrypted file: " + encFile);
        }
        // Decrypt all files
        for (String path : files) {
            String encFile = path.substring(0, path.lastIndexOf('.')) + ".kaalka";
            String decFile = kaalka.decryptFile(encFile, timeKey);
            Files.copy(Paths.get(decFile), Paths.get("decrypted/" + new File(decFile).getName()), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            boolean match = Arrays.equals(Files.readAllBytes(Paths.get(path)), Files.readAllBytes(Paths.get(decFile)));
            System.out.println("Decrypted file: " + decFile + " | Match: " + match);
        }
        // Clean up demo files
        for (String path : files) {
            try { new File(path).delete(); } catch (Exception e) {}
            try { new File(path.substring(0, path.lastIndexOf('.')) + ".kaalka").delete(); } catch (Exception e) {}
        }
    }
}
