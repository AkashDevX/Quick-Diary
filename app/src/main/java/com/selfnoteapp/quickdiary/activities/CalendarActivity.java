package com.selfnoteapp.quickdiary.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.selfnoteapp.quickdiary.R;
import com.selfnoteapp.quickdiary.data.DiaryEntry;
import com.selfnoteapp.quickdiary.repo.DiaryRepository;
import com.selfnoteapp.quickdiary.utils.DateUtils;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CalendarActivity extends AppCompatActivity {
    private CalendarView calendarView;
    private MaterialCardView selectedDayPanel;
    private TextView selectedDateText, selectedDayStatus;
    private MaterialButton actionButton;
    private DiaryRepository repository;
    private Handler mainHandler;
    private String selectedDateKey;
    private DiaryEntry selectedEntry;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        
        mainHandler = new Handler(Looper.getMainLooper());
        repository = new DiaryRepository(this);
        
        calendarView = findViewById(R.id.calendarView);
        selectedDayPanel = findViewById(R.id.selectedDayPanel);
        selectedDateText = findViewById(R.id.selectedDateText);
        selectedDayStatus = findViewById(R.id.selectedDayStatus);
        actionButton = findViewById(R.id.actionButton);
        
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            try {
                Date date = new SimpleDateFormat("yyyy-MM-dd", Locale.US)
                    .parse(String.format(Locale.US, "%d-%02d-%02d", year, month + 1, dayOfMonth));
                if (date != null) {
                    selectedDateKey = sdf.format(date);
                    loadEntryForDate(selectedDateKey);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        
        // Set initial date to today
        selectedDateKey = DateUtils.getTodayDateKey();
        loadEntryForDate(selectedDateKey);
    }
    
    private void loadEntryForDate(String dateKey) {
        repository.getByDate(dateKey, entry -> {
            mainHandler.post(() -> {
                selectedEntry = entry;
                selectedDateText.setText(DateUtils.formatDisplayDate(dateKey));
                selectedDayPanel.setVisibility(View.VISIBLE);
                
                if (entry != null && entry.text != null && !entry.text.trim().isEmpty()) {
                    selectedDayStatus.setText(entry.text.length() > 100 ? 
                        entry.text.substring(0, 100) + "..." : entry.text);
                    actionButton.setText(R.string.open_entry);
                    actionButton.setOnClickListener(v -> {
                        Intent intent = new Intent(this, ViewEntryActivity.class);
                        intent.putExtra("entry", entry);
                        startActivity(intent);
                    });
                } else {
                    if (DateUtils.isToday(dateKey)) {
                        selectedDayStatus.setText(R.string.write_today_button);
                        actionButton.setText(R.string.write_today_button);
                        actionButton.setOnClickListener(v -> {
                            startActivity(new Intent(this, WriteEntryActivity.class));
                        });
                    } else {
                        selectedDayStatus.setText(R.string.no_entry_for_day);
                        actionButton.setVisibility(View.GONE);
                    }
                }
            });
        });
    }
    
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}

