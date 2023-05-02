package com.example.fooddeliveryapplication.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.fooddeliveryapplication.HomeAdapter.FoodDrinkFrgAdapter;
import com.example.fooddeliveryapplication.Model.Food;
import com.example.fooddeliveryapplication.Model.Products;
import com.example.fooddeliveryapplication.R;
import com.example.fooddeliveryapplication.databinding.FragmentDrinkHomeFrgBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;


public class FoodHomeFrg extends Fragment {
    FragmentDrinkHomeFrgBinding binding;
    private ArrayList<Products>dsFood;
    private final DatabaseReference productsReference= FirebaseDatabase.getInstance().getReference("Products");
    private FoodDrinkFrgAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding=FragmentDrinkHomeFrgBinding.inflate(inflater,container,false);
        View view=binding.getRoot();
        initData();
        initUI();
        return view;
    }

    private void initUI() {
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity().getApplicationContext(),LinearLayoutManager.HORIZONTAL,false);
        binding.rycDrinkHome.setLayoutManager(linearLayoutManager);
        adapter=new FoodDrinkFrgAdapter(dsFood);
        binding.rycDrinkHome.setAdapter(adapter);
        binding.rycDrinkHome.setHasFixedSize(true);
    }


    private void initData() {
        dsFood=new ArrayList<>();

        productsReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot item:snapshot.getChildren()) {
                    Products tmp=item.getValue(Products.class);
                    if (tmp.getProductType().equals("food")) {
                        dsFood.add(tmp);
                    }
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding=null;
    }
}