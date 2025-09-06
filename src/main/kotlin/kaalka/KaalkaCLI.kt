package kaalka

import java.io.File
import kotlin.system.exitProcess

/**
 * Kaalka CLI tool
 * Supports envelope, text, and file encryption/decryption
 */
object KaalkaCLI {
    @JvmStatic
    fun main(args: Array<String>) {
        if (args.isEmpty() || args.contains("--help")) {
            printUsage()
            exitProcess(0)
        }
        try {
            when (args[0]) {
                "encrypt" -> handleEncrypt(args)
                "decrypt" -> handleDecrypt(args)
                "envelope" -> handleEnvelope(args)
                else -> {
                    println("Unknown command. Use --help for usage.")
                    exitProcess(1)
                }
            }
        } catch (e: Exception) {
            println("Error: ${e.message}")
            exitProcess(1)
        }
    }

    private fun printUsage() {
        println("""
Kaalka CLI (Kotlin)
Usage:
  encrypt --in <inputFile> --out <outputFile> --sender <id> --receiver <id> --timestamp <time>
  decrypt --in <inputFile> --out <outputFile> --receiver <id> --timestamp <time>
  envelope --text "message" --sender <id> --receiver <id>
""")
    }

    private fun handleEncrypt(args: Array<String>) {
        val inFile = getArg(args, "--in")
        val outFile = getArg(args, "--out")
        val sender = getArg(args, "--sender")
        val receiver = getArg(args, "--receiver")
        val timestamp = getArg(args, "--timestamp")
        if (inFile == null || outFile == null || sender == null || receiver == null) {
            println("Missing required argument for encrypt")
            printUsage()
            exitProcess(1)
        }
        val fileBytes = File(inFile).readBytes()
        val chunks = KaalkaFile.encryptFileChunks(fileBytes, sender, receiver, timestamp)
        File(outFile).writeBytes(chunks.joinToString("\n") { it.toString() }.toByteArray())
        println("Chunks written to $outFile")
    }

    private fun handleDecrypt(args: Array<String>) {
        val inFile = getArg(args, "--in")
        val outFile = getArg(args, "--out")
        val receiver = getArg(args, "--receiver")
        val timestamp = getArg(args, "--timestamp")
        if (inFile == null || outFile == null || receiver == null) {
            println("Missing required argument for decrypt")
            printUsage()
            exitProcess(1)
        }
        val lines = File(inFile).readLines()
        val chunks = lines.map { parseEnvelope(it) }
        val fileBytes = KaalkaFile.decryptFileChunks(chunks, receiver, timestamp)
        File(outFile).writeBytes(fileBytes)
        println("File written to $outFile")
    }

    private fun handleEnvelope(args: Array<String>) {
        val text = getArg(args, "--text")
        val sender = getArg(args, "--sender")
        val receiver = getArg(args, "--receiver")
        val timestamp = getArg(args, "--timestamp")
        if (text == null || sender == null || receiver == null) {
            println("Missing required argument for envelope")
            printUsage()
            exitProcess(1)
        }
        val env = KaalkaProtocol.encryptEnvelope(text, sender, receiver, timestamp)
        println("Envelope: $env")
    }

    private fun getArg(args: Array<String>, name: String): String? {
        val idx = args.indexOf(name)
        return if (idx != -1 && idx + 1 < args.size) args[idx + 1] else null
    }

    private fun parseEnvelope(str: String): KaalkaEnvelope {
        // Simple parser for KaalkaEnvelope from string (for demo purposes)
        val regex = Regex("KaalkaEnvelope\\{senderId='(.*?)', receiverId='(.*?)', timestamp='(.*?)', window=(\\d+), seq=(\\d+), ciphertext='(.*?)', seal='(.*?)'\\}")
        val match = regex.matchEntire(str)
        if (match != null) {
            val (senderId, receiverId, timestamp, window, seq, ciphertext, seal) = match.destructured
            return KaalkaEnvelope(senderId, receiverId, timestamp, window.toInt(), seq.toInt(), ciphertext, seal)
        }
        throw Exception("Invalid envelope format")
    }
}
