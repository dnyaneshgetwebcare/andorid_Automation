package com.getwebcare.automation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.getwebcare.automation.R;
import com.getwebcare.automation.models.CriticalDevices;
import com.getwebcare.automation.models.DevicesModel;

import java.util.List;

public class CriticalDeviceAdapter extends RecyclerView.Adapter<CriticalDeviceAdapter.ViewHolder> {
    List<CriticalDevices> criticalDevicesList;
    Context context;

    public CriticalDeviceAdapter(List<CriticalDevices> criticalDevicesList, Context context) {
        this.criticalDevicesList = criticalDevicesList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.critical_item, parent, false);
        CriticalDeviceAdapter.ViewHolder viewHolder = new CriticalDeviceAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final CriticalDevices myListData = criticalDevicesList.get(position);
        holder.device_name.setText(myListData.getName()+"("+myListData.getType()+")");
        //holder.device_status.;
    }

    @Override
    public int getItemCount() {
        return criticalDevicesList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView device_name;
        TextView device_status;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.device_name=itemView.findViewById(R.id.device_name);
            this.device_status=itemView.findViewById(R.id.device_status);
        }
    }
}
