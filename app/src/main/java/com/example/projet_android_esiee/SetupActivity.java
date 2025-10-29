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

public class SetupActivity extends AppCompatActivity {

    private EditText secretAnswerInput;
    private TextView AfficheurPasswordInput;
    private TextView TitleParamInput;
    public static final String PREFS_NAME = "MySecurePrefsFile";
    public static final String SECRET_ANSWER_KEY = "secretAnswer";
    public static final String PASSWORD_KEY = "password";

    private EncryptedSharedPreferences FichierSecuriseVar; // On déclare la variable pour la réutiliser

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);//Execution du code de la fonction mere

        setContentView(R.layout.activity_screen_parametres); //On recupere l'interface graphique de screen_parametre

        setupEncryptedSharedPreferences();//Appel de la fonction setupEncryptedSharedPreferences qui permet de creer ou de d'ouvrir le fichier securiser

        MaterialButton modifierBtn = findViewById(R.id.ModifierBTN); //On fait le lien entre la variable modifierBTN et le bouton ModifierBTN de l'interface

        MaterialButton retourBtn = findViewById(R.id.RetourParamToNoteBTN); //On fait le lien entre la variable retourBTN et le bouton RetourParamToNoteBTN de l'interface
        retourBtn.setVisibility(View.GONE); //On cache le bouton RetourParamToNoteBTN sur l'interface

        MaterialButton ValidateSetupBTN = findViewById(R.id.ValidateSetupBTN); //On fait le lien entre la variable ValidateSetupBTN et le bouton ValidateSetupBTN de l'interface
        ValidateSetupBTN.setVisibility(View.VISIBLE);//On affiche le bouton ValidateSetupBTN sur l'interface

        TitleParamInput = findViewById(R.id.TitleParam);//On fait le lien entre la variable TitleParamInput et le TextView TitleParam de l'interface
        secretAnswerInput = findViewById(R.id.SecretAnswer);//On fait le lien entre la variable secretAnswerInput et le EditText SecretAnswer de l'interface
        AfficheurPasswordInput = findViewById(R.id.AfficheurPassword);//On fait le lien entre la variable AfficheurPasswordInput et le TextView AfficheurPassword de l'interface

        TitleParamInput.setText("Initialisation"); //On modifie le texte du TextView TitleParamInput

        modifierBtn.setOnClickListener(new View.OnClickListener() { //On ajoute un listener sur le bouton ModifierBTN
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SetupActivity.this, PasswordModif.class));//On lance l'activité PasswordModif ce qui fait changer l'interface graphique
            }
        });

        ValidateSetupBTN.setOnClickListener(new View.OnClickListener() {//On ajoute un listener sur le bouton ValidateSetupBTN
            @Override
            public void onClick(View v) {
                saveSettings();//On appelle la fonction saveSettings qui va sauvegarder les données dans un fichier securise
            }
        });

    }

    private void setupEncryptedSharedPreferences() {
        try {
            String masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);//On crée une clé de chiffrement principale
            FichierSecuriseVar = (EncryptedSharedPreferences) EncryptedSharedPreferences.create(//On creer ou on ouvre (si il existe deja) le fichier securise que l'on va manipuler dans le code avec la variable FichierSecuriseVar
                    PREFS_NAME,//Nom du fichier
                    masterKeyAlias,//Clé de chiffrement principale
                    this,//context
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,//Méthode de chiffrement de la clé
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM//Méthode de chiffrement des valeurs
            );
        } catch (GeneralSecurityException | IOException e) {//Si une erreur a lieu
            e.printStackTrace();//On affiche l'erreur dans le log cat
            Toast.makeText(this, "Erreur critique de sécurité, impossible d'initialiser.", Toast.LENGTH_LONG).show();//Affichage d'un message d'erreur
            finish(); // On ferme l'activité si le fichier sécurisé ne peut pas être créé.
        }
    }

    private void loadSettings() { //Fonction qui a pour but de charger les informations contenuent dans le fichier securise pour ensuite les afficher sur l'interface
        if (FichierSecuriseVar != null) {
            String savedAnswer = FichierSecuriseVar.getString(SECRET_ANSWER_KEY, "");//On récupère la réponse secrète sauvegardée dont le titre est SECRET_ANSWER_KEY dans le fichier securise
            secretAnswerInput.setText(savedAnswer);//On l'affiche sur l'interface
            String savedPassword = FichierSecuriseVar.getString(PASSWORD_KEY, "");//On récupère le mot de passe sauvegardé dont le titre est PASSWORD_KEY dans le fichier securise
            AfficheurPasswordInput.setText(savedPassword);//On l'affiche sur l'interface
        }
    }

    private void saveSettings() {//Fonction qui a pour but de sauvegarder les informations dans le fichier securise puis de lancer l'activité MainActivity
        String password = AfficheurPasswordInput.getText().toString();//On récupère le mot de passe saisi dans l'interface
        String secretAnswer = secretAnswerInput.getText().toString();//On récupère la réponse secrète saisi dans l'interface

        if (password.isEmpty()) { //On vérifie que le mot de passe n'est pas vide
            Toast.makeText(this, "Veuillez d'abord définir un mot de passe.", Toast.LENGTH_LONG).show();//Affichage d'un message d'erreur
            return;
        }
        if (secretAnswer.isEmpty()) {//On vérifie que la réponse secrète n'est pas vide
            Toast.makeText(this, "Veuillez définir une réponse à la question secrète.", Toast.LENGTH_LONG).show();//Affichage d'un message d'erreur
            return;
        }

        if (FichierSecuriseVar != null) {
            SharedPreferences.Editor editor = FichierSecuriseVar.edit();//On creer un objet editeur qui a les droits pour modifier le fichier securise
            editor.putString(Screen_Parametres.SECRET_ANSWER_KEY, secretAnswer);//On sauvegarde la réponse secrète dans le fichier securise dans la clé SECRET_ANSWER_KEY
            editor.apply();//On applique les modifications du fichier securise

            SharedPreferences AppConfigVar = getSharedPreferences("AppConfig", MODE_PRIVATE);//On creer ou on ouvre (si il existe deja) le fichier de configuration AppConfig qu'on manipule ensuite dans le code avec la variable AppConfigVar
            AppConfigVar.edit().putBoolean("isFirstLaunch", false).apply();//On initialise ou on modifie la variable isFirstLaunch du fichier de configuration AppConfig a false

            Toast.makeText(this, "Configuration terminée !", Toast.LENGTH_SHORT).show();//Affichage d'un message de confirmation

            startActivity(new Intent(this, MainActivity.class));//On lance l'activité MainActivity
            finish();//On ferme l'activité SetupActivity
        }
    }

    private void saveSecretAnswerOnly() {//Fonction qui a pour but de sauvegarder uniquement la réponse secrète dans le fichier securise
        String secretAnswer = secretAnswerInput.getText().toString();//On récupère la réponse secrète saisi dans l'interface

        if (secretAnswer.isEmpty()) {//On vérifie que la réponse secrète n'est pas vide (Pour eviter de faire toute la procedure de sauvegarde pour rien)
            return;
        }

        if (FichierSecuriseVar != null) {
            SharedPreferences.Editor editor = FichierSecuriseVar.edit();//On creer un objet editeur qui a les droits pour modifier le fichier securise
            editor.putString(SECRET_ANSWER_KEY, secretAnswer);//On sauvegarde la réponse secrète dans le fichier securise dans la clé SECRET_ANSWER_KEY
            editor.apply();//On applique les modifications du fichier securise
        }
    }

    @Override
    protected void onResume() {//Fonction qui est appelee chaque fois que l'activité redevient visible et qui recharge les informations a afficher en appelant la fonction loadSettings
        super.onResume();//Execution du code de la fonction mere
        loadSettings();//On appelle la fonction loadSettings qui va charger les informations contenuent dans le fichier securise pour ensuite les afficher sur l'interface
    }

    @Override
    protected void onPause() {//Fonction qui est appelee chaque fois que l'activite est mise en pause (c'est a dire que l'activite n'est plus visible) et qui sauvegarde uniquement la réponse secrète en appelant la fonction saveSecretAnswerOnly
        super.onPause();//Execution du code de la fonction mere
        saveSecretAnswerOnly();//On appelle la fonction saveSecretAnswerOnly qui va sauvegarder uniquement la réponse secrète dans le fichier securise
    }

}
