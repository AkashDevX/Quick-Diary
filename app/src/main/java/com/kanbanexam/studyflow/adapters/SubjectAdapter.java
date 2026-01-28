package com.kanbanexam.studyflow.adapters;

import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.kanbanexam.studyflow.R;
import com.kanbanexam.studyflow.activities.SubjectDetailActivity;
import com.kanbanexam.studyflow.data.Chapter;
import com.kanbanexam.studyflow.data.Subject;
import com.kanbanexam.studyflow.repo.StudyRepository;
import java.util.List;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.SubjectViewHolder> {
    private List<Subject> subjects;
    private StudyRepository repository;
    
    public SubjectAdapter(List<Subject> subjects, StudyRepository repository) {
        this.subjects = subjects;
        this.repository = repository;
    }
    
    public void updateSubjects(List<Subject> newSubjects) {
        this.subjects = newSubjects;
        notifyDataSetChanged();
    }
    
    @NonNull
    @Override
    public SubjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_subject, parent, false);
        return new SubjectViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull SubjectViewHolder holder, int position) {
        Subject subject = subjects.get(position);
        holder.subjectName.setText(subject.name);
        holder.weeklyGoal.setText(subject.weeklyGoalMinutes / 60 + "h/week");
        
        // Load chapter count
        repository.getChaptersBySubject(subject.id, chapters -> {
            if (chapters != null) {
                holder.chapterCount.setText(chapters.size() + " chapters");
            } else {
                holder.chapterCount.setText("0 chapters");
            }
        });
        
        try {
            int color = Color.parseColor(subject.colorHex);
            holder.colorIndicator.setBackgroundColor(color);
        } catch (Exception e) {
            holder.colorIndicator.setBackgroundColor(holder.itemView.getContext().getResources().getColor(R.color.primary));
        }
        
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), SubjectDetailActivity.class);
            intent.putExtra("subjectId", subject.id);
            intent.putExtra("subjectName", subject.name);
            v.getContext().startActivity(intent);
        });
    }
    
    @Override
    public int getItemCount() {
        return subjects != null ? subjects.size() : 0;
    }
    
    static class SubjectViewHolder extends RecyclerView.ViewHolder {
        TextView subjectName, chapterCount, weeklyGoal;
        View colorIndicator;
        
        SubjectViewHolder(@NonNull View itemView) {
            super(itemView);
            subjectName = itemView.findViewById(R.id.subjectName);
            chapterCount = itemView.findViewById(R.id.chapterCount);
            weeklyGoal = itemView.findViewById(R.id.weeklyGoal);
            colorIndicator = itemView.findViewById(R.id.colorIndicator);
        }
    }
}

