package com.example.fooddeliveryapplication.Fragments.Order;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fooddeliveryapplication.Activities.Order.OrderActivity;
import com.example.fooddeliveryapplication.Adapters.OrderAdapter.OrderAdapter;
import com.example.fooddeliveryapplication.Model.Bill;
import com.example.fooddeliveryapplication.databinding.FragmentCurrentOrderBinding;

import java.util.ArrayList;


public class CurrentOrderFragment extends Fragment {
    private FragmentCurrentOrderBinding binding;
    private ArrayList<Bill> dsBill;
    private String userId;

    public CurrentOrderFragment(ArrayList<Bill> ds,String id) {
        dsBill = ds;
        userId = id;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCurrentOrderBinding.inflate(inflater,container,false);

        OrderAdapter adapter=new OrderAdapter(getContext(), dsBill, OrderActivity.CURRENT_ORDER, userId);
        binding.ryc.setAdapter(adapter);
        binding.ryc.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL,false));

        return binding.getRoot();
    }
}