package com.think.runex.common

fun Int.displayFormat(): String = try {
    String.format("%,d", this)
} catch (e: Exception) {
    e.printStackTrace()
    ""
}

fun Long.displayFormat(): String = try {
    String.format("%,d", this)
} catch (e: Exception) {
    e.printStackTrace()
    ""
}

fun Float.displayFormat(): String = try {
    String.format("%,.2f", this).remove00Decimal()
} catch (e: Exception) {
    e.printStackTrace()
    ""
}

fun Double.displayFormat(): String = try {
    String.format("%,.2f", this).remove00Decimal()
} catch (e: Exception) {
    e.printStackTrace()
    ""
}

fun String.remove00Decimal(): String = this.let {
    when {
        it.contains(".00") -> it.replace(".00", "")
        it[it.lastIndex] == '0' -> it.substring(0, it.lastIndex)
        else -> it
    }
}

/**
 * Convert number 0-9 to 00 - 09 at string.
 */
fun Int.to2Digits(): String = when (this < 10) {
    true -> "0$this"
    false -> this.toString()
}