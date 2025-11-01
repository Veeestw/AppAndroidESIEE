package com.example.projet_android_esiee;import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class LaunchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);//Execution du code de la fonction mere

        // 1. On vérifie nos SharedPreferences pour voir si c'est le premier lancement
        SharedPreferences prefs = getSharedPreferences("AppConfig", MODE_PRIVATE);//On creer ou on ouvre (s'il existe deja) le fichier AppConfig que l'on va manipuler dans le code avec la variable prefs
        boolean isFirstLaunch = prefs.getBoolean("isFirstLaunch", true); //On recupere la valeur correspondant a l'identifiant "isFirstLaunch" dans le fichier AppConfig. Si elle n'existe pas on lui donne la valeur Par défaut true

        if (isFirstLaunch) {//Si c'est le premier lancement donc isFirstLaunch est true
            startActivity(new Intent(this, SetupActivity.class));//On lance l'activité SetupActivity
        } else {//Si ce n'est pas le premier lancement donc isFirstLaunch est false
            startActivity(new Intent(this, MainActivity.class));//On lance l'activité MainActivity
        }
        finish();//On ferme l'activité LaunchActivity car elle n'est plus utile
    }
}
