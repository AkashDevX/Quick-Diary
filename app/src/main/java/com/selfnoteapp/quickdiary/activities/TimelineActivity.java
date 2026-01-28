package com.selfnoteapp.quickdiary.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.textfield.TextInputEditText;
import com.selfnoteapp.quickdiary.R;
import com.selfnoteapp.quickdiary.adapters.EntryAdapter;
import com.selfnoteapp.quickdiary.data.DiaryEntry;
import com.selfnoteapp.quickdiary.repo.DiaryRepository;
import java.util.ArrayList;
import java.util.List;

public class TimelineActivity extends AppCompatActivity {
    private RecyclerView entriesRecyclerView;
    private TextInputEditText searchEditText;
    private LinearLayout emptyStateLayout;
    private EntryAdapter adapter;
    private DiaryRepository repository;
    private Handler mainHandler;
    private List<DiaryEntry> allEntries = new ArrayList<>();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        
        mainHandler = new Handler(Looper.getMainLooper());
        repository = new DiaryRepository(this);
        
        entriesRecyclerView = findViewById(R.id.entriesRecyclerView);
        searchEditText = findViewById(R.id.searchEditText);
        emptyStateLayout = findViewById(R.id.emptyStateLayout);
        
        adapter = new EntryAdapter(new ArrayList<>(), entry -> {
            Intent intent = new Intent(this, ViewEntryActivity.class);
            intent.putExtra("entry", entry);
            startActivity(intent);
        });
        
        entriesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        entriesRecyclerView.setAdapter(adapter);
        
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString();
                if (query.isEmpty()) {
                    loadAllEntries();
                } else {
                    searchEntries(query);
                }
            }
            
            @Override
            public void afterTextChanged(Editable s) {}
        });
        
        loadAllEntries();
    }
    
    private void loadAllEntries() {
        repository.getAllDesc(entries -> {
            mainHandler.post(() -> {
                allEntries = entries != null ? entries : new ArrayList<>();
                adapter.updateEntries(allEntries);
                emptyStateLayout.setVisibility(allEntries.isEmpty() ? View.VISIBLE : View.GONE);
            });
        });
    }
    
    private void searchEntries(String query) {
        repository.search(query, entries -> {
            mainHandler.post(() -> {
                List<DiaryEntry> results = entries != null ? entries : new ArrayList<>();
                adapter.updateEntries(results);
                emptyStateLayout.setVisibility(results.isEmpty() ? View.VISIBLE : View.GONE);
            });
        });
    }
    
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}

