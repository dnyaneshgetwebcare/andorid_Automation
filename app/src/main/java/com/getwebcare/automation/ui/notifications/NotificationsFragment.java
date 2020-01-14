package com.getwebcare.automation.ui.notifications;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.getwebcare.automation.MainActivity;
import com.getwebcare.automation.R;
import com.getwebcare.automation.SignIn;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NotificationsFragment extends Fragment {
    @BindView(R.id.img_profile)
    ImageView imgProfile;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_email)
    TextView tvEmail;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.logout)
    Button logout;
    FirebaseAuth mAuth;
    //private NotificationsViewModel notificationsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //notificationsViewModel =ViewModelProviders.of(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        ButterKnife.bind(this,root);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
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

        tvName.setText(user.getDisplayName());
        tvEmail.setText(user.getEmail());
        return root;
    }

    @OnClick(R.id.logout)
    public void onViewClicked() {
        mAuth.signOut();
        getActivity().finish();
        startActivity(new Intent(getContext(), SignIn.class));
    }
}