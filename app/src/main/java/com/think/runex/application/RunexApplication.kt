package com.think.runex.application

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration

class RunexApplication : Application() {

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