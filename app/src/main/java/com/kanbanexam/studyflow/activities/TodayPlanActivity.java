package com.kanbanexam.studyflow.activities;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.kanbanexam.studyflow.R;
import com.kanbanexam.studyflow.repo.StudyRepository;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TodayPlanActivity extends AppCompatActivity {
    private TextView todayDateText, planText;
    private StudyRepository repository;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_plan);
        
        repository = new StudyRepository(this);
        todayDateText = findViewById(R.id.todayDateText);
        planText = findViewById(R.id.planText);
        
        todayDateText.setText(new SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.getDefault()).format(new Date()));
        
        loadTodayPlan();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        loadTodayPlan();
    }
    
    private void loadTodayPlan() {
        // Load due revisions for today
        repository.getDueRevisions(dueChapters -> {
            if (dueChapters != null && !dueChapters.isEmpty()) {
                StringBuilder plan = new StringBuilder("📚 Today's Revision Plan:\n\n");
                for (com.kanbanexam.studyflow.data.Chapter chapter : dueChapters) {
                    plan.append("✓ ").append(chapter.title).append("\n");
                }
                plan.append("\n✨ Stay focused and keep up the great work!");
                planText.setText(plan.toString());
            } else {
                planText.setText("🎉 All caught up! No revisions due today.\n\n💡 Tip: Add subjects and chapters to start planning your study schedule. When you mark chapters as done, they'll automatically be scheduled for revision!");
            }
        });
    }
}
