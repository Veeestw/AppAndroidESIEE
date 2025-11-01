package com.example.projet_android_esiee;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import com.google.android.material.button.MaterialButton;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class Screen_Parametres extends AppCompatActivity {

    private EditText secretAnswerInput;
    private TextView AfficheurPasswordInput;
    public static final String PREFS_NAME = "MySecurePrefsFile";
    public static final String SECRET_ANSWER_KEY = "secretAnswer";
    public static final String PASSWORD_KEY = "password";

    private EncryptedSharedPreferences FichierSecuriseVar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {//Lors de la creation de l'activité
        super.onCreate(savedInstanceState);//Execution du code de la fonction mere
        setContentView(R.layout.activity_screen_parametres);//On recupere l'interface graphique de screen_parametre

        MaterialButton modifierBtn = findViewById(R.id.ModifierBTN);//On fait le lien entre la variable modifierBTN et le bouton ModifierBTN de l'interface
        MaterialButton retourBtn = findViewById(R.id.RetourParamToNoteBTN);//On fait le lien entre la variable retourBTN et le bouton RetourParamToNoteBTN de l'interface

        secretAnswerInput = findViewById(R.id.SecretAnswer);//On fait le lien entre la variable secretAnswerInput et le EditText SecretAnswer de l'interface
        AfficheurPasswordInput = findViewById(R.id.AfficheurPassword);//On fait le lien entre la variable AfficheurPasswordInput et le TextView AfficheurPassword de l'interface

        modifierBtn.setOnClickListener(new View.OnClickListener() {//On ajoute un listener sur le bouton ModifierBTN
            @Override
            public void onClick(View v) {//Si on clique sur le bouton ModifierBTN
                startActivity(new Intent(Screen_Parametres.this, PasswordModif.class));//On lance l'activité PasswordModif ce qui fait changer l'interface graphique
            }
        });

        retourBtn.setOnClickListener(new View.OnClickListener() {//On ajoute un listener sur le bouton RetourParamToNoteBTN
            @Override
            public void onClick(View v) {//Si on clique sur le bouton RetourParamToNoteBTN
                finish();//On ferme l'activité
            }
        });
    }

    private void loadSettings(){
        try {
            String masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);//On crée une clé de chiffrement principale
            FichierSecuriseVar = (EncryptedSharedPreferences) EncryptedSharedPreferences.create(//On creer ou on ouvre (si il existe deja) le fichier securise que l'on va manipuler dans le code avec la variable FichierSecuriseVar
                    PREFS_NAME, //Nom du fichier
                    masterKeyAlias,//Clé de chiffrement principale
                    this, //context
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,//Méthode de chiffrement de la clé
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM//Méthode de chiffrement des valeurs
            );
            String savedAnswer = FichierSecuriseVar.getString(SECRET_ANSWER_KEY, "");//On récupère la réponse secrète sauvegardée dont le titre est SECRET_ANSWER_KEY dans le fichier securise
            secretAnswerInput.setText(savedAnswer);//On l'affiche sur l'interface
            String savedPassword = FichierSecuriseVar.getString(PASSWORD_KEY, "");//On récupère le mot de passe sauvegardé dont le titre est PASSWORD_KEY dans le fichier securise
            AfficheurPasswordInput.setText(savedPassword);//On l'affiche sur l'interface

        } catch (GeneralSecurityException | IOException e) {//Si une erreur a lieu
            e.printStackTrace();//On affiche l'erreur dans le log cat
            Toast.makeText(this, "Erreur de sécurité, impossible de charger les paramètres", Toast.LENGTH_SHORT).show();//Affichage d'un message d'erreur
        }
    }

    @Override
    protected void onResume() {//Fonction qui est appelee chaque fois que l'activité redevient visible et qui recharge les informations a afficher en appelant la fonction loadSettings
        super.onResume();//Execution du code de la fonction mere
        loadSettings();//On appelle la fonction loadSettings qui va charger les informations contenuent dans le fichier securise pour ensuite les afficher sur l'interface
    }

    @Override
    protected void onPause() {//Fonction qui est appelee chaque fois que l'activite est mise en pause (c'est a dire que l'activite n'est plus visible)
        super.onPause();//Execution du code de la fonction mere
        if (FichierSecuriseVar != null) {//Si le fichier securiser est ouvert
            SharedPreferences.Editor editor = FichierSecuriseVar.edit();//On creer un objet editeur qui a les droits pour modifier le fichier securise
            editor.putString(SECRET_ANSWER_KEY, secretAnswerInput.getText().toString());//On sauvegarde la réponse secrète de l'utilisateur dans le fichier securise dans la clé SECRET_ANSWER_KEY
            editor.apply();//On applique les modifications du fichier securise
            Toast.makeText(this, "Informations sauvegardées", Toast.LENGTH_SHORT).show();//Affichage d'un message de confirmation
        }
    }
}
