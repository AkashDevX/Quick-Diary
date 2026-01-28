package com.kanbanexam.studyflow.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.kanbanexam.studyflow.R;
import com.kanbanexam.studyflow.repo.StudyRepository;
import com.kanbanexam.studyflow.utils.UiUtils;

public class SettingsActivity extends AppCompatActivity {
    private TextView themeText, resetDataText;
    private StudyRepository repository;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        
        repository = new StudyRepository(this);
        themeText = findViewById(R.id.themeText);
        resetDataText = findViewById(R.id.resetDataText);
        
        updateThemeText();
        
        themeText.setOnClickListener(v -> showThemeDialog());
        resetDataText.setOnClickListener(v -> showResetDialog());
    }
    
    private void updateThemeText() {
        int mode = UiUtils.getThemeMode(this);
        String[] modes = {"Orange Light", "Orange Dark", "System"};
        themeText.setText("Theme: " + modes[mode]);
    }
    
    private void showThemeDialog() {
        String[] options = {"Orange Light", "Orange Dark", "System"};
        int currentMode = UiUtils.getThemeMode(this);
        
        new AlertDialog.Builder(this)
            .setTitle("Select Theme")
            .setSingleChoiceItems(options, currentMode, (dialog, which) -> {
                UiUtils.setThemeMode(this, which);
                updateThemeText();
                Toast.makeText(this, "Theme changed. Restart app to apply.", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            })
            .show();
    }
    
    private void showResetDialog() {
        new AlertDialog.Builder(this)
            .setTitle("Reset All Data")
            .setMessage(getString(R.string.reset_confirm))
            .setPositiveButton("Reset", (dialog, which) -> {
                // Clear database - simplified for now
                Toast.makeText(this, "Data reset (feature to be implemented)", Toast.LENGTH_SHORT).show();
            })
            .setNegativeButton("Cancel", null)
            .show();
    }
}
