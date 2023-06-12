package com.example.fooddeliveryapplication.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.fooddeliveryapplication.Activities.Home.LoginActivity;
import com.example.fooddeliveryapplication.R;

public class AddProductActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_add_product);
        ImageView back = findViewById(R.id.imgBack);

        Button Addproduct = findViewById(R.id.btnAdd);





    }
}