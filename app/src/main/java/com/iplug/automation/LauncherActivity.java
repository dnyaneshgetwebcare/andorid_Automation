package com.iplug.automation;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.iplug.automation.adapter.CriticalDeviceAdapter;
import com.iplug.automation.adapter.OtherDeviceAdapter;
import com.iplug.automation.models.CriticalDevices;
import com.iplug.automation.models.OtherDevices;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LauncherActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    String TAG = "LauncherActivity";
    List<CriticalDevices> criticalDevicesList;
    List<OtherDevices> otherDevicesList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        getSupportActionBar().hide();
        BottomNavigationView navView = findViewById(R.id.nav_view);
        mAuth = FirebaseAuth.getInstance();
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        Bundle bunble = new Bundle();
        bunble.putSerializable("key", null);
        navController.setGraph(navController.getGraph(), bunble);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

    public Object[] getCriticalData() {
        Object[] return_array = new Object[2];
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        otherDevicesList = new ArrayList<OtherDevices>();
        //  Log.d(TAG, "User Signed In " + user.getEmail());
        //Toast.makeText(getContext(), "User Signed In " + user.getEmail(), Toast.LENGTH_SHORT).show();
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
                                    if (criticalDevices == null) {
                                        criticalDevices = new CriticalDevices();
                                    }
                                    //myRef.getKey()
                                    if (entry.getKey().equalsIgnoreCase("roomtype")) {
                                        // devicesModel.setName(entry.getKey());
                                        if (criticalDevices != null) {
                                            criticalDevices.setType(entry.getValue().toString());

                                        }
                                    } else {
                                        // criticalDevices = new CriticalDevices();
                                        criticalDevices.setName(entry.getKey());
                                        criticalDevices.setId(entry.getValue().toString());

                                        otherDevices = new OtherDevices();
                                        otherDevices.setRoomtype(entry.getKey().toString());
                                        otherDevices.setOffDevices(1);
                                        otherDevices.setOnDevices(1);
                                       /* if (!otherDevicesList.contains(otherDevices)) {

                                            otherDevicesList.add(otherDevices);
                                        }else{*/
                                        boolean new_device = true;
                                        for (OtherDevices p : otherDevicesList) {
                                            if (p.getRoomtype().equals(entry.getKey().toString())) {
                                                p.setOnDevices(p.getOnDevices() + 1);
                                                new_device = false;
                                                break;
                                            }
                                        }
                                        if (new_device) {
                                            otherDevicesList.add(otherDevices);
                                        }
                                        //}

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
                            // criticalDevicesAdapter = new CriticalDeviceAdapter(criticalDevicesList,getContext());
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
                            // otherDeviceAdapter=new OtherDeviceAdapter(otherDevicesList,getContext());
                            //   otherDeviceList.setAdapter(otherDeviceAdapter);
                            //  criticalDevice.setAdapter(criticalDevicesAdapter);
                            return_array[0] = criticalDevicesList;
                            return_array[1] = otherDevicesList;
                            //return return_array;
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
        return return_array;
    }
}
