package com.example.fooddeliveryapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.fooddeliveryapplication.Adapters.CartProductAdapter;
import com.example.fooddeliveryapplication.Interfaces.IAdapterItemListener;
import com.example.fooddeliveryapplication.Models.Cart;
import com.example.fooddeliveryapplication.Models.CartInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {
    private FirebaseUser firebaseUser;

    private RecyclerView recyclerViewCartProducts;
    private CartProductAdapter cartProductAdapter;
    private List<CartInfo> cartInfoList;
    private TextView totalPrice;
    private TextView selected;
    private CheckBox checkAll;
    private Button proceedOrder;

    private String cartId;
    private boolean isCheckAll = false;
    private ArrayList<CartInfo> buyProducts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        initToolbar();

        recyclerViewCartProducts = findViewById(R.id.recycler_view_cart_product);
        recyclerViewCartProducts.setHasFixedSize(true);
        recyclerViewCartProducts.setLayoutManager(new LinearLayoutManager(this));
        cartInfoList = new ArrayList<>();
        totalPrice = findViewById(R.id.total_price);
        selected = findViewById(R.id.selected);
        checkAll = findViewById(R.id.check_all);
        proceedOrder = findViewById(R.id.proceed_order);

        getCartProducts();

        checkAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                cartProductAdapter.setCheckAll(isChecked);
                cartProductAdapter.notifyDataSetChanged();
            }
        });

        proceedOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CartActivity.this, ProceedOrderActivity.class);
                intent.putExtra("buyProduucts", buyProducts);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (cartProductAdapter != null) {
            cartProductAdapter.saveStates(outState);
        }
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (cartProductAdapter != null) {
            cartProductAdapter.restoreStates(savedInstanceState);
        }
    }

    private void getCartProducts() {
        FirebaseDatabase.getInstance().getReference().child("Carts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Cart cart = ds.getValue(Cart.class);
                    if (cart.getUserId().equals(firebaseUser.getUid())) {
                        cartId = cart.getCartId();
                    }
                }

                cartProductAdapter = new CartProductAdapter(CartActivity.this, cartInfoList, cartId, isCheckAll);
                cartProductAdapter.setAdapterItemListener(new IAdapterItemListener() {
                    @Override
                    public void onCheckedItemCountChanged(int count, long price, ArrayList<CartInfo> selectedItems) {
                        selected.setText("Selected: " + String.valueOf(count));
                        totalPrice.setText("Total price: " + convertToMoney(price) + "đ");
                        buyProducts = selectedItems;

                        if (count > 0) {
                            proceedOrder.setEnabled(true);
                        }
                    }
                });
                recyclerViewCartProducts.setAdapter(cartProductAdapter);

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

    private String convertToMoney(long price) {
        String temp = String.valueOf(price);
        String output = "";
        int count = 3;
        for (int i = temp.length() - 1; i >= 0; i--) {
            count--;
            if (count == 0) {
                count = 3;
                output = "," + temp.charAt(i) + output;
            }
            else {
                output = temp.charAt(i) + output;
            }
        }

        if (output.charAt(0) == ',')
            return output.substring(1);

        return output;
    }
}