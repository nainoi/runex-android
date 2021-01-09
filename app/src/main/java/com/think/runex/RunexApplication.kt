package com.think.runex

import android.app.Application
import android.content.Context
import com.think.runex.util.Localization
import com.think.runex.util.NightMode
import io.realm.Realm
import io.realm.RealmConfiguration

class RunexApplication : Application() {

    override fun attachBaseContext(base: Context?) {
        //Set ui mode and language
        super.attachBaseContext(NightMode.applyNightMode(Localization.applyLanguage(base)))
    }

    override fun onCreate() {
        super.onCreate()
        initRealm()
    }

    private fun initRealm(){
        Realm.init(this)
        val config = RealmConfiguration.Builder()
                .name("runex.realm")
                .schemaVersion(1)
                .deleteRealmIfMigrationNeeded()
                .build()
        Realm.setDefaultConfiguration(config)
    }
}