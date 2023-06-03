package com.example.fooddeliveryapplication.Adapters.OrderAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;


import com.example.fooddeliveryapplication.Fragments.Order.CurrentOrderFragment;
import com.example.fooddeliveryapplication.Fragments.Order.HistoryOrderFragment;
import com.example.fooddeliveryapplication.Model.Bill;

import java.util.ArrayList;

public class OrderViewPaperAdapter extends FragmentStateAdapter {

    ArrayList<Bill> dsCurrentOrder=new ArrayList<>();
    ArrayList <Bill> dsHistoryOrder=new ArrayList<>();
    String userId;

    public OrderViewPaperAdapter(@NonNull FragmentActivity fragmentActivity, ArrayList<Bill> dsCurrentOrder, ArrayList <Bill> dsHistoryOrder,String id) {
        super(fragmentActivity);
        this.dsCurrentOrder=dsCurrentOrder;
        this.dsHistoryOrder=dsHistoryOrder;
        this.userId = id;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 1:
                return new HistoryOrderFragment(dsHistoryOrder,userId);
            default:
                return new CurrentOrderFragment(dsCurrentOrder,userId);
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
