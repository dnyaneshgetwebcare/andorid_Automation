package com.iplug.automation;

import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddSchedual extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    @BindView(R.id.device_details)
    TextView deviceDetails;
    @BindView(R.id.device_status)
    ToggleButton deviceStatus;
    @BindView(R.id.sch_time)
    TextView schTime;
    @BindView(R.id.week_all)
    CheckBox weekAll;
    @BindView(R.id.week_sun)
    CheckBox weekSun;
    @BindView(R.id.week_mon)
    CheckBox weekMon;
    @BindView(R.id.week_tue)
    CheckBox weekTue;
    @BindView(R.id.week_wed)
    CheckBox weekWed;
    @BindView(R.id.week_thu)
    CheckBox weekThu;
    @BindView(R.id.week_fri)
    CheckBox weekFri;
    @BindView(R.id.week_sat)
    CheckBox weekSat;
    @BindView(R.id.add_schedual)
    Button addSchedual;
    FirebaseAuth mAuth;
    //ArrayList<CheckBox> checkBoxes;
    private int  mHour, mMinute;
    String device_id,document_id,device_name;
    String TAG="AddSchedual";
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schedual);
        ButterKnife.bind(this);
        context=this;
        weekAll.setOnCheckedChangeListener(this);
        //checkBoxes.add(weekSun);
        final Calendar c = Calendar.getInstance();
        device_id=getIntent().getStringExtra("device_id");
        device_name=getIntent().getStringExtra("device_name");
        document_id=getIntent().getStringExtra("document_id");
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);
        schTime.setText(mHour + ":" + mMinute);
    }

    @OnClick({R.id.sch_time, R.id.add_schedual})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.sch_time:
                // Get Current Time
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                String am_pm = "";

                                Calendar datetime = Calendar.getInstance();
                                datetime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                datetime.set(Calendar.MINUTE, minute);

                            /*    if (datetime.get(Calendar.AM_PM) == Calendar.AM)
                                    am_pm = "AM";
                                else if (datetime.get(Calendar.AM_PM) == Calendar.PM)
                                    am_pm = "PM";*/

                               // String strHrsToShow = (datetime.get(Calendar.HOUR) == 0) ?"12":datetime.get(Calendar.HOUR)+"";


                                schTime.setText(hourOfDay + ":" + minute);
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
                break;
            case R.id.add_schedual:
                String check_duration=checksselection();
                if(check_duration!=null){
                    String dev_status="OFF";
                    String time=schTime.getText().toString();
                    if(deviceStatus.isChecked()){
                         dev_status="ON";
                    }

                    submit_data(dev_status+"-"+check_duration+"-"+time+";",dev_status,device_id,time);
                }else{
                    Toast.makeText(AddSchedual.this,"Please select Duration",Toast.LENGTH_LONG);
                }
                break;
        }
    }

    private void submit_data(String check_duration,String deviceAction,String deviceId,String time) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        Log.d(TAG, "User Signed In " + user.getEmail());
        Toast.makeText(context, "User Signed In " + user.getEmail(), Toast.LENGTH_SHORT).show();
        DocumentReference documentReference=db.collection("users").document(user.getEmail()).collection("devices").document(document_id);
         documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                Map<String,Object> fields=task.getResult().getData();
                boolean addnew=true;
                for (Map.Entry<String, Object> entry : fields.entrySet()) {
                    if(entry.getKey().equalsIgnoreCase("schedule")){
                        addnew=false;
                        updateSchedual(entry.getValue().toString()+check_duration,documentReference);
                        break;
                    }
                }
                if(addnew){
                    updateSchedual(check_duration,documentReference);
                }
            }
        });
         db.collection("schedules").document("add").update("deviceAction",deviceAction,
                 "deviceId",deviceId,
                 "time",time
         );
    }



    public void  updateSchedual(String schedual,DocumentReference documentReference){

      documentReference.update(
              "schedule",schedual
      );
    }
    private String checksselection() {
        String duration_string=null;
        if(weekAll.isChecked()){

            return "all";
        }

        if(weekSun.isChecked()){
            duration_string="sun";

        }
        if(weekWed.isChecked()){
            if(duration_string!=null){
                duration_string=duration_string+",wed";
            }else{
                duration_string="wed";
            }

        }
        if(weekTue.isChecked()){
            if(duration_string!=null){
                duration_string=duration_string+",tue";
            }else{
                duration_string="tue";
            }
        }
        if(weekThu.isChecked()){
            if(duration_string!=null){
                duration_string=duration_string+",thu";
            }else{
                duration_string="thu";
            }
        }
        if(weekSat.isChecked()){
            if(duration_string!=null){
                duration_string=duration_string+",sat";
            }else{
                duration_string="sat";
            }
        }
        if(weekFri.isChecked()){
            if(duration_string!=null){
                duration_string=duration_string+",fri";
            }else{
                duration_string="fri";
            }
        }
        if(weekMon.isChecked()){
            if(duration_string!=null){
                duration_string=duration_string+",mon";
            }else{
                duration_string="mon";
            }
        }
        return duration_string;
    }

    public void setAll(boolean isChecked){
        weekSun.setChecked(isChecked);
        weekFri.setChecked(isChecked);
        weekMon.setChecked(isChecked);
        weekSat.setChecked(isChecked);
        weekThu.setChecked(isChecked);
        weekTue.setChecked(isChecked);
        weekWed.setChecked(isChecked);

        weekSun.setEnabled(!isChecked);
        weekFri.setEnabled(!isChecked);
        weekMon.setEnabled(!isChecked);
        weekSat.setEnabled(!isChecked);
        weekThu.setEnabled(!isChecked);
        weekTue.setEnabled(!isChecked);
        weekWed.setEnabled(!isChecked);
     }
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()){
            case R.id.week_all:
                setAll(isChecked);
                break;

        }
    }
}
