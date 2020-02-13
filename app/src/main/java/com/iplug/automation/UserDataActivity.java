package com.iplug.automation;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.iplug.automation.adapter.DeviceAdapter;
import com.iplug.automation.models.DevicesModel;
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

//import androidx.recyclerview.widget.RecyclerView;
//import android.support.v7.widget.RecyclerView;

public class UserDataActivity extends AppCompatActivity {

    private static final String TAG = "UserDataActivity";
    @BindView(R.id.recyclerView)

    RecyclerView recyclerView;
    DeviceAdapter adapter;
    List<DevicesModel> devicesModels;
    FirebaseDatabase database;
    FirebaseAuth mAuth;
    @BindView(R.id.progress_rv)
    RelativeLayout progressRv;
Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user_data);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        ButterKnife.bind(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        context=this;

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        Log.d(TAG, "User Signed In " + user.getEmail());
        Toast.makeText(UserDataActivity.this, "User Signed In " + user.getEmail(), Toast.LENGTH_SHORT).show();
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
                                for (Map.Entry<String, Object> entry : group.entrySet()) {

                                    //myRef.getKey()
                                    DevicesModel devicesModel = new DevicesModel();
                                    devicesModel.setName(entry.getKey());
                                    devicesModel.setId(entry.getValue().toString());
                                    devicesModels.add(devicesModel);
                                }
                            }

                            //getRealtimeListner(devicesModels.get(0).getId());
                            progressRv.setVisibility(View.GONE);
                            adapter = new DeviceAdapter(devicesModels,context);
                            recyclerView.setAdapter(adapter);
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

}
