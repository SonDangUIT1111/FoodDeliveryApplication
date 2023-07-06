package com.example.fooddeliveryapplication.Activities.MyShop;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fooddeliveryapplication.Activities.OrderSellerManagement.DeliveryManagementActivity;
import com.example.fooddeliveryapplication.databinding.ActivityMyShopBinding;

public class MyShopActivity extends AppCompatActivity {
    private ActivityMyShopBinding binding;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMyShopBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getWindow().setStatusBarColor(Color.parseColor("#E8584D"));
        getWindow().setNavigationBarColor(Color.parseColor("#E8584D"));

        userId = getIntent().getStringExtra("userId");
        binding.cardMyFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MyShopActivity.this,MyFoodActivity.class);
                intent.putExtra("userId",userId);
                startActivity(intent);
            }
        });


        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        binding.cardDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MyShopActivity.this, DeliveryManagementActivity.class);
                intent.putExtra("userId",userId);
                startActivity(intent);
            }
        });
    }
}