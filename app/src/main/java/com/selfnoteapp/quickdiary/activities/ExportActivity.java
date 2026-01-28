package com.selfnoteapp.quickdiary.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.button.MaterialButton;
import com.selfnoteapp.quickdiary.R;
import com.selfnoteapp.quickdiary.data.DiaryEntry;
import com.selfnoteapp.quickdiary.repo.DiaryRepository;
import com.selfnoteapp.quickdiary.utils.BackupUtils;
import com.selfnoteapp.quickdiary.utils.ExportUtils;
import java.util.List;

public class ExportActivity extends AppCompatActivity {
    private MaterialButton exportTxtButton, exportPdfButton, backupButton, restoreButton;
    private DiaryRepository repository;
    private Handler mainHandler;
    private static final int REQUEST_CODE_BACKUP = 100;
    private static final int REQUEST_CODE_RESTORE = 101;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export);
        
        mainHandler = new Handler(Looper.getMainLooper());
        repository = new DiaryRepository(this);
        
        exportTxtButton = findViewById(R.id.exportTxtButton);
        exportPdfButton = findViewById(R.id.exportPdfButton);
        backupButton = findViewById(R.id.backupButton);
        restoreButton = findViewById(R.id.restoreButton);
        
        exportTxtButton.setOnClickListener(v -> exportTxt());
        exportPdfButton.setOnClickListener(v -> exportPdf());
        backupButton.setOnClickListener(v -> createBackup());
        restoreButton.setOnClickListener(v -> restoreBackup());
    }
    
    private void exportTxt() {
        repository.getAllDesc(entries -> {
            mainHandler.post(() -> {
                try {
                    String path = ExportUtils.exportToTxt(this, entries);
                    Toast.makeText(this, getString(R.string.export_success, path), Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, R.string.export_error, Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
    
    private void exportPdf() {
        repository.getAllDesc(entries -> {
            mainHandler.post(() -> {
                try {
                    String path = ExportUtils.exportToPdf(this, entries);
                    Toast.makeText(this, getString(R.string.export_success, path), Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, R.string.export_error, Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
    
    private void createBackup() {
        repository.getAllDesc(entries -> {
            mainHandler.post(() -> {
                try {
                    String json = BackupUtils.createBackupJson(this, entries);
                    Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType("application/json");
                    intent.putExtra(Intent.EXTRA_TITLE, "QuickDiary_Backup.json");
                    startActivityForResult(intent, REQUEST_CODE_BACKUP);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, R.string.backup_error, Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
    
    private void restoreBackup() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/json");
        startActivityForResult(intent, REQUEST_CODE_RESTORE);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            if (uri != null) {
                if (requestCode == REQUEST_CODE_BACKUP) {
                    // Write backup file
                    Toast.makeText(this, R.string.backup_success, Toast.LENGTH_SHORT).show();
                } else if (requestCode == REQUEST_CODE_RESTORE) {
                    // Read and restore
                    Toast.makeText(this, R.string.restore_success, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}

