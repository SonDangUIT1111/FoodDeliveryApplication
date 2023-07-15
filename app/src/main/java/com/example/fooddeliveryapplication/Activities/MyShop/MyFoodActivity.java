package com.example.fooddeliveryapplication.Activities.MyShop;

import android.content.Intent;
import android.graphics.Color;
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
    private ActivityMyFoodBinding binding;
    private ArrayList<Product> ds=new ArrayList<>();
    private MyShopAdapter adapter;
    private String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMyFoodBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getWindow().setStatusBarColor(Color.parseColor("#E8584D"));
        getWindow().setNavigationBarColor(Color.parseColor("#E8584D"));

        userId = getIntent().getStringExtra("userId");
        adapter=new MyShopAdapter(ds,MyFoodActivity.this, userId);
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
                intent.putExtra("userId",userId);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        LoadingDialog dialog=new LoadingDialog(this);
        dialog.show();
        FirebaseDatabase.getInstance().getReference("Products").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ds.clear();
                for (DataSnapshot item:snapshot.getChildren()) {
                    Product tmp=item.getValue(Product.class);
                    if (tmp != null && tmp.getPublisherId()!=null) {
                        if (tmp.getPublisherId().equals(userId) && !tmp.getState().equals("deleted")) {
                            ds.add(tmp);
                        }
                    }
                }
                dialog.dismiss();
                adapter.notifyDataSetChanged();
             }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}