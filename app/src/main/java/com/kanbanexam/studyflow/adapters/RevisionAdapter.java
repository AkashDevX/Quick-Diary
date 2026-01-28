package com.kanbanexam.studyflow.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import com.kanbanexam.studyflow.R;
import com.kanbanexam.studyflow.data.Chapter;
import com.kanbanexam.studyflow.repo.StudyRepository;
import com.kanbanexam.studyflow.utils.RevisionUtils;
import com.kanbanexam.studyflow.utils.UiUtils;
import java.util.List;

public class RevisionAdapter extends RecyclerView.Adapter<RevisionAdapter.RevisionViewHolder> {
    private List<Chapter> chapters;
    private StudyRepository repository;
    private boolean isDue;
    private Runnable refreshCallback;
    
    public RevisionAdapter(List<Chapter> chapters, StudyRepository repository, boolean isDue, Runnable refreshCallback) {
        this.chapters = chapters;
        this.repository = repository;
        this.isDue = isDue;
        this.refreshCallback = refreshCallback;
    }
    
    public void updateChapters(List<Chapter> newChapters) {
        this.chapters = newChapters;
        notifyDataSetChanged();
    }
    
    @NonNull
    @Override
    public RevisionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_revision, parent, false);
        return new RevisionViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull RevisionViewHolder holder, int position) {
        Chapter chapter = chapters.get(position);
        holder.chapterTitle.setText(chapter.title);
        
        if (isDue) {
            holder.dueDateText.setText("Due: Today");
            holder.revisedButton.setVisibility(View.VISIBLE);
            holder.snoozeButton.setVisibility(View.VISIBLE);
        } else {
            holder.dueDateText.setText("Due: " + UiUtils.formatDate(chapter.nextRevisionDue));
            holder.revisedButton.setVisibility(View.GONE);
            holder.snoozeButton.setVisibility(View.GONE);
        }
        
        holder.revisedButton.setOnClickListener(v -> {
            RevisionUtils.markRevised(chapter);
            repository.updateChapter(chapter, result -> {
                Toast.makeText(v.getContext(), "Revision completed!", Toast.LENGTH_SHORT).show();
                if (refreshCallback != null) refreshCallback.run();
            });
        });
        
        holder.snoozeButton.setOnClickListener(v -> {
            RevisionUtils.snoozeRevision(chapter);
            repository.updateChapter(chapter, result -> {
                Toast.makeText(v.getContext(), "Snoozed for 1 day", Toast.LENGTH_SHORT).show();
                if (refreshCallback != null) refreshCallback.run();
            });
        });
    }
    
    @Override
    public int getItemCount() {
        return chapters != null ? chapters.size() : 0;
    }
    
    static class RevisionViewHolder extends RecyclerView.ViewHolder {
        TextView chapterTitle, dueDateText;
        MaterialButton revisedButton, snoozeButton;
        
        RevisionViewHolder(@NonNull View itemView) {
            super(itemView);
            chapterTitle = itemView.findViewById(R.id.chapterTitle);
            dueDateText = itemView.findViewById(R.id.dueDateText);
            revisedButton = itemView.findViewById(R.id.revisedButton);
            snoozeButton = itemView.findViewById(R.id.snoozeButton);
        }
    }
}

