package com.example.fooddeliveryapplication.Adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.fooddeliveryapplication.Fragments.CurrentProductFragment;
import com.example.fooddeliveryapplication.Fragments.HistoryFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                return new CurrentProductFragment();
            case 1:
                return new HistoryFragment();
            default:
                return new CurrentProductFragment();

        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        switch (position)
        {
            case 0:
                title = "Current";
                break;
            case 1:
                title = "History";
                break;

        }
        return title;
    }
}
