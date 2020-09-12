package com.think.runex.java.Activities;

import android.app.Application;
import android.widget.Toast;

import com.facebook.FacebookSdk;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class xApplication extends Application {
    public xApplication() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
    }

    private void initRealm() {
//        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(getApplicationContext())
//                .name("android.realm")
//                .schemaVersion(0)
//                .deleteRealmIfMigrationNeeded()
//                .build();
//        Realm.setDefaultConfiguration(realmConfiguration);
    }
}
