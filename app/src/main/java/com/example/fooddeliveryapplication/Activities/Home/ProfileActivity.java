package com.example.fooddeliveryapplication.Activities.Home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.fooddeliveryapplication.Activities.EditProfileActivity;
import com.example.fooddeliveryapplication.Model.User;
import com.example.fooddeliveryapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    private FirebaseUser firebaseUser;

    private TextView userName;
    private TextView userEmail;
    private TextView userPhoneNumber;
    private ImageView userAvatar;
    private CardView cardViewOrders;
    private CardView cardViewMyShop;
    private TextView change;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        userName = (TextView) findViewById(R.id.user_name);
        userEmail = (TextView) findViewById(R.id.user_email);
        userPhoneNumber = (TextView) findViewById(R.id.user_phone_number);
        userAvatar = (ImageView) findViewById(R.id.user_avatar);
        cardViewOrders = (CardView) findViewById(R.id.card_view_orders);
        cardViewMyShop = (CardView) findViewById(R.id.card_view_my_shop);
        change = (TextView) findViewById(R.id.change);

        getUserInfo(ProfileActivity.this);

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
                startActivity(new Intent(ProfileActivity.this, EditProfileActivity.class));
            }
        });

    }

    private void getUserInfo(Context mContext) {
        FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);

                userName.setText(user.getUserName());
                userEmail.setText(user.getEmail());
                userPhoneNumber.setText(user.getPhoneNumber());
                Glide.with(mContext).load(user.getAvatarURL()).placeholder(R.drawable.profile_image).into(userAvatar);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}