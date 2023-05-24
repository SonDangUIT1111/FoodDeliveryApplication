package com.example.fooddeliveryapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.fooddeliveryapplication.Adapters.CartProductAdapter;
import com.example.fooddeliveryapplication.Models.Cart;
import com.example.fooddeliveryapplication.Models.CartInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ktx.Firebase;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {
    private FirebaseUser firebaseUser;

    private RecyclerView recyclerViewCartProducts;
    private CartProductAdapter cartProductAdapter;
    private List<CartInfo> cartInfoList;

    private String cartId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        cartId = getIntent().getStringExtra("cartId");

        initToolbar();

        recyclerViewCartProducts = findViewById(R.id.recycler_view_cart_product);
        recyclerViewCartProducts.setHasFixedSize(true);
        recyclerViewCartProducts.setLayoutManager(new LinearLayoutManager(this));
        cartInfoList = new ArrayList<>();
        cartProductAdapter = new CartProductAdapter(this, cartInfoList, cartId);
        recyclerViewCartProducts.setAdapter(cartProductAdapter);

        getCartProducts();
    }

    private void getCartProducts() {
        FirebaseDatabase.getInstance().getReference().child("CartInfos").child(cartId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cartInfoList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    CartInfo cartInfo = ds.getValue(CartInfo.class);
                    cartInfoList.add(cartInfo);
                }
                cartProductAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Cart");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}