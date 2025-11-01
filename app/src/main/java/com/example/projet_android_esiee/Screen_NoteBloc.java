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
        super.onCreate(savedInstanceState);//Execution du code de la fonction mere
        setContentView(R.layout.activity_screen_note_bloc);//On recupere l'interface graphique de screen_note_bloc
        MaterialButton addNoteBtn = findViewById(R.id.addnewbtn);//On fait le lien entre la variable addNoteBtn et le bouton addnewbtn de l'interface
        MaterialButton retourBtn = findViewById(R.id.RetourcalculatriceBTN);//On fait le lien entre la variable retourBtn et le bouton RetourcalculatriceBTN de l'interface
        MaterialButton ParametresBtn = findViewById(R.id.ParametresBTN);//On fait le lien entre la variable ParametresBtn et le bouton ParametresBTN de l'interface
        RecyclerView recyclerView = findViewById(R.id.recyclerview);//On fait le lien entre la variable recyclerView et le RecyclerView recyclerview de l'interface

        realm = Realm.getDefaultInstance();//On precise que l'object "realm" correspond a notre base de donnée par defaut sa structure est definie dans le Note.java

        notesList = realm.where(Note.class).sort("createdTime", Sort.DESCENDING).findAll();//Dans la base de donnee correspondant a la variable realm trouve moi la classe Note. Puis trie les resultats en fonction de leurs composante "createdTime" dans l ordre decroissant. Enfin retourne le resultat.

        recyclerView.setLayoutManager(new LinearLayoutManager(this));//On definis le format de notre liste de notes. Ici LinearLayoutManager veut dire que nos notes seront les unes au dessus des autres.
        myAdapter = new MyAdapter(this, notesList);//On creer une instance de MyAdapter qui va avoir pour role de relier les informations de la base de donnee avec notre RecyclerView. Il va notamment nous informer du nombre de notes a afficher et de la maniere dont elles doivent etre affichees (il assemble les informations de la note de la basse de donnee pour les mettres dans le canva definis dans item_view.xml)
        recyclerView.setAdapter(myAdapter);//On informe le recyclerview qu'il doit ce baser sur les informations et la disposition de l'instance myAdapter

        realmChangeListener = new RealmChangeListener<RealmResults<Note>>() {//On ajoute un listener sur la base de donnee
            @Override
            public void onChange(RealmResults<Note> updatedNotes) {//Si il y a du changement sur une des notes
                if (myAdapter != null) {//On verifie que myAdapter est bien defini
                    myAdapter.notifyDataSetChanged();//On demande a myAdapter de refaire tout son travail de mise en forme grace au informations de la base de donnée et au canva definit dans item_view.xml
                }
            }
        };
        notesList.addChangeListener(realmChangeListener);//Activation de l ecoute sur la base de donnee

        addNoteBtn.setOnClickListener(new View.OnClickListener() {//On ajoute un listener sur le bouton addnewbtn
            @Override
            public void onClick(View v) {//Quand on clique sur le bouton addnewbtn
                startActivity(new Intent(Screen_NoteBloc.this, AddNoteActivity.class));//On lance l'activité AddNoteActivity
            }
        });

        ParametresBtn.setOnClickListener(new View.OnClickListener() {//On ajoute un listener sur le bouton ParametresBTN
            @Override
            public void onClick(View v) {//Quand on clique sur le bouton ParametresBTN
                startActivity(new Intent(Screen_NoteBloc.this, Screen_Parametres.class));//On lance l'activité Screen_Parametres
            }
        });

        retourBtn.setOnClickListener(new View.OnClickListener(){//On ajoute un listener sur le bouton RetourcalculatriceBTN
            @Override
            public void onClick(View v){//Quand on clique sur le bouton RetourcalculatriceBTN
                finish();//On ferme l'activité
            }
        });
    }

    @Override
    protected void onDestroy() {//Fonction qui est appelee juste avant que l'activite ce ferme
        super.onDestroy();//Execution du code de la fonction mere
        if (notesList != null) {//Securite pour eviter un crash : si la notelist est vide alors on desactive le listener
            notesList.removeChangeListener(realmChangeListener);//On desactive le listener
        }

        if (realm != null) {//Si la base de donnee est ouverte
            realm.close();//On ferme la communication avec la base de donnee
        }
    }
}
