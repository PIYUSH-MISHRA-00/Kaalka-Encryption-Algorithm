import java.net.InetAddress
import org.apache.commons.net.ntp.NTPUDPClient
import org.apache.commons.net.ntp.TimeInfo

class KaalkaNTP {
    private var second = 0

    init {
        updateTimestamp()
    }

    private fun updateTimestamp() {
        val ntpClient = NTPUDPClient()
        ntpClient.defaultTimeout = 10000
        val address = InetAddress.getByName("pool.ntp.org")
        val timeInfo: TimeInfo = ntpClient.getTime(address)
        val ntpTime = timeInfo.message.transmitTimeStamp.time / 1000
        second = (ntpTime % 60).toInt()
    }

    fun encrypt(data: String): String {
        val encryptedValues = data.map { char ->
            applyTrigonometricFunction(char.toInt())
        }
        return encryptedValues.joinToString("") { value -> value.toString() }
    }

    fun decrypt(encryptedMessage: String): String {
        val decryptedValues = encryptedMessage.chunked(2).map { chunk ->
            applyInverseFunction(chunk.toInt())
        }
        return decryptedValues.joinToString("") { char -> char.toChar().toString() }
    }

    private fun applyTrigonometricFunction(value: Int): Int {
        val quadrant = determineQuadrant(second)
        return when (quadrant) {
            1 -> round(value + sin(second.toDouble())).toInt()
            2 -> round(value + 1 / tan(second.toDouble())).toInt()
            3 -> round(value + cos(second.toDouble())).toInt()
            4 -> round(value + tan(second.toDouble())).toInt()
            else -> value // In case of an invalid quadrant, do not modify the value.
        }
    }

    private fun applyInverseFunction(value: Int): Int {
        val quadrant = determineQuadrant(second)
        return when (quadrant) {
            1 -> round(value - sin(second.toDouble())).toInt()
            2 -> round(value - 1 / tan(second.toDouble())).toInt()
            3 -> round(value - cos(second.toDouble())).toInt()
            4 -> round(value - tan(second.toDouble())).toInt()
            else -> value // In case of an invalid quadrant, do not modify the value.
        }
    }

    private fun determineQuadrant(second: Int): Int {
        return when {
            second in 0..15 -> 1
            second in 16..30 -> 2
            second in 31..45 -> 3
            else -> 4
        }
    }
}
