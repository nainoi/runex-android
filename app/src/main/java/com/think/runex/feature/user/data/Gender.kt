package com.think.runex.feature.user.data

import android.content.Context
import com.think.runex.R

enum class Gender {
    Female, Male, Other
}

fun Gender.getDisplayName(context: Context): String = when (this) {
    Gender.Female -> context.getString(R.string.female)
    Gender.Male -> context.getString(R.string.male)
    else -> context.getString(R.string.other)
}
