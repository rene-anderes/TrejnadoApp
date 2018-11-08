package org.anderes.app.trejnado

import org.junit.Test

import org.junit.Assert.*
import java.time.LocalDate
import java.time.Month
import java.time.ZoneOffset
import java.util.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        val dateLong = LocalDate.of(2018, Month.APRIL, 10)
        System.out.printf("Datum: %s - Long: %s", dateLong, dateLong.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli())

        assertEquals(4, 2 + 2)
    }
}
