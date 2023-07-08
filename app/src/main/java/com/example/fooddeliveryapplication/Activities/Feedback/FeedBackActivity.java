package com.example.fooddeliveryapplication.Activities.Feedback;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fooddeliveryapplication.Adapters.FeedbackAdapter.FeedBackAdapter;
import com.example.fooddeliveryapplication.Model.Bill;
import com.example.fooddeliveryapplication.Model.BillInfo;
import com.example.fooddeliveryapplication.databinding.ActivityFeedBackBinding;

import java.util.ArrayList;

public class FeedBackActivity extends AppCompatActivity {
    private ActivityFeedBackBinding binding;
    private ArrayList<BillInfo> dsBillInfo;
    private Bill currentBill;
    private String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityFeedBackBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent=getIntent();
        dsBillInfo= (ArrayList<BillInfo>) intent.getSerializableExtra("List of billInfo");
        currentBill= (Bill) intent.getSerializableExtra("Current Bill");
        userId = intent.getStringExtra("userId");
        initUI();
    }

    private void initUI() {
        getWindow().setStatusBarColor(Color.parseColor("#E8584D"));
        getWindow().setNavigationBarColor(Color.parseColor("#E8584D"));
        FeedBackAdapter adapter=new FeedBackAdapter(this,dsBillInfo,currentBill,userId);
        binding.ryc.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL,false));
        binding.ryc.setHasFixedSize(true);
        binding.ryc.setAdapter(adapter);
        //Set sự kiện cho nút back
        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}