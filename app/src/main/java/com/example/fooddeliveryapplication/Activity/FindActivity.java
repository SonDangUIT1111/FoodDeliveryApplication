package com.example.fooddeliveryapplication.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.fooddeliveryapplication.HomeAdapter.FindAdapter;
import com.example.fooddeliveryapplication.Model.Food;
import com.example.fooddeliveryapplication.Model.Products;
import com.example.fooddeliveryapplication.R;
import com.example.fooddeliveryapplication.databinding.ActivityFindBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;

public class FindActivity extends AppCompatActivity {
    ActivityFindBinding binding;
    private final DatabaseReference productsReference= FirebaseDatabase.getInstance().getReference("Products");
    private ArrayList<Products> dsAll=new ArrayList<>();
    private FindAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =ActivityFindBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Hàm tạo danh sách thức ăn


        initData();
        adapter=new FindAdapter(dsAll);
        binding.recycleFoodFinded.setAdapter(adapter);

        initUI();

        //

    }

    private void initUI() {


        //Thêm adapter và layout của adapter
        binding.searhView.setIconifiedByDefault(false);
        StaggeredGridLayoutManager layoutManager=new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        binding.recycleFoodFinded.setLayoutManager(layoutManager);
        binding.recycleFoodFinded.setHasFixedSize(true);
        //set Sự kiện cho nút backHome
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //set Sự kiện của ô nhập
        binding.searhView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                adapter.getFilter().filter(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.getFilter().filter(s);
                return false;
            }
        });
    }

    private void initData() {
        dsAll=new ArrayList<>();

        productsReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot item:snapshot.getChildren()) {
                    Products tmp=item.getValue(Products.class);
                        dsAll.add(tmp);

                }
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