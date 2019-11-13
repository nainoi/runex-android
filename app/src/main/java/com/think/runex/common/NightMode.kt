package com.think.runex.common

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.think.runex.datasource.local.getAppPreference
import com.think.runex.datasource.local.getNightMode
import com.think.runex.datasource.local.setNightMode

fun AppCompatActivity.setCurrentNightMode(nightMode: Int) {
    getAppPreference().setNightMode(nightMode)
    //AppCompatDelegate.setDefaultNightMode(nightMode)
    //delegate.localNightMode = nightMode
    //delegate.applyDayNight()
    recreate()
}

fun AppCompatActivity.getCurrentNightMode(): Int = getAppPreference().getNightMode().let { mode ->
    if (mode == AppCompatDelegate.MODE_NIGHT_YES ||
            (mode == AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM &&
                    resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES)) {
        //Night mode is active, we're using dark theme
        return@let AppCompatDelegate.MODE_NIGHT_YES
    } else {
        return@let AppCompatDelegate.MODE_NIGHT_NO
    }
}