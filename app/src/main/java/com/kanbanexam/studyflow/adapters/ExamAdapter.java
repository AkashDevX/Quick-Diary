package com.kanbanexam.studyflow.adapters;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.kanbanexam.studyflow.R;
import com.kanbanexam.studyflow.data.Exam;
import com.kanbanexam.studyflow.repo.StudyRepository;
import com.kanbanexam.studyflow.utils.UiUtils;
import java.util.List;

public class ExamAdapter extends RecyclerView.Adapter<ExamAdapter.ExamViewHolder> {
    private List<Exam> exams;
    private StudyRepository repository;
    private Runnable refreshCallback;
    
    public ExamAdapter(List<Exam> exams, StudyRepository repository, Runnable refreshCallback) {
        this.exams = exams;
        this.repository = repository;
        this.refreshCallback = refreshCallback;
    }
    
    public void updateExams(List<Exam> newExams) {
        this.exams = newExams;
        notifyDataSetChanged();
    }
    
    @NonNull
    @Override
    public ExamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_exam, parent, false);
        return new ExamViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ExamViewHolder holder, int position) {
        Exam exam = exams.get(position);
        holder.examName.setText(exam.name);
        holder.examDate.setText(UiUtils.formatDate(exam.examDate));
        
        long now = System.currentTimeMillis();
        long daysUntil = (exam.examDate - now) / (24 * 60 * 60 * 1000L);
        if (daysUntil < 0) {
            holder.daysUntil.setText("Past exam");
        } else if (daysUntil == 0) {
            holder.daysUntil.setText("Today!");
        } else {
            holder.daysUntil.setText(daysUntil + " days until exam");
        }
        
        holder.itemView.setOnLongClickListener(v -> {
            new AlertDialog.Builder(v.getContext())
                .setTitle("Delete Exam")
                .setMessage("Are you sure you want to delete this exam?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    repository.deleteExam(exam, result -> {
                        Toast.makeText(v.getContext(), "Exam deleted", Toast.LENGTH_SHORT).show();
                        if (refreshCallback != null) refreshCallback.run();
                    });
                })
                .setNegativeButton("Cancel", null)
                .show();
            return true;
        });
    }
    
    @Override
    public int getItemCount() {
        return exams != null ? exams.size() : 0;
    }
    
    static class ExamViewHolder extends RecyclerView.ViewHolder {
        TextView examName, daysUntil, examDate;
        
        ExamViewHolder(@NonNull View itemView) {
            super(itemView);
            examName = itemView.findViewById(R.id.examName);
            daysUntil = itemView.findViewById(R.id.daysUntil);
            examDate = itemView.findViewById(R.id.examDate);
        }
    }
}

