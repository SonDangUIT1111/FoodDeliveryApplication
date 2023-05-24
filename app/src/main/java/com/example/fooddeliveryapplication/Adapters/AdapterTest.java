package com.example.fooddeliveryapplication.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.fooddeliveryapplication.Fragments.NotificationFragment;

public class AdapterTest extends FragmentStateAdapter {
    String userId;
    public AdapterTest(@NonNull FragmentActivity fragmentActivity, String Id) {
        super(fragmentActivity);
        userId = Id;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return new NotificationFragment(userId);
    }

    @Override
    public int getItemCount() {
        return 1;
    }
}
