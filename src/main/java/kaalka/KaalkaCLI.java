package kaalka;

import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * KaalkaCLI: Command-line interface for Kaalka protocol
 * Supports envelope, text, and file encryption/decryption
 */
public class KaalkaCLI {
    public static void main(String[] args) {
        if (args.length == 0 || Arrays.asList(args).contains("--help")) {
            printUsage();
            return;
        }
        try {
            switch (args[0]) {
                case "encrypt":
                    handleEncrypt(args);
                    break;
                case "decrypt":
                    handleDecrypt(args);
                    break;
                case "envelope":
                    handleEnvelope(args);
                    break;
                default:
                    System.out.println("Unknown command. Use --help for usage.");
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void printUsage() {
        System.out.println("""
Kaalka CLI (Java)
Usage:
  encrypt --in <inputFile> --out <outputFile> --sender <id> --receiver <id> --timestamp <time>
  decrypt --in <inputFile> --out <outputFile> --receiver <id> --timestamp <time>
  envelope --text "message" --sender <id> --receiver <id>
""");
    }

    private static void handleEncrypt(String[] args) throws Exception {
        String inFile = getArg(args, "--in");
        String outFile = getArg(args, "--out");
        String sender = getArg(args, "--sender");
        String receiver = getArg(args, "--receiver");
        String timestamp = getArg(args, "--timestamp");
        if (inFile == null || outFile == null || sender == null || receiver == null) {
            System.out.println("Missing required argument for encrypt");
            printUsage();
            return;
        }
        byte[] fileBytes = Files.readAllBytes(Paths.get(inFile));
        List<KaalkaEnvelope> chunks = KaalkaFile.encryptFileChunks(fileBytes, sender, receiver, timestamp);
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(outFile))) {
            oos.writeObject(chunks);
        }
        System.out.println("Chunks written to " + outFile);
    }

    private static void handleDecrypt(String[] args) throws Exception {
        String inFile = getArg(args, "--in");
        String outFile = getArg(args, "--out");
        String receiver = getArg(args, "--receiver");
        String timestamp = getArg(args, "--timestamp");
        if (inFile == null || outFile == null || receiver == null) {
            System.out.println("Missing required argument for decrypt");
            printUsage();
            return;
        }
        List<KaalkaEnvelope> chunks;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(inFile))) {
            chunks = (List<KaalkaEnvelope>) ois.readObject();
        }
        byte[] fileBytes = KaalkaFile.decryptFileChunks(chunks, receiver, timestamp);
        Files.write(Paths.get(outFile), fileBytes);
        System.out.println("File written to " + outFile);
    }

    private static void handleEnvelope(String[] args) throws Exception {
        String text = getArg(args, "--text");
        String sender = getArg(args, "--sender");
        String receiver = getArg(args, "--receiver");
        String timestamp = getArg(args, "--timestamp");
        if (text == null || sender == null || receiver == null) {
            System.out.println("Missing required argument for envelope");
            printUsage();
            return;
        }
        KaalkaEnvelope env = KaalkaProtocol.encryptEnvelope(text, sender, receiver, timestamp, 120, 1);
        System.out.println("Envelope: " + env);
    }

    private static String getArg(String[] args, String name) {
        for (int i = 0; i < args.length - 1; i++) {
            if (args[i].equals(name)) return args[i + 1];
        }
        return null;
    }
}
