package com.example.fooddeliveryapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.example.fooddeliveryapplication.Adapters.AdapterTest;
import com.example.fooddeliveryapplication.Adapters.StatusManagementPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class TestActivityForFragmentTask extends AppCompatActivity {

    String userId;

    TabLayout tabLayoutTest;
    ViewPager2 viewPagerTest;
    AdapterTest adapterTest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_for_fragment_task);

        //get input
        userId = "randomUserId1";

        // find view by id
        tabLayoutTest = (TabLayout) findViewById(R.id.tabLayoutTest);
        viewPagerTest = (ViewPager2) findViewById(R.id.viewPagerTest);
        adapterTest = new AdapterTest(this,userId);
        viewPagerTest.setAdapter(adapterTest);


        // connect tab layout with view pager
        tabLayoutTest.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPagerTest.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
        viewPagerTest.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayoutTest.getTabAt(position).select();
            }
        });
    }
}