package com.example.projet_android_esiee;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import io.realm.RealmResults;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    Context context;
    RealmResults<Note> notesList;

    public MyAdapter(Context context, RealmResults<Note> notesList) {
        this.context = context;
        this.notesList = notesList;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Note note = notesList.get(position);
        holder.titleOutput.setText(note.getTitle());
        holder.descriptionOutput.setText(note.getDescription());
        String formatedDate = DateFormat.getDateInstance().format(note.createdTime);
        holder.dateOutput.setText(formatedDate);
    }

    @Override
    public int getItemCount() {
        return notesList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView titleOutput;
        TextView descriptionOutput;
        TextView dateOutput;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            titleOutput = itemView.findViewById(R.id.TitleOutput);
            descriptionOutput = itemView.findViewById(R.id.DescriptionInput);
            dateOutput = itemView.findViewById(R.id.DateOutput);
        }
    }
}
