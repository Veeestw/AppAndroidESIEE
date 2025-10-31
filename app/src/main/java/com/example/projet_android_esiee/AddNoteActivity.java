package com.example.projet_android_esiee;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;import com.google.android.material.button.MaterialButton;
import java.util.UUID;
import io.realm.Realm;

public class AddNoteActivity extends AppCompatActivity {
    private Realm realm;//On declare un object de type Realm
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);//Execution du code de la fonction mere
        setContentView(R.layout.activity_add_note);//On recupere l'interface graphique de add_note

        EditText titleInput = findViewById(R.id.TitleInput);//On fait le lien entre la variable titleInput et le EditText TitleInput de l'interface
        EditText descriptionInput = findViewById(R.id.DescriptionInput);//On fait le lien entre la variable descriptionInput et le EditText DescriptionInput de l'interface
        MaterialButton saveBtn = findViewById(R.id.SaveBtn);//On fait le lien entre la variable saveBtn et le bouton SaveBtn de l'interface
        MaterialButton retourBtn = findViewById(R.id.RetourAddToNoteBTN);//On fait le lien entre la variable retourBtn et le bouton RetourAddToNoteBTN de l'interface

        realm = Realm.getDefaultInstance();//On precise que l'object "realm" correspond a notre base de donnée par defaut sa structure est definie dans le Note.java

        saveBtn.setOnClickListener(new View.OnClickListener() {//On ajoute un listener sur le bouton SaveBtn
            @Override
            public void onClick(View v) {//Quand on clique sur le bouton SaveBtn
                String title = titleInput.getText().toString();//On récupère le titre saisi dans l'interface
                String description = descriptionInput.getText().toString();//On récupère la description saisi dans l'interface
                long createdTime = System.currentTimeMillis();//On recupere la date de actuelle en millisecondes

                if (!realm.isClosed()) {//Si la base de donnee est ouverte
                    realm.executeTransaction(r -> {//On fait une modification dans la base de donnee
                        String newId = UUID.randomUUID().toString();//On genere un id unique pour la note grace a la librairie UUID et un generateur aleatoire
                        Note note = r.createObject(Note.class, newId);//On creer une object de type note qui s'appelle note ce qui a pour consequence de creer aussi un nouvelle note dans la basse de donnée
                        note.setTitle(title);//On definit le titre de la note (dans le fichier .realm)
                        note.setDescription(description);//On definit la description de la note (dans le fichier .realm)
                        note.setCreatedTime(createdTime);//On definit la date de creation de la note (dans le fichier .realm)
                    });
                    Toast.makeText(AddNoteActivity.this, "Note ajoutée avec succès", Toast.LENGTH_SHORT).show();//Affichage d'un message de confirmation
                    finish();//On ferme l'activité
                }
            }
        });

        retourBtn.setOnClickListener(new View.OnClickListener(){//On ajoute un listener sur le bouton RetourAddToNoteBTN
            @Override
            public void onClick(View v){//Quand on clique sur le bouton RetourAddToNoteBTN
                finish();//On ferme l'activité
            }
        });

    }

    @Override
    protected void onDestroy() {//Fonction qui est appelee juste avant que l'activite ce ferme
        super.onDestroy();//Execution du code de la fonction mere
        if (realm != null) {//Si la base de donnée est ouverte
            realm.close();//On ferme la communication avec la base de donnée
        }
    }
}
