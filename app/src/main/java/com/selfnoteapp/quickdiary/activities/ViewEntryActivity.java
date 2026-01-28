package com.selfnoteapp.quickdiary.activities;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.selfnoteapp.quickdiary.R;
import com.selfnoteapp.quickdiary.data.DiaryEntry;
import com.selfnoteapp.quickdiary.utils.DateUtils;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ViewEntryActivity extends AppCompatActivity {
    private DiaryEntry entry;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_entry);
        
        entry = (DiaryEntry) getIntent().getSerializableExtra("entry");
        if (entry == null) {
            finish();
            return;
        }
        
        TextView dateText = findViewById(R.id.dateText);
        TextView entryText = findViewById(R.id.entryText);
        ImageView moodIcon = findViewById(R.id.moodIcon);
        Chip tagChip = findViewById(R.id.tagChip);
        MaterialButton copyButton = findViewById(R.id.copyButton);
        MaterialButton shareButton = findViewById(R.id.shareButton);
        
        if (dateText != null) {
            dateText.setText(DateUtils.formatDisplayDate(entry.dateKey));
        }
        if (entryText != null) {
            entryText.setText(entry.text != null ? entry.text : "");
        }
        
        if (entry.tag != null && !entry.tag.isEmpty() && tagChip != null) {
            tagChip.setVisibility(android.view.View.VISIBLE);
            tagChip.setText(entry.tag);
        }
        
        if (entry.mood >= 0 && entry.mood <= 4 && moodIcon != null) {
            moodIcon.setVisibility(android.view.View.VISIBLE);
            String[] moods = {"Great", "Good", "Okay", "Bad", "Awful"};
            // You can set mood icon based on mood value
        }
        
        if (copyButton != null) {
            copyButton.setOnClickListener(v -> {
                try {
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                    if (clipboard != null && entry.text != null) {
                        ClipData clip = ClipData.newPlainText("Diary Entry", entry.text);
                        clipboard.setPrimaryClip(clip);
                        Toast.makeText(this, R.string.text_copied, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        
        if (shareButton != null) {
            shareButton.setOnClickListener(v -> {
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_TEXT, entry.text != null ? entry.text : "");
                    startActivity(Intent.createChooser(shareIntent, "Share Entry"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }
    
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}

