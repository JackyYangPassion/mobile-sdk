package io.inappchat.inappchat.remote.util

import io.inappchat.inappchat.remote.util.HeaderUtils
import java.lang.Byte
import java.lang.Exception
import java.lang.StringBuilder
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import kotlin.experimental.and

object HeaderUtils {
    const val SEPARATOR = "~"
    fun getHeaderSignature(apiKey: String, packageName: String, time: Long): String? {
        val rawHeader = apiKey + SEPARATOR + packageName + SEPARATOR + time
        return getHash(rawHeader)
    }

    fun getHeaderTimestamp(time: Long): String {
        return time.toString() + ""
    }

    private fun getHash(data: String): String? {
        return try {
            var digest: MessageDigest? = null
            try {
                digest = MessageDigest.getInstance("SHA-256")
            } catch (e1: NoSuchAlgorithmException) {
                e1.printStackTrace()
            }
            digest!!.reset()
            bin2hex(digest.digest(data.toByteArray()))
        } catch (ignored: Exception) {
            null
        }
    }

    private fun bin2hex(data: ByteArray): String {
        val hex = StringBuilder(data.size * 2)
        val max = (255u).toByte()
        for (b in data) hex.append(String.format("%02x", b and max))
        return hex.toString()
    }
}