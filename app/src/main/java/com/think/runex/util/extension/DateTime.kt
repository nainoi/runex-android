package com.think.runex.util.extension

import java.time.Duration
import java.util.concurrent.TimeUnit

/**
 * Convert time millisecond to time display format 'HH:mm:ss'
 */
fun Long.timeDisplayFormat(): String {
    return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
        var timeLeft = Duration.ofMillis(this)
        val hours = timeLeft.toHours()
        timeLeft = timeLeft.minusHours(hours)
        val minutes = timeLeft.toMinutes()
        timeLeft = timeLeft.minusMinutes(minutes)
        String.format("%02d:%02d:%02d", hours, minutes, timeLeft.seconds)
    } else {
        String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(this),
                TimeUnit.MILLISECONDS.toMinutes(this) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(this)),
                TimeUnit.MILLISECONDS.toSeconds(this) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(this)))
    }
}

