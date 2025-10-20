package com.example.projet_android_esiee;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;import com.google.android.material.button.MaterialButton;
import java.util.UUID;
import io.realm.Realm;

public class AddNoteActivity extends AppCompatActivity {
    private Realm realm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        EditText titleInput = findViewById(R.id.TitleInput);
        EditText descriptionInput = findViewById(R.id.DescriptionInput);
        MaterialButton saveBtn = findViewById(R.id.SaveBtn);
        MaterialButton retourBtn = findViewById(R.id.RetourAddToNoteBTN);

        realm = Realm.getDefaultInstance();

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = titleInput.getText().toString();
                String description = descriptionInput.getText().toString();
                long createdTime = System.currentTimeMillis();

                if (!realm.isClosed()) {
                    realm.executeTransaction(r -> {
                        String newId = UUID.randomUUID().toString();
                        Note note = r.createObject(Note.class, newId);
                        note.setTitle(title);
                        note.setDescription(description);
                        note.setCreatedTime(createdTime);
                    });
                    Toast.makeText(AddNoteActivity.this, "Note ajoutée avec succès", Toast.LENGTH_SHORT).show();
                    finish();
                }
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
        if (realm != null) {
            realm.close();
        }
    }
}
