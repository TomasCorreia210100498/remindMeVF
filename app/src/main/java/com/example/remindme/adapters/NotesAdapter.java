package com.example.remindme.adapters;

import android.annotation.SuppressLint;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.RoundedCorner;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.remindme.R;
import com.example.remindme.listeners.NotesListener;
import com.example.remindme.singleton.Nota;
import com.makeramen.roundedimageview.RoundedImageView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder> {

    private static List<Nota>notas;
    private NotesListener notesListener;
    private static Timer timer;
    private static List<Nota>notesSource;

    public NotesAdapter(List<Nota> notas, NotesListener notesListener) {
        this.notas = notas;
        this.notesListener = notesListener;
        notesSource = notas;
    }


    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NoteViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_container_note,
                        parent,
                        false

                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        holder.setNote(notas.get(position));
        holder.layoutNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 notesListener.onNoteClicked(notas.get(position), position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return notas.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    static class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView textTitle, textSubtitle, textDateTime;
        LinearLayout layoutNote;
        RoundedImageView imageNote;

        NoteViewHolder(@NonNull View itemView) {
            super(itemView);

            textTitle = itemView.findViewById(R.id.textTitle);
            textSubtitle = itemView.findViewById(R.id.textSubtitle);
            textDateTime = itemView.findViewById(R.id.textDateTime);
            layoutNote = itemView.findViewById(R.id.layoutNote);
            imageNote = itemView.findViewById(R.id.imageNote);
        }

        void setNote(Nota note) {
            textTitle.setText(note.getTitle());
            if (note.getSubtitle().trim().isEmpty()) {
                textSubtitle.setVisibility(View.GONE);
            } else {
                textSubtitle.setText(note.getSubtitle());
            }
            textDateTime.setText(note.getDateTime());

            GradientDrawable gradientDrawable = (GradientDrawable) layoutNote.getBackground();
            if (note.getColor() != null) {
                gradientDrawable.setColor(Color.parseColor(note.getColor()));
            } else {
                gradientDrawable.setColor(Color.parseColor("#333333"));
            }

            if (note.getImagePath() != null) {
                imageNote.setImageBitmap(BitmapFactory.decodeFile(note.getImagePath()));
                imageNote.setVisibility(View.VISIBLE);
            } else {
                imageNote.setVisibility(View.GONE);
            }
        }
    }
        public void searchNotes(final String searchKeyword){
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (searchKeyword.trim().isEmpty()){
                       notas = notesSource;
                    }else {
                        ArrayList<Nota> temp = new ArrayList<>();
                        for (Nota nota: notesSource){
                            if(nota.getTitle().toLowerCase().contains(searchKeyword.toLowerCase())
                                ||nota.getSubtitle().toLowerCase().contains(searchKeyword.toLowerCase())
                                ||nota.getNoteText().toLowerCase().contains(searchKeyword.toLowerCase())){
                                temp.add(nota);
                                 }
                        }
                        notas = temp;
                    }
                   new Handler(Looper.getMainLooper()).post(new Runnable() {
                       @Override
                       public void run() {
                           notifyDataSetChanged();
                       }
                   });
                }
            }, 500);
        }
        public void cancelTimer(){
        if(timer != null){
            timer.cancel();
        }
        }
    }

