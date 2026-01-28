package com.kanbanexam.studyflow.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.kanbanexam.studyflow.R;
import com.kanbanexam.studyflow.data.Chapter;
import com.kanbanexam.studyflow.repo.StudyRepository;
import com.kanbanexam.studyflow.utils.RevisionUtils;
import java.util.List;

public class ChapterAdapter extends RecyclerView.Adapter<ChapterAdapter.ChapterViewHolder> {
    private List<Chapter> chapters;
    private StudyRepository repository;
    
    public ChapterAdapter(List<Chapter> chapters, StudyRepository repository) {
        this.chapters = chapters;
        this.repository = repository;
    }
    
    public void updateChapters(List<Chapter> newChapters) {
        this.chapters = newChapters;
        notifyDataSetChanged();
    }
    
    @NonNull
    @Override
    public ChapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chapter, parent, false);
        return new ChapterViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ChapterViewHolder holder, int position) {
        Chapter chapter = chapters.get(position);
        holder.chapterTitle.setText(chapter.title);
        
        String[] statuses = {"Not Started", "In Progress", "Done"};
        String[] difficulties = {"Easy", "Medium", "Hard"};
        int[] statusColors = {R.color.text_muted, R.color.warning, R.color.success};
        
        holder.statusBadge.setText(statuses[chapter.status]);
        try {
            holder.statusBadge.setBackgroundColor(holder.itemView.getContext().getResources().getColor(statusColors[chapter.status]));
        } catch (Exception e) {
            holder.statusBadge.setBackgroundColor(holder.itemView.getContext().getResources().getColor(R.color.text_muted));
        }
        
        holder.difficultyText.setText(difficulties[chapter.difficulty]);
        holder.revisionLevelText.setText("Revision: " + chapter.revisionLevel);
        
        holder.itemView.setOnClickListener(v -> showChapterOptions(v.getContext(), chapter, position));
    }
    
    private void showChapterOptions(Context context, Chapter chapter, int position) {
        String[] options = {"Mark In Progress", "Mark Done", "Schedule Revision", "Cancel"};
        
        new AlertDialog.Builder(context)
            .setTitle(chapter.title)
            .setItems(options, (dialog, which) -> {
                switch (which) {
                    case 0: // In Progress
                        chapter.status = 1;
                        repository.updateChapter(chapter, v -> {
                            Toast.makeText(context, "Marked as In Progress", Toast.LENGTH_SHORT).show();
                            notifyItemChanged(position);
                        });
                        break;
                    case 1: // Done
                        chapter.status = 2;
                        RevisionUtils.scheduleNextRevision(chapter);
                        repository.updateChapter(chapter, v -> {
                            Toast.makeText(context, "Marked as Done", Toast.LENGTH_SHORT).show();
                            notifyItemChanged(position);
                        });
                        break;
                    case 2: // Schedule Revision
                        RevisionUtils.markRevised(chapter);
                        repository.updateChapter(chapter, v -> {
                            Toast.makeText(context, "Revision scheduled", Toast.LENGTH_SHORT).show();
                            notifyItemChanged(position);
                        });
                        break;
                }
            })
            .show();
    }
    
    @Override
    public int getItemCount() {
        return chapters != null ? chapters.size() : 0;
    }
    
    static class ChapterViewHolder extends RecyclerView.ViewHolder {
        TextView chapterTitle, statusBadge, difficultyText, revisionLevelText;
        
        ChapterViewHolder(@NonNull View itemView) {
            super(itemView);
            chapterTitle = itemView.findViewById(R.id.chapterTitle);
            statusBadge = itemView.findViewById(R.id.statusBadge);
            difficultyText = itemView.findViewById(R.id.difficultyText);
            revisionLevelText = itemView.findViewById(R.id.revisionLevelText);
        }
    }
}

