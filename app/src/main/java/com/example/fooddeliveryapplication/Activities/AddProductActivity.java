package com.example.fooddeliveryapplication.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.fooddeliveryapplication.LoginActivity;
import com.example.fooddeliveryapplication.MyShopActivity;
import com.example.fooddeliveryapplication.R;

public class AddProductActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_add_product);
        ImageView back = findViewById(R.id.imgBack);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddProductActivity.this, MyShopActivity.class);
                startActivity(intent);
            }
        });
    }
}