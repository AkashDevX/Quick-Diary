package com.selfnoteapp.quickdiary.activities;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.selfnoteapp.quickdiary.R;
import com.selfnoteapp.quickdiary.data.DiaryEntry;
import com.selfnoteapp.quickdiary.repo.DiaryRepository;
import com.selfnoteapp.quickdiary.utils.DateUtils;

public class MoodActivity extends AppCompatActivity {
    private MaterialButton moodGreat, moodGood, moodOkay, moodBad, moodAwful;
    private TextInputEditText moodNoteEditText;
    private DiaryRepository repository;
    private Handler mainHandler;
    private int selectedMood = -1;
    private String currentDateKey;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood);
        
        mainHandler = new Handler(Looper.getMainLooper());
        repository = new DiaryRepository(this);
        currentDateKey = DateUtils.getTodayDateKey();
        
        moodGreat = findViewById(R.id.moodGreat);
        moodGood = findViewById(R.id.moodGood);
        moodOkay = findViewById(R.id.moodOkay);
        moodBad = findViewById(R.id.moodBad);
        moodAwful = findViewById(R.id.moodAwful);
        moodNoteEditText = findViewById(R.id.moodNoteEditText);
        
        moodGreat.setOnClickListener(v -> selectMood(0));
        moodGood.setOnClickListener(v -> selectMood(1));
        moodOkay.setOnClickListener(v -> selectMood(2));
        moodBad.setOnClickListener(v -> selectMood(3));
        moodAwful.setOnClickListener(v -> selectMood(4));
        
        loadMood();
    }
    
    private void selectMood(int mood) {
        selectedMood = mood;
        // Reset all buttons
        moodGreat.setSelected(false);
        moodGood.setSelected(false);
        moodOkay.setSelected(false);
        moodBad.setSelected(false);
        moodAwful.setSelected(false);
        
        // Highlight selected
        switch (mood) {
            case 0: moodGreat.setSelected(true); break;
            case 1: moodGood.setSelected(true); break;
            case 2: moodOkay.setSelected(true); break;
            case 3: moodBad.setSelected(true); break;
            case 4: moodAwful.setSelected(true); break;
        }
        
        saveMood();
    }
    
    private void loadMood() {
        repository.getByDate(currentDateKey, entry -> {
            mainHandler.post(() -> {
                if (entry != null && entry.mood >= 0) {
                    selectedMood = entry.mood;
                    selectMood(entry.mood);
                    if (entry.moodNote != null) {
                        moodNoteEditText.setText(entry.moodNote);
                    }
                }
            });
        });
    }
    
    private void saveMood() {
        if (selectedMood < 0) return;
        
        repository.getByDate(currentDateKey, entry -> {
            mainHandler.post(() -> {
                DiaryEntry diaryEntry = entry;
                if (diaryEntry == null) {
                    diaryEntry = new DiaryEntry();
                    diaryEntry.dateKey = currentDateKey;
                    diaryEntry.text = "";
                    diaryEntry.createdTime = System.currentTimeMillis();
                }
                
                diaryEntry.mood = selectedMood;
                diaryEntry.moodNote = moodNoteEditText.getText().toString();
                diaryEntry.lastEditedTime = System.currentTimeMillis();
                
                repository.insert(diaryEntry, result -> {
                    mainHandler.post(() -> {
                        Toast.makeText(this, R.string.mood_saved, Toast.LENGTH_SHORT).show();
                    });
                });
            });
        });
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        if (selectedMood >= 0) {
            saveMood();
        }
    }
    
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}

