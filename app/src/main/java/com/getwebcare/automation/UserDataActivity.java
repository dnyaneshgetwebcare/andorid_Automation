package com.getwebcare.automation;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
//import androidx.recyclerview.widget.RecyclerView;
//import android.support.v7.widget.RecyclerView;

import com.getwebcare.automation.adapter.DeviceAdapter;
import com.getwebcare.automation.models.DevicesModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserDataActivity extends AppCompatActivity {

    private static final String TAG = "UserDataActivity";
    @BindView(R.id.recyclerView)

    RecyclerView recyclerView;
    DeviceAdapter adapter;
    List<DevicesModel> devicesModels;
    FirebaseDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user_data);
        database = FirebaseDatabase.getInstance();
        ButterKnife.bind(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document("1234").collection("devices")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                             devicesModels = new ArrayList<DevicesModel>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //DocumentReference snapshot=document.get();
                                Map<String,Object> group = document.getData();
                                Log.d(TAG, document.getId() + " => " + document.getData().values().toString());
                                //Log.d(TAG, reference.getId() + " => " + reference.getPath());
                                for (Map.Entry<String, Object> entry : group.entrySet()) {

                                    //myRef.getKey()
                                    DevicesModel devicesModel = new DevicesModel();
                                    devicesModel.setName(entry.getKey());
                                    devicesModel.setId(entry.getValue().toString());
                                    devicesModels.add(devicesModel);
                                }
                            }

                            //getRealtimeListner(devicesModels.get(0).getId());
                            adapter = new DeviceAdapter(devicesModels);
                            recyclerView.setAdapter(adapter);
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

}
