package com.example.fooddeliveryapplication.Activities.MyShop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fooddeliveryapplication.databinding.ActivityMyShopBinding;

public class MyShopActivity extends AppCompatActivity {

    ActivityMyShopBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMyShopBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.cardMyFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MyShopActivity.this,MyFoodActivity.class);
                startActivity(intent);
            }
        });


    }
}