package com.example.fooddeliveryapplication.Adapters.Cart_PlaceOrder;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fooddeliveryapplication.Activities.Home.HomeActivity;
import com.example.fooddeliveryapplication.Adapters.Cart.OrderProductAdapter;
import com.example.fooddeliveryapplication.GlobalConfig;
import com.example.fooddeliveryapplication.Model.Address;
import com.example.fooddeliveryapplication.Model.Bill;
import com.example.fooddeliveryapplication.Model.Cart;
import com.example.fooddeliveryapplication.Model.CartInfo;
import com.example.fooddeliveryapplication.Model.Product;
import com.example.fooddeliveryapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ProceedOrderActivity extends AppCompatActivity {
    private TextView receiverName;
    private TextView detailAddress;
    private TextView receiverPhoneNumber;
    private TextView change;
    private RecyclerView recyclerViewOrderProducts;
    private OrderProductAdapter orderProductAdapter;
    private ArrayList<CartInfo> cartInfoList;
    private Button complete;
    private TextView totalPrice;
    String totalPriceDisplay;
    String userId;
    private ActivityResultLauncher<Intent> changeAddressLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proceed_order);

        initToolbar();
        initChangeAddressActivity();

        receiverName = findViewById(R.id.receiver_name);
        detailAddress = findViewById(R.id.detail_address);
        receiverPhoneNumber = findViewById(R.id.receiver_phone_number);
        change = findViewById(R.id.change);
        recyclerViewOrderProducts = findViewById(R.id.recycler_view_order_product);
        recyclerViewOrderProducts.setHasFixedSize(true);
        recyclerViewOrderProducts.setLayoutManager(new LinearLayoutManager(this));
        cartInfoList = (ArrayList<CartInfo>) getIntent().getSerializableExtra("buyProducts");
        orderProductAdapter = new OrderProductAdapter(this, cartInfoList);
        recyclerViewOrderProducts.setAdapter(orderProductAdapter);
        totalPriceDisplay = getIntent().getStringExtra("totalPrice");
        userId = getIntent().getStringExtra("userId");
        complete = findViewById(R.id.complete);
        totalPrice = findViewById(R.id.total_price);

        loadInfo();

        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (CartInfo cartInfo : cartInfoList) {
                    // Add bills
                    FirebaseDatabase.getInstance().getReference().child("Products").child(cartInfo.getProductId()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Product product = snapshot.getValue(Product.class);
                            FirebaseDatabase.getInstance().getReference().child("Bills").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot1) {
                                    boolean isHavingBill = false;
                                    for (DataSnapshot ds : snapshot1.getChildren()) {
                                        Bill bill = ds.getValue(Bill.class);

                                        if (bill.getSenderId().equals(product.getPublisherId())) {
                                            isHavingBill = true;

                                            // Add to BillInfos
                                            String billInfoId = FirebaseDatabase.getInstance().getReference().push().getKey();
                                            HashMap<String, Object> map1 = new HashMap<>();
                                            map1.put("amount", cartInfo.getAmount());
                                            map1.put("billInfoId", billInfoId);
                                            map1.put("productId", cartInfo.getProductId());
                                            FirebaseDatabase.getInstance().getReference().child("BillInfos").child(bill.getBillId()).child(billInfoId).setValue(map1);

                                            // Update Carts and CartInfos
                                            FirebaseDatabase.getInstance().getReference().child("Carts").addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    for (DataSnapshot ds : snapshot.getChildren()) {
                                                        Cart cart = ds.getValue(Cart.class);
                                                        if (cart.getUserId().equals(userId)) {
                                                            FirebaseDatabase.getInstance().getReference().child("CartInfos").child(cart.getCartId()).child(cartInfo.getCartInfoId()).removeValue();
                                                            FirebaseDatabase.getInstance().getReference().child("Products").child(cartInfo.getProductId()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                    Product product = snapshot.getValue(Product.class);
                                                                    FirebaseDatabase.getInstance().getReference().child("Carts").child(cart.getCartId()).child("totalAmount").setValue(cart.getTotalAmount() - cartInfo.getAmount());
                                                                    FirebaseDatabase.getInstance().getReference().child("Carts").child(cart.getCartId()).child("totalPrice").setValue(cart.getTotalPrice() - cartInfo.getAmount() * product.getProductPrice());
                                                                }

                                                                @Override
                                                                public void onCancelled(@NonNull DatabaseError error) {

                                                                }
                                                            });

                                                            FirebaseDatabase.getInstance().getReference().child("CartInfos").child(cart.getCartId()).child(cartInfo.getCartInfoId()).removeValue();
                                                        }
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });

                                            Toast.makeText(ProceedOrderActivity.this, "Order completed!", Toast.LENGTH_SHORT).show();
                                            break;
                                        }
                                    }

                                    if (!isHavingBill) {
                                        // Add to Bills
                                        String billId = FirebaseDatabase.getInstance().getReference().push().getKey();
                                        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                                        Date date = new Date();
                                        Product product = snapshot.getValue(Product.class);

                                        HashMap<String, Object> map = new HashMap<>();
                                        map.put("addressId", GlobalConfig.choseAddressId);
                                        map.put("billId", billId);
                                        map.put("orderDate", formatter.format(date));
                                        map.put("orderStatus", "Confirm");
                                        map.put("recipientId", userId);
                                        map.put("senderId", product.getPublisherId());
                                        map.put("checkAllComment", false);
                                        map.put("totalPrice", convertMoneyToValue(totalPriceDisplay));
                                        FirebaseDatabase.getInstance().getReference().child("Bills").child(billId).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    // Add to BillInfos
                                                    String billInfoId = FirebaseDatabase.getInstance().getReference().push().getKey();
                                                    HashMap<String, Object> map1 = new HashMap<>();
                                                    map1.put("amount", cartInfo.getAmount());
                                                    map1.put("billInfoId", billInfoId);
                                                    map1.put("productId", cartInfo.getProductId());
                                                    FirebaseDatabase.getInstance().getReference().child("BillInfos").child(billId).child(billInfoId).setValue(map1);

                                                    // Update Carts and CartInfos
                                                    FirebaseDatabase.getInstance().getReference().child("Carts").addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            for (DataSnapshot ds : snapshot.getChildren()) {
                                                                Cart cart = ds.getValue(Cart.class);
                                                                if (cart.getUserId().equals(userId)) {
                                                                    FirebaseDatabase.getInstance().getReference().child("CartInfos").child(cart.getCartId()).child(cartInfo.getCartInfoId()).removeValue();
                                                                    FirebaseDatabase.getInstance().getReference().child("Products").child(cartInfo.getProductId()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                        @Override
                                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                            Product product = snapshot.getValue(Product.class);
                                                                            FirebaseDatabase.getInstance().getReference().child("Carts").child(cart.getCartId()).child("totalAmount").setValue(cart.getTotalAmount() - cartInfo.getAmount());
                                                                            FirebaseDatabase.getInstance().getReference().child("Carts").child(cart.getCartId()).child("totalPrice").setValue(cart.getTotalPrice() - cartInfo.getAmount() * product.getProductPrice());
                                                                        }

                                                                        @Override
                                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                                        }
                                                                    });

                                                                    FirebaseDatabase.getInstance().getReference().child("CartInfos").child(cart.getCartId()).child(cartInfo.getCartInfoId()).removeValue();
                                                                }
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                        }
                                                    });

                                                    Toast.makeText(ProceedOrderActivity.this, "Order completed!", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
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
                cartInfoList.clear();
                Intent intent = new Intent(ProceedOrderActivity.this, HomeActivity.class);
                setResult(RESULT_OK, intent);
                finish();

            }
        });

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProceedOrderActivity.this, ChangeAddressActivity.class);
                intent.putExtra("userId",userId);
                changeAddressLauncher.launch(intent);
                //startActivity(new Intent(ProceedOrderActivity.this, ChangeAddressActivity.class));
            }
        });
    }

    private void initChangeAddressActivity() {
        // Init launcher
        changeAddressLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                loadAddressData();
            }
        });
    }

    private void loadAddressData() {
        FirebaseDatabase.getInstance().getReference().child("Address").child(userId).child(GlobalConfig.choseAddressId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Address address = snapshot.getValue(Address.class);
                receiverName.setText(address.getReceiverName());
                detailAddress.setText(address.getDetailAddress());
                receiverPhoneNumber.setText(address.getReceiverPhoneNumber());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Proceed order");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    private void loadInfo() {
        // totalPrice
        totalPrice.setText(totalPriceDisplay);

        // Address
        FirebaseDatabase.getInstance().getReference().child("Address").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Address address = ds.getValue(Address.class);

                    if (address.getState().equals("default")) {
                        GlobalConfig.choseAddressId = address.getAddressId();
                        receiverName.setText(address.getReceiverName());
                        detailAddress.setText(address.getDetailAddress());
                        receiverPhoneNumber.setText(address.getReceiverPhoneNumber());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private long convertMoneyToValue(String money) {
        String output = "";
        for (int i = 0; i < money.length(); i++) {
            if (money.charAt(i) != ',' && money.charAt(i) != 'đ') {
                output += money.charAt(i);
            }
        }
        return Long.parseLong(output);
    }
}