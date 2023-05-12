package com.example.fooddeliveryapplication.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.TableLayout;

import com.example.fooddeliveryapplication.Adapters.ViewPagerAdapter;
import com.example.fooddeliveryapplication.Fragments.CurrentProductFragment;
import com.example.fooddeliveryapplication.R;
import com.google.android.material.tabs.TabLayout;

public class MyOrderActivity extends AppCompatActivity {

    private TabLayout mTabLayout;
    private ViewPager mviewPager;

    private final Fragment[] PAGES = new Fragment[]{
            new CurrentProductFragment(),
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_my_order);

        mTabLayout = findViewById(R.id.tab_layout);
        mviewPager = findViewById(R.id.view_pager);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        mviewPager.setAdapter(viewPagerAdapter);
        mTabLayout.setupWithViewPager(mviewPager);

    }
}