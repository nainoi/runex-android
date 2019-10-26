package com.think.runex.common

import android.os.Build
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

fun String.toTimeStamp(pattern: String): Long = try {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        LocalDate.parse(this, DateTimeFormatter.ofPattern(pattern))
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant().epochSecond
    } else {
        SimpleDateFormat(pattern, Locale.getDefault()).parse(this).time
    }
} catch (e: Exception) {
    e.printStackTrace()
    0
}