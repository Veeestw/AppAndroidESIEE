package com.example.projet_android_esiee;

import io.realm.DynamicRealm;
import io.realm.RealmMigration;
import io.realm.RealmSchema;

public class RealmMigrations implements RealmMigration {

    @Override
    public void migrate(final DynamicRealm realm, long oldVersion, long newVersion) {
        // On récupère le schéma de la base de données
        RealmSchema schema = realm.getSchema();

        // Le code pour les futures migrations sera placé ici.
        // Par exemple, si tu passes un jour de la version 2 à 3, tu ajouteras
        // une condition comme celle-ci :
        //
        // if (oldVersion == 2) {
        //     // Faire des changements sur le schéma
        //     // schema.get("NomDeLaClasse").addField("nouveauChamp", String.class);
        //     oldVersion++;
        // }
    }
}
