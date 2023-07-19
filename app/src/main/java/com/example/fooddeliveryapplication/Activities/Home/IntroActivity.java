package com.example.fooddeliveryapplication.Activities.Home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.fooddeliveryapplication.Adapters.Home.IntroAdapter;
import com.example.fooddeliveryapplication.R;
import com.example.fooddeliveryapplication.databinding.ActivityIntroBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class IntroActivity extends AppCompatActivity {
    private ActivityIntroBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        boolean isIntroShown = preferences.getBoolean("isIntroShown", false);
        if (isIntroShown) {
            startActivity(new Intent(IntroActivity.this, LoginActivity.class));
            finish();
            return;
        }

        binding = ActivityIntroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ArrayList<Integer> ds = new ArrayList<>();
        ds.add(R.drawable.choice);
        ds.add(R.drawable.delivery);
        ds.add(R.drawable.tracking);
        IntroAdapter introAdapter = new IntroAdapter(ds,IntroActivity.this);
        binding.viewpaper.setAdapter(introAdapter);
        binding.viewpaper.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        binding.dotsIndicator.attachTo(binding.viewpaper);
        new TabLayoutMediator(binding.tablayout, binding.viewpaper, (tab, position)-> {
        }).attach();
        binding.tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition()==2) {
                    binding.btnNext.setVisibility(View.VISIBLE);
                } else {
                    binding.btnNext.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        binding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(IntroActivity.this, LoginActivity.class));
                finish();
            }
        });

        FirebaseAuth.getInstance().signOut();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isIntroShown", true);
        editor.apply();
    }
}