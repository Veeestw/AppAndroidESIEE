package com.example.projet_android_esiee;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.os.Handler;
import com.google.android.material.button.MaterialButton;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import android.widget.Toast;
import android.content.SharedPreferences;
import androidx.appcompat.app.AlertDialog;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;
import java.io.IOException;
import java.security.GeneralSecurityException;

import android.util.Log;


public class PasswordModif extends AppCompatActivity implements View.OnClickListener{

    TextView afficheurCalcul,afficheurResultat;
    MaterialButton button7,button8,button9,button4,button5,button6,button1,button2,button3,button0;
    MaterialButton buttonDiv,buttonMult,buttonSous,buttonAdd,buttonPoint;
    MaterialButton buttonC,buttonParentheseG,buttonParentheseD,buttonRetour,buttonEgal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            afficheurResultat = findViewById(R.id.afficheur_resultat);
            afficheurCalcul = findViewById(R.id.afficheur_calcul);

            assignationId(button0,R.id.button_0);
            assignationId(button1,R.id.button_1);
            assignationId(button2,R.id.button_2);
            assignationId(button3,R.id.button_3);
            assignationId(button4,R.id.button_4);
            assignationId(button5,R.id.button_5);
            assignationId(button6,R.id.button_6);
            assignationId(button7,R.id.button_7);
            assignationId(button8,R.id.button_8);
            assignationId(button9,R.id.button_9);
            assignationId(buttonDiv,R.id.button_div);
            assignationId(buttonMult,R.id.button_mult);
            assignationId(buttonSous,R.id.button_sous);
            assignationId(buttonAdd,R.id.button_add);
            assignationId(buttonPoint,R.id.button_point);
            assignationId(buttonC,R.id.button_C);
            assignationId(buttonParentheseG,R.id.button_parentheseG);
            assignationId(buttonParentheseD,R.id.button_parentheseD);
            assignationId(buttonRetour,R.id.button_retour);
            assignationId(buttonEgal,R.id.button_egal);
            return insets;
        });
    }

    private void NewPassword(String NewPassword){
        try {
            String masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
            SharedPreferences sharedPreferences = EncryptedSharedPreferences.create(
                    Screen_Parametres.PREFS_NAME,
                    masterKeyAlias,
                    this,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(Screen_Parametres.PASSWORD_KEY, NewPassword);
            editor.apply();
            Toast.makeText(this, "Nouveau mot de passe enregistré !", Toast.LENGTH_SHORT).show();
            finish();

        } catch (GeneralSecurityException | IOException e) {
            // This block will run if an error occurs while trying to access the encrypted data.
            e.printStackTrace();
            Toast.makeText(this, "Security Error: Could not verify password.", Toast.LENGTH_SHORT).show();
        }
    }
    void assignationId(MaterialButton btn, int id){
        btn = findViewById(id);
        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        MaterialButton button = (MaterialButton) v;
        String buttonText = button.getText().toString();
        String DonneesCalcul = afficheurCalcul.getText().toString();

        if(buttonText.equals("C")){
            afficheurCalcul.setText("");
            afficheurResultat.setText("0");
            return;
        }
        else if(buttonText.equals("×")){
            DonneesCalcul = DonneesCalcul + "*";
        }

        else if(buttonText.equals("=")){
            afficheurCalcul.setText(afficheurResultat.getText());
            NewPassword(DonneesCalcul);
            return;
        }

        else if(buttonText.equals(" ")){
            DonneesCalcul = DonneesCalcul.substring(0,DonneesCalcul.length()-1);
        }else {
            DonneesCalcul = DonneesCalcul + buttonText;
        }
        afficheurCalcul.setText(DonneesCalcul);
        String ResultatFinal = Resultats(DonneesCalcul);
        if(!ResultatFinal.equals("Err")){
            afficheurResultat.setText(ResultatFinal);
        }
    }

    String Resultats(String donnee){
        try {
            Context context = Context.enter();
            context.setOptimizationLevel(-1);
            Scriptable scriptable = context.initStandardObjects();
            String donnee_final = donnee.replaceAll("(?<=[\\+\\-\\*\\/\\(\\s]|^)0+(?=[1-9])", "");

            if (donnee_final.startsWith("00")) {
                donnee_final = donnee_final.substring(2);
            }

            if(donnee_final.isEmpty()){
                donnee_final = "0";
            }

            if (donnee_final.startsWith(".")) {
                donnee_final = "0" + donnee_final;
            }

            String Resultat = context.evaluateString(scriptable,  donnee_final,"Javascript",1,null).toString();
            if (Resultat.endsWith(".0")){
                Resultat=Resultat.replace(".0","");
            }
            Log.d("DEBUG", "donnee = " + donnee);
            Log.d("DEBUG", "donnee_final = " + donnee_final);
            Log.d("DEBUG", "Resultat = " + Resultat);
            return Resultat;
        }catch (Exception e){
            return "Err";
        }


    }
}