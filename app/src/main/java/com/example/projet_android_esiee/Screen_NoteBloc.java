package com.example.projet_android_esiee;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class Screen_NoteBloc extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_note_bloc);
        MaterialButton AddNoteBtn = findViewById(R.id.addnewbtn);

        AddNoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Screen_NoteBloc.this, AddNoteActivity.class));
            }
        });

        Realm.init(getApplicationContext());
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Note> notesList = realm.where(Note.class).findAll();
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        MyAdapter myAdapter = new MyAdapter(getApplicationContext(),notesList);,
        recyclerView.setAdapter(myAdapter);

        notesList.addChangeListener(new RealmChangeListener<RealmResults<Note>>() {
            @Override
            public void onChange(RealmResults<Note> notes) {
                myAdapter.notifyDataSetChanged();
            }
        });

        }
    }
}