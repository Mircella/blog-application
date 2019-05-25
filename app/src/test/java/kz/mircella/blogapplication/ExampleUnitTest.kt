package kz.mircella.blogapplication

import org.junit.Test

import org.junit.Assert.*
import java.time.LocalDate

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }


    @Test
    fun testDates() {
        val date1 = LocalDate.parse("2018-02-01")
        val date2 = LocalDate.parse("2018-02-05")

        val result = minOf(date1, date2)
        print(result)
    }
}
