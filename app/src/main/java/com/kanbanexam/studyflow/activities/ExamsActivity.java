package com.kanbanexam.studyflow.activities;

import android.app.AlertDialog;
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
import com.kanbanexam.studyflow.adapters.ExamAdapter;
import com.kanbanexam.studyflow.data.Exam;
import com.kanbanexam.studyflow.repo.StudyRepository;
import com.kanbanexam.studyflow.utils.UiUtils;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ExamsActivity extends AppCompatActivity {
    private RecyclerView examsRecyclerView;
    private FloatingActionButton addExamFab;
    private View emptyStateLayout;
    private StudyRepository repository;
    private ExamAdapter adapter;
    private List<Exam> exams = new ArrayList<>();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exams);
        
        repository = new StudyRepository(this);
        examsRecyclerView = findViewById(R.id.examsRecyclerView);
        addExamFab = findViewById(R.id.addExamFab);
        emptyStateLayout = findViewById(R.id.emptyStateLayout);
        
        adapter = new ExamAdapter(exams, repository, this::loadExams);
        examsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        examsRecyclerView.setAdapter(adapter);
        
        addExamFab.setOnClickListener(v -> showAddExamDialog());
        
        loadExams();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        loadExams();
    }
    
    private void loadExams() {
        repository.getAllExams(examsList -> {
            if (examsList != null) {
                exams = examsList;
                adapter.updateExams(exams);
                emptyStateLayout.setVisibility(exams.isEmpty() ? View.VISIBLE : View.GONE);
            }
        });
    }
    
    private void showAddExamDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Exam");
        
        final EditText nameInput = new EditText(this);
        nameInput.setHint("Exam Name");
        nameInput.setInputType(InputType.TYPE_CLASS_TEXT);
        
        final EditText dateInput = new EditText(this);
        dateInput.setHint("Date (YYYY-MM-DD)");
        dateInput.setInputType(InputType.TYPE_CLASS_TEXT);
        
        android.widget.LinearLayout layout = new android.widget.LinearLayout(this);
        layout.setOrientation(android.widget.LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 10);
        layout.addView(nameInput);
        layout.addView(dateInput);
        
        builder.setView(layout);
        
        builder.setPositiveButton("Add", (dialog, which) -> {
            String name = nameInput.getText().toString().trim();
            String dateStr = dateInput.getText().toString().trim();
            
            if (name.isEmpty()) {
                Toast.makeText(this, "Please enter an exam name", Toast.LENGTH_SHORT).show();
                return;
            }
            
            if (dateStr.isEmpty()) {
                Toast.makeText(this, "Please enter a date", Toast.LENGTH_SHORT).show();
                return;
            }
            
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                sdf.setLenient(false); // Strict parsing
                Date date = sdf.parse(dateStr);
                if (date == null) throw new Exception();
                
                // Set time to start of day to avoid timezone issues
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);
                
                long examTimestamp = cal.getTimeInMillis();
                
                Exam exam = new Exam(name, examTimestamp);
                repository.insertExam(exam, id -> {
                    Toast.makeText(this, "Exam added successfully", Toast.LENGTH_SHORT).show();
                    loadExams();
                });
            } catch (Exception e) {
                Toast.makeText(this, "Invalid date format. Please use YYYY-MM-DD (e.g., 2024-12-25)", Toast.LENGTH_LONG).show();
            }
        });
        
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }
}
