package com.selfnoteapp.quickdiary.activities;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.selfnoteapp.quickdiary.R;
import com.selfnoteapp.quickdiary.data.DiaryEntry;
import com.selfnoteapp.quickdiary.repo.DiaryRepository;
import com.selfnoteapp.quickdiary.utils.DateUtils;
import com.selfnoteapp.quickdiary.utils.UiUtils;
import java.util.Arrays;
import java.util.List;

public class WriteEntryActivity extends AppCompatActivity {
    private EditText entryText;
    private TextView wordCountText, lastSavedText;
    private ChipGroup tagsContainer;
    private DiaryRepository repository;
    private Handler mainHandler;
    private String selectedTag = null;
    private String currentDateKey;
    private DiaryEntry currentEntry;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_entry);
        
        try {
            mainHandler = new Handler(Looper.getMainLooper());
            repository = new DiaryRepository(this);
            currentDateKey = DateUtils.getTodayDateKey();
            
            if (repository == null || currentDateKey == null) {
                finish();
                return;
            }
            
            entryText = findViewById(R.id.entryText);
            wordCountText = findViewById(R.id.wordCountText);
            lastSavedText = findViewById(R.id.lastSavedText);
            tagsContainer = findViewById(R.id.tagsContainer);
            
            if (entryText == null || wordCountText == null) {
                finish();
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            finish();
            return;
        }
        
        com.google.android.material.button.MaterialButton doneButton = findViewById(R.id.doneButton);
        if (doneButton != null) {
            doneButton.setOnClickListener(v -> {
                saveEntry();
                finish();
            });
        }
        
        setupTags();
        loadEntry();
        
        entryText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateWordCount();
                autoSave();
            }
            
            @Override
            public void afterTextChanged(Editable s) {}
        });
        
        // Check if past entry and lock if needed
        if (DateUtils.isPast(currentDateKey) && UiUtils.isLockPastEntries(this)) {
            entryText.setEnabled(false);
            entryText.setHint("Past entries are locked");
        }
        
        // Handle prompt pre-fill
        String prompt = getIntent().getStringExtra("prompt");
        if (prompt != null && !prompt.isEmpty()) {
            entryText.setText(prompt + "\n\n");
            entryText.setSelection(entryText.getText().length());
        }
    }
    
    @Override
    public void onBackPressed() {
        saveEntry();
        super.onBackPressed();
    }
    
    private void setupTags() {
        if (tagsContainer == null) return;
        try {
            List<String> tags = Arrays.asList("Work", "Study", "Personal", "Ideas", "Health");
            for (String tag : tags) {
                Chip chip = new Chip(this);
                chip.setText(tag);
                chip.setCheckable(true);
                chip.setChipBackgroundColorResource(android.R.color.white);
                try {
                    chip.setTextColor(getResources().getColorStateList(R.color.text_dark, getTheme()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                chip.setChipStrokeWidth(2f);
                chip.setChipStrokeColorResource(R.color.primary);
                chip.setChipCornerRadius(20f);
                chip.setTextSize(13f);
                chip.setChipMinHeight(36f);
                chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    try {
                        if (isChecked) {
                            selectedTag = tag;
                            chip.setChipBackgroundColorResource(R.color.primary);
                            try {
                                chip.setTextColor(getResources().getColorStateList(R.color.text_on_pink, getTheme()));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            chip.setChipStrokeWidth(0f);
                            for (int i = 0; i < tagsContainer.getChildCount(); i++) {
                                Chip c = (Chip) tagsContainer.getChildAt(i);
                                if (c != chip) {
                                    c.setChecked(false);
                                    c.setChipBackgroundColorResource(android.R.color.white);
                                    try {
                                        c.setTextColor(getResources().getColorStateList(R.color.text_dark, getTheme()));
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    c.setChipStrokeWidth(2f);
                                }
                            }
                        } else {
                            selectedTag = null;
                            chip.setChipBackgroundColorResource(android.R.color.white);
                            try {
                                chip.setTextColor(getResources().getColorStateList(R.color.text_dark, getTheme()));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            chip.setChipStrokeWidth(2f);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                tagsContainer.addView(chip);
                // Add spacing between chips
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) chip.getLayoutParams();
                if (params == null) {
                    params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                }
                params.setMargins(0, 0, 12, 0);
                chip.setLayoutParams(params);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void loadEntry() {
        try {
            if (repository == null || currentDateKey == null) return;
            
            repository.getByDate(currentDateKey, entry -> {
                if (mainHandler != null) {
                    mainHandler.post(() -> {
                        try {
                            if (entry != null) {
                                currentEntry = entry;
                                if (entryText != null) {
                                    entryText.setText(entry.text != null ? entry.text : "");
                                }
                                selectedTag = entry.tag;
                                if (entry.tag != null && tagsContainer != null) {
                                    for (int i = 0; i < tagsContainer.getChildCount(); i++) {
                                        Chip chip = (Chip) tagsContainer.getChildAt(i);
                                        if (chip != null && chip.getText().toString().equals(entry.tag)) {
                                            chip.setChecked(true);
                                            break;
                                        }
                                    }
                                }
                                updateWordCount();
                                updateLastSaved();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void updateWordCount() {
        try {
            if (entryText != null && wordCountText != null) {
                String text = entryText.getText().toString().trim();
                int count = text.isEmpty() ? 0 : text.split("\\s+").length;
                wordCountText.setText(getString(R.string.word_count, count));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void updateLastSaved() {
        try {
            if (currentEntry != null && lastSavedText != null) {
                lastSavedText.setText(getString(R.string.last_saved, 
                    DateUtils.formatTime(currentEntry.lastEditedTime)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void autoSave() {
        try {
            if (mainHandler != null) {
                mainHandler.removeCallbacksAndMessages(null);
                mainHandler.postDelayed(this::saveEntry, 2000); // Auto-save after 2 seconds of no typing
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void saveEntry() {
        try {
            if (entryText == null || repository == null) return;
            
            String text = entryText.getText().toString();
            long now = System.currentTimeMillis();
            
            if (currentEntry == null) {
                currentEntry = new DiaryEntry();
                currentEntry.dateKey = currentDateKey;
                currentEntry.createdTime = now;
            }
            
            currentEntry.text = text;
            currentEntry.tag = selectedTag;
            currentEntry.lastEditedTime = now;
            
            repository.insert(currentEntry, result -> {
                if (mainHandler != null) {
                    mainHandler.post(() -> {
                        updateLastSaved();
                    });
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

