package com.example.fooddeliveryapplication.HomeAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.fooddeliveryapplication.Fragment.DrinkHomeFrg;
import com.example.fooddeliveryapplication.Fragment.FoodHomeFrg;

import java.text.NumberFormat;

public class FoodDrinkAdapter extends FragmentStateAdapter {

    NumberFormat formatter = NumberFormat.getCurrencyInstance();
    public FoodDrinkAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new FoodHomeFrg();
            case 1:
                return new DrinkHomeFrg();
            default:
                return new FoodHomeFrg();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
