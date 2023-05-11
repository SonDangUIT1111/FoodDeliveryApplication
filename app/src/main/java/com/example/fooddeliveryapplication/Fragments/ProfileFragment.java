package com.example.fooddeliveryapplication.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.fooddeliveryapplication.Models.User;
import com.example.fooddeliveryapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileFragment extends Fragment {
    private FirebaseUser firebaseUser;

    private TextView userName;
    private TextView userEmail;
    private TextView userPhoneNumber;
    private ImageView userAvatar;

    String userId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        userName = view.findViewById(R.id.user_name);
        userEmail = view.findViewById(R.id.user_email);
        userPhoneNumber = view.findViewById(R.id.user_phone_number);
        userAvatar = view.findViewById(R.id.user_avatar);

        userId = firebaseUser.getUid();

        FirebaseDatabase.getInstance().getReference().child("Users").child("D1doRWCDmJVH4mmWYGRhEUdmlqD3").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);

                if (user == null) {
                    Toast.makeText(getContext(), "Something went wrong!", Toast.LENGTH_SHORT).show();
                }

                userName.setText(user.getUserName());
                userEmail.setText(user.getEmail());
                userPhoneNumber.setText(user.getPhoneNumber());
                Glide.with(view).load(user.getAvatarURL()).into(userAvatar);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //Toast.makeText(getContext(), FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser.getUid()).getKey(), Toast.LENGTH_SHORT).show();

        return view;
    }

    private void getUserInfo(View view) {

    }
}