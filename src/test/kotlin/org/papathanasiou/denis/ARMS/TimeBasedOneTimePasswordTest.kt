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
    val time = 30L
    val unit = TimeUnit.SECONDS

    private fun testPassword(seed: String, date: Date, algo: String): Int {
        return TimeBasedOneTimePassword.generate(seed, time, unit, date, algo)
    }

    /* Test Vectors to confirm: TOTP Table from Appendix B

  +-------------+--------------+------------------+----------+--------+
  |  Time (sec) |   UTC Time   | Value of T (hex) |   TOTP   |  Mode  |
  +-------------+--------------+------------------+----------+--------+
  |      59     |  1970-01-01  | 0000000000000001 | 94287082 |  SHA1  |
  |             |   00:00:59   |                  |          |        |
  |      59     |  1970-01-01  | 0000000000000001 | 46119246 | SHA256 |
  |             |   00:00:59   |                  |          |        |
  |      59     |  1970-01-01  | 0000000000000001 | 90693936 | SHA512 |
  |             |   00:00:59   |                  |          |        |
  |  1111111109 |  2005-03-18  | 00000000023523EC | 07081804 |  SHA1  |
  |             |   01:58:29   |                  |          |        |
  |  1111111109 |  2005-03-18  | 00000000023523EC | 68084774 | SHA256 |
  |             |   01:58:29   |                  |          |        |
  |  1111111109 |  2005-03-18  | 00000000023523EC | 25091201 | SHA512 |
  |             |   01:58:29   |                  |          |        |
  |  1111111111 |  2005-03-18  | 00000000023523ED | 14050471 |  SHA1  |
  |             |   01:58:31   |                  |          |        |
  |  1111111111 |  2005-03-18  | 00000000023523ED | 67062674 | SHA256 |
  |             |   01:58:31   |                  |          |        |
  |  1111111111 |  2005-03-18  | 00000000023523ED | 99943326 | SHA512 |
  |             |   01:58:31   |                  |          |        |
  |  1234567890 |  2009-02-13  | 000000000273EF07 | 89005924 |  SHA1  |
  |             |   23:31:30   |                  |          |        |
  |  1234567890 |  2009-02-13  | 000000000273EF07 | 91819424 | SHA256 |
  |             |   23:31:30   |                  |          |        |
  |  1234567890 |  2009-02-13  | 000000000273EF07 | 93441116 | SHA512 |
  |             |   23:31:30   |                  |          |        |
  |  2000000000 |  2033-05-18  | 0000000003F940AA | 69279037 |  SHA1  |
  |             |   03:33:20   |                  |          |        |
  |  2000000000 |  2033-05-18  | 0000000003F940AA | 90698825 | SHA256 |
  |             |   03:33:20   |                  |          |        |
  |  2000000000 |  2033-05-18  | 0000000003F940AA | 38618901 | SHA512 |
  |             |   03:33:20   |                  |          |        |
  | 20000000000 |  2603-10-11  | 0000000027BC86AA | 65353130 |  SHA1  |
  |             |   11:33:20   |                  |          |        |
  | 20000000000 |  2603-10-11  | 0000000027BC86AA | 77737706 | SHA256 |
  |             |   11:33:20   |                  |          |        |
  | 20000000000 |  2603-10-11  | 0000000027BC86AA | 47863826 | SHA512 |
  |             |   11:33:20   |                  |          |        |
  +-------------+--------------+------------------+----------+--------+
     */

    @Test
    fun confirmSHA1() {
        val algo: String = "HmacSHA1"
        val seed: String = "12345678901234567890"

        // confirm all the Test Vectors which use SHA1
        Assert.assertEquals(94287082, testPassword(seed, Date(TimeUnit.SECONDS.toMillis(59L)), algo))
        Assert.assertEquals(7081804, testPassword(seed, Date(TimeUnit.SECONDS.toMillis(1111111109L)), algo))
        Assert.assertEquals(14050471, testPassword(seed, Date(TimeUnit.SECONDS.toMillis(1111111111L)), algo))
        Assert.assertEquals(89005924, testPassword(seed, Date(TimeUnit.SECONDS.toMillis(1234567890L)), algo))
        Assert.assertEquals(69279037, testPassword(seed, Date(TimeUnit.SECONDS.toMillis(2000000000L)), algo))
        Assert.assertEquals(65353130, testPassword(seed, Date(TimeUnit.SECONDS.toMillis(20000000000L)), algo))
    }

    @Test
    fun confirmSHA256() {
        val algo: String = "HmacSHA256"
        val seed: String = "12345678901234567890123456789012"

        // confirm all the Test Vectors which use SHA256
        Assert.assertEquals(46119246, testPassword(seed, Date(TimeUnit.SECONDS.toMillis(59L)), algo))
        Assert.assertEquals(68084774, testPassword(seed, Date(TimeUnit.SECONDS.toMillis(1111111109L)), algo))
        Assert.assertEquals(67062674, testPassword(seed, Date(TimeUnit.SECONDS.toMillis(1111111111L)), algo))
        Assert.assertEquals(91819424, testPassword(seed, Date(TimeUnit.SECONDS.toMillis(1234567890L)), algo))
        Assert.assertEquals(90698825, testPassword(seed, Date(TimeUnit.SECONDS.toMillis(2000000000L)), algo))
        Assert.assertEquals(77737706, testPassword(seed, Date(TimeUnit.SECONDS.toMillis(20000000000L)), algo))
    }

    @Test
    fun confirmSHA512() {
        val algo: String = "HmacSHA512"
        val seed: String = "1234567890123456789012345678901234567890123456789012345678901234"

        // confirm all the Test Vectors which use SHA512
        Assert.assertEquals(90693936, testPassword(seed, Date(TimeUnit.SECONDS.toMillis(59L)), algo))
        Assert.assertEquals(25091201, testPassword(seed, Date(TimeUnit.SECONDS.toMillis(1111111109L)), algo))
        Assert.assertEquals(99943326, testPassword(seed, Date(TimeUnit.SECONDS.toMillis(1111111111L)), algo))
        Assert.assertEquals(93441116, testPassword(seed, Date(TimeUnit.SECONDS.toMillis(1234567890L)), algo))
        Assert.assertEquals(38618901, testPassword(seed, Date(TimeUnit.SECONDS.toMillis(2000000000L)), algo))
        Assert.assertEquals(47863826, testPassword(seed, Date(TimeUnit.SECONDS.toMillis(20000000000L)), algo))
    }
}