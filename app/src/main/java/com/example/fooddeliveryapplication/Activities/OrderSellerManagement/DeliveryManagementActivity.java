package com.example.fooddeliveryapplication.Activities.OrderSellerManagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.example.fooddeliveryapplication.Activities.Order.OrderDetailActivity;
import com.example.fooddeliveryapplication.Adapters.DeliveryManagement_Seller.StatusManagementPagerAdapter;
import com.example.fooddeliveryapplication.Helpers.FirebaseNotificationHelper;
import com.example.fooddeliveryapplication.Model.Notification;
import com.example.fooddeliveryapplication.databinding.ActivityDeliveryManagementBinding;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

public class DeliveryManagementActivity extends AppCompatActivity {
    private ActivityDeliveryManagementBinding binding;
    private String userId;
    private StatusManagementPagerAdapter statusPagerAdapter;
    private Notification notification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDeliveryManagementBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getWindow().setStatusBarColor(Color.parseColor("#E8584D"));
        getWindow().setNavigationBarColor(Color.parseColor("#E8584D"));

        //get input
        userId = getIntent().getStringExtra("userId");

        statusPagerAdapter = new StatusManagementPagerAdapter(this,userId);
        binding.viewPagerStatus.setAdapter(statusPagerAdapter);


        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // connect tab layout with view pager
        binding.tabLayoutDelivery.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.viewPagerStatus.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
        binding.viewPagerStatus.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                binding.tabLayoutDelivery.getTabAt(position).select();
            }
        });
    }
}