package com.kanbanexam.studyflow.activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.kanbanexam.studyflow.R;
import com.kanbanexam.studyflow.data.Chapter;
import com.kanbanexam.studyflow.data.StudySession;
import com.kanbanexam.studyflow.data.Subject;
import com.kanbanexam.studyflow.repo.StudyRepository;
import java.util.ArrayList;
import java.util.List;

public class StudyTimerActivity extends AppCompatActivity {
    private TextView timerDisplay;
    private Button startStopButton;
    private Spinner subjectSpinner, chapterSpinner;
    private EditText noteInput;
    private View subjectCard, noSubjectsLayout;
    private StudyRepository repository;
    private CountDownTimer timer;
    private boolean isRunning = false;
    private long timeLeftInMillis = 25 * 60 * 1000; // 25 minutes default
    private long startTime;
    private int selectedMinutes = 25;
    private int sessionType = 0; // 0=Focus, 1=Revision, 2=Practice
    private List<Subject> subjects = new ArrayList<>();
    private List<Chapter> chapters = new ArrayList<>();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_timer);
        
        repository = new StudyRepository(this);
        timerDisplay = findViewById(R.id.timerDisplay);
        startStopButton = findViewById(R.id.startStopButton);
        subjectSpinner = findViewById(R.id.subjectSpinner);
        chapterSpinner = findViewById(R.id.chapterSpinner);
        noteInput = findViewById(R.id.noteInput);
        subjectCard = findViewById(R.id.subjectCard);
        noSubjectsLayout = findViewById(R.id.noSubjectsLayout);
        
        setupTimerButtons();
        loadSubjects();
        
        startStopButton.setOnClickListener(v -> {
            if (isRunning) {
                stopTimer();
            } else {
                startTimer();
            }
        });
    }
    
    private void setupTimerButtons() {
        findViewById(R.id.btnFocus25).setOnClickListener(v -> setTimer(25, 0));
        findViewById(R.id.btnFocus50).setOnClickListener(v -> setTimer(50, 0));
        findViewById(R.id.btnRevision15).setOnClickListener(v -> setTimer(15, 1));
        findViewById(R.id.btnRevision30).setOnClickListener(v -> setTimer(30, 1));
    }
    
    private void setTimer(int minutes, int type) {
        if (isRunning) return;
        selectedMinutes = minutes;
        sessionType = type;
        timeLeftInMillis = minutes * 60 * 1000;
        updateTimerDisplay();
    }
    
    private void loadSubjects() {
        repository.getAllSubjects(subjectsList -> {
            if (subjectsList != null && !subjectsList.isEmpty()) {
                subjects = subjectsList;
                List<String> subjectNames = new ArrayList<>();
                for (Subject s : subjects) {
                    subjectNames.add(s.name);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, subjectNames);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                subjectSpinner.setAdapter(adapter);
                
                subjectCard.setVisibility(View.VISIBLE);
                noSubjectsLayout.setVisibility(View.GONE);
                
                subjectSpinner.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                        loadChapters(subjects.get(position).id);
                    }
                    
                    @Override
                    public void onNothingSelected(android.widget.AdapterView<?> parent) {}
                });
            } else {
                subjectCard.setVisibility(View.GONE);
                noSubjectsLayout.setVisibility(View.VISIBLE);
            }
        });
    }
    
    private void loadChapters(long subjectId) {
        repository.getChaptersBySubject(subjectId, chaptersList -> {
            if (chaptersList != null) {
                chapters = chaptersList;
                List<String> chapterNames = new ArrayList<>();
                chapterNames.add("None");
                for (Chapter c : chapters) {
                    chapterNames.add(c.title);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, chapterNames);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                chapterSpinner.setAdapter(adapter);
            }
        });
    }
    
    private void startTimer() {
        if (subjects.isEmpty()) {
            Toast.makeText(this, "Please add a subject first", Toast.LENGTH_SHORT).show();
            return;
        }
        
        if (subjectSpinner.getSelectedItemPosition() < 0) {
            Toast.makeText(this, "Please select a subject", Toast.LENGTH_SHORT).show();
            return;
        }
        
        isRunning = true;
        startTime = System.currentTimeMillis();
        startStopButton.setText("Stop");
        
        timer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateTimerDisplay();
            }
            
            @Override
            public void onFinish() {
                timeLeftInMillis = 0;
                updateTimerDisplay();
                stopTimer();
                showSessionCompleteDialog();
            }
        }.start();
    }
    
    private void stopTimer() {
        isRunning = false;
        if (timer != null) {
            timer.cancel();
        }
        startStopButton.setText("Start Study");
        
        if (timeLeftInMillis < selectedMinutes * 60 * 1000) {
            // Session was started, save it
            saveSession();
        }
    }
    
    private void updateTimerDisplay() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;
        timerDisplay.setText(String.format("%02d:%02d", minutes, seconds));
    }
    
    private void saveSession() {
        if (subjects.isEmpty() || subjectSpinner.getSelectedItemPosition() < 0) return;
        
        Subject selectedSubject = subjects.get(subjectSpinner.getSelectedItemPosition());
        Long chapterId = null;
        if (chapterSpinner.getSelectedItemPosition() > 0) {
            chapterId = chapters.get(chapterSpinner.getSelectedItemPosition() - 1).id;
        }
        
        long endTime = System.currentTimeMillis();
        int durationMinutes = (int) ((endTime - startTime) / (60 * 1000));
        String note = noteInput.getText().toString().trim();
        
        StudySession session = new StudySession(selectedSubject.id, chapterId, startTime, endTime, durationMinutes, sessionType);
        session.note = note.isEmpty() ? null : note;
        
        repository.insertSession(session, id -> {
            Toast.makeText(this, "Session saved: " + durationMinutes + " minutes", Toast.LENGTH_SHORT).show();
        });
    }
    
    private void showSessionCompleteDialog() {
        new AlertDialog.Builder(this)
            .setTitle("Session Complete!")
            .setMessage("Great job! Your study session has been saved.")
            .setPositiveButton("OK", null)
            .show();
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
    }
}
