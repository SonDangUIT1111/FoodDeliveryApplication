package com.example.fooddeliveryapplication.Fragments.Home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.fooddeliveryapplication.Adapters.HomeAdapter.FoodDrinkFrgAdapter;
import com.example.fooddeliveryapplication.Model.Product;
import com.example.fooddeliveryapplication.databinding.FragmentDrinkHomeFrgBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;


public class DrinkHomeFrg extends Fragment {
    private ArrayList<Product>dsDrink;
    private final DatabaseReference productsReference= FirebaseDatabase.getInstance().getReference("Products");
    FragmentDrinkHomeFrgBinding binding;
    private FoodDrinkFrgAdapter adapter;
    String userId;

    public DrinkHomeFrg(String id) {
        userId = id;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = FragmentDrinkHomeFrgBinding.inflate(inflater,container,false);


        View view=binding.getRoot();
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity().getApplicationContext(),LinearLayoutManager.HORIZONTAL,false);
        binding.rycDrinkHome.setLayoutManager(linearLayoutManager);
        binding.rycDrinkHome.setHasFixedSize(true);
        binding.txtSeemoreDrink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "See more", Toast.LENGTH_SHORT).show();
            }
        });
        initData();
        adapter=new FoodDrinkFrgAdapter(dsDrink,userId,getContext());
        binding.rycDrinkHome.setAdapter(adapter);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding=null;
    }
    private void initData() {
        dsDrink=new ArrayList<>();

        productsReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot item:snapshot.getChildren()) {
                    Product tmp=item.getValue(Product.class);
                    if (tmp.getProductType().equals("drink")) {
                        dsDrink.add(tmp);
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