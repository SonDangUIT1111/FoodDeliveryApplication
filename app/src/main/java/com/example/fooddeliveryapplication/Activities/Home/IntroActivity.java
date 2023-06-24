package com.example.fooddeliveryapplication.Activities.Home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.fooddeliveryapplication.Adapters.Home.IntroAdapter;
import com.example.fooddeliveryapplication.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import java.util.ArrayList;

public class IntroActivity extends AppCompatActivity {

    TabLayout.Tab tabSelected=null;
    Button btnNext;
    ViewPager2 viewPager;
    DotsIndicator dotsIndicator;
    TabLayout tabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        ArrayList<Integer> ds=new ArrayList<>();
        ds.add(R. drawable.choice);
        ds.add(R. drawable.delivery);
        ds.add(R. drawable.tracking);
        IntroAdapter introAdapter=new IntroAdapter(ds,IntroActivity.this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        btnNext=findViewById(R.id.btnNext);
        viewPager=findViewById(R.id.viewpaper);
        dotsIndicator=findViewById(R.id.dots_indicator);
        viewPager.setAdapter(introAdapter);
        viewPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        dotsIndicator.attachTo(viewPager);
        tabLayout=findViewById(R.id.tablayout);
        new TabLayoutMediator(tabLayout,viewPager, (tab, position)-> {
        }).attach();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition()==2) {
                    btnNext.setVisibility(View.VISIBLE);
                } else {
                    btnNext.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(IntroActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}