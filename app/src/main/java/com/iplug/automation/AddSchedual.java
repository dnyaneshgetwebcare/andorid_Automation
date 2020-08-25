package com.iplug.automation;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

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
    @BindView(R.id.seek_bar)
    SeekBar seekBar;
    @BindView(R.id.seek_val)
    TextView seekVal;
    @BindView(R.id.ll_dimmer)
    LinearLayout llDimmer;
    //ArrayList<CheckBox> checkBoxes;
    private int mHour, mMinute;
    String device_id, document_id, device_name, room_type,device_type;
    String TAG = "AddSchedual";
    Context context;
    String dev_brightness="0";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schedual);
        setTitle("Add Routines");
        ButterKnife.bind(this);
        context = this;
        weekAll.setOnCheckedChangeListener(this);
        //checkBoxes.add(weekSun);
        final Calendar c = Calendar.getInstance();
        device_id = getIntent().getStringExtra("device_id");
        device_name = getIntent().getStringExtra("device_name");
        document_id = getIntent().getStringExtra("document_id");
        room_type = getIntent().getStringExtra("room_type");
        device_type = getIntent().getStringExtra("device_type");
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);
        schTime.setText(String.format("%02d", mHour) + ":" + String.format("%02d", mMinute));
        deviceDetails.setText(device_name + " [" + room_type + "]");
        llDimmer.setVisibility(View.GONE);
        if(device_type.equalsIgnoreCase("fan") || device_type.equalsIgnoreCase("Dlight")){
            llDimmer.setVisibility(View.VISIBLE);
        }
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekVal.setText(progress+" %");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
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


                                schTime.setText(String.format("%02d", hourOfDay) + ":" + String.format("%02d", minute));
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
                break;
            case R.id.add_schedual:
                String check_duration = checksselection();
                if (check_duration != null) {
                    String dev_status = "OFF";
                    String time = schTime.getText().toString();
                    if (deviceStatus.isChecked()) {
                        dev_status = "ON";
                    }

                    dev_brightness="0";
                    String request_string=dev_status + "-" + check_duration + "-" + time+"-"+dev_brightness + ";";
                    if(device_type.equalsIgnoreCase("fan") || device_type.equalsIgnoreCase("Dlight")){
                        dev_brightness=seekBar.getProgress()+"";
                        request_string=dev_status + "-" + check_duration + "-" + time+"-"+dev_brightness + ";";
                    }
                    submit_data(request_string, dev_status, device_id, check_duration + "-" + time,dev_brightness);
                } else {
                    Toast.makeText(AddSchedual.this, "Please select Duration", Toast.LENGTH_LONG);
                }
                break;
        }
    }

    private void submit_data(String check_duration, String deviceAction, String deviceId, String time,String brightness) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        Log.d(TAG, "User Signed In " + user.getEmail());
        // Toast.makeText(context, "User Signed In " + user.getEmail(), Toast.LENGTH_SHORT).show();
        DocumentReference documentReference = db.collection("users").document(user.getEmail()).collection("devices").document(document_id);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                Map<String, Object> fields = task.getResult().getData();
                boolean addnew = true;
                for (Map.Entry<String, Object> entry : fields.entrySet()) {
                    if (entry.getKey().equalsIgnoreCase("schedule")) {
                        addnew = false;
                        updateSchedual(entry.getValue().toString() + check_duration, documentReference);
                        break;
                    }
                }
                if (addnew) {
                    updateSchedual(check_duration, documentReference);
                }
                Toast.makeText(context, "Added Successfully", Toast.LENGTH_LONG).show();
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result", check_duration);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });
        db.collection("schedules").document("add").update("deviceAction", deviceAction,
                "deviceId", deviceId,"deviceBrightness",brightness,
                "time", time
        );
    }


    public void updateSchedual(String schedual, DocumentReference documentReference) {

        documentReference.update(
                "schedule", schedual
        );
    }

    private String checksselection() {
        String duration_string = null;
        if (weekAll.isChecked()) {

            return "all";
        }

        if (weekSun.isChecked()) {
            duration_string = "sun";

        }
        if (weekWed.isChecked()) {
            if (duration_string != null) {
                duration_string = duration_string + ",wed";
            } else {
                duration_string = "wed";
            }

        }
        if (weekTue.isChecked()) {
            if (duration_string != null) {
                duration_string = duration_string + ",tue";
            } else {
                duration_string = "tue";
            }
        }
        if (weekThu.isChecked()) {
            if (duration_string != null) {
                duration_string = duration_string + ",thu";
            } else {
                duration_string = "thu";
            }
        }
        if (weekSat.isChecked()) {
            if (duration_string != null) {
                duration_string = duration_string + ",sat";
            } else {
                duration_string = "sat";
            }
        }
        if (weekFri.isChecked()) {
            if (duration_string != null) {
                duration_string = duration_string + ",fri";
            } else {
                duration_string = "fri";
            }
        }
        if (weekMon.isChecked()) {
            if (duration_string != null) {
                duration_string = duration_string + ",mon";
            } else {
                duration_string = "mon";
            }
        }
        return duration_string;
    }

    public void setAll(boolean isChecked) {
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
        switch (buttonView.getId()) {
            case R.id.week_all:
                setAll(isChecked);
                break;

        }
    }
}
