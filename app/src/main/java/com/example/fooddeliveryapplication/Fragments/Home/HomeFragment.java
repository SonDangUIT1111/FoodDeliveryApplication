package com.example.fooddeliveryapplication.Fragments.Home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.fooddeliveryapplication.Activities.Home.FindActivity;
import com.example.fooddeliveryapplication.Adapters.Home.FoodDrinkAdapter;
import com.example.fooddeliveryapplication.databinding.FragmentHomeBinding;

import com.google.android.material.tabs.TabLayoutMediator;


public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private String userId;

    public HomeFragment(String id) {
        userId = id;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater,container,false);

        initUI();

        return binding.getRoot();
    }

    private void initUI() {
        //Set adapter cho recycleView
        FoodDrinkAdapter adapter1=new FoodDrinkAdapter(HomeFragment.this, userId);
        binding.viewpaperHome.setAdapter(adapter1);
        binding.viewpaperHome.setUserInputEnabled(false);
        binding.layoutSearchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), FindActivity.class);
                intent.putExtra("userId",userId);
                startActivity(intent);
            }
        });
        //Tạo Tab
        new TabLayoutMediator(binding.tabHome,binding.viewpaperHome, ((tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Food");
                    break;
                case 1:
                    tab.setText("Drink");
                    break;
            }
        })).attach();
    }
}