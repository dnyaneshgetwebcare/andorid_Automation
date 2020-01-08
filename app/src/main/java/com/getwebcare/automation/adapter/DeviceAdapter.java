package com.getwebcare.automation.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.getwebcare.automation.R;
import com.getwebcare.automation.models.DevicesModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.ViewHolder> {
    List<DevicesModel> deviceList;
    FirebaseDatabase database;
    Context context;

    @NonNull
    @Override
    public DeviceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.item_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    public DeviceAdapter(List<DevicesModel> deviceList,Context context) {
        this.deviceList = deviceList;
        database = FirebaseDatabase.getInstance();
        this.context=context;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final DevicesModel myListData = deviceList.get(position);
        holder.relativeLayout.setVisibility(View.GONE);
        holder.progressBar.setVisibility(View.VISIBLE);
        holder.device_name.setText(deviceList.get(position).getName());
        holder.device_img.setImageResource(getDeviceType(deviceList.get(position).getName(),deviceList.get(position).getStatus()));
        holder.device_status.setImageResource(getDrawable(deviceList.get(position).getStatus()));
      //  holder.change_status.setText(deviceList.get(position).getStatus());

        if(deviceList.get(position).getStatus()==null || deviceList.get(position).getStatus().equalsIgnoreCase("")){
            getRealtimeListner(deviceList.get(position).getId(),position);
        }else{
            holder.progressBar.setVisibility(View.GONE);
            holder.relativeLayout.setVisibility(View.VISIBLE);
        }

       holder.device_status.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               changeStatus(deviceList.get(position).getId(),deviceList.get(position).getStatus());
           }
       });
        //Log.d(TAG, myRef.getKey());
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(),"click on item: "+myListData.getId(),Toast.LENGTH_LONG).show();
            }
        });
    }

    public int getDrawable(String status){
        if(status==null){
            return R.mipmap.ic_loader;
        }
        switch (status){
            case "false":

                return R.mipmap.power_off;
                default:
                    return R.mipmap.power_on;

        }

    }
public int getDeviceType(String type,String status){
        if(status!=null) {
            switch (type) {
                case "light":
                    if (status.equalsIgnoreCase("true")) {
                        return R.drawable.ic_lightbulb_on;
                    } else {
                        return R.drawable.ic_lightbulb_2;
                    }
                case "plug":

                    if (status.equalsIgnoreCase("true")) {
                        return R.drawable.ic_plug_on;
                    } else {
                        return R.drawable.ic_plug_off;
                    }
            }
        }
return R.drawable.ic_unknown;
}
    @Override
    public int getItemCount() {
        return deviceList.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView device_name;
        public ImageView device_img;
        public RelativeLayout relativeLayout;
        public ImageButton device_status;
        public ProgressBar progressBar;
        public ViewHolder(View itemView) {
            super(itemView);
            this.device_name = (TextView) itemView.findViewById(R.id.deveice_name);
            this.device_status=(ImageButton) itemView.findViewById(R.id.btn_change);
            this.device_img = (ImageView) itemView.findViewById(R.id.img_vw);
           this.progressBar=(ProgressBar) itemView.findViewById(R.id.prgress_bar_power);
            relativeLayout = (RelativeLayout)itemView.findViewById(R.id.relativeLayout);
        }
    }

    public void getRealtimeListner(String device_id, final int position){
        DatabaseReference myRef = database.getReference().getRoot().child(device_id);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {

                    for(DataSnapshot ds : dataSnapshot.getChildren()) {
                        //User expense = ds.getValue(User.class);
                        // users.add(expense.getUsername());
                        Log.d(TAG, ds.getKey() + " => " + ds.getValue());
                        if(ds.getKey().equalsIgnoreCase("OnOff")){

                            Map<String,Object> device_status= (Map<String, Object>) ds.getValue();
                            for (Map.Entry<String, Object> entry : device_status.entrySet()) {
                                Log.d(TAG, entry.getKey() + " => " + entry.getValue());
                                deviceList.get(position).setStatus(entry.getValue().toString());
                            }

                        }else if(ds.getKey().equalsIgnoreCase("Brightness")){

                            deviceList.get(position).setBrightness(ds.getValue().toString());
                        }


                    }

                    notifyItemChanged(position);

                }catch(Exception e){
                    Log.e("MODEL_ERROR","Error Occurred while mapping to model");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("FIREBASE",databaseError.getMessage());
            }


        });
    }
    public void changeStatus(String device_id,String status_val){
        DatabaseReference myRef = database.getReference().getRoot().child(device_id).child("OnOff").child("on");
        boolean bool = Boolean.parseBoolean(status_val);
        myRef.setValue(!bool);
    }
}
