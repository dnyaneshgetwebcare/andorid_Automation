package com.iplug.automation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.iplug.automation.adapter.SchedualAdapter;
import com.iplug.automation.models.SchedualDetails;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ScheduleActivity extends AppCompatActivity {
    String[] weekarray = {"all", "sun", "mon", "tue", "wed", "thus", "fri", "sat"};
    List<SchedualDetails> schedualDetails;
    @BindView(R.id.device_name)
    TextView deviceName;
    @BindView(R.id.title)
    LinearLayout title;
    @BindView(R.id.rv_scheduel)
    RecyclerView rvScheduel;
    @BindView(R.id.empty_id)
    TextView emptyId;
    String device_name,document_id,device_id;
    FirebaseAuth mAuth;
    String TAG = "";
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.add_schedual)
    FloatingActionButton addSchedual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedual_main);
        ButterKnife.bind(this);
        schedualDetails = new ArrayList<SchedualDetails>();
        String schedual_string = getIntent().getStringExtra("schedual_string");
         document_id = getIntent().getStringExtra("document_id");
        device_id = getIntent().getStringExtra("device_id");
        device_name = getIntent().getStringExtra("device_name");
        mAuth = FirebaseAuth.getInstance();
        rvScheduel.setHasFixedSize(true);
        rvScheduel.setLayoutManager(new LinearLayoutManager(this));
        if (schedual_string != null) {
            String[] schedule_array = schedual_string.split(";");

            for (int i = 0; i < schedule_array.length; i++) {
                String[] schedule = schedule_array[i].split("-");
                SchedualDetails schedualDetail = new SchedualDetails();
                schedualDetail.setStatus(schedule[0]);
                schedualDetail.setDuration(schedule[1]);
                schedualDetail.setTime(schedule[2]);
                schedualDetail.setDevice_id(device_name);
                schedualDetails.add(schedualDetail);
            }

        }

       /*  FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        Log.d(TAG, "User Signed In " + user.getEmail());
        Toast.makeText(this, "User Signed In " + user.getEmail(), Toast.LENGTH_SHORT).show();
       db.collection("users").document(user.getEmail()).collection("schedules").document("device1A4:CF:12:25:D5:78")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                      // DocumentSnapshot documentSnapshot=task.getResult();

                        Map<String, Object> group = task.getResult().getData();
                        if(group!=null) {
                            for (Map.Entry<String, Object> entry : group.entrySet()) {
                                Log.w(TAG, entry.getKey() + " value :: " + entry.getValue());
                                String[] schedule=entry.getValue().toString().split("-");
                                SchedualDetails schedualDetail=new SchedualDetails();
                                schedualDetail.setStatus(schedule[0]);
                                schedualDetail.setDuration(schedule[1]);
                                schedualDetail.setTime(schedule[2]);
                                schedualDetail.setDevice_id(device_name);
                                schedualDetails.add(schedualDetail);
                            }
                        }
                        if (schedualDetails.size() == 0) {
                            rvScheduel.setVisibility(View.GONE);
                            emptyId.setVisibility(View.VISIBLE);
                        }else{
                            rvScheduel.setVisibility(View.VISIBLE);
                            emptyId.setVisibility(View.GONE);
                            SchedualAdapter schedualAdapter=new SchedualAdapter(schedualDetails);
                            rvScheduel.setAdapter(schedualAdapter);
                        }

                    }
                });*/
        //RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 2);
        // recyclerView.setLayoutManager(mLayoutManager);


        if (schedualDetails.size() == 0) {
            rvScheduel.setVisibility(View.GONE);
            emptyId.setVisibility(View.VISIBLE);
        } else {
            rvScheduel.setVisibility(View.VISIBLE);
            emptyId.setVisibility(View.GONE);
            SchedualAdapter schedualAdapter = new SchedualAdapter(schedualDetails);
            rvScheduel.setAdapter(schedualAdapter);
        }

    }

    @OnClick(R.id.add_schedual)
    public void onViewClicked() {
        Intent intent=new Intent(ScheduleActivity.this,AddSchedual.class);
        intent.putExtra("document_id",document_id);
        intent.putExtra("device_d",device_id);
        intent.putExtra("device_name",device_name);
        startActivity(intent);
    }
}
