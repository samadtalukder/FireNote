package com.samad.talukder.androidkeepnotewithfirebase.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.samad.talukder.androidkeepnotewithfirebase.R;
import com.samad.talukder.androidkeepnotewithfirebase.model.Note;


import java.util.ArrayList;
import java.util.List;

public class ViewNoteAdapter extends RecyclerView.Adapter<ViewNoteAdapter.ViewNoteHolder> {
    private Context context;
    private List<Note> noteArrayList;

    public ViewNoteAdapter(Context context, List<Note> noteArrayList) {
        this.context = context;
        this.noteArrayList = noteArrayList;
    }

    @NonNull
    @Override
    public ViewNoteHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_view_note, viewGroup, false);
        return new ViewNoteHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewNoteHolder viewNoteHolder, int i) {
        final Note note = noteArrayList.get(i);
        viewNoteHolder.noteTitle.setText(note.getNoteTitle());
        viewNoteHolder.noteText.setText(note.getNoteDescription());

    }

    @Override
    public int getItemCount() {
        return noteArrayList.size();
    }

    public class ViewNoteHolder extends RecyclerView.ViewHolder {
        private TextView noteTitle, noteText;

        public ViewNoteHolder(@NonNull View itemView) {
            super(itemView);
            noteTitle = itemView.findViewById(R.id.title);
            noteText = itemView.findViewById(R.id.text);
        }
    }
}
