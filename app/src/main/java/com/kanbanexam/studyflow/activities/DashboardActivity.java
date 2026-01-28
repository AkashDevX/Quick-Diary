package com.kanbanexam.studyflow.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.chip.Chip;
import com.kanbanexam.studyflow.R;
import com.kanbanexam.studyflow.adapters.TileAdapter;
import com.kanbanexam.studyflow.model.DashboardTile;
import com.kanbanexam.studyflow.repo.StudyRepository;
import com.kanbanexam.studyflow.utils.UiUtils;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DashboardActivity extends AppCompatActivity {
    private RecyclerView tilesRecyclerView;
    private TextView greetingText, dateText;
    private Chip nextExamChip, streakChip;
    private StudyRepository repository;
    private Handler mainHandler;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        
        mainHandler = new Handler(Looper.getMainLooper());
        repository = new StudyRepository(this);
        
        greetingText = findViewById(R.id.greetingText);
        dateText = findViewById(R.id.dateText);
        nextExamChip = findViewById(R.id.nextExamChip);
        streakChip = findViewById(R.id.streakChip);
        tilesRecyclerView = findViewById(R.id.tilesRecyclerView);
        
        if (greetingText != null) {
            greetingText.setText(UiUtils.getGreeting());
        }
        if (dateText != null) {
            dateText.setText(new SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.getDefault()).format(new Date()));
        }
        
        setupTiles();
        loadNextExam();
        loadStreak();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        loadNextExam();
        loadStreak();
    }
    
    private void setupTiles() {
        List<DashboardTile> tiles = new ArrayList<>();
        DashboardActivity activity = this;
        
        tiles.add(new DashboardTile(
            getString(R.string.today_plan),
            getString(R.string.today_plan_subtitle),
            R.drawable.ic_today,
            R.drawable.g_tile_today,
            () -> {
                Intent intent = new Intent(activity, TodayPlanActivity.class);
                activity.startActivity(intent);
            }
        ));
        
        tiles.add(new DashboardTile(
            getString(R.string.subjects),
            getString(R.string.subjects_subtitle),
            R.drawable.ic_subjects,
            R.drawable.g_tile_subjects,
            () -> {
                Intent intent = new Intent(activity, SubjectsActivity.class);
                activity.startActivity(intent);
            }
        ));
        
        tiles.add(new DashboardTile(
            getString(R.string.study_timer),
            getString(R.string.study_timer_subtitle),
            R.drawable.ic_timer,
            R.drawable.g_tile_timer,
            () -> {
                Intent intent = new Intent(activity, StudyTimerActivity.class);
                activity.startActivity(intent);
            }
        ));
        
        tiles.add(new DashboardTile(
            getString(R.string.revision_plan),
            getString(R.string.revision_plan_subtitle),
            R.drawable.ic_revision,
            R.drawable.g_tile_revision,
            () -> {
                Intent intent = new Intent(activity, RevisionPlanActivity.class);
                activity.startActivity(intent);
            }
        ));
        
        tiles.add(new DashboardTile(
            getString(R.string.exams),
            getString(R.string.exams_subtitle),
            R.drawable.ic_exams,
            R.drawable.g_tile_exams,
            () -> {
                Intent intent = new Intent(activity, ExamsActivity.class);
                activity.startActivity(intent);
            }
        ));
        
        tiles.add(new DashboardTile(
            getString(R.string.progress),
            getString(R.string.progress_subtitle),
            R.drawable.ic_progress,
            R.drawable.g_tile_progress,
            () -> {
                Intent intent = new Intent(activity, ProgressActivity.class);
                activity.startActivity(intent);
            }
        ));
        
        tiles.add(new DashboardTile(
            getString(R.string.notes_vault),
            getString(R.string.notes_vault_subtitle),
            R.drawable.ic_notes,
            R.drawable.g_tile_notes,
            () -> {
                Intent intent = new Intent(activity, NotesVaultActivity.class);
                activity.startActivity(intent);
            }
        ));
        
        tiles.add(new DashboardTile(
            getString(R.string.settings),
            getString(R.string.settings_subtitle),
            R.drawable.ic_settings,
            R.drawable.g_tile_settings,
            () -> {
                Intent intent = new Intent(activity, SettingsActivity.class);
                activity.startActivity(intent);
            }
        ));
        
        tiles.add(new DashboardTile(
            getString(R.string.about_tile),
            getString(R.string.about_tile_subtitle),
            R.drawable.ic_about,
            R.drawable.g_tile_about,
            () -> {
                Intent intent = new Intent(activity, AboutActivity.class);
                activity.startActivity(intent);
            }
        ));
        
        tiles.add(new DashboardTile(
            getString(R.string.privacy_policy_tile),
            getString(R.string.privacy_policy_tile_subtitle),
            R.drawable.ic_privacy,
            R.drawable.g_tile_privacy,
            () -> {
                Intent intent = new Intent(activity, PrivacyPolicyActivity.class);
                activity.startActivity(intent);
            }
        ));
        
        TileAdapter adapter = new TileAdapter(tiles);
        if (tilesRecyclerView != null) {
            tilesRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
            tilesRecyclerView.setAdapter(adapter);
        }
    }
    
    private void loadNextExam() {
        repository.getNextExam(exam -> {
            if (exam != null && nextExamChip != null) {
                long now = System.currentTimeMillis();
                long daysUntil = (exam.examDate - now) / (24 * 60 * 60 * 1000L);
                if (daysUntil >= 0) {
                    nextExamChip.setText(getString(R.string.next_exam_days, (int)daysUntil));
                    nextExamChip.setVisibility(android.view.View.VISIBLE);
                } else {
                    nextExamChip.setVisibility(android.view.View.GONE);
                }
            } else if (nextExamChip != null) {
                nextExamChip.setVisibility(android.view.View.GONE);
            }
        });
    }
    
    private void loadStreak() {
        // Calculate streak from study sessions
        // For now, show placeholder
        if (streakChip != null) {
            streakChip.setText(getString(R.string.streak, 0));
        }
    }
}

