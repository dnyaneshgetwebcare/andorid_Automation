package com.iplug.automation.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.iplug.automation.R;
import com.iplug.automation.models.SchedualDetails;

import java.util.List;

public class SchedualAdapter extends RecyclerView.Adapter<SchedualAdapter.ViewHolder> {
    List<SchedualDetails> schedualList;

    public SchedualAdapter(List<SchedualDetails> schedualList) {
        this.schedualList = schedualList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.scheduel_item, parent, false);
        SchedualAdapter.ViewHolder viewHolder = new SchedualAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SchedualDetails schedualDetails = schedualList.get(position);
        holder.time_tv.setText(schedualDetails.getTime());
        holder.duration_tv.setText(schedualDetails.getDuration());
        holder.status_tv.setText(schedualDetails.getStatus());
    }

    @Override
    public int getItemCount() {
        return schedualList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView duration_tv, time_tv, status_tv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.duration_tv = itemView.findViewById(R.id.sch_duration);
            this.status_tv = itemView.findViewById(R.id.sch_status);
            this.time_tv = itemView.findViewById(R.id.sch_time);
        }
    }
}
