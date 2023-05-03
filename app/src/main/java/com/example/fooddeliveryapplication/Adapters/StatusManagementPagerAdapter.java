package com.example.fooddeliveryapplication.Adapters;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.fooddeliveryapplication.Fragments.CompletedStatusDeliveryFragment;
import com.example.fooddeliveryapplication.Fragments.ConfirmStatusDeliveryFragment;
import com.example.fooddeliveryapplication.Fragments.ShippingStatusDeliveryFragment;

public class StatusManagementPagerAdapter extends FragmentStateAdapter {

    public StatusManagementPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 1:
                return new ShippingStatusDeliveryFragment();
            case 2:
                return new CompletedStatusDeliveryFragment();
            default:
                return new ConfirmStatusDeliveryFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }

}
