package org.papathanasiou.denis.ARMS

import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets

import java.security.NoSuchAlgorithmException
import java.util.Date
import java.util.concurrent.TimeUnit

import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

import kotlin.experimental.and

/**
 * TimeBasedOneTimePassword is an implementation of <a href="https://tools.ietf.org/html/rfc6238">RFC 6238</a>,
 * using the given seed and acceptable timestep range to produce a one-time integer password using the given
 * Date and HMAC algorithm (defaults to now and SHA512, respectively).
 */
object TimeBasedOneTimePassword {
    fun generate(seed: String,
                 timestep: Long,
                 timeunit: TimeUnit,
                 timestamp: Date = Date(),
                 algo: String = "HmacSHA512"): Int {

        // define a "Message Authentication Code" (MAC) algorithm, and initialize it with the seed
        val mac = Mac.getInstance(algo)
                ?: throw NoSuchAlgorithmException("""unsupported algorithm: $algo""")
        mac.init(SecretKeySpec(seed.byteInputStream(StandardCharsets.US_ASCII).readBytes(), "RAW"))

        // generate a password as an integer, based on the Date + TimeUnit values
        val buffer: ByteBuffer = ByteBuffer.allocate(8)
        buffer.putLong(0, timestamp.getTime() / timeunit.toMillis(timestep))

        val hmac = mac.doFinal(buffer.array())
        val offset = (hmac[hmac.size - 1] and 0x0f).toInt()
        for(i in 0 until 4) {
            buffer.put(i, hmac[i + offset]);
        }

        return (buffer.getInt(0) and 0x7fffffff) % 100_000_000
    }
    val TOTP = fun (seed: String): Int {
        // define a "standard" computation, i.e., using 30 seconds as the timestep range
        return TimeBasedOneTimePassword.generate(seed,30L, TimeUnit.SECONDS)
    }

}

