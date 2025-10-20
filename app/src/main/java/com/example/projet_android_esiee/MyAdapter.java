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
    public MyAdapter(Context context, RealmResults<Note> notesList) {
        this.context = context;
        this.notesList = notesList;
        this.realm = Realm.getDefaultInstance();
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        // Cette méthode REMPLIT la coquille avec les bonnes données

        // 1. On récupère la note spécifique pour cette ligne
        Note note = notesList.get(position);
        if (note == null || !note.isValid()) {
            return; // Sécurité pour éviter un crash si la note vient d'être supprimée
        }

        // 2. On remplit les TextViews, comme avant
        holder.titleOutput.setText(note.getTitle());
        holder.descriptionOutput.setText(note.getDescription());
        String formatedDate = DateFormat.getDateTimeInstance().format(note.getCreatedTime());
        holder.dateOutput.setText(formatedDate);

        holder.itemView.setOnClickListener(v -> {
            // On crée un "Intent" pour démarrer une nouvelle activité
            Intent intent = new Intent(context, EditNoteActivity.class);
            // On attache l'ID de la note à l'Intent. "NOTE_ID" est une clé unique.
            intent.putExtra("NOTE_ID", note.getId());
            // On démarre la nouvelle activité
            context.startActivity(intent);
        });

        // On attache un écouteur de clic sur le bouton de suppression de CETTE ligne
        holder.deleteButton.setOnClickListener(v -> {

            // 4. On crée une boîte de dialogue pour demander confirmation
            new AlertDialog.Builder(context)
                    .setTitle("Supprimer la note")
                    .setMessage("Êtes-vous sûr de vouloir supprimer cette note ?")

                    // 5. On définit ce qui se passe si l'utilisateur appuie sur "Oui"
                    .setPositiveButton("Oui", (dialog, which) -> {
                        // On s'assure que la connexion à la DB est ouverte
                        if (!realm.isClosed()) {
                            // On lance une transaction pour effectuer une modification
                            realm.executeTransaction(r -> {
                                // On récupère à nouveau la note pour être sûr d'avoir la bonne
                                Note noteToDelete = notesList.get(position);
                                if (noteToDelete != null) {
                                    // C'est l'action de suppression !
                                    noteToDelete.deleteFromRealm();
                                }
                            });
                            // Petit message pour l'utilisateur
                            Toast.makeText(context, "Note supprimée", Toast.LENGTH_SHORT).show();
                            // Pas besoin d'appeler myAdapter.notifyDataSetChanged() ici,
                            // le RealmChangeListener dans Screen_NoteBloc s'en charge pour nous !
                        }
                    })

                    // 6. Si l'utilisateur appuie sur "Non", on ne fait rien (null)
                    .setNegativeButton("Non", null)

                    // 7. On affiche la boîte de dialogue
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView titleOutput;
        TextView descriptionOutput;
        TextView dateOutput;
        MaterialButton deleteButton;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            titleOutput = itemView.findViewById(R.id.TitleOutput);
            descriptionOutput = itemView.findViewById(R.id.DescriptionOutput);
            dateOutput = itemView.findViewById(R.id.DateOutput);
            deleteButton = itemView.findViewById(R.id.Suppbtn);
        }
    }
}
