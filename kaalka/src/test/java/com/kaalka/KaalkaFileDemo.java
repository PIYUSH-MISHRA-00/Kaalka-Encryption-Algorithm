package test.java.com.kaalka;

import main.java.com.kaalka.Kaalka;
import java.io.File;

public class KaalkaFileDemo {
    public static void main(String[] args) throws Exception {
        Kaalka kaalka = new Kaalka();
        String timeKey = "12:34:56";
        new File("encrypted").mkdir();
        new File("decrypted").mkdir();

        // Create dummy files
        java.nio.file.Files.write(java.nio.file.Paths.get("demo.txt"), "Hello Java file!".getBytes());
        java.nio.file.Files.write(java.nio.file.Paths.get("demo_utf8.txt"), "हैलो जावा!".getBytes());
        byte[] bin = new byte[256];
        for (int i = 0; i < 256; i++) bin[i] = (byte) i;
        java.nio.file.Files.write(java.nio.file.Paths.get("demo.bin"), bin);
        java.nio.file.Files.write(java.nio.file.Paths.get("demo_large.txt"), "A".repeat(5000).getBytes());

        String[] files = {"demo.txt", "demo_utf8.txt", "demo.bin", "demo_large.txt"};
        // Encrypt all files
        for (String path : files) {
            String encFile = kaalka.encryptFile(path, timeKey);
            java.nio.file.Files.copy(java.nio.file.Paths.get(encFile), java.nio.file.Paths.get("encrypted/" + new File(encFile).getName()), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Encrypted file: " + encFile);
        }
        // Decrypt all files
        for (String path : files) {
            String encFile = path.substring(0, path.lastIndexOf('.')) + ".kaalka";
            String decFile = kaalka.decryptFile(encFile, timeKey);
            java.nio.file.Files.copy(java.nio.file.Paths.get(decFile), java.nio.file.Paths.get("decrypted/" + new File(decFile).getName()), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Decrypted file: " + decFile);
        }
        // Clean up demo files
        for (String path : files) {
            try { new File(path).delete(); } catch (Exception e) {}
            try { new File(path.substring(0, path.lastIndexOf('.')) + ".kaalka").delete(); } catch (Exception e) {}
        }
    }
}
