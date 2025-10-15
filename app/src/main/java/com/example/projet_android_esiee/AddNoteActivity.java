package com.example.projet_android_esiee;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import io.realm.Realm;

import com.google.android.material.button.MaterialButton;

public class AddNoteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        EditText titleInput = findViewById(R.id.TitleInput);
        EditText descriptionInput = findViewById(R.id.DescriptionInput);
        MaterialButton saveBtn = findViewById(R.id.SaveBtn);

        Realm.init(getApplicationContext());
        Realm realm = Realm.getDefaultInstance();


        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = titleInput.getText().toString();
                String description = descriptionInput.getText().toString();
                long createdTime = System.currentTimeMillis();

                realm.beginTransaction();
                Note note = realm.createObject(Note.class);
                note.setTitle(title);
                note.setDescription(description);
                note.setCreatedTime(createdTime);
                realm.commitTransaction();
                Toast.makeText(AddNoteActivity.this, "Note ajoutée avec succès", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }
}