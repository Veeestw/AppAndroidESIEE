package com.example.projet_android_esiee;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;

public class Screen_NoteBloc extends AppCompatActivity {

    private Realm realm;
    private RealmResults<Note> notesList;
    private MyAdapter myAdapter;
    private RealmChangeListener<RealmResults<Note>> realmChangeListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_note_bloc);

        MaterialButton addNoteBtn = findViewById(R.id.addnewbtn);
        MaterialButton retourBtn = findViewById(R.id.RetourcalculatriceBTN);
        MaterialButton ParametresBtn = findViewById(R.id.ParametresBTN);
        RecyclerView recyclerView = findViewById(R.id.recyclerview);

        realm = Realm.getDefaultInstance();

        notesList = realm.where(Note.class).sort("createdTime", Sort.DESCENDING).findAll();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        myAdapter = new MyAdapter(this, notesList);
        recyclerView.setAdapter(myAdapter);

        realmChangeListener = new RealmChangeListener<RealmResults<Note>>() {
            @Override
            public void onChange(RealmResults<Note> updatedNotes) {
                if (myAdapter != null) {
                    myAdapter.notifyDataSetChanged();
                }
            }
        };

        notesList.addChangeListener(realmChangeListener);

        addNoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Screen_NoteBloc.this, AddNoteActivity.class));
            }
        });

        ParametresBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Screen_NoteBloc.this, Screen_Parametres.class));
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

        if (notesList != null) {
            notesList.removeChangeListener(realmChangeListener);
        }

        if (realm != null) {
            realm.close();
        }
    }
}
