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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        EditText titleInput = findViewById(R.id.edit_title_input);
        EditText descriptionInput = findViewById(R.id.edit_description_input);
        MaterialButton updateButton = findViewById(R.id.update_note_btn);
        MaterialButton retourBtn = findViewById(R.id.RetourEditToNoteBTN);



        realm = Realm.getDefaultInstance();

        // 1. Récupérer l'ID de la note envoyé par l'adaptateur
        String noteId = getIntent().getStringExtra("NOTE_ID");

        // 2. Trouver la note dans Realm en utilisant cet ID
        //    findFirst() retourne la première note qui correspond à la condition.
        noteToEdit = realm.where(Note.class).equalTo("id", noteId).findFirst();

        // 3. Remplir les champs avec les données actuelles de la note
        if (noteToEdit != null) {
            titleInput.setText(noteToEdit.getTitle());
            descriptionInput.setText(noteToEdit.getDescription());
        }

        // 4. Définir le clic sur le bouton "Mettre à jour"
        updateButton.setOnClickListener(v -> {
            if (noteToEdit != null) {
                String newTitle = titleInput.getText().toString();
                String newDescription = descriptionInput.getText().toString();
                long createdTime = System.currentTimeMillis();
                // On lance une transaction pour modifier l'objet
                realm.executeTransaction(r -> {
                    // C'est ici qu'on met à jour les champs de notre objet existant
                    noteToEdit.setTitle(newTitle);
                    noteToEdit.setDescription(newDescription);
                    noteToEdit.setCreatedTime(createdTime);
                });

                Toast.makeText(this, "Note mise à jour", Toast.LENGTH_SHORT).show();
                finish(); // On ferme l'écran et on retourne à la liste
            }
        });

        retourBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Toujours fermer Realm quand l'activité est détruite
        if (realm != null) {
            realm.close();
        }
    }
}
