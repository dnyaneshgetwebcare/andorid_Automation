package com.getwebcare.automation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.getwebcare.automation.R;
import com.getwebcare.automation.models.OtherDevices;

import java.util.List;

public class OtherDeviceAdapter extends RecyclerView.Adapter<OtherDeviceAdapter.ViewHolder>  {
    List<OtherDevices> otherDevicesList;
    Context context;
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.other_item, parent, false);
        OtherDeviceAdapter.ViewHolder viewHolder = new OtherDeviceAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    public OtherDeviceAdapter(List<OtherDevices> otherDevicesList, Context context) {
        this.otherDevicesList = otherDevicesList;
        this.context = context;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
OtherDevices otherDevices =otherDevicesList.get(position);
holder.header_room.setText(otherDevices.getRoomtype());
holder.on_count.setText(otherDevices.getOnDevices()+"");
holder.off_status.setText(otherDevices.getOffDevices()+"");
    }

    @Override
    public int getItemCount() {
        return otherDevicesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView on_count,header_room,off_status;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            on_count=(TextView) itemView.findViewById(R.id.on_count);
            off_status=(TextView) itemView.findViewById(R.id.off_count);
            header_room=(TextView)itemView.findViewById(R.id.other_header_title);
        }
    }
}
