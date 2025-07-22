
package com.kaalka

import java.net.InetAddress
import org.apache.commons.net.ntp.NTPUDPClient
import org.apache.commons.net.ntp.TimeInfo
import kotlin.math.*
import java.time.LocalTime

open class Kaalka {
    protected var h = 0
    protected var m = 0
    protected var s = 0

    private fun updateTimestamp() {
        val currentTime = LocalTime.now()
        h = currentTime.hour
        m = currentTime.minute
        s = currentTime.second
    }

    init {
        updateTimestamp()
    }

    protected fun encryptMessage(data: String): String {
        val key = "$h$m$s"
        val keyHash = key.hashCode()
        return data.map { char ->
            (char.toInt() + keyHash).toChar()
        }.joinToString("")
    }

    protected fun decryptMessage(encryptedData: String): String {
        val key = "$h$m$s"
        val keyHash = key.hashCode()
        return encryptedData.map { char ->
            (char.toInt() - keyHash).toChar()
        }.joinToString("")
    }
}

class KaalkaNTP : Kaalka() {
    private fun getNtpTime(): Triple<Int, Int, Int> {
        val ntpClient = NTPUDPClient()
        ntpClient.defaultTimeout = 10000
        val address = InetAddress.getByName("pool.ntp.org")
        val timeInfo: TimeInfo = ntpClient.getTime(address)
        val ntpTime = timeInfo.message.transmitTimeStamp.time / 1000
        val s = (ntpTime % 60).toInt()
        val m = ((ntpTime / 60) % 60).toInt()
        val h = ((ntpTime / 3600) % 24).toInt() % 12
        return Triple(h, m, s)
    }

    fun encrypt(data: String, timeKey: String? = null): String {
        val (hh, mm, ss) = if (timeKey == null) getNtpTime() else parseTime(timeKey)
        this.javaClass.superclass.getDeclaredField("h").apply { isAccessible = true }.setInt(this, hh)
        this.javaClass.superclass.getDeclaredField("m").apply { isAccessible = true }.setInt(this, mm)
        this.javaClass.superclass.getDeclaredField("s").apply { isAccessible = true }.setInt(this, ss)
        val encryptMethod = this.javaClass.superclass.getDeclaredMethod("encryptMessage", String::class.java)
        encryptMethod.isAccessible = true
        return encryptMethod.invoke(this, data) as String
    }

    fun decrypt(encryptedMessage: String, timeKey: String? = null): String {
        val (hh, mm, ss) = if (timeKey == null) getNtpTime() else parseTime(timeKey)
        this.javaClass.superclass.getDeclaredField("h").apply { isAccessible = true }.setInt(this, hh)
        this.javaClass.superclass.getDeclaredField("m").apply { isAccessible = true }.setInt(this, mm)
        this.javaClass.superclass.getDeclaredField("s").apply { isAccessible = true }.setInt(this, ss)
        val decryptMethod = this.javaClass.superclass.getDeclaredMethod("decryptMessage", String::class.java)
        decryptMethod.isAccessible = true
        return decryptMethod.invoke(this, encryptedMessage) as String
    }

    private fun parseTime(timeKey: String): Triple<Int, Int, Int> {
        val hours = timeKey.substring(0, 2).toInt()
        val minutes = timeKey.substring(2, 4).toInt()
        val seconds = timeKey.substring(4, 6).toInt()
        return Triple(hours, minutes, seconds)
    }
}
