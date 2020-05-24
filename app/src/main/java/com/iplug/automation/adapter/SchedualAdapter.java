package com.iplug.automation.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.iplug.automation.MainActivity;
import com.iplug.automation.R;
import com.iplug.automation.models.SchedualDetails;

import java.util.List;

public class SchedualAdapter extends RecyclerView.Adapter<SchedualAdapter.ViewHolder>  {
    List<SchedualDetails> schedualList;
    String[] sch_array;
    DeleteItem deleteItem;
    Context context;

    public SchedualAdapter(List<SchedualDetails> schedualList,String[] string_sch,DeleteItem deleteItem,Context context) {
        this.schedualList = schedualList;
        this.sch_array=string_sch;
        this.deleteItem=deleteItem;
        this.context=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.scheduel_item, parent, false);
        SchedualAdapter.ViewHolder viewHolder = new SchedualAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SchedualDetails schedualDetails=schedualList.get(position);
        holder.time_tv.setText(schedualDetails.getTime());
        holder.duration_tv.setText(schedualDetails.getDuration());
        holder.status_tv.setText(schedualDetails.getStatus());
        holder.deletesch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                      //  .setTitle("Title")
                        .setMessage("Are you sure want to delete?")
                       // .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                deleteSchdual(position);
                            }})
                        .setNegativeButton(android.R.string.no, null).show();

            }
        });
    }

    private void deleteSchdual(int sch_pos) {

        String check_duration=schedualList.get(sch_pos).getDuration();
        String deviceAction=schedualList.get(sch_pos).getStatus();
        String deviceId=schedualList.get(sch_pos).getDevice_id();
        String time=schedualList.get(sch_pos).getTime();
        schedualList.remove(sch_pos);
        notifyItemRemoved(sch_pos);

        notifyItemRangeChanged(sch_pos, schedualList.size());

        deleteItem.deleteItem(deviceAction+"-"+check_duration+"-"+time+";",deviceAction,deviceId,check_duration+"-"+time,schedualList.size());
    }

    @Override
    public int getItemCount() {
        return schedualList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView duration_tv,time_tv,status_tv;
        ImageButton deletesch;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.duration_tv=itemView.findViewById(R.id.sch_duration);
            this.status_tv=itemView.findViewById(R.id.sch_status);
            this.time_tv=itemView.findViewById(R.id.sch_time);
            this.deletesch=itemView.findViewById(R.id.delete_sch);
        }
    }
    public  interface DeleteItem{
        void deleteItem(String check_duration,String deviceAction,String deviceId,String time,int list_size);
    }
}
