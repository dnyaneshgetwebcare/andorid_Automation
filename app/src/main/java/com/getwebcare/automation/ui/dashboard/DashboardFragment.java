package com.getwebcare.automation.ui.dashboard;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.getwebcare.automation.R;
import com.getwebcare.automation.UserDataActivity;
import com.getwebcare.automation.adapter.DeviceAdapter;


import com.getwebcare.automation.models.DevicesModel;
import com.getwebcare.automation.models.RoomsModel;
import com.getwebcare.automation.until.SortByRoom;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

import static android.content.ContentValues.TAG;

public class DashboardFragment extends Fragment implements ExpandableRoomSection.ClickListener {
    @BindView(R.id.progress_rv)
    RelativeLayout progressRv;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    List<String> roomsModels;
    ExpandableRoomSection.ClickListener clickListener;
    private static final String TAG = "UserDataActivity";
   // DeviceAdapter adapter;
    List<DevicesModel> devicesModels;
    FirebaseDatabase database;
    FirebaseAuth mAuth;
    private SectionedRecyclerViewAdapter sectionedAdapter;
    //private DashboardViewModel dashboardViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
       /* dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);*/
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        // final TextView textView = root.findViewById(R.id.text_dashboard);
        /*dashboardViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        clickListener=this;
        sectionedAdapter = new SectionedRecyclerViewAdapter();
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        ButterKnife.bind(this,root);
        recyclerView.setHasFixedSize(true);
        roomsModels=new ArrayList<String>();
       // RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 2);
      //  recyclerView.setLayoutManager(mLayoutManager);
        //recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        final GridLayoutManager glm = new GridLayoutManager(getContext(), 2);
        glm.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(final int position) {
                if (sectionedAdapter.getSectionItemViewType(position) == SectionedRecyclerViewAdapter.VIEW_TYPE_HEADER) {
                    return 2;
                }
                return 1;
            }
        });
        recyclerView.setLayoutManager(glm);
        recyclerView.setAdapter(sectionedAdapter);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        Log.d(TAG, "User Signed In " + user.getEmail());
        Toast.makeText(getContext(), "User Signed In " + user.getEmail(), Toast.LENGTH_SHORT).show();
        db.collection("users").document(user.getEmail()).collection("devices")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            devicesModels = new ArrayList<DevicesModel>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //DocumentReference snapshot=document.get();
                                Map<String, Object> group = document.getData();
                                Log.d(TAG, document.getId() + " => " + document.getData().values().toString());
                                //Log.d(TAG, reference.getId() + " => " + reference.getPath());
                                DevicesModel devicesModel = null;
                                RoomsModel roomM=null;
                                for (Map.Entry<String, Object> entry : group.entrySet()) {

                                    //myRef.getKey()
                                    if(entry.getKey().equalsIgnoreCase("roomtype")) {
                                       // devicesModel.setName(entry.getKey());
                                        if(devicesModel!=null) {
                                            devicesModel.setType(entry.getValue().toString());
                                            //roomM=new RoomsModel();
                                            //roomM.setRoomName(entry.getValue().toString());
                                            if(!roomsModels.contains(entry.getValue().toString())){
                                                roomsModels.add(entry.getValue().toString());
                                            }
                                        }
                                    }else{
                                        devicesModel = new DevicesModel();
                                        devicesModel.setName(entry.getKey());
                                        devicesModel.setId(entry.getValue().toString());
                                    }

                                }
                                if(devicesModel!=null){
                                    devicesModels.add(devicesModel);

                                }

                            }
                            Collections.sort(devicesModels,new SortByRoom());
                            Collections.sort(roomsModels);
                            //getRealtimeListner(devicesModels.get(0).getId());
                            progressRv.setVisibility(View.GONE);
                            //adapter = new DeviceAdapter(devicesModels,getContext());
                            List<DevicesModel> devicelist=new ArrayList<DevicesModel>();
                            for(int i=0;i<roomsModels.size();i++){
                                devicelist=new ArrayList<DevicesModel>();
                                for (DevicesModel dev_model:devicesModels
                                     ) {
                                    if(dev_model.getType().equalsIgnoreCase(roomsModels.get(i))) {
                                        devicelist.add(dev_model);
                                    }
                                }

                                sectionedAdapter.addSection(roomsModels.get(i),new ExpandableRoomSection(roomsModels.get(i),devicelist,clickListener,getContext()));
                            }

                          //  recyclerView.setAdapter(adapter);
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
        return root;
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

                                devicesModels.get(adapterpos).setStatus(entry.getValue().toString());
                            }
                        }else if(ds.getKey().equalsIgnoreCase("Brightness")){

                            devicesModels.get(adapterpos).setBrightness(ds.getValue().toString());
                        }
                    }

                    sectionedAdapter.notifyItemChanged(adapterpos);
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
    public void onHeaderRootViewClicked(@NonNull String sectionTitle, @NonNull ExpandableRoomSection section) {
    /*    final SectionAdapter sectionAdapter = sectionedAdapter.getAdapterForSection(section);

        // store info of current section state before changing its state
        final boolean wasExpanded = section.isExpanded();
        final int previousItemsTotal = section.getContentItemsTotal();

        section.setExpanded(!wasExpanded);
        sectionAdapter.notifyHeaderChanged();

        if (wasExpanded) {
            sectionAdapter.notifyItemRangeRemoved(0, previousItemsTotal);
        } else {
            sectionAdapter.notifyAllItemsInserted();
        }*/
    }

    @Override
    public void onItemRootViewClicked(@NonNull String sectionTitle, int itemAdapterPosition) {

    }

    @Override
    public void onStateChanged(int adapterpos) {
        //getRealtimeListner(device_id,position,adapterpos);
        sectionedAdapter.notifyItemChanged(adapterpos);

    }
}