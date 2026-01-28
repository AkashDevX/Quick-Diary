package com.selfnoteapp.quickdiary.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.chip.Chip;
import com.selfnoteapp.quickdiary.R;
import com.selfnoteapp.quickdiary.adapters.TileAdapter;
import com.selfnoteapp.quickdiary.model.DashboardTile;
import com.selfnoteapp.quickdiary.repo.DiaryRepository;
import com.selfnoteapp.quickdiary.utils.DateUtils;
import com.selfnoteapp.quickdiary.utils.UiUtils;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity {
    private RecyclerView tilesRecyclerView;
    private TextView greetingText, dateText;
    private Chip streakChip;
    private DiaryRepository repository;
    private Handler mainHandler;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        
        mainHandler = new Handler(Looper.getMainLooper());
        repository = new DiaryRepository(this);
        
        greetingText = findViewById(R.id.greetingText);
        dateText = findViewById(R.id.dateText);
        streakChip = findViewById(R.id.streakChip);
        tilesRecyclerView = findViewById(R.id.tilesRecyclerView);
        
        if (greetingText != null) {
            greetingText.setText(UiUtils.getGreeting());
        }
        if (dateText != null) {
            dateText.setText(new SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.getDefault()).format(new Date()));
        }
        
        setupTiles();
        loadStreak();
        
        // Check PIN if enabled - DISABLED FOR TESTING
        // Uncomment the lines below to enable PIN verification
        /*
        if (UiUtils.isPinEnabled(this)) {
            Intent pinIntent = new Intent(this, PinLockActivity.class);
            pinIntent.putExtra("verify", true);
            startActivity(pinIntent);
        }
        */
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        loadStreak();
    }
    
    private void setupTiles() {
        List<DashboardTile> tiles = new ArrayList<>();
        HomeActivity activity = this;
        
        tiles.add(new DashboardTile(
            getString(R.string.write_today),
            getString(R.string.write_today_subtitle),
            R.drawable.ic_write,
            R.drawable.g_tile_write,
            () -> {
                try {
                    if (activity != null && !activity.isFinishing()) {
                        Intent intent = new Intent(activity, WriteEntryActivity.class);
                        activity.startActivity(intent);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        ));
        
        tiles.add(new DashboardTile(
            getString(R.string.timeline),
            getString(R.string.timeline_subtitle),
            R.drawable.ic_timeline,
            R.drawable.g_tile_timeline,
            () -> {
                try {
                    if (activity != null && !activity.isFinishing()) {
                        Intent intent = new Intent(activity, TimelineActivity.class);
                        activity.startActivity(intent);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        ));
        
        tiles.add(new DashboardTile(
            getString(R.string.calendar),
            getString(R.string.calendar_subtitle),
            R.drawable.ic_calendar,
            R.drawable.g_tile_calendar,
            () -> {
                try {
                    if (activity != null && !activity.isFinishing()) {
                        Intent intent = new Intent(activity, CalendarActivity.class);
                        activity.startActivity(intent);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        ));
        
        tiles.add(new DashboardTile(
            getString(R.string.mood_checkin),
            getString(R.string.mood_checkin_subtitle),
            R.drawable.ic_mood,
            R.drawable.g_tile_mood,
            () -> {
                try {
                    if (activity != null && !activity.isFinishing()) {
                        Intent intent = new Intent(activity, MoodActivity.class);
                        activity.startActivity(intent);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        ));
        
        tiles.add(new DashboardTile(
            getString(R.string.ideas_vault),
            getString(R.string.ideas_vault_subtitle),
            R.drawable.ic_prompts,
            R.drawable.g_tile_prompts,
            () -> {
                try {
                    if (activity != null && !activity.isFinishing()) {
                        Intent intent = new Intent(activity, PromptsActivity.class);
                        activity.startActivity(intent);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        ));
        
        tiles.add(new DashboardTile(
            getString(R.string.secure_diary),
            getString(R.string.secure_diary_subtitle),
            R.drawable.ic_lock,
            R.drawable.g_tile_secure,
            () -> {
                try {
                    if (activity != null && !activity.isFinishing()) {
                        Intent intent = new Intent(activity, PinLockActivity.class);
                        activity.startActivity(intent);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        ));
        
        tiles.add(new DashboardTile(
            getString(R.string.export_backup),
            getString(R.string.export_backup_subtitle),
            R.drawable.ic_export,
            R.drawable.g_tile_export,
            () -> {
                try {
                    if (activity != null && !activity.isFinishing()) {
                        Intent intent = new Intent(activity, ExportActivity.class);
                        activity.startActivity(intent);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        ));
        
        tiles.add(new DashboardTile(
            getString(R.string.settings),
            getString(R.string.settings_subtitle),
            R.drawable.ic_settings,
            R.drawable.g_tile_settings,
            () -> {
                try {
                    if (activity != null && !activity.isFinishing()) {
                        Intent intent = new Intent(activity, SettingsActivity.class);
                        activity.startActivity(intent);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        ));
        
        tiles.add(new DashboardTile(
            getString(R.string.about_tile),
            getString(R.string.about_tile_subtitle),
            R.drawable.ic_about,
            R.drawable.g_tile_about,
            () -> {
                try {
                    if (activity != null && !activity.isFinishing()) {
                        Intent intent = new Intent(activity, AboutActivity.class);
                        activity.startActivity(intent);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        ));
        
        tiles.add(new DashboardTile(
            getString(R.string.privacy_policy_tile),
            getString(R.string.privacy_policy_tile_subtitle),
            R.drawable.ic_privacy,
            R.drawable.g_tile_privacy,
            () -> {
                try {
                    if (activity != null && !activity.isFinishing()) {
                        Intent intent = new Intent(activity, PrivacyPolicyActivity.class);
                        activity.startActivity(intent);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        ));
        
        TileAdapter adapter = new TileAdapter(tiles);
        if (tilesRecyclerView != null) {
            tilesRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
            tilesRecyclerView.setAdapter(adapter);
        }
    }
    
    private void loadStreak() {
        try {
            repository.getAllDateKeys(result -> {
                mainHandler.post(() -> {
                    try {
                        if (streakChip != null && result != null) {
                            int streak = DateUtils.calculateStreak(result);
                            streakChip.setText(getString(R.string.streak, streak));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

