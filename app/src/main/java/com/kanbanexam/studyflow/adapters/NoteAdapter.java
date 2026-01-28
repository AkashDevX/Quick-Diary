package com.kanbanexam.studyflow.adapters;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.kanbanexam.studyflow.R;
import com.kanbanexam.studyflow.data.Note;
import com.kanbanexam.studyflow.repo.StudyRepository;
import com.kanbanexam.studyflow.utils.UiUtils;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {
    private List<Note> notes;
    private StudyRepository repository;
    private Runnable refreshCallback;
    
    public NoteAdapter(List<Note> notes, StudyRepository repository, Runnable refreshCallback) {
        this.notes = notes;
        this.repository = repository;
        this.refreshCallback = refreshCallback;
    }
    
    public void updateNotes(List<Note> newNotes) {
        this.notes = newNotes;
        notifyDataSetChanged();
    }
    
    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_note, parent, false);
        return new NoteViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = notes.get(position);
        holder.noteText.setText(note.text);
        holder.noteDate.setText(UiUtils.formatDateTime(note.createdAt));
        
        holder.itemView.setOnLongClickListener(v -> {
            new AlertDialog.Builder(v.getContext())
                .setTitle("Delete Note")
                .setMessage("Are you sure you want to delete this note?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    repository.deleteNote(note, result -> {
                        Toast.makeText(v.getContext(), "Note deleted", Toast.LENGTH_SHORT).show();
                        if (refreshCallback != null) refreshCallback.run();
                    });
                })
                .setNegativeButton("Cancel", null)
                .show();
            return true;
        });
    }
    
    @Override
    public int getItemCount() {
        return notes != null ? notes.size() : 0;
    }
    
    static class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView noteText, noteDate;
        
        NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            noteText = itemView.findViewById(R.id.noteText);
            noteDate = itemView.findViewById(R.id.noteDate);
        }
    }
}

