package com.kanbanexam.studyflow.activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kanbanexam.studyflow.R;
import com.kanbanexam.studyflow.adapters.NoteAdapter;
import com.kanbanexam.studyflow.data.Note;
import com.kanbanexam.studyflow.repo.StudyRepository;
import java.util.ArrayList;
import java.util.List;

public class NotesVaultActivity extends AppCompatActivity {
    private RecyclerView notesRecyclerView;
    private FloatingActionButton addNoteFab;
    private SearchView searchView;
    private View emptyStateLayout;
    private StudyRepository repository;
    private NoteAdapter adapter;
    private List<Note> notes = new ArrayList<>();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_vault);
        
        repository = new StudyRepository(this);
        notesRecyclerView = findViewById(R.id.notesRecyclerView);
        addNoteFab = findViewById(R.id.addNoteFab);
        searchView = findViewById(R.id.searchView);
        emptyStateLayout = findViewById(R.id.emptyStateLayout);
        
        adapter = new NoteAdapter(notes, repository, this::loadNotes);
        notesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        notesRecyclerView.setAdapter(adapter);
        
        addNoteFab.setOnClickListener(v -> showAddNoteDialog());
        
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchNotes(query);
                return true;
            }
            
            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    loadNotes();
                } else {
                    searchNotes(newText);
                }
                return true;
            }
        });
        
        loadNotes();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        loadNotes();
    }
    
    private void loadNotes() {
        repository.getAllNotes(notesList -> {
            if (notesList != null) {
                notes = notesList;
                adapter.updateNotes(notes);
                emptyStateLayout.setVisibility(notes.isEmpty() ? View.VISIBLE : View.GONE);
            }
        });
    }
    
    private void searchNotes(String query) {
        repository.searchNotes(query, notesList -> {
            if (notesList != null) {
                notes = notesList;
                adapter.updateNotes(notes);
                emptyStateLayout.setVisibility(notes.isEmpty() ? View.VISIBLE : View.GONE);
            }
        });
    }
    
    private void showAddNoteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Note");
        
        final EditText noteInput = new EditText(this);
        noteInput.setHint("Note text");
        noteInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        noteInput.setMinLines(3);
        noteInput.setMaxLines(8);
        
        android.widget.LinearLayout layout = new android.widget.LinearLayout(this);
        layout.setOrientation(android.widget.LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 10);
        layout.addView(noteInput);
        
        builder.setView(layout);
        
        builder.setPositiveButton("Add", (dialog, which) -> {
            String text = noteInput.getText().toString().trim();
            if (text.isEmpty()) {
                Toast.makeText(this, "Please enter note text", Toast.LENGTH_SHORT).show();
                return;
            }
            
            Note note = new Note(0, null, text); // Subject 0 = general note
            repository.insertNote(note, id -> {
                Toast.makeText(this, "Note added", Toast.LENGTH_SHORT).show();
                loadNotes();
            });
        });
        
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }
}
