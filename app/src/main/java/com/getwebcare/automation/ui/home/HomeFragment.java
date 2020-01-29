package com.getwebcare.automation.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.getwebcare.automation.R;
import com.getwebcare.automation.adapter.CriticalDeviceAdapter;
import com.getwebcare.automation.models.CriticalDevices;
import com.getwebcare.automation.models.OtherDevices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeFragment extends Fragment {
    String TAG = "HomeFragment";
    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.critical_device)
    RecyclerView criticalDevice;
    @BindView(R.id.other_text)
    TextView otherText;
    @BindView(R.id.other_device_list)
    RecyclerView otherDeviceList;
    private HomeViewModel homeViewModel;
    List<CriticalDevices> criticalDevicesList;
    List<OtherDevices> otherDevicesList;
    FirebaseAuth mAuth;
    CriticalDeviceAdapter criticalDevicesAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this,root);
        criticalDevice.setHasFixedSize(true);
        otherDevicesList=new ArrayList<OtherDevices>();
        criticalDevice.setLayoutManager(new LinearLayoutManager(getContext()));
        otherDeviceList.setLayoutManager(new LinearLayoutManager(getContext()));
        mAuth = FirebaseAuth.getInstance();
       // database = FirebaseDatabase.getInstance()
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
                            criticalDevicesList = new ArrayList<CriticalDevices>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //DocumentReference snapshot=document.get();
                                Map<String, Object> group = document.getData();
                                Log.d(TAG, document.getId() + " => " + document.getData().values().toString());
                                //Log.d(TAG, reference.getId() + " => " + reference.getPath());
                                CriticalDevices criticalDevices = null;
                                OtherDevices otherDevices = null;
                                for (Map.Entry<String, Object> entry : group.entrySet()) {

                                    //myRef.getKey()
                                    if (entry.getKey().equalsIgnoreCase("roomtype")) {
                                        // devicesModel.setName(entry.getKey());
                                        if (criticalDevices != null) {
                                            criticalDevices.setType(entry.getValue().toString());
                                            otherDevices = new OtherDevices();
                                            otherDevices.setRoomtype(entry.getValue().toString());
                                            if (!otherDevicesList.contains(entry.getValue().toString())) {
                                                otherDevices.setOffDevices(0);
                                                otherDevices.setOnDevices(0);
                                                otherDevicesList.add(otherDevices);
                                            }
                                        }
                                    } else {
                                        criticalDevices = new CriticalDevices();
                                        criticalDevices.setName(entry.getKey());
                                        criticalDevices.setId(entry.getValue().toString());
                                    }

                                }
                                if (criticalDevices != null) {
                                    criticalDevicesList.add(criticalDevices);

                                }

                            }
                            //Collections.sort(devicesModels,new SortByRoom());
                            // Collections.sort(roomsModels,new RoomModelSort());
                            //getRealtimeListner(devicesModels.get(0).getId());
                            // progressRv.setVisibility(View.GONE);
                            criticalDevicesAdapter = new CriticalDeviceAdapter(criticalDevicesList,getContext());
                          /*  List<DevicesModel> devicelist=new ArrayList<DevicesModel>();
                            for(int i=0;i<roomsModels.size();i++){
                                devicelist=new ArrayList<DevicesModel>();
                                for (DevicesModel dev_model:devicesModels
                                ) {
                                    if(dev_model.getType().equalsIgnoreCase(roomsModels.get(i).getRoomName())) {
                                        devicelist.add(dev_model);
                                    }
                                }

                                sectionedAdapter.addSection(roomsModels.get(i).getRoomName(),new ExpandableRoomSection(roomsModels.get(i).getRoomName(),devicelist,clickListener,getContext(),roomsModels));
                            }*/

                              criticalDevice.setAdapter(criticalDevicesAdapter);
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
        return root;
    }
}