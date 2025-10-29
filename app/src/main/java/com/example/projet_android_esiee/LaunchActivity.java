package com.example.projet_android_esiee;import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class LaunchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Pas besoin de setContentView, cette activité est invisible.

        // 1. On vérifie nos SharedPreferences pour voir si c'est le premier lancement
        SharedPreferences prefs = getSharedPreferences("AppConfig", MODE_PRIVATE);
        boolean isFirstLaunch = prefs.getBoolean("isFirstLaunch", true); // Par défaut, c'est true

        if (isFirstLaunch) {
            // C'est le premier lancement : on va vers l'écran de configuration
            startActivity(new Intent(this, SetupActivity.class));
        } else {
            // Ce n'est pas le premier lancement : on va directement à la calculatrice
            startActivity(new Intent(this, MainActivity.class));
        }

        // 3. On ferme cette activité de transition pour qu'on ne puisse pas y revenir avec le bouton "Retour"
        finish();
    }
}
