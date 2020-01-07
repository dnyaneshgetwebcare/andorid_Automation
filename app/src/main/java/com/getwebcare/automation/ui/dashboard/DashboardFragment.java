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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.getwebcare.automation.R;
import com.getwebcare.automation.UserDataActivity;
import com.getwebcare.automation.adapter.DeviceAdapter;
import com.getwebcare.automation.models.DevicesModel;
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

public class DashboardFragment extends Fragment {
    @BindView(R.id.progress_rv)
    RelativeLayout progressRv;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;


    private static final String TAG = "UserDataActivity";
    DeviceAdapter adapter;
    List<DevicesModel> devicesModels;
    FirebaseDatabase database;
    FirebaseAuth mAuth;

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
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        ButterKnife.bind(this,root);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


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
                            adapter = new DeviceAdapter(devicesModels);
                            recyclerView.setAdapter(adapter);
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
        return root;
    }
}