package com.example.fooddeliveryapplication.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.fooddeliveryapplication.EditProfileActivity;
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
    private CardView cardViewOrders;
    private CardView cardViewMyShop;
    private TextView change;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        userName = view.findViewById(R.id.user_name);
        userEmail = view.findViewById(R.id.user_email);
        userPhoneNumber = view.findViewById(R.id.user_phone_number);
        userAvatar = view.findViewById(R.id.user_avatar);
        cardViewOrders = view.findViewById(R.id.card_view_orders);
        cardViewMyShop = view.findViewById(R.id.card_view_my_shop);
        change = view.findViewById(R.id.change);

        getUserInfo(view);

        cardViewOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO - Go to orders activity
            }
        });

        cardViewMyShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO - Go to my shop activity
            }
        });

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), EditProfileActivity.class));
            }
        });

        return view;
    }

    private void getUserInfo(View view) {
        FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);

                userName.setText(user.getUserName());
                userEmail.setText(user.getEmail());
                userPhoneNumber.setText(user.getPhoneNumber());
                Glide.with(view).load(user.getAvatarURL()).placeholder(R.drawable.profile_image).into(userAvatar);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}