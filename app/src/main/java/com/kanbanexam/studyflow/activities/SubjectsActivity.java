package com.kanbanexam.studyflow.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kanbanexam.studyflow.R;
import com.kanbanexam.studyflow.adapters.SubjectAdapter;
import com.kanbanexam.studyflow.data.Subject;
import com.kanbanexam.studyflow.repo.StudyRepository;
import java.util.ArrayList;
import java.util.List;

public class SubjectsActivity extends AppCompatActivity {
    private RecyclerView subjectsRecyclerView;
    private FloatingActionButton addSubjectFab;
    private View emptyStateLayout;
    private StudyRepository repository;
    private SubjectAdapter adapter;
    private List<Subject> subjects = new ArrayList<>();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subjects);
        
        repository = new StudyRepository(this);
        subjectsRecyclerView = findViewById(R.id.subjectsRecyclerView);
        addSubjectFab = findViewById(R.id.addSubjectFab);
        emptyStateLayout = findViewById(R.id.emptyStateLayout);
        
        adapter = new SubjectAdapter(subjects, repository);
        subjectsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        subjectsRecyclerView.setAdapter(adapter);
        
        addSubjectFab.setOnClickListener(v -> showAddSubjectDialog());
        
        loadSubjects();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        loadSubjects();
    }
    
    private void loadSubjects() {
        repository.getAllSubjects(subjectsList -> {
            if (subjectsList != null) {
                subjects = subjectsList;
                adapter.updateSubjects(subjects);
                emptyStateLayout.setVisibility(subjects.isEmpty() ? View.VISIBLE : View.GONE);
            }
        });
    }
    
    private void showAddSubjectDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Subject");
        
        final EditText nameInput = new EditText(this);
        nameInput.setHint("Subject Name");
        nameInput.setInputType(InputType.TYPE_CLASS_TEXT);
        
        final EditText goalInput = new EditText(this);
        goalInput.setHint("Weekly Goal (hours)");
        goalInput.setInputType(InputType.TYPE_CLASS_NUMBER);
        
        android.widget.LinearLayout layout = new android.widget.LinearLayout(this);
        layout.setOrientation(android.widget.LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 10);
        layout.addView(nameInput);
        layout.addView(goalInput);
        
        builder.setView(layout);
        
        builder.setPositiveButton("Add", (dialog, which) -> {
            String name = nameInput.getText().toString().trim();
            String goalStr = goalInput.getText().toString().trim();
            
            if (name.isEmpty()) {
                Toast.makeText(this, "Please enter a subject name", Toast.LENGTH_SHORT).show();
                return;
            }
            
            int goalHours = 0;
            try {
                if (!goalStr.isEmpty()) {
                    goalHours = Integer.parseInt(goalStr);
                }
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid goal hours", Toast.LENGTH_SHORT).show();
                return;
            }
            
            String[] colors = {"#FFA726", "#FFC107", "#FF8F00", "#FFD54F", "#FFB300", "#F57C00"};
            String colorHex = colors[subjects.size() % colors.length];
            
            Subject subject = new Subject(name, colorHex, goalHours * 60);
            repository.insertSubject(subject, id -> {
                Toast.makeText(this, "Subject added", Toast.LENGTH_SHORT).show();
                loadSubjects();
            });
        });
        
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }
}
