package com.think.runex

import android.app.Application
import android.content.Context
import com.think.runex.datasource.api.ApiConfig
import com.think.runex.util.AppPreference
import com.think.runex.util.Localization
import com.think.runex.util.NightMode

class RunexApplication : Application() {

    override fun attachBaseContext(base: Context?) {
        //Set ui mode and language
        super.attachBaseContext(NightMode.applyNightMode(Localization.applyLanguage(base)))
    }

    override fun onCreate() {
        super.onCreate()
        ApiConfig.updateBaseUrl(AppPreference.getEnvironment(this))
    }
}