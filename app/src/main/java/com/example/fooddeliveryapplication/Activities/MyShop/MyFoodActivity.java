package com.example.fooddeliveryapplication.Activities.MyShop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.fooddeliveryapplication.Adapters.MyShopAdapter.MyShopAdapter;
import com.example.fooddeliveryapplication.Dialog.LoadingDialog;
import com.example.fooddeliveryapplication.Model.Product;
import com.example.fooddeliveryapplication.databinding.ActivityMyFoodBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyFoodActivity extends AppCompatActivity {
    ActivityMyFoodBinding binding;
    FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    private ArrayList<Product> ds=new ArrayList<>();
    private MyShopAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMyFoodBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        adapter=new MyShopAdapter(ds,MyFoodActivity.this);
        binding.recycleView.setHasFixedSize(true);
        binding.recycleView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL,false));
        binding.recycleView.setAdapter(adapter);
        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        binding.flpAddFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MyFoodActivity.this,AddFoodActivity.class);
                startActivity(intent);
            }
        });
//        firebaseDatabase.getReference("Products").addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//                Products products=snapshot.getValue(Products.class);
//                ds.add(products);
//                adapter.notifyItemInserted(ds.size()-1);
//            }
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//                Products updatedProduct = snapshot.getValue(Products.class);
//                int index = -1;
//                for (int i = 0; i < ds.size(); i++) {
//                    if (ds.get(i).getProductId().equals(updatedProduct.getProductId())) {
//                        index = i;
//                        break;
//                    }
//                }
//                if (index != -1) {
//                    ds.set(index, updatedProduct);
//                    adapter.notifyItemChanged(index);
//                }
//                LayoutFoodItemBinding foodItemBinding=LayoutFoodItemBinding.inflate(getLayoutInflater());
//
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
//                Products products=snapshot.getValue(Products.class);
//                ds.remove(products);
//                adapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        LoadingDialog dialog=new LoadingDialog(this);
        dialog.show();
        firebaseDatabase.getReference("Products").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ds.clear();
                for (DataSnapshot item:snapshot.getChildren()) {
                    Product tmp=item.getValue(Product.class);
                    ds.add(tmp);
                }
                dialog.dismiss();
                adapter.notifyDataSetChanged();
             }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding=null;
    }
}