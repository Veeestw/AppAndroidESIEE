package com.example.projet_android_esiee;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import io.realm.Realm;

public class EditNoteActivity extends AppCompatActivity {

    private Realm realm;
    private Note noteToEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);//Execution du code de la fonction mere
        setContentView(R.layout.activity_edit_note);//On recupere l'interface graphique de edit_note

        EditText titleInput = findViewById(R.id.edit_title_input);//On fait le lien entre la variable titleInput et le champ de texte title_input de l'interface
        EditText descriptionInput = findViewById(R.id.edit_description_input);//On fait le lien entre la variable descriptionInput et le champ de texte description_input de l'interface
        MaterialButton updateButton = findViewById(R.id.update_note_btn);//On fait le lien entre la variable updateButton et le bouton update_note_btn de l'interface
        MaterialButton retourBtn = findViewById(R.id.RetourEditToNoteBTN);//On fait le lien entre la variable retourBtn et le bouton RetourEditToNoteBTN de l'interface

        realm = Realm.getDefaultInstance();//On precise que l'object "realm" correspond a notre base de donnée par defaut sa structure est definie dans le Note.java

        String noteId = getIntent().getStringExtra("NOTE_ID");//On recupere l'id qui nous a ete transmit lors de l'ouverture de cette activite

        noteToEdit = realm.where(Note.class).equalTo("id", noteId).findFirst();//On recupere la note de la base de donnee qui correspond a cet id (recherche optimise grace a findfirst() qui est concu pour ce genre de cas)

        if (noteToEdit != null) {//Si la note a ete trouvee
            titleInput.setText(noteToEdit.getTitle());//On remplis les champs "TitleInput" de l'interface avec les informations de la note
            descriptionInput.setText(noteToEdit.getDescription());//On remplis les champs "DescriptionInput" de l'interface avec les informations de la note
        }

        updateButton.setOnClickListener(v -> {//On ajoute un listener sur le bouton update_note_btn
            if (noteToEdit != null) {//Si la note a ete trouvee
                String newTitle = titleInput.getText().toString();//On recupere le nouveau titre de la note et on le formate en string
                String newDescription = descriptionInput.getText().toString();//On recupere la nouvelle description de la note et on la formate en string
                long createdTime = System.currentTimeMillis();//On recupere le temps actuel en millisecondes
                realm.executeTransaction(r -> {//On commence une transaction (qui va permettre de faire des modifications sur la base de donnee)
                    noteToEdit.setTitle(newTitle);//On modifie la base de donnee avec le nouveau titre de la note
                    noteToEdit.setDescription(newDescription);//On modifie la base de donee avec la nouvelle description de la note
                    noteToEdit.setCreatedTime(createdTime);//On modifie la base de donnee avec le nouveau temps de creation de la note
                });

                Toast.makeText(this, "Note mise à jour", Toast.LENGTH_SHORT).show();//Affichage d'un message de confirmation
                finish(); // On ferme l'écran et on retourne à la liste
            }
        });

        retourBtn.setOnClickListener(new View.OnClickListener(){//On ajoute un listener sur le bouton RetourEditToNoteBTN
            @Override
            public void onClick(View v){//Si on clique sur le bouton RetourEditToNoteBTN
                finish();//On ferme l'activite
            }
        });

    }

    @Override
    protected void onDestroy() {//Fonction appelee lorsque l'activité est détruite
        super.onDestroy();//Execution du code de la fonction mere
        if (realm != null) {//Si la base de donnee est ouverte
            realm.close();//On la ferme
        }
    }
}
