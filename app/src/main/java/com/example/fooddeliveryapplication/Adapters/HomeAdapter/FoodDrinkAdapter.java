package com.example.fooddeliveryapplication.Adapters.HomeAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.fooddeliveryapplication.Fragments.Home.DrinkHomeFrg;
import com.example.fooddeliveryapplication.Fragments.Home.FoodHomeFrg;

import java.text.NumberFormat;

public class FoodDrinkAdapter extends FragmentStateAdapter {
    private String userId;

    NumberFormat formatter = NumberFormat.getCurrencyInstance();
    public FoodDrinkAdapter(@NonNull Fragment fragment,String id) {
        super(fragment);
        userId = id;
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 1:
                return new DrinkHomeFrg(userId);
            default:
                return new FoodHomeFrg(userId);
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
