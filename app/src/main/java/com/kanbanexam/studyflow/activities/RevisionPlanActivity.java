package com.kanbanexam.studyflow.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.kanbanexam.studyflow.R;
import com.kanbanexam.studyflow.adapters.RevisionAdapter;
import com.kanbanexam.studyflow.data.Chapter;
import com.kanbanexam.studyflow.repo.StudyRepository;
import com.kanbanexam.studyflow.utils.RevisionUtils;
import java.util.ArrayList;
import java.util.List;

public class RevisionPlanActivity extends AppCompatActivity {
    private RecyclerView dueRecyclerView, upcomingRecyclerView;
    private TextView dueTitle, upcomingTitle;
    private View noDueLayout, noUpcomingLayout;
    private StudyRepository repository;
    private RevisionAdapter dueAdapter, upcomingAdapter;
    private List<Chapter> dueChapters = new ArrayList<>();
    private List<Chapter> upcomingChapters = new ArrayList<>();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revision_plan);
        
        repository = new StudyRepository(this);
        dueRecyclerView = findViewById(R.id.dueRecyclerView);
        upcomingRecyclerView = findViewById(R.id.upcomingRecyclerView);
        dueTitle = findViewById(R.id.dueTitle);
        upcomingTitle = findViewById(R.id.upcomingTitle);
        noDueLayout = findViewById(R.id.noDueLayout);
        noUpcomingLayout = findViewById(R.id.noUpcomingLayout);
        
        dueAdapter = new RevisionAdapter(dueChapters, repository, true, this::loadRevisions);
        upcomingAdapter = new RevisionAdapter(upcomingChapters, repository, false, this::loadRevisions);
        
        dueRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        upcomingRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        dueRecyclerView.setAdapter(dueAdapter);
        upcomingRecyclerView.setAdapter(upcomingAdapter);
        
        loadRevisions();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        loadRevisions();
    }
    
    private void loadRevisions() {
        repository.getDueRevisions(dueList -> {
            if (dueList != null) {
                dueChapters = dueList;
                dueAdapter.updateChapters(dueChapters);
                noDueLayout.setVisibility(dueChapters.isEmpty() ? View.VISIBLE : View.GONE);
            }
        });
        
        repository.getUpcomingRevisions(upcomingList -> {
            if (upcomingList != null) {
                upcomingChapters = upcomingList;
                upcomingAdapter.updateChapters(upcomingChapters);
                noUpcomingLayout.setVisibility(upcomingChapters.isEmpty() ? View.VISIBLE : View.GONE);
            }
        });
    }
}
