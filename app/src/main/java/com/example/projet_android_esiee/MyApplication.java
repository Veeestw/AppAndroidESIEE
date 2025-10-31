package com.example.projet_android_esiee;

import android.app.Application;
import android.content.SharedPreferences;
import android.widget.Toast;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import io.realm.Realm;
import io.realm.RealmConfiguration;




public class MyApplication extends Application {
    public static final String PREFS_NAME = "MySecurePrefsFile";//Nom du fichier securise
    private EncryptedSharedPreferences FichierSecuriseVar;
    public static final String REALM_ENCRYPTION_KEY = "realmEncryptionKey";

    @Override
    public void onCreate() {
        super.onCreate();//Execution du code de la fonction mere
        Realm.init(this);//Initialisation de la base de donnee realm pour toute l'application
        byte[] encryptionKey = getRealmEncryptionKey(); //Recuperation de la cle de chiffrement realm (pour les notes)

        RealmConfiguration config = new RealmConfiguration.Builder() //Configuration de la base de donnee dans laquelle on va stocker les notes
                .name("notes.realm")//Nom du fichier de la base de donnee
                .schemaVersion(3)//Version de la base de donnee
                .migration(new RealmMigrations())//Methode de migration de la base de donnee (si jamais on fait des modifs sur la structure de la base de donnée)
                .deleteRealmIfMigrationNeeded() //Suppression de la base de donnee si la version change
                .allowWritesOnUiThread(true)// permettre d'ecrire dans la base de donnee a partie de l'interface
                .encryptionKey(encryptionKey)//Configuration de la cle de chiffrement de la base de donnee
                .build();//Fin de la configuration de la base de donnee
        Realm.setDefaultConfiguration(config);//Faire de cette base de donnee la base de donnee par defaut de cette application
    }

    private byte[] getRealmEncryptionKey() {//Fonction qui permet de recupere la cle de chiffrement de la base de donnee
        try {
            String masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);//On crée ou on recupere la clé de chiffrement principale (plus precisement l'identifiant de la cle)
            FichierSecuriseVar = (EncryptedSharedPreferences) EncryptedSharedPreferences.create(//On creer ou on ouvre (si il existe deja) le fichier securise que l'on va manipuler dans le code avec la variable FichierSecuriseVar
                    PREFS_NAME,//Nom du fichier
                    masterKeyAlias,//Identifiant de la cle de chiffrement principale
                    this,//context
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,//Méthode de chiffrement de la clé
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM//Méthode de chiffrement des valeurs
            );

            String encryptionKey = FichierSecuriseVar.getString(REALM_ENCRYPTION_KEY, null);//On recupere la cle de chiffrement dans le fichier securise

            if (encryptionKey == null) { //Si la cle est vide donc premier lancement
                byte[] newkey = new byte[64];//On attribue une taille de 64 octets dans la memoire vive de l'appareil pour stocker la cle
                new SecureRandom().nextBytes(newkey);//On rempli le tableau newkey avec des valeurs aleatoires utilisable pour le chiffrement de donnees (secureRandom())
                SharedPreferences.Editor editor = FichierSecuriseVar.edit();//On creer un objet editeur qui a les droits pour modifier le fichier securise
                //A savoir : les fichiers SharedPreferences ne peuvent pas stocker des caracteres non imprimables or nous avec notre fichier randomize une suite binaire qui ne fait pas de sens donc pas de charactere imprimable c est pourquoi on a besoin de d'encoder la cle avec un algorithme complexe
                editor.putString(REALM_ENCRYPTION_KEY, android.util.Base64.encodeToString(newkey, android.util.Base64.DEFAULT));//On sauvegarde la cle de chiffrement (encode et convertie en string) dans le fichier securise dans la clé REALM_ENCRYPTION_KEY
                editor.apply();//On applique les modifications du fichier securise
                return newkey;//On retourne la cle de chiffrement
            } else {
                return android.util.Base64.decode(encryptionKey, android.util.Base64.DEFAULT);//On retourne la cle de chiffrement decodee grace au processus algorithmique inverse
            }

        } catch (GeneralSecurityException | IOException e) {//Si une erreur a lieu
            e.printStackTrace();//On affiche l'erreur dans le log cat
            throw new RuntimeException("Impossible de récupérer la clé de chiffrement de Realm", e);//On ferme l'application de force avec un message d'erreur dans le logcat
        }
    }
}