package com.example.projet_android_esiee;

import io.realm.DynamicRealm;
import io.realm.RealmMigration;
import io.realm.RealmSchema;

public class RealmMigrations implements RealmMigration {

    @Override
    public void migrate(final DynamicRealm realm, long oldVersion, long newVersion) {//Fonction qui permet de faire une mise a niveau de la base de donnée si il y eu des changements
        RealmSchema schema = realm.getSchema();// On récupère le schéma actuel de la base de données, c'est-à-dire la description de ses tables et colonnes

        // EXEMPLE POUR LE FUTUR : Si on passait à la version 2 et qu'on ajoutait un champ "couleur" à la classe Note.
        // if (oldVersion == 1) {
        //     schema.get("Note")
        //           .addField("color", String.class);
        //     oldVersion++;
        // }


    }
}
