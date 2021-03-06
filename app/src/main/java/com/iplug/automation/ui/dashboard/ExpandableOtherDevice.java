package com.iplug.automation.ui.dashboard;

import android.content.Context;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.iplug.automation.R;

import com.iplug.automation.holders.HeaderViewHolder;
import com.iplug.automation.holders.ItemViewHolder;
import com.iplug.automation.models.OtherDevices;
import com.iplug.automation.models.RoomsModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Map;

import io.github.luizgrp.sectionedrecyclerviewadapter.Section;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;

import static android.content.ContentValues.TAG;

public class ExpandableOtherDevice extends Section {

    final String title;
    final List<RoomsModel> list;
    final ClickListener clickListener;
    List<OtherDevices> deviceList;
    //SectionedRecyclerViewAdapter sectionedRecyclerViewAdapter;
    FirebaseDatabase database;
    Context context;
    private boolean expanded = true;
    ExpandableOtherDevice(@NonNull final String title, @NonNull final List<OtherDevices> deviceList,
                          @NonNull final ClickListener clickListener, Context context, List<RoomsModel> list) {
        super(SectionParameters.builder()
                .itemResourceId(R.layout.other_item)
                .headerResourceId(R.layout.list_header)
                .build());
        //this.sectionedRecyclerViewAdapter=sectionedRecyclerViewAdapter;
        this.title = title;

        this.list = list;
        this.clickListener = clickListener;
        this.deviceList = deviceList;
        database = FirebaseDatabase.getInstance();
        this.context=context;
    }

    @Override
    public int getContentItemsTotal() {
        return expanded ? deviceList.size() : 0;
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(final View view) {
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindItemViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final ItemViewHolder itemHolder = (ItemViewHolder) holder;
        //position=itemHolder.getAdapterPosition();
        final OtherDevices myListData = deviceList.get(position);
        itemHolder.rl_container.setVisibility(View.GONE);
        itemHolder.progressBar.setVisibility(View.VISIBLE);
        itemHolder.device_name.setText(deviceList.get(position).getRoomtype());
        //itemHolder.room_type.setText(deviceList.get(position).getType());
      //  itemHolder.device_img.setImageResource(getDeviceType(deviceList.get(position).getName(),deviceList.get(position).getStatus()));
      //  itemHolder.device_status.setImageResource(getDrawable(deviceList.get(position).getStatus()));
        //  holder.change_status.setText(deviceList.get(position).getStatus());

      /*  if(deviceList.get(position).getStatus()==null || deviceList.get(position).getStatus().equalsIgnoreCase("")){
            getRealtimeListner(deviceList.get(position).getId(),position,itemHolder.getAdapterPosition());
            //clickListener.onStateChanged(deviceList.get(position).getId(),position,itemHolder.getAdapterPosition());
        }else{
            itemHolder.progressBar.setVisibility(View.GONE);
            itemHolder.rl_container.setVisibility(View.VISIBLE);
        }

        itemHolder.device_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    changeStatus(deviceList.get(position).getId(), deviceList.get(position).getStatus());
                }catch (Exception ex){
                    Log.w("Status",ex.getMessage());
                }
            }
        });
        //Log.d(TAG, myRef.getKey());
        itemHolder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(),"click on item: "+myListData.getId(),Toast.LENGTH_LONG).show();
            }
        });*/
    }
    public void getRealtimeListner(String device_id, final int position,int adapterpos){
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
                               // deviceList.get(position).setStatus(entry.getValue().toString());
                            }
                        }else if(ds.getKey().equalsIgnoreCase("Brightness")){

                            //deviceList.get(position).setBrightness(ds.getValue().toString());
                        }
                    }
                    clickListener.onStateChanged(adapterpos);
                    //sectionedRecyclerViewAdapter.notifyItemChanged(adapterpos);
                    //sectionedRecyclerViewAdapter.notifyItemChangedInSection(title,position);
                    //notifyItemChanged(position);

                }catch(Exception e){
                    Log.e("MODEL_ERROR",e.getMessage());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("FIREBASE",databaseError.getMessage());
            }


        });
    }
    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(final View view) {
        return new HeaderViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(final RecyclerView.ViewHolder holder) {
        final HeaderViewHolder headerHolder = (HeaderViewHolder) holder;

        headerHolder.tvTitle.setText(title);
      /*  headerHolder.imgArrow.setImageResource(
                expanded ? R.drawable.ic_keyboard_arrow_up_black_18dp : R.drawable.ic_keyboard_arrow_down_black_18dp
        );
*/
//Log.w("Adapter Postion",)
        headerHolder.rootView.setOnClickListener(v ->
                clickListener.onHeaderRootViewClicked(title, this)
        );
    }
    public void changeStatus(String device_id,String status_val){
        try {
            DatabaseReference myRef = database.getReference().getRoot().child(device_id).child("OnOff").child("on");
            boolean bool = Boolean.parseBoolean(status_val);
            myRef.setValue((!bool) + "");
        }catch (Exception ex){
            Log.w("Status",ex.getMessage());
        }
    }


    boolean isExpanded() {
        return expanded;
    }

    void setExpanded(final boolean expanded) {
        this.expanded = expanded;
    }

    interface ClickListener {

        void onHeaderRootViewClicked(@NonNull final String sectionTitle, @NonNull final ExpandableOtherDevice section);

        void onItemRootViewClicked(@NonNull final String sectionTitle, final int itemAdapterPosition);

        void onStateChanged(int adapterpos);
    }
}
