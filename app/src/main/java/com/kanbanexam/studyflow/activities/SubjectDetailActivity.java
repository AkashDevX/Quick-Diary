package com.kanbanexam.studyflow.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kanbanexam.studyflow.R;
import com.kanbanexam.studyflow.adapters.ChapterAdapter;
import com.kanbanexam.studyflow.data.Chapter;
import com.kanbanexam.studyflow.data.Subject;
import com.kanbanexam.studyflow.repo.StudyRepository;
import java.util.ArrayList;
import java.util.List;

public class SubjectDetailActivity extends AppCompatActivity {
    private long subjectId;
    private String subjectName;
    private RecyclerView chaptersRecyclerView;
    private FloatingActionButton addChapterFab;
    private View emptyStateLayout;
    private StudyRepository repository;
    private ChapterAdapter adapter;
    private List<Chapter> chapters = new ArrayList<>();
    private TextView subjectTitle;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_detail);
        
        subjectId = getIntent().getLongExtra("subjectId", -1);
        subjectName = getIntent().getStringExtra("subjectName");
        
        if (subjectId == -1) {
            finish();
            return;
        }
        
        repository = new StudyRepository(this);
        subjectTitle = findViewById(R.id.subjectTitle);
        chaptersRecyclerView = findViewById(R.id.chaptersRecyclerView);
        addChapterFab = findViewById(R.id.addChapterFab);
        emptyStateLayout = findViewById(R.id.emptyStateLayout);
        
        subjectTitle.setText(subjectName);
        
        adapter = new ChapterAdapter(chapters, repository);
        chaptersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chaptersRecyclerView.setAdapter(adapter);
        
        addChapterFab.setOnClickListener(v -> showAddChapterDialog());
        
        loadChapters();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        loadChapters();
    }
    
    private void loadChapters() {
        repository.getChaptersBySubject(subjectId, chaptersList -> {
            if (chaptersList != null) {
                chapters = chaptersList;
                adapter.updateChapters(chapters);
                emptyStateLayout.setVisibility(chapters.isEmpty() ? View.VISIBLE : View.GONE);
            }
        });
    }
    
    private void showAddChapterDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Chapter");
        
        final EditText nameInput = new EditText(this);
        nameInput.setHint("Chapter Title");
        nameInput.setInputType(InputType.TYPE_CLASS_TEXT);
        
        builder.setView(nameInput);
        
        builder.setPositiveButton("Add", (dialog, which) -> {
            String title = nameInput.getText().toString().trim();
            if (title.isEmpty()) {
                Toast.makeText(this, "Please enter a chapter title", Toast.LENGTH_SHORT).show();
                return;
            }
            
            Chapter chapter = new Chapter(subjectId, title, 1, 0); // Medium difficulty, Not Started
            repository.insertChapter(chapter, id -> {
                Toast.makeText(this, "Chapter added", Toast.LENGTH_SHORT).show();
                loadChapters();
            });
        });
        
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }
}

