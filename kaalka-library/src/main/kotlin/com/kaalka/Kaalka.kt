import kotlin.math.*
import java.time.LocalTime

class Kaalka {
    private var h: Int = 0
    private var m: Int = 0
    private var s: Int = 0

    init {
        updateTimestamp()
    }

    // Encrypt a file (text or binary)
    fun encryptFile(inputPath: String, outputPath: String, timeKey: String? = null): Boolean {
        return try {
            val bytes = java.io.File(inputPath).readBytes()
            val ext = inputPath.substringAfterLast('.', "")
            val encryptedBytes = encryptBytes(bytes, timeKey)
            java.io.File(outputPath).writeBytes(encryptedBytes)
            if (ext.isNotEmpty()) {
                java.io.File(outputPath + ".ext").writeText(ext)
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    // Decrypt a file (text or binary)
    fun decryptFile(inputPath: String, outputPath: String, timeKey: String? = null): Boolean {
        return try {
            val bytes = java.io.File(inputPath).readBytes()
            val extFile = java.io.File(inputPath + ".ext")
            val ext = if (extFile.exists()) extFile.readText() else ""
            val decryptedBytes = decryptBytes(bytes, timeKey)
            java.io.File(outputPath).writeBytes(decryptedBytes)
            if (ext.isNotEmpty()) {
                val newPath = if (outputPath.endsWith(ext)) outputPath else outputPath + "." + ext
                java.io.File(outputPath).renameTo(java.io.File(newPath))
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    // Encrypt raw bytes (for binary/media)
    fun encryptBytes(data: ByteArray, timeKey: String? = null): ByteArray {
        val (hh, mm, ss) = parseTime(timeKey)
        h = hh; m = mm; s = ss
        val (angleHm, angleMs, angleHs) = getAngles()
        return data.mapIndexed { idx, b ->
            val factor = (h + m + s + idx + 1).takeIf { it != 0 } ?: 1
            val offset = (selectTrig(angleHm) + selectTrig(angleMs) + selectTrig(angleHs)) * factor + (idx + 1)
            ((b.toInt() + offset.roundToInt()) % 256).toByte()
        }.toByteArray()
    }

    // Decrypt raw bytes (for binary/media)
    fun decryptBytes(data: ByteArray, timeKey: String? = null): ByteArray {
        val (hh, mm, ss) = parseTime(timeKey)
        h = hh; m = mm; s = ss
        val (angleHm, angleMs, angleHs) = getAngles()
        return data.mapIndexed { idx, b ->
            val factor = (h + m + s + idx + 1).takeIf { it != 0 } ?: 1
            val offset = (selectTrig(angleHm) + selectTrig(angleMs) + selectTrig(angleHs)) * factor + (idx + 1)
            ((b.toInt() - offset.roundToInt() + 256) % 256).toByte()
        }.toByteArray()
    }

    private fun updateTimestamp() {
        val now = LocalTime.now()
        h = now.hour % 12
        m = now.minute
        s = now.second
    }

    private fun parseTime(timeStr: String?): Triple<Int, Int, Int> {
        if (timeStr == null) {
            val now = LocalTime.now()
            return Triple(now.hour % 12, now.minute, now.second)
        }
        val parts = timeStr.split(":")
        return when (parts.size) {
            3 -> Triple(parts[0].toInt() % 12, parts[1].toInt(), parts[2].toInt())
            2 -> Triple(0, parts[0].toInt(), parts[1].toInt())
            1 -> Triple(0, 0, parts[0].toInt())
            else -> throw IllegalArgumentException("Invalid time format. Use HH:MM:SS, MM:SS, or SS.")
        }
    }

    fun encrypt(data: String, timeKey: String? = null): String {
        val (hh, mm, ss) = parseTime(timeKey)
        h = hh; m = mm; s = ss
        return encryptMessage(data)
    }

    fun decrypt(encryptedMessage: String, timeKey: String? = null): String {
        val (hh, mm, ss) = parseTime(timeKey)
        h = hh; m = mm; s = ss
        return decryptMessage(encryptedMessage)
    }

    private fun getAngles(): Triple<Double, Double, Double> {
        val hourAngle = (30 * h) + (0.5 * m) + (0.5 / 60 * s)
        val minuteAngle = (6 * m) + (0.1 * s)
        val secondAngle = 6 * s
        val angleHm = min(abs(hourAngle - minuteAngle), 360 - abs(hourAngle - minuteAngle))
        val angleMs = min(abs(minuteAngle - secondAngle), 360 - abs(minuteAngle - secondAngle))
        val angleHs = min(abs(hourAngle - secondAngle), 360 - abs(hourAngle - secondAngle))
        return Triple(angleHm, angleMs, angleHs)
    }

    private fun selectTrig(angle: Double): Double {
        val quadrant = (angle / 90).toInt() + 1
        return when (quadrant) {
            1 -> sin(Math.toRadians(angle))
            2 -> cos(Math.toRadians(angle))
            3 -> tan(Math.toRadians(angle))
            else -> {
                val tanVal = tan(Math.toRadians(angle))
                if (tanVal != 0.0) 1 / tanVal else 0.0
            }
        }
    }

    private fun encryptMessage(data: String): String {
        val (angleHm, angleMs, angleHs) = getAngles()
        val encrypted = StringBuilder()
        for ((idx, c) in data.withIndex()) {
            val factor = (h + m + s + idx + 1).takeIf { it != 0 } ?: 1
            val offset = (selectTrig(angleHm) + selectTrig(angleMs) + selectTrig(angleHs)) * factor + (idx + 1)
            encrypted.append(((c.code + offset.roundToInt()) % 256).toChar())
        }
        return encrypted.toString()
    }

    private fun decryptMessage(encryptedMessage: String): String {
        val (angleHm, angleMs, angleHs) = getAngles()
        val decrypted = StringBuilder()
        for ((idx, c) in encryptedMessage.withIndex()) {
            val factor = (h + m + s + idx + 1).takeIf { it != 0 } ?: 1
            val offset = (selectTrig(angleHm) + selectTrig(angleMs) + selectTrig(angleHs)) * factor + (idx + 1)
            decrypted.append(((c.code - offset.roundToInt() + 256) % 256).toChar())
        }
        return decrypted.toString()
    }
}
