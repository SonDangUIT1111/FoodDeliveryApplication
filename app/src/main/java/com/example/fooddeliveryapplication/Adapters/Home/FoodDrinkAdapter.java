package com.example.fooddeliveryapplication.Adapters.Home;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.fooddeliveryapplication.Fragments.Home.DrinkHomeFrg;
import com.example.fooddeliveryapplication.Fragments.Home.FoodHomeFrg;

public class FoodDrinkAdapter extends FragmentStateAdapter {
    private final String userId;

    public FoodDrinkAdapter(@NonNull Fragment fragment, String id) {
        super(fragment);
        userId = id;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 1) {
            return new DrinkHomeFrg(userId);
        }
        return new FoodHomeFrg(userId);
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
