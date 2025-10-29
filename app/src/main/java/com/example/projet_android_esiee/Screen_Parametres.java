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

    private EncryptedSharedPreferences sharedPreferences; // On déclare la variable pour la réutiliser

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_parametres);

        MaterialButton modifierBtn = findViewById(R.id.ModifierBTN);
        MaterialButton retourBtn = findViewById(R.id.RetourParamToNoteBTN);

        secretAnswerInput = findViewById(R.id.SecretAnswer);
        AfficheurPasswordInput = findViewById(R.id.AfficheurPassword);

        modifierBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Screen_Parametres.this, PasswordModif.class));
            }
        });

        retourBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void loadSettings(){
        try {
            // 1. On crée une clé de chiffrement principale gérée par le Keystore
            String masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);

            // 2. On crée une instance de EncryptedSharedPreferences
            sharedPreferences = (EncryptedSharedPreferences) EncryptedSharedPreferences.create(
                    PREFS_NAME, //Nom du fichier
                    masterKeyAlias,
                    this, //context
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
            String savedAnswer = sharedPreferences.getString(SECRET_ANSWER_KEY, "");
            secretAnswerInput.setText(savedAnswer);
            String savedPassword = sharedPreferences.getString(PASSWORD_KEY, "");
            AfficheurPasswordInput.setText(savedPassword);

        } catch (GeneralSecurityException | IOException e) {
            // Gérer l'erreur : il est peu probable qu'elle se produise, mais c'est une bonne pratique
            e.printStackTrace();
            Toast.makeText(this, "Erreur de sécurité, impossible de charger les paramètres", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // onResume est appelé chaque fois que l'activité redevient visible.
        // C'est le moment parfait pour recharger et afficher les données à jour.
        loadSettings();
    }

    @Override
    protected void onPause() {
        super.onPause();

        // On réutilise notre instance de SharedPreferences chiffrée
        if (sharedPreferences != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(SECRET_ANSWER_KEY, secretAnswerInput.getText().toString());
            editor.apply();
            Toast.makeText(this, "Informations sauvegardées", Toast.LENGTH_SHORT).show();
        }
    }
}
