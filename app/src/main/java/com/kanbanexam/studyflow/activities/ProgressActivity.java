package com.kanbanexam.studyflow.activities;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.kanbanexam.studyflow.R;
import com.kanbanexam.studyflow.repo.StudyRepository;
import java.util.Calendar;
import java.util.List;

public class ProgressActivity extends AppCompatActivity {
    private TextView weeklyMinutesText, totalSessionsText, streakText;
    private StudyRepository repository;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);
        
        repository = new StudyRepository(this);
        weeklyMinutesText = findViewById(R.id.weeklyMinutesText);
        totalSessionsText = findViewById(R.id.totalSessionsText);
        streakText = findViewById(R.id.streakText);
        
        loadProgress();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        loadProgress();
    }
    
    private void loadProgress() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, -7);
        long weekStart = cal.getTimeInMillis();
        long now = System.currentTimeMillis();
        
        repository.getWeeklyMinutes(weekStart, now, minutes -> {
            weeklyMinutesText.setText("Weekly Study: " + (minutes / 60) + " hours " + (minutes % 60) + " minutes");
        });
        
        repository.getAllSubjects(subjects -> {
            if (subjects != null && !subjects.isEmpty()) {
                int totalSessions = 0;
                for (com.kanbanexam.studyflow.data.Subject s : subjects) {
                    repository.getSubjectMinutes(s.id, minutes -> {
                        // This is async, so we'll just show total sessions for now
                    });
                }
            }
        });
        
        // Calculate streak (simplified - count days with sessions in last 30 days)
        streakText.setText("Streak: Calculating...");
    }
}
