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


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    TextView afficheurCalcul,afficheurResultat;
    MaterialButton button7,button8,button9,button4,button5,button6,button1,button2,button3,button0;
    MaterialButton buttonDiv,buttonMult,buttonSous,buttonAdd,buttonPoint;
    MaterialButton buttonC,buttonParentheseG,buttonParentheseD,buttonRetour,buttonEgal;

    private Handler longPressHandler = new Handler();//Gestionnaire des taches qui permet d'executer des morceaux de code sous certaine condition que l'on peut definir
    private Runnable longPressRunnable = new Runnable() {//Correspond au code qui va etre executer par le longPressHandler
        @Override
        public void run() {
            showSecretQuestionDialog();
        }//pas comrpis
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);//Execution du code de la fonction mere
        setContentView(R.layout.activity_main);//On recupere l'interface graphique de activity_main


        EdgeToEdge.enable(this);//Activation du mode "bord a bord" prendre tout l'ecran disponible
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {//Permet gerer l'affiche de l'app en prenant en compte les barres d'etats
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);


            afficheurResultat = findViewById(R.id.afficheur_resultat);//On fait le lien entre la variable afficheurResultat et le TextView afficheur_resultat de l'interface
            afficheurCalcul = findViewById(R.id.afficheur_calcul);//On fait le lien entre la variable afficheurCalcul et le TextView afficheur_calcul de l'interface

            assignationId(button0,R.id.button_0);//On fait le lien entre la variable button0 et le bouton button_0 de l'interface
            assignationId(button1,R.id.button_1);//On fait le lien entre la variable button1 et le bouton button_1 de l'interface
            assignationId(button2,R.id.button_2);//On fait le lien entre la variable button2 et le bouton button_2 de l'interface
            assignationId(button3,R.id.button_3);//On fait le lien entre la variable button3 et le bouton button_3 de l'interface
            assignationId(button4,R.id.button_4);//On fait le lien entre la variable button4 et le bouton button_4 de l'interface
            assignationId(button5,R.id.button_5);//On fait le lien entre la variable button5 et le bouton button_5 de l'interface
            assignationId(button6,R.id.button_6);//On fait le lien entre la variable button6 et le bouton button_6 de l'interface
            assignationId(button7,R.id.button_7);//On fait le lien entre la variable button7 et le bouton button_7 de l'interface
            assignationId(button8,R.id.button_8);//On fait le lien entre la variable button8 et le bouton button_8 de l'interface
            assignationId(button9,R.id.button_9);//On fait le lien entre la variable button9 et le bouton button_9 de l'interface
            assignationId(buttonDiv,R.id.button_div);//On fait le lien entre la variable buttonDiv et le bouton button_div de l'interface
            assignationId(buttonMult,R.id.button_mult);//On fait le lien entre la variable buttonMult et le bouton button_mult de l'interface
            assignationId(buttonSous,R.id.button_sous);//On fait le lien entre la variable buttonSous et le bouton button_sous de l'interface
            assignationId(buttonAdd,R.id.button_add);//On fait le lien entre la variable buttonAdd et le bouton button_add de l'interface
            assignationId(buttonPoint,R.id.button_point);//On fait le lien entre la variable buttonPoint et le bouton button_point de l'interface
            assignationId(buttonC,R.id.button_C);//On fait le lien entre la variable buttonC et le bouton button_C de l'interface
            assignationId(buttonParentheseG,R.id.button_parentheseG);//On fait le lien entre la variable buttonParentheseG et le bouton button_parentheseG de l'interface
            assignationId(buttonParentheseD,R.id.button_parentheseD);//On fait le lien entre la variable buttonParentheseD et le bouton button_parentheseD de l'interface
            assignationId(buttonRetour,R.id.button_retour);//On fait le lien entre la variable buttonRetour et le bouton button_retour de l'interface
            assignationId(buttonEgal,R.id.button_egal);//On fait le lien entre la variable buttonEgal et le bouton button_egal de l'interface
            return insets;
        });
    }

    private void showSecretQuestionDialog() {//Fonction qui permet d'afficher la boite de dialogue de question secrète
        AlertDialog.Builder builder = new AlertDialog.Builder(this);//Création de la boite de dialogue
        builder.setTitle("| Accès Sécurisé |");//Titre de la boite de dialogue
        builder.setMessage("Quelle est votre prof d'Android ?");//Message de la boite de dialogue
        final android.widget.EditText input = new android.widget.EditText(this);//Création de l'input de la boite de dialogue
        input.setHint("Votre réponse");//On definit le texte qui sera ecrit en grisé dans le fond de l'input
        builder.setView(input);//On definit l'input dans la boite de dialogue

        builder.setPositiveButton("Valider", (dialog, which) -> {//Définition du bouton "Valider"
            String userAnswer = input.getText().toString();//On recupere la réponse de l'utilisateur
            try {
                String masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);//Création de la clé de chiffrement principale
                SharedPreferences FichierSecuriseVar = EncryptedSharedPreferences.create(//On creer ou on ouvre (si il existe deja) le fichier securise que l'on va manipuler dans le code avec la variable FichierSecuriseVar
                        Screen_Parametres.PREFS_NAME,//Nom du fichier
                        masterKeyAlias,//Clé de chiffrement principale
                        this,//Context
                        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,//Méthode de chiffrement de la clé
                        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM//Méthode de chiffrement des valeurs
                );

                String correctAnswer = FichierSecuriseVar.getString(Screen_Parametres.SECRET_ANSWER_KEY, "");//On récupère la réponse secrète sauvegardée dont le titre est SECRET_ANSWER_KEY dans le fichier securise

                if (!correctAnswer.isEmpty() && userAnswer.equals(correctAnswer)) {//On vérifie que la réponse sauvegardée n'est pas vide ET qu'elle correspond à celle de l'utilisateur
                    startActivity(new Intent(MainActivity.this, Screen_NoteBloc.class));//On lance l'écran des notes
                } else {
                    Toast.makeText(MainActivity.this, "Mauvaise réponse", Toast.LENGTH_SHORT).show();//Affichage d'un message d'erreur
                }

            } catch (GeneralSecurityException | IOException e) {//Si une erreur a lieu
                e.printStackTrace();//On affiche l'erreur dans le log cat
                Toast.makeText(this, "Erreur de sécurité, impossible de vérifier la réponse", Toast.LENGTH_SHORT).show();//Affichage d'un message d'erreur
            }
        });

        builder.setNegativeButton("Annuler", (dialog, which) -> dialog.cancel());//Définition du bouton "Annuler". Par defaut le negativebutton permet de fermer la boite de dialogue
        builder.show();//On affiche la boite de dialogue
    }

    private void PasswordVerification(String DonneesCalcul){//Fonction qui permet de verifier le mot de passe
        try {
            String masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);//Création de la clé de chiffrement principale
            SharedPreferences FichierSecuriseVar = EncryptedSharedPreferences.create(//On creer ou on ouvre (si il existe deja) le fichier securise que l'on va manipuler dans le code avec la variable FichierSecuriseVar
                    Screen_Parametres.PREFS_NAME,//Nom du fichier
                    masterKeyAlias,//Clé de chiffrement principale
                    this,//Context
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,//Méthode de chiffrement de la clé
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM//Méthode de chiffrement des valeurs
            );

            String correctAnswer = FichierSecuriseVar.getString(Screen_Parametres.PASSWORD_KEY, "");//On récupère le mot de passe sauvegardé dont le titre est PASSWORD_KEY dans le fichier securise

            if (!correctAnswer.isEmpty() && DonneesCalcul.equals(correctAnswer)) {//On vérifie que le mot de passe sauvegardé n'est pas vide ET qu'il correspond au mot de passe entre par l'utilisateur
                startActivity(new Intent(MainActivity.this, Screen_NoteBloc.class));//On lance l'écran des notes
            }
        } catch (GeneralSecurityException | IOException e) {//Si une erreur a lieu
            e.printStackTrace();//On affiche l'erreur dans le log cat
            Toast.makeText(this, "Security Error: Could not verify password.", Toast.LENGTH_SHORT).show();//Affichage d'un message d'erreur
        }

    }
    void assignationId(MaterialButton btn, int id){//Fonction qui permet d'associer une variable bouton a un bouton de l'interface
        btn = findViewById(id);//On associe la variable bouton a un bouton de l'interface
        if (id == R.id.button_C) {//Si le bouton est le bouton C
            btn.setOnClickListener(this);//On ajoute un listener sur le bouton
            btn.setOnTouchListener(new View.OnTouchListener() {//On ajoute un listener de type "long click" sur le bouton
                @Override
                public boolean onTouch(View v, android.view.MotionEvent event) {//Fonction qui est appelee lorsque l'on clique sur le bouton
                    switch (event.getAction()) {//En fonction de l'event qui vient d'avoir lieu sur le bouton
                        case android.view.MotionEvent.ACTION_DOWN://Si l'utilisateur a appuyer sur le bouton
                            longPressHandler.postDelayed(longPressRunnable, 5000);//Parametrage du Handler qui va executer la fonction longPressRunnable apres 5 secondes
                            break;//On sort de la condition
                        case android.view.MotionEvent.ACTION_UP://Si l'utilisateur a relever son doigt
                        case android.view.MotionEvent.ACTION_CANCEL://Si l'utilisateur a annule sont action
                            longPressHandler.removeCallbacks(longPressRunnable);//On supprime la tache du Handler
                            break;//On sort de la condition
                    }
                    return false;//On retourne false pour indiquer que l'evenement n'a pas ete consomme ce qui veut dire qu'il peut encore declancher d'autre listener et notamment "OnClick". Ainsi si l'utilisateur ne reste pas appuyer assez longtemps sur le bouton ce sera la fonction "normal" du bouton C qui va s'executer
                }
            });
        }else btn.setOnClickListener(this);//Pour tout les autre boutons on ajoute simplement un listener
    }

    @Override
    public void onClick(View v) {//Fonction qui est appelee lorsque l'on clique sur un bouton
        MaterialButton button = (MaterialButton) v;//On cast v pour s'assurer que c'est bien un bouton et on recupere le bouton cliqué
        String buttonText = button.getText().toString();//On recupere le texte du bouton cliqué au format string
        String DonneesCalcul = afficheurCalcul.getText().toString();//On recupere le texte du TextView afficheur_calcul (l'afficheur du calcul en cours) au format string

        if(buttonText.equals("C")){//Si on clique sur le bouton C
            afficheurCalcul.setText("");//On efface le calcul en cours
            afficheurResultat.setText("0");//On passe le resultat a 0
            return;//On quitte la fonction
        }
        else if(buttonText.equals("×")){//Si on clique sur le bouton ×
            DonneesCalcul = DonneesCalcul + "*";//On ajoute le symbole * a la chaine de caractere qui correspond au calcul en cours
        }

        else if(buttonText.equals("=")){//Si on clique sur le bouton =
            afficheurCalcul.setText(afficheurResultat.getText());//On passe le resultat du calcul en cours a l'afficheur du calcul
            PasswordVerification(DonneesCalcul);//On appelle la fonction PasswordVerification qui permet de verifier le mot de passe
            return;//On quitte la fonction
        }

        else if(buttonText.equals(" ")){//Si on clique sur le bouton retour
            if(!DonneesCalcul.isEmpty()){//Si le calcul n'est pas vide
                DonneesCalcul = DonneesCalcul.substring(0,DonneesCalcul.length()-1);//On supprime le dernier caractere de la chaine de caractere correspondant au calcul en cours
            }
            else{//Si le calcul est vide
                afficheurCalcul.setText(""); // On veut que l'afficheur du haut soit vide
                afficheurResultat.setText("0"); // Et celui du bas à 0
                return;//On quitte la fonction pour eviter de faire tout ce qui suit et notamment demander a l'afficheur d'afficher du "rien"
            }
        }else {
            DonneesCalcul = DonneesCalcul + buttonText;//Pour tout les autres cas on ajoute le texte du bouton cliqué a la chaine de caractere correspondant au calcul en cours
        }

        afficheurCalcul.setText(DonneesCalcul);//On affiche le calcul en cours sur l'afficheur du calcul
        String ResultatFinal = Resultats(DonneesCalcul);//On appelle la fonction Resultats qui permet de calculer le resultat du calcul en cours
        if(!ResultatFinal.equals("Err")){//Si le calcul n'est pas en erreur
            afficheurResultat.setText(ResultatFinal);//On affiche le resultat du calcul en cours sur l'afficheur du resultat
        }
    }

    String Resultats(String donnee){//Fonction qui permet de calculer le resultat du calcul
        try {
            Context context = Context.enter();//Context necessaire pour l'activation du JavaScript
            context.setOptimizationLevel(-1);//Desactivation de l'optimisation du JavaScript (pour les perf)
            Scriptable scriptable = context.initStandardObjects();//Activation du JavaScript
            donnee = donnee.replaceAll("0+\\.", "0.");//Si on trouve un ou plusieurs 0 de suite (0+) suivis par le caractere point (\\.) on les remplace par 0.
            String donnee_final = donnee.replaceAll("(?<=[\\+\\-\\*\\/\\(\\s]|^)0+(?=[1-9])", "");//Si on trouve un ou plusieurs 0 de suite (0+) qui sont places apres un operature : +;-;*;/;(;esp;tab ([\+\-\*\/\(\s]) OU que l'on est au depuis de la chaine de caractere (|^) ET qui sont suivis par un chiffre de 1 a 9 ((?=[1-9]))

            if (donnee_final.startsWith("00")) {//Si le calcul commence par 00
                donnee_final = donnee_final.substring(2);//On supprime les deux premiers caracteres (donc les deux 0)
            }

            if(donnee_final.isEmpty()){//Si le calcul est vide
                donnee_final = "0";//le resultat est 0
            }

            if (donnee_final.startsWith(".")) {//Si le calcul commence par .
                donnee_final = "0" + donnee_final;//On ajoute un 0 devant
            }

            String Resultat = context.evaluateString(scriptable,  donnee_final,"Javascript",1,null).toString();//Fonction qui permet de calculer le resultat du calcul en cours grace a un environnement JavaScript
            if (Resultat.endsWith(".0")){//Si le resultat se termine par .0
                Resultat=Resultat.replace(".0","");//On supprime le .0
            }
            Log.d("DEBUG", "donnee = " + donnee);//Log de debug
            Log.d("DEBUG", "donnee_final = " + donnee_final);//Log de debug
            Log.d("DEBUG", "Resultat = " + Resultat);//Log de debug
            return Resultat;//On retourne le resultat
        }catch (Exception e){//Si une erreur a lieu
            e.printStackTrace();//On affiche l'erreur dans le log cat
            return "Err";//On retourne Err
        }

    }
}