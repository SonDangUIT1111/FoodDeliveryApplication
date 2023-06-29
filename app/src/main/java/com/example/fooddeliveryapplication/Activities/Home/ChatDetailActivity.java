package com.example.fooddeliveryapplication.Activities.Home;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.fooddeliveryapplication.databinding.ActivityChatDetailBinding;

public class ChatDetailActivity extends AppCompatActivity {
    ActivityChatDetailBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityChatDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


    }
}