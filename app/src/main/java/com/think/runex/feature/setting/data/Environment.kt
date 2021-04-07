package com.think.runex.feature.setting.data

import androidx.annotation.IntDef

@IntDef(value = [Environment.DEV, Environment.PRODUCTION])
annotation class Environment {
    companion object {
        const val DEV = 0
        const val PRODUCTION = 1
    }
}