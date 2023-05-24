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

    String userId;
    public StatusManagementPagerAdapter(@NonNull FragmentActivity fragmentActivity,String Id) {
        super(fragmentActivity);
        userId = Id;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 1:
                return new ShippingStatusDeliveryFragment(userId);
            case 2:
                return new CompletedStatusDeliveryFragment(userId);
            default:
                return new ConfirmStatusDeliveryFragment(userId);
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }

}
