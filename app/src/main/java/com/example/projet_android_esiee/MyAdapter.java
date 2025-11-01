package com.example.projet_android_esiee;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.content.Intent;
import com.google.android.material.button.MaterialButton;

import io.realm.Realm;
import java.text.DateFormat;
import io.realm.RealmResults;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    Context context;
    RealmResults<Note> notesList;
    private Realm realm;
    public MyAdapter(Context context, RealmResults<Note> notesList) {//Constructeur
        this.context = context;//On recupere le contexte de l'application
        this.notesList = notesList;//On recupere la liste des notes
        this.realm = Realm.getDefaultInstance();//On precise que l'object "realm" correspond a notre base de donnée par defaut sa structure est definie dans le Note.java
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {//Fonction qui est appelee lors de l'affichage du Recyclerview afin de le remplir de avec des elements de layout item_view
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_view,parent,false));//On creer un nouveau ViewHolder avec le layout item_view vide pour le moment
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {//Fonction appelee lors de l'affichage d'une nouvelle note a l'ecran (quand on scroll dans la liste).Elle reutilise les elements deja construit par onCreateViewHolder et les modifie pour afficher la nouvelle note

        Note note = notesList.get(position);//On recupere une note specifique grace a sa position
        if (note == null || !note.isValid()) {//Si la note qu'on a recuperee n'est pas conforme
            return; //On annule l'action
        }

        holder.titleOutput.setText(note.getTitle());//On remplis les champs "TitleOutput" de l'interface avec les informations de la note
        holder.descriptionOutput.setText(note.getDescription());//On remplis les champs "DescriptionOutput" de l'interface avec les informations de la note
        String formatedDate = DateFormat.getDateTimeInstance().format(note.getCreatedTime());//On recupere la date de creation de la note formatee
        holder.dateOutput.setText(formatedDate);//On remplis les champs "DateOutput" de l'interface avec les informations de la note

        holder.itemView.setOnClickListener(v -> {//Si on clique sur un item de la liste
            Intent intent = new Intent(context, EditNoteActivity.class);//On se prepare au changement d'activite
            intent.putExtra("NOTE_ID", note.getId());//Lors du changement d'activité on veut garder l'information de l'id de la note
            context.startActivity(intent);//On change d'activite
        });

        holder.deleteButton.setOnClickListener(v -> {//On ajoute un listener sur le bouton Suppbtn

            new AlertDialog.Builder(context)//On creer une boite de dialogue pour confirmer la suppression de la note
                    .setTitle("Supprimer la note")//On definit le titre de la boite de dialogue
                    .setMessage("Êtes-vous sûr de vouloir supprimer cette note ?")//On definit le message de la boite de dialogue

                    .setPositiveButton("Oui", (dialog, which) -> {//On definit ce qu'il ce passe quand l'utilisateur clique sur oui (mais j'ai pas compris tous les arguments de la fonction)
                        if (!realm.isClosed()) {//Si la base de donnee est ouverte
                            realm.executeTransaction(r -> {//On commence une transaction (qui va permettre de faire des modifications sur la base de donnee)
                                Note noteToDelete = notesList.get(position);//On recupere la note a supprimer grace a sa position
                                if (noteToDelete != null) {//Si la note en question a ete trouvee
                                    noteToDelete.deleteFromRealm();//On la supprime
                                }
                            });
                            Toast.makeText(context, "Note supprimée", Toast.LENGTH_SHORT).show();//Affichage d'un message de confirmation
                        }
                    })
                    .setNegativeButton("Non", null)//Si l'utilisateur clique sur non alors on ferme la boite de dialogue (comportement par defaut)

                    .show();//On affiche la boite de dialogue
        });
    }

    @Override
    public int getItemCount() { //Fonction qui compte le nombre de notes dans la liste
        return notesList.size();//On retourne le nombre de notes
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {//Classe qui permet de creer un ViewHolder (la partie graphique)

        TextView titleOutput;
        TextView descriptionOutput;
        TextView dateOutput;
        MaterialButton deleteButton;
        public MyViewHolder(@NonNull View itemView) {//Constructeur
            super(itemView);//Execution du code de la fonction mere
            titleOutput = itemView.findViewById(R.id.TitleOutput);//On fait le lien entre la variable titleOutput et le TextView TitleOutput de l'interface
            descriptionOutput = itemView.findViewById(R.id.DescriptionOutput);//On fait le lien entre la variable descriptionOutput et le TextView DescriptionOutput de l'interface
            dateOutput = itemView.findViewById(R.id.DateOutput);//On fait le lien entre la variable dateOutput et le TextView DateOutput de l'interface
            deleteButton = itemView.findViewById(R.id.Suppbtn);//On fait le lien entre la variable deleteButton et le bouton Suppbtn de l'interface
        }
    }
}
