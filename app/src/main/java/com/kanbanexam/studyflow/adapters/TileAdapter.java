package com.kanbanexam.studyflow.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.kanbanexam.studyflow.R;
import com.kanbanexam.studyflow.model.DashboardTile;
import java.util.List;

public class TileAdapter extends RecyclerView.Adapter<TileAdapter.TileViewHolder> {
    private List<DashboardTile> tiles;
    
    public TileAdapter(List<DashboardTile> tiles) {
        this.tiles = tiles;
    }
    
    @NonNull
    @Override
    public TileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_dashboard_tile, parent, false);
        return new TileViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull TileViewHolder holder, int position) {
        DashboardTile tile = tiles.get(position);
        holder.icon.setImageResource(tile.iconRes);
        holder.title.setText(tile.title);
        holder.subtitle.setText(tile.subtitle);
        if (holder.contentLayout != null) {
            holder.contentLayout.setBackgroundResource(tile.gradientDrawableRes);
        }
        holder.itemView.setOnClickListener(v -> {
            if (tile.action != null) {
                try {
                    tile.action.run();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    
    @Override
    public int getItemCount() {
        return tiles != null ? tiles.size() : 0;
    }
    
    static class TileViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView title;
        TextView subtitle;
        View contentLayout;
        
        TileViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.tileIcon);
            title = itemView.findViewById(R.id.tileTitle);
            subtitle = itemView.findViewById(R.id.tileSubtitle);
            contentLayout = itemView.findViewById(R.id.tileContentLayout);
        }
    }
}

