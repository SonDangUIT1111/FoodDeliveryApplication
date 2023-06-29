package com.example.fooddeliveryapplication.Activities.Home;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.fooddeliveryapplication.databinding.ActivityChatBinding;

public class ChatActivity extends AppCompatActivity {
    ActivityChatBinding binding;
    String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        userId=getIntent().getStringExtra("userId");

    }
}