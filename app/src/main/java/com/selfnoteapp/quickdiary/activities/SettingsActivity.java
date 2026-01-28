package com.selfnoteapp.quickdiary.activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.selfnoteapp.quickdiary.R;
import com.selfnoteapp.quickdiary.repo.DiaryRepository;
import com.selfnoteapp.quickdiary.utils.UiUtils;

public class SettingsActivity extends AppCompatActivity {
    private SwitchMaterial lockPastSwitch;
    private MaterialButton resetButton;
    private DiaryRepository repository;
    private Handler mainHandler;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        
        mainHandler = new Handler(Looper.getMainLooper());
        repository = new DiaryRepository(this);
        
        lockPastSwitch = findViewById(R.id.lockPastSwitch);
        resetButton = findViewById(R.id.resetButton);
        
        loadSettings();
        
        lockPastSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            UiUtils.setLockPastEntries(this, isChecked);
        });
        
        resetButton.setOnClickListener(v -> showResetDialog());
    }
    
    private void loadSettings() {
        lockPastSwitch.setChecked(UiUtils.isLockPastEntries(this));
    }
    
    private void showResetDialog() {
        new AlertDialog.Builder(this)
            .setTitle(R.string.reset_all_data)
            .setMessage(R.string.reset_confirm)
            .setPositiveButton(R.string.delete, (dialog, which) -> {
                repository.deleteAll(result -> {
                    mainHandler.post(() -> {
                        Toast.makeText(this, R.string.reset_success, Toast.LENGTH_SHORT).show();
                    });
                });
            })
            .setNegativeButton(R.string.cancel, null)
            .show();
    }
    
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}

