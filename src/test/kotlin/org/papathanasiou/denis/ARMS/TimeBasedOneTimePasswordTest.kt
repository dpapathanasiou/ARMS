package org.papathanasiou.denis.ARMS

import org.junit.Assert
import org.junit.Test
import java.util.Date
import java.util.concurrent.TimeUnit

/**
 * These are unit tests based on the Test Vectors defined in Append B of
 * <a href="https://tools.ietf.org/html/rfc6238">RFC 6238</a>.
 */
class TimeBasedOneTimePasswordTest {
    // initial values as defined in Append B
    val seed: String = "12345678901234567890"
    val time = 30L
    val unit = TimeUnit.SECONDS

    private fun testPassword(date: Date, algo: String): Int {
        return TimeBasedOneTimePassword.generate(seed, time, unit, date, algo)
    }


    @Test
    fun confirmSHA1() {
        val algo = "HmacSHA1"

        // confirm all the Test Vectors which use SHA1
        Assert.assertEquals(94287082, testPassword(Date(TimeUnit.SECONDS.toMillis(59L)), algo))
        Assert.assertEquals(7081804, testPassword(Date(TimeUnit.SECONDS.toMillis(1111111109L)), algo))
        Assert.assertEquals(14050471, testPassword(Date(TimeUnit.SECONDS.toMillis(1111111111L)), algo))
        Assert.assertEquals(89005924, testPassword(Date(TimeUnit.SECONDS.toMillis(1234567890L)), algo))
        Assert.assertEquals(69279037, testPassword(Date(TimeUnit.SECONDS.toMillis(2000000000L)), algo))
        Assert.assertEquals(65353130, testPassword(Date(TimeUnit.SECONDS.toMillis(20000000000L)), algo))
    }
}