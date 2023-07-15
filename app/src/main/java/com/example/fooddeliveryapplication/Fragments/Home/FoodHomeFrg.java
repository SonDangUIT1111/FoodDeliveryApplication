package com.example.fooddeliveryapplication.Fragments.Home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.fooddeliveryapplication.Activities.Home.FindActivity;
import com.example.fooddeliveryapplication.Adapters.Home.FoodDrinkFrgAdapter;
import com.example.fooddeliveryapplication.Model.Product;
import com.example.fooddeliveryapplication.databinding.FragmentDrinkHomeFrgBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;


public class FoodHomeFrg extends Fragment {
    private FragmentDrinkHomeFrgBinding binding;
    private ArrayList<Product> dsFood;
    private FoodDrinkFrgAdapter adapter;
    private String userId;

    public FoodHomeFrg(String id) {
        userId = id;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDrinkHomeFrgBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        initData();
        initUI();
        return view;
    }

    private void initUI() {
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity().getApplicationContext(),LinearLayoutManager.HORIZONTAL,false);
        binding.rycDrinkHome.setLayoutManager(linearLayoutManager);
        adapter=new FoodDrinkFrgAdapter(dsFood, userId,getContext());
        binding.rycDrinkHome.setAdapter(adapter);
        binding.rycDrinkHome.setHasFixedSize(true);
        binding.txtSeemoreDrink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), FindActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });
    }


    private void initData() {
        dsFood = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("Products").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Product product = ds.getValue(Product.class);
                    if (product != null && !product.getState().equals("deleted") && product.getProductType().equalsIgnoreCase("Food") && !product.getPublisherId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        dsFood.add(product);
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}