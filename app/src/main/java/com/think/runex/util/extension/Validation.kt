package com.think.runex.util.extension

import com.jozzee.android.core.text.isAllDigit

fun String.isThaiCitizenId(): Boolean {
    if (isBlank() || isAllDigit().not() || length != 13) return false

    var sum = 0
    for (i in 0 until (length - 1)) {
        sum += ((get(i).toString().toIntOrNull() ?: 0) * (length - i))
    }

    val checkDigit: Int = (11 - (sum % 11)).let { digit ->
        if (digit >= 10) {
            val digitNumber = digit.toString()
            digitNumber.last().toString().toIntOrNull() ?: 0
        } else {
            digit
        }
    }

    return last().toString().toIntOrNull() == checkDigit
}

////TODO("Wait the complete validation of thai laser code")
//fun String.isThaiLaserCode(): Boolean {
//    if (isBlank() || length != 12) return false
//
//    if (get(0).isDigit() || get(1).isDigit()) return false
//
//    return true
//}