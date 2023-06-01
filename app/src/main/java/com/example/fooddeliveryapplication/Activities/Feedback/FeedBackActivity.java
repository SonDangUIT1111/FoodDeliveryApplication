package com.example.fooddeliveryapplication.Activities.Feedback;

import android.content.Intent;
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
    ActivityFeedBackBinding binding;
    ArrayList<BillInfo> dsBillInfo;
    Bill currentBill;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityFeedBackBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent=getIntent();
        dsBillInfo= (ArrayList<BillInfo>) intent.getSerializableExtra("List of billInfo");
        currentBill= (Bill) intent.getSerializableExtra("Current Bill");
        initUI();
    }



    private void initUI() {
        FeedBackAdapter adapter=new FeedBackAdapter(this,dsBillInfo,currentBill);
        binding.ryc.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL,false));
        binding.ryc.setAdapter(adapter);
        binding.ryc.setHasFixedSize(true);
        //Set sự kiện cho nút back
        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}