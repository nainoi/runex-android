package com.think.runex.util.extension

import androidx.annotation.IntRange
import com.jozzee.android.core.text.toFloatOrZero

fun Int.displayFormat(): String = try {
    String.format("%,d", this)
} catch (error: Throwable) {
    error.printStackTrace()
    toString()
}

fun Int.displayFormat(@IntRange(from = 1, to = 10) decimalCount: Int): String = try {
    String.format("%,.${decimalCount}f", this.toFloat())
} catch (error: Throwable) {
    error.printStackTrace()
    toString()
}

fun Long.displayFormat(): String = try {
    String.format("%,d", this)
} catch (error: Throwable) {
    error.printStackTrace()
    toString()
}

fun Long.displayFormat(@IntRange(from = 1, to = 10) decimalCount: Int): String = try {
    String.format("%,.${decimalCount}f", this)
} catch (error: Throwable) {
    error.printStackTrace()
    toString()
}


fun Float.displayFormat(@IntRange(from = 1, to = 10) decimalCount: Int = 2,
                        awaysShowDecimal: Boolean = false): String {
    return try {
        when (this % 1 != 0f || awaysShowDecimal) {
            true -> String.format("%,.${decimalCount}f", this)
            false -> String.format("%,d", this.toInt())
        }
    } catch (e: Exception) {
        e.printStackTrace()
        toString()
    }
}

fun Double.displayFormat(@IntRange(from = 1, to = 10) decimalCount: Int = 2,
                         awaysShowDecimal: Boolean = false): String {

    return try {
        when (this % 1 != 0.0 || awaysShowDecimal) {
            true -> String.format("%,.${decimalCount}f", this)
            false -> String.format("%,d", this.toInt())
        }
    } catch (e: Exception) {
        e.printStackTrace()
        toString()
    }
}

fun String.isAllDigit() = this.matches("^[0-9]*\\.?[0-9]*".toRegex())

fun String.numberDisplayFormat(): String {
    if (this.isAllDigit().not()) return this
    return this.toFloatOrZero().displayFormat()
}