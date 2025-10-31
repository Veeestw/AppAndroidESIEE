package com.example.projet_android_esiee;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Note extends RealmObject {//En faisant extends RealmObject, Note devient un object de la base de donnee Realm ainsi lorsque l on va appler les getter et setter, c est Realm qui va aller chercher ou modifier dans la base de donnee les informations
    @PrimaryKey
    private String id;
    String title;
    String description;
    long createdTime;

    public String getId() {//Fonction de recuperation de l'id de la note
        return id;
    }

    public void setId(String id) {//Fonction de modification de l'id de la note
        this.id = id;
    }

    public String getTitle() {
        return title;
    } //Fonction de recuperation du titre de la note

    public void setTitle(String title) {//Fonction de modification du titre de la note
        this.title = title;
    }

    public String getDescription() {//Fonction de recuperation de la description de la note
        return description;
    }


    public void setDescription(String description) {//Fonction de modification de la description de la note
        this.description = description;
    }


    public long getCreatedTime() {//Fonction de recuperation de la date de creation de la note
        return createdTime;
    }


    public void setCreatedTime(long createdTime) {//Fonction de modification de la date de creation de la note
        this.createdTime = createdTime;
    }
}
