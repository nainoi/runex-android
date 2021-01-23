package com.idever.runnex

import com.jozzee.android.core.datetime.dateTimeFormat
import org.junit.Test

import org.junit.Assert.*
import java.time.Duration

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
    fun time_milliseconds_to_time_display() {
        val timeMillis: Long = 14000


        var timeLeft = Duration.ofMillis(timeMillis)
        val hours = timeLeft.toHours()
        timeLeft = timeLeft.minusHours(hours)
        val minutes = timeLeft.toMinutes()
        timeLeft = timeLeft.minusMinutes(minutes)
        val seconds = timeLeft.seconds

        print("Time: ${String.format("%02d:%02d:%02d", hours, minutes, seconds)}")

    }
}
