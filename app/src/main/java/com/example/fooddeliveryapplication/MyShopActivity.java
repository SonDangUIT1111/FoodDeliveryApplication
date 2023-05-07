package com.example.fooddeliveryapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.fooddeliveryapplication.Activities.AddProductActivity;
import com.example.fooddeliveryapplication.Model.Product;
import com.example.fooddeliveryapplication.Adapters.ProductAdapter;

import java.util.ArrayList;
import java.util.List;

public class MyShopActivity extends AppCompatActivity {

    private RecyclerView rcvProduct;
    private ProductAdapter productAdapter;
    private Button btnAddProduct;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_shop);
        getSupportActionBar().hide();
        rcvProduct = findViewById(R.id.rcv_product);
        productAdapter = new ProductAdapter();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvProduct.setLayoutManager(linearLayoutManager);
        productAdapter.setData(getListProduct());
        rcvProduct.setAdapter(productAdapter);


        ImageView back = findViewById(R.id.imgBack);

        Button btnAdd = findViewById(R.id.btnAddProduct);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyShopActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyShopActivity.this, AddProductActivity.class);
                startActivity(intent);
            }
        });
    }

    private List<Product> getListProduct(){
        List<Product> list = new ArrayList<>();

        list.add(new Product(R.drawable.pop_1, "Product1", "2000"));
        list.add(new Product(R.drawable.pop_1, "Product2", "3000"));
        list.add(new Product(R.drawable.pop_1, "Product3", "4000"));

        list.add(new Product(R.drawable.pop_1, "Product1", "2000"));
        list.add(new Product(R.drawable.pop_1, "Product2", "3000"));
        list.add(new Product(R.drawable.pop_1, "Product3", "4000"));
        list.add(new Product(R.drawable.pop_1, "Product3", "4000"));
        list.add(new Product(R.drawable.pop_1, "Product3", "4000"));
        list.add(new Product(R.drawable.pop_1, "Product3", "4000"));
        list.add(new Product(R.drawable.pop_1, "Product3", "4000"));
        list.add(new Product(R.drawable.pop_1, "Product3", "4000"));
        list.add(new Product(R.drawable.pop_1, "Product3", "4000"));
        list.add(new Product(R.drawable.pop_1, "Product3", "4000"));
        list.add(new Product(R.drawable.pop_1, "Product3", "4000"));
        list.add(new Product(R.drawable.pop_1, "Product3", "4000"));
        list.add(new Product(R.drawable.pop_1, "Product3", "4000"));
        list.add(new Product(R.drawable.pop_1, "Product3", "4000"));
        list.add(new Product(R.drawable.pop_1, "Product3", "4000"));
        list.add(new Product(R.drawable.pop_1, "Product3", "4000"));
        list.add(new Product(R.drawable.pop_1, "Product3", "4000"));
        list.add(new Product(R.drawable.pop_1, "Product3", "4000"));
        list.add(new Product(R.drawable.pop_1, "Product3", "4000"));
        list.add(new Product(R.drawable.pop_1, "Product3", "4000"));
        list.add(new Product(R.drawable.pop_1, "Product3", "4000"));
        list.add(new Product(R.drawable.pop_1, "Product3", "4000"));
        list.add(new Product(R.drawable.pop_1, "Product3", "4000"));
        list.add(new Product(R.drawable.pop_1, "Product3", "4000"));
        list.add(new Product(R.drawable.pop_1, "Product3", "4000"));
        list.add(new Product(R.drawable.pop_1, "Product3", "4000"));
        list.add(new Product(R.drawable.pop_1, "Product3", "4000"));
        list.add(new Product(R.drawable.pop_1, "Product3", "4000"));
        list.add(new Product(R.drawable.pop_1, "Product3", "4000"));
        list.add(new Product(R.drawable.pop_1, "Product3", "4000"));
        list.add(new Product(R.drawable.pop_1, "Product3", "4000"));
        list.add(new Product(R.drawable.pop_1, "Product3", "4000"));
        list.add(new Product(R.drawable.pop_1, "Product3", "4000"));
        list.add(new Product(R.drawable.pop_1, "Product3", "4000"));
        list.add(new Product(R.drawable.pop_1, "Product3", "4000"));
        list.add(new Product(R.drawable.pop_1, "Product3", "4000"));
        list.add(new Product(R.drawable.pop_1, "Product3", "4000"));
        list.add(new Product(R.drawable.pop_1, "Product3", "4000"));
        list.add(new Product(R.drawable.pop_1, "Product3", "4000"));
        list.add(new Product(R.drawable.pop_1, "Product3", "4000"));
        list.add(new Product(R.drawable.pop_1, "Product3", "4000"));
        list.add(new Product(R.drawable.pop_1, "Product3", "4000"));
        list.add(new Product(R.drawable.pop_1, "Product3", "4000"));
        list.add(new Product(R.drawable.pop_1, "Product3", "4000"));
        list.add(new Product(R.drawable.pop_1, "Product3", "4000"));
        list.add(new Product(R.drawable.pop_1, "Product3", "4000"));
        list.add(new Product(R.drawable.pop_1, "Product3", "4000"));
        list.add(new Product(R.drawable.pop_1, "Product3", "4000"));
        list.add(new Product(R.drawable.pop_1, "Product3", "4000"));
        list.add(new Product(R.drawable.pop_1, "Product3", "4000"));
        list.add(new Product(R.drawable.pop_1, "Product3", "4000"));
        list.add(new Product(R.drawable.pop_1, "Product3", "4000"));
        list.add(new Product(R.drawable.pop_1, "Product3", "4000"));
        list.add(new Product(R.drawable.pop_1, "Product3", "4000"));
        list.add(new Product(R.drawable.pop_1, "Product3", "4000"));
        list.add(new Product(R.drawable.pop_1, "Product3", "4000"));
        list.add(new Product(R.drawable.pop_1, "Product3", "4000"));
        list.add(new Product(R.drawable.pop_1, "Product3", "4000"));
        list.add(new Product(R.drawable.pop_1, "Product3", "4000"));
        list.add(new Product(R.drawable.pop_1, "Product3", "4000"));
        list.add(new Product(R.drawable.pop_1, "Product3", "4000"));




        return list;
    }
}