package com.example.fooddeliveryapplication.Activities.OrderSellerManagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.fooddeliveryapplication.Adapters.DeliveryManagement_Seller.StatusManagementPagerAdapter;
import com.example.fooddeliveryapplication.R;
import com.google.android.material.tabs.TabLayout;

public class DeliveryManagementActivity extends AppCompatActivity {
    String userId;

    TabLayout tabLayoutDelivery;
    ViewPager2 viewPagerStatus;
    StatusManagementPagerAdapter statusPagerAdapter;
    ImageButton btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_management);
        getWindow().setStatusBarColor(Color.parseColor("#E8584D"));
        getWindow().setNavigationBarColor(Color.parseColor("#E8584D"));
        //get input
        userId = getIntent().getStringExtra("userId");

        // find view by id
        btnBack = (ImageButton) findViewById(R.id.btnBack);
        tabLayoutDelivery = (TabLayout) findViewById(R.id.tabLayoutDelivery);
        viewPagerStatus = (ViewPager2) findViewById(R.id.viewPagerStatus);
        statusPagerAdapter = new StatusManagementPagerAdapter(this,userId);
        viewPagerStatus.setAdapter(statusPagerAdapter);


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // connect tab layout with view pager
        tabLayoutDelivery.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPagerStatus.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
        viewPagerStatus.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayoutDelivery.getTabAt(position).select();
            }
        });
    }
}