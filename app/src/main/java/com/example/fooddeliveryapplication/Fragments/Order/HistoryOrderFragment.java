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
import com.example.fooddeliveryapplication.Bill;
import com.example.fooddeliveryapplication.databinding.FragmentHistoryOrderBinding;

import java.util.ArrayList;


public class HistoryOrderFragment extends Fragment {
    FragmentHistoryOrderBinding binding;
    ArrayList<Bill> dsBill;

    public HistoryOrderFragment(ArrayList<Bill> ds) {
        dsBill=ds;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding=FragmentHistoryOrderBinding.inflate(inflater,container,false);
        OrderAdapter adapter=new OrderAdapter(getContext(),dsBill, OrderActivity.HISTORY_ORDER);
        binding.ryc.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL,false));
        binding.ryc.setAdapter(adapter);
        return binding.getRoot();
    }
}