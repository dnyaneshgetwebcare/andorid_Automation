package com.getwebcare.automation.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.getwebcare.automation.R;
import com.getwebcare.automation.models.DevicesModel;
import com.google.firebase.FirebaseError;
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

    @NonNull
    @Override
    public DeviceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.item_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    public DeviceAdapter(List<DevicesModel> deviceList) {
        this.deviceList = deviceList;
        database = FirebaseDatabase.getInstance();
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final DevicesModel myListData = deviceList.get(position);
        holder.textView.setText(deviceList.get(position).getName());
        //holder.imageView.setImageResource(deviceList.get(position).getImgId());
        holder.device_status.setText(deviceList.get(position).getStatus());
        holder.change_status.setText(deviceList.get(position).getStatus());
       getRealtimeListner(deviceList.get(position).getId(),position);

        //Log.d(TAG, myRef.getKey());
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(),"click on item: "+myListData.getId(),Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return deviceList.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView device_status;
        public TextView textView;
        public RelativeLayout relativeLayout;
        public Button change_status;
        public ViewHolder(View itemView) {
            super(itemView);
            this.device_status = (TextView) itemView.findViewById(R.id.device_status);
            this.change_status=(Button)itemView.findViewById(R.id.btn_change);
            this.textView = (TextView) itemView.findViewById(R.id.textView);
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
                        Map<String,Object> device_status= (Map<String, Object>) ds.getValue();
                        deviceList.get(position).setStatus(device_status.values().toString());

                    }
                    notifyDataSetChanged();

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
}
