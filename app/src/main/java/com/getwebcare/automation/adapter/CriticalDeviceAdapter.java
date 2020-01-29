package com.getwebcare.automation.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
//import org.apache.commons.lang3.text.WordUtils;
import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.getwebcare.automation.R;
import com.getwebcare.automation.models.CriticalDevices;
import com.getwebcare.automation.models.DevicesModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class CriticalDeviceAdapter extends RecyclerView.Adapter<CriticalDeviceAdapter.ViewHolder> {
    List<CriticalDevices> criticalDevicesList;
    Context context;
    FirebaseDatabase database;

    public CriticalDeviceAdapter(List<CriticalDevices> criticalDevicesList, Context context) {
        this.criticalDevicesList = criticalDevicesList;
        this.context = context;
        database = FirebaseDatabase.getInstance();
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
        holder.device_name.setText(myListData.getName().toUpperCase()+" ("+myListData.getType()+")");
        //holder.device_status.;

        if(criticalDevicesList.get(position).getStatus()==null || criticalDevicesList.get(position).getStatus().equalsIgnoreCase("")){
            getRealtimeListner(criticalDevicesList.get(position).getId(),position);
        }else{
            //holder.progressBar.setVisibility(View.GONE);
          //  holder.rl_container.setVisibility(View.VISIBLE);

            holder.device_status.setText(criticalDevicesList.get(position).getStatus().equalsIgnoreCase("true")?"On":"Off");
            holder.device_status.setTextColor(criticalDevicesList.get(position).getStatus().equalsIgnoreCase("true")?
                    ResourcesCompat.getColor(context.getResources(),R.color.on_status,null):
                    ResourcesCompat.getColor(context.getResources(),R.color.off_status,null));
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
                                criticalDevicesList.get(position).setStatus(entry.getValue().toString());
                            }

                        }else if(ds.getKey().equalsIgnoreCase("Brightness")){

                           // criticalDevicesList.get(position).setBrightness(ds.getValue().toString());
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
