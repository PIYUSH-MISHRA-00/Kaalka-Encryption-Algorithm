package kaalka

import java.text.SimpleDateFormat
import java.util.*

/**
 * Utility functions for UTC time handling
 */
object KaalkaUtils {
    fun utcNow(): String = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(Date())
    fun parseUtc(ts: String): Date = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(ts)
}
