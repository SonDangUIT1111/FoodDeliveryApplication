package com.example.fooddeliveryapplication.Fragments.Home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.fooddeliveryapplication.Adapters.Home.FoodDrinkFrgAdapter;
import com.example.fooddeliveryapplication.Model.Product;
import com.example.fooddeliveryapplication.databinding.FragmentDrinkHomeFrgBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;


public class FoodHomeFrg extends Fragment {
    FragmentDrinkHomeFrgBinding binding;
    private ArrayList<Product>dsFood;
    private final DatabaseReference productsReference= FirebaseDatabase.getInstance().getReference("Products");
    private FoodDrinkFrgAdapter adapter;
    private String userId;

    public FoodHomeFrg(String id) {
        userId = id;
    }

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
        adapter=new FoodDrinkFrgAdapter(dsFood, userId,getContext());
        binding.rycDrinkHome.setAdapter(adapter);
        binding.rycDrinkHome.setHasFixedSize(true);
    }


    private void initData() {
        dsFood=new ArrayList<>();
        productsReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot item:snapshot.getChildren()) {
                    Product tmp=item.getValue(Product.class);
                    if (tmp.getProductType().equalsIgnoreCase("food") && !tmp.getPublisherId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
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