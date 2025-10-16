package com.example.projet_android_esiee;

import android.app.Application;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name("notes.realm")
                .schemaVersion(3)
                .migration(new RealmMigrations())
                .deleteRealmIfMigrationNeeded()
                .allowWritesOnUiThread(true)
                .build();
        Realm.setDefaultConfiguration(config);
    }
}
