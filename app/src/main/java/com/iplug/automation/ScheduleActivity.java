package com.iplug.automation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.iplug.automation.adapter.SchedualAdapter;
import com.iplug.automation.models.SchedualDetails;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ScheduleActivity extends AppCompatActivity implements SchedualAdapter.DeleteItem {
    String[] weekarray = {"all", "sun", "mon", "tue", "wed", "thus", "fri", "sat"};
    List<SchedualDetails> schedualDetails;
    @BindView(R.id.device_name)
    TextView deviceName;
    @BindView(R.id.room_name)
    TextView roomName;
    @BindView(R.id.title)
    LinearLayout title;
    @BindView(R.id.rv_scheduel)
    RecyclerView rvScheduel;
    @BindView(R.id.empty_id)
    TextView emptyId;
    String device_name,document_id,device_id,room_type,device_type;
    FirebaseAuth mAuth;
    String TAG = "";
  /*  @BindView(R.id.toolbar)
    Toolbar toolbar;*/
    @BindView(R.id.add_schedual)
    FloatingActionButton addSchedual;
    String[] schedule_array=null;
    int INTENT_RESULT=1;
    Context context;
    SchedualAdapter schedualAdapter;
    int total_sch=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedual_main);
        setTitle("Routines");
        ButterKnife.bind(this);
        schedualDetails = new ArrayList<SchedualDetails>();
        String schedual_string = getIntent().getStringExtra("schedual_string");
         document_id = getIntent().getStringExtra("document_id");
        device_id = getIntent().getStringExtra("device_id");
        device_name = getIntent().getStringExtra("device_name");
        room_type = getIntent().getStringExtra("room_type");
        device_type = getIntent().getStringExtra("device_type");
        context=this;
        roomName.setText(room_type);
        // deviceName.setText(room_type+ "->"+ device_name);
        deviceName.setText(device_name);
        mAuth = FirebaseAuth.getInstance();
        rvScheduel.setHasFixedSize(true);
        rvScheduel.setLayoutManager(new LinearLayoutManager(this));
  /*      if (schedual_string != null) {
            schedule_array = schedual_string.split(";");

            for (int i = 0; i < schedule_array.length; i++) {
                String[] schedule = schedule_array[i].split("-");
                SchedualDetails schedualDetail = new SchedualDetails();
                schedualDetail.setStatus(schedule[0]);
                schedualDetail.setDuration(schedule[1]);
                schedualDetail.setTime(schedule[2]);
                schedualDetail.setSch_pos(i);
                schedualDetail.setDevice_id(device_name);
                schedualDetails.add(schedualDetail);
            }

        }*/

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        Log.d(TAG, "User Signed In " + user.getEmail());
       // Toast.makeText(this, "User Signed In " + user.getEmail(), Toast.LENGTH_SHORT).show();
            db.collection("users").document(user.getEmail()).collection("devices").document(document_id)
                .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {

                                    Map<String, Object> map = document.getData();
                                    if (map != null) {
                                        if (map.get("schedule")!=null) {
                                            String schedual_string = map.get("schedule").toString();
                                            if (schedual_string != null) {
                                                schedule_array = schedual_string.split(";");

                                                for (int i = 0; i < schedule_array.length; i++) {
                                                    String[] schedule = schedule_array[i].split("-");
                                                    SchedualDetails schedualDetail = new SchedualDetails();
                                                    if(schedule.length<2){
                                                        continue;
                                                    }
                                                    schedualDetail.setBrightness("0");
                                                    if(device_type.equalsIgnoreCase("fan") || device_type.equalsIgnoreCase("Dlight")){

                                                        if(schedule.length==4){
                                                            schedualDetail.setBrightness(schedule[3]);
                                                        }
                                                    }

                                                    schedualDetail.setStatus(schedule[0]);
                                                    schedualDetail.setDuration(schedule[1]);
                                                    schedualDetail.setTime(schedule[2]);
                                                    schedualDetail.setSch_pos(i);
                                                    schedualDetail.setDevice_id(device_name);
                                                    schedualDetail.setDevice_type(device_type);
                                                    schedualDetails.add(schedualDetail);
                                                }
                                                if (schedualDetails.size() == 0) {
                                                    rvScheduel.setVisibility(View.GONE);
                                                    emptyId.setVisibility(View.VISIBLE);
                                                } else {
                                                    rvScheduel.setVisibility(View.VISIBLE);
                                                    emptyId.setVisibility(View.GONE);
                                                    total_sch= schedualDetails.size();
                                                    SchedualAdapter schedualAdapter = new SchedualAdapter(schedualDetails, schedule_array, ScheduleActivity.this,context);
                                                    rvScheduel.setAdapter(schedualAdapter);
                                                }
                                            }
                                        }

                                    }

                                }
                            }
                        }
                    });

        //RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 2);
        // recyclerView.setLayoutManager(mLayoutManager);


        if (schedualDetails.size() == 0) {
            rvScheduel.setVisibility(View.GONE);
            emptyId.setVisibility(View.VISIBLE);
        } else {
            rvScheduel.setVisibility(View.VISIBLE);
            emptyId.setVisibility(View.GONE);
             schedualAdapter = new SchedualAdapter(schedualDetails,schedule_array,this,context);
            rvScheduel.setAdapter(schedualAdapter);
        }

    }
    private void submit_data(String check_duration,String deviceAction,String deviceId,String time,String bright) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        Log.d(TAG, "User Signed In " + user.getEmail());
       //Toast.makeText(context, "User Signed In " + user.getEmail(), Toast.LENGTH_SHORT).show();
        DocumentReference documentReference=db.collection("users").document(user.getEmail()).collection("devices").document(document_id);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                Map<String,Object> fields=task.getResult().getData();
                boolean addnew=true;
                for (Map.Entry<String, Object> entry : fields.entrySet()) {
                    if(entry.getKey().equalsIgnoreCase("schedule")){
                        addnew=false;
                        String result=entry.getValue().toString().replace(check_duration,"");
                        updateSchedual(result,documentReference);
                        break;
                    }
                }
                /*if(addnew){
                    updateSchedual(check_duration,documentReference);
                }*/

            }
        });
        db.collection("schedules").document("remove").update("deviceAction",deviceAction,
                "deviceId",deviceId,"deviceBrightness",bright,
                "time",time
        );
    }

    public void  updateSchedual(String schedual,DocumentReference documentReference){

        documentReference.update(
                "schedule",schedual
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == INTENT_RESULT) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getStringExtra("result");
              //  Toast.makeText(context,"Added Succesfully",Toast.LENGTH_LONG).show();
                if (result != null) {
                    schedule_array = result.split(";");

                   // for (int i = 0; i < schedule_array.length; i++) {
                        String[] schedule = schedule_array[0].split("-");
                        SchedualDetails schedualDetail = new SchedualDetails();
                    schedualDetail.setBrightness("0");
                    if(device_type.equalsIgnoreCase("fan") || device_type.equalsIgnoreCase("Dlight")){

                        if(schedule.length==4){
                            schedualDetail.setBrightness(schedule[3]);
                        }
                    }
                        schedualDetail.setStatus(schedule[0]);
                        schedualDetail.setDuration(schedule[1]);
                        schedualDetail.setTime(schedule[2]);
                    schedualDetail.setDevice_type(device_type);
                        schedualDetail.setSch_pos(schedualDetails.size());
                        schedualDetail.setDevice_id(device_name);
                        schedualDetails.add(schedualDetail);
                   // }
                    rvScheduel.setVisibility(View.VISIBLE);
                    emptyId.setVisibility(View.GONE);
                    if(schedualAdapter==null){

                        schedualAdapter = new SchedualAdapter(schedualDetails,schedule_array,this,context);
                        rvScheduel.setAdapter(schedualAdapter);
                    }else {
                        schedualAdapter.notifyDataSetChanged();
                    }
                    total_sch=schedualDetails.size();
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

    @OnClick(R.id.add_schedual)
    public void onViewClicked() {
        if(total_sch<6) {
            Intent intent = new Intent(ScheduleActivity.this, AddSchedual.class);
            intent.putExtra("document_id", document_id);
            intent.putExtra("device_id", device_id);
            intent.putExtra("device_name", device_name);
            intent.putExtra("room_type", room_type);
            intent.putExtra("device_type", device_type);
            startActivityForResult(intent, INTENT_RESULT);
        }else{
            Toast.makeText(context, " Sorry! Maximum 6 routines can be added for each device", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void deleteItem(String check_duration, String deviceAction, String deviceId, String time,int list_size,String bright) {
        submit_data(check_duration,deviceAction,deviceId,time,bright);
        total_sch=list_size;
        if(list_size==0){
            rvScheduel.setVisibility(View.GONE);
            emptyId.setVisibility(View.VISIBLE);
        }
    }
}
