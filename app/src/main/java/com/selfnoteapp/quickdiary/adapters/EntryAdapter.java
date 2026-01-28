package com.selfnoteapp.quickdiary.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.chip.Chip;
import com.selfnoteapp.quickdiary.R;
import com.selfnoteapp.quickdiary.data.DiaryEntry;
import com.selfnoteapp.quickdiary.utils.DateUtils;
import java.util.List;

public class EntryAdapter extends RecyclerView.Adapter<EntryAdapter.EntryViewHolder> {
    private List<DiaryEntry> entries;
    private OnEntryClickListener listener;
    
    public interface OnEntryClickListener {
        void onEntryClick(DiaryEntry entry);
    }
    
    public EntryAdapter(List<DiaryEntry> entries, OnEntryClickListener listener) {
        this.entries = entries;
        this.listener = listener;
    }
    
    @NonNull
    @Override
    public EntryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_entry_row, parent, false);
        return new EntryViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull EntryViewHolder holder, int position) {
        DiaryEntry entry = entries.get(position);
        holder.dateText.setText(DateUtils.formatShortDate(entry.dateKey));
        
        String preview = entry.text.length() > 100 ? entry.text.substring(0, 100) + "..." : entry.text;
        holder.previewText.setText(preview);
        
        int wordCount = entry.text.trim().isEmpty() ? 0 : entry.text.trim().split("\\s+").length;
        holder.wordCountText.setText(wordCount + " words");
        
        if (entry.tag != null && !entry.tag.isEmpty()) {
            holder.tagChip.setVisibility(View.VISIBLE);
            holder.tagChip.setText(entry.tag);
        } else {
            holder.tagChip.setVisibility(View.GONE);
        }
        
        if (entry.mood >= 0 && entry.mood <= 4) {
            holder.moodDot.setVisibility(View.VISIBLE);
            int[] moodColors = {0xFF22C55E, 0xFF4A66FF, 0xFFF59E0B, 0xFFEF4444, 0xFF8B0000};
            holder.moodDot.setColorFilter(moodColors[entry.mood]);
        } else {
            holder.moodDot.setVisibility(View.GONE);
        }
        
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEntryClick(entry);
            }
        });
    }
    
    @Override
    public int getItemCount() {
        return entries != null ? entries.size() : 0;
    }
    
    public void updateEntries(List<DiaryEntry> newEntries) {
        this.entries = newEntries;
        notifyDataSetChanged();
    }
    
    static class EntryViewHolder extends RecyclerView.ViewHolder {
        TextView dateText;
        TextView previewText;
        TextView wordCountText;
        ImageView moodDot;
        Chip tagChip;
        
        EntryViewHolder(@NonNull View itemView) {
            super(itemView);
            dateText = itemView.findViewById(R.id.dateText);
            previewText = itemView.findViewById(R.id.previewText);
            wordCountText = itemView.findViewById(R.id.wordCountText);
            moodDot = itemView.findViewById(R.id.moodDot);
            tagChip = itemView.findViewById(R.id.tagChip);
        }
    }
}

