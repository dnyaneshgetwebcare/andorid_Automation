package com.getwebcare.automation.ui.notifications;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.getwebcare.automation.R;
import com.getwebcare.automation.SignIn;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class ProfileFragment extends Fragment {
    @BindView(R.id.img_profile)
    ImageView imgProfile;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_email)
    TextView tvEmail;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.logout)
    ImageButton logout;
    FirebaseAuth mAuth;
    @BindView(R.id.tv_address)
    EditText tvAddress;
    @BindView(R.id.property_type)
    EditText propertyType;
    @BindView(R.id.property_variant)
    EditText propertyVariant;
    @BindView(R.id.save)
    Button save;
    //private NotificationsViewModel notificationsViewModel;
    FirebaseFirestore db;
    FirebaseUser user;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //notificationsViewModel =ViewModelProviders.of(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, root);
        mAuth = FirebaseAuth.getInstance();
         user = mAuth.getCurrentUser();
        //final TextView textView = root.findViewById(R.id.text_notifications);
       /* notificationsViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        Glide.with(this)
                .load(user.getPhotoUrl())
                .into(imgProfile);

        //tvName.setText(user.getDisplayName());
        tvEmail.setText(user.getEmail());
         db = FirebaseFirestore.getInstance();
       // FirebaseUser user = mAuth.getCurrentUser();
      //  Log.d(TAG, "User Signed In " + user.getEmail());
       // Toast.makeText(getContext(), "User Signed In " + user.getEmail(), Toast.LENGTH_SHORT).show();
        db.collection("users").document(user.getEmail()).collection("user_details").document("personal_details")
                .get()
       .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                       // document.getData();
                        Map<String, Object> group = document.getData();
                        for (Map.Entry<String, Object> entry : group.entrySet()) {
                            Log.w(TAG,entry.getKey()+" => "+entry.getValue());
                            switch (entry.getKey()){
                                case "name":
                                    tvName.setText(entry.getValue().toString());
                                    break;
                                case "address":
                                    tvAddress.setText(entry.getValue().toString());
                                    break;

                                case "number":
                                    tvPhone.setText(entry.getValue().toString());
                                    break;

                                case "property_type":
                                    propertyType.setText(entry.getValue().toString());
                                    break;

                                case "property_vairent":
                                    propertyVariant.setText(entry.getValue().toString());
                                    break;


                            }

                        }
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        return root;
    }


    @OnClick({R.id.logout, R.id.save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.logout:
                mAuth.signOut();
                getActivity().finish();
                startActivity(new Intent(getContext(), SignIn.class));
                break;
            case R.id.save:
                updateProfile();
                break;
        }
    }

    public void updateProfile(){
        Map<String, Object> docData = new HashMap<>();
        docData.put("name",tvName.getText().toString());
        docData.put("address",tvAddress.getText().toString());
        docData.put("number",tvPhone.getText().toString());
        docData.put("property_type",propertyType.getText().toString());
        docData.put("property_vairent",propertyVariant.getText().toString());
        //docData.put("address",tvAddress.getText().toString());

        db.collection("users").document(user.getEmail()).collection("user_details").document("personal_details")
                .set(docData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }

}