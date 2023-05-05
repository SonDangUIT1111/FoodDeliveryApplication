package com.example.fooddeliveryapplication.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.example.fooddeliveryapplication.Adapters.StatusManagementPagerAdapter;
import com.example.fooddeliveryapplication.R;
import com.google.android.material.tabs.TabLayout;

public class DeliveryManagementActivity extends AppCompatActivity {
    String userId;

    TabLayout tabLayoutDelivery;
    ViewPager2 viewPagerStatus;
    StatusManagementPagerAdapter statusPagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_management);

        //get input
        userId = "randomUserId2";

        // find view by id
        tabLayoutDelivery = (TabLayout) findViewById(R.id.tabLayoutDelivery);
        viewPagerStatus = (ViewPager2) findViewById(R.id.viewPagerStatus);
        statusPagerAdapter = new StatusManagementPagerAdapter(this,userId);
        viewPagerStatus.setAdapter(statusPagerAdapter);


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