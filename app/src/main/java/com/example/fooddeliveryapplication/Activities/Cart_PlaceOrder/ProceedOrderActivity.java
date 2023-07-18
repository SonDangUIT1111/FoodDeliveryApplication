package com.example.fooddeliveryapplication.Activities.Cart_PlaceOrder;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.fooddeliveryapplication.Activities.Home.HomeActivity;
import com.example.fooddeliveryapplication.Adapters.Cart.OrderProductAdapter;
import com.example.fooddeliveryapplication.CustomMessageBox.FailToast;
import com.example.fooddeliveryapplication.CustomMessageBox.SuccessfulToast;
import com.example.fooddeliveryapplication.GlobalConfig;
import com.example.fooddeliveryapplication.Helpers.FirebaseNotificationHelper;
import com.example.fooddeliveryapplication.Model.Address;
import com.example.fooddeliveryapplication.Model.Bill;
import com.example.fooddeliveryapplication.Model.Cart;
import com.example.fooddeliveryapplication.Model.CartInfo;
import com.example.fooddeliveryapplication.Model.Notification;
import com.example.fooddeliveryapplication.Model.Product;
import com.example.fooddeliveryapplication.databinding.ActivityProceedOrderBinding;
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
import java.util.Set;

public class ProceedOrderActivity extends AppCompatActivity {
    private ActivityProceedOrderBinding binding;
    private OrderProductAdapter orderProductAdapter;
    private ArrayList<CartInfo> cartInfoList;
    private String totalPriceDisplay;
    private String userId;
    private ActivityResultLauncher<Intent> changeAddressLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProceedOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initToolbar();
        initChangeAddressActivity();

        binding.recyclerViewOrderProduct.setHasFixedSize(true);
        binding.recyclerViewOrderProduct.setLayoutManager(new LinearLayoutManager(this));
        cartInfoList = (ArrayList<CartInfo>) getIntent().getSerializableExtra("buyProducts");
        orderProductAdapter = new OrderProductAdapter(this, cartInfoList);
        binding.recyclerViewOrderProduct.setAdapter(orderProductAdapter);
        totalPriceDisplay = getIntent().getStringExtra("totalPrice");
        userId = getIntent().getStringExtra("userId");

        loadInfo();

        binding.complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateDate()) {
                    // Filter new map to add to bills
                    HashMap<String, CartInfo> cartInfoMap = new HashMap<>();
                    for (CartInfo cartInfo : cartInfoList) {
                        cartInfoMap.put(cartInfo.getProductId(), cartInfo);
                    }

                    Set<String> cartInfoKeySet = cartInfoMap.keySet();
                    HashMap<String, List<CartInfo>> filterCartInfoMap = new HashMap<>();
                    HashMap<String, Long> filterCartInfoPriceMap = new HashMap<>();
                    HashMap<String, String> filterCartInfoImageUrlMap = new HashMap<>();
                    FirebaseDatabase.getInstance().getReference().child("Products").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot ds : snapshot.getChildren()) {
                                Product product = ds.getValue(Product.class);
                                for (String productId : cartInfoKeySet) {
                                    if (product.getProductId().equals(productId)) {
                                        if (filterCartInfoMap.containsKey(product.getPublisherId())) {
                                            // filterCartInfoMap
                                            filterCartInfoMap.get(product.getPublisherId()).add(cartInfoMap.get(productId));

                                            // filterCartInfoPriceMap
                                            long totalPrice = filterCartInfoPriceMap.get(product.getPublisherId());
                                            totalPrice += (long) product.getProductPrice() * cartInfoMap.get(productId).getAmount();
                                            filterCartInfoPriceMap.put(product.getPublisherId(), totalPrice);
                                        }
                                        else {
                                            // filterCartInfoMap
                                            List<CartInfo> tempList = new ArrayList<>();
                                            tempList.add(cartInfoMap.get(productId));
                                            filterCartInfoMap.put(product.getPublisherId(), tempList);

                                            // filterCartInfoPriceMap
                                            long currentTotalPrice = (long) product.getProductPrice() * cartInfoMap.get(productId).getAmount();
                                            filterCartInfoPriceMap.put(product.getPublisherId(), currentTotalPrice);

                                            // filterCartInfoImageUrlMap
                                            String currentImageUrl = product.getProductImage1();
                                            filterCartInfoImageUrlMap.put(product.getPublisherId(), currentImageUrl);
                                        }
                                    }
                                }
                            }

                            // Loop through all cartInfo
                            Set<String> filterCartInfoKeySet = filterCartInfoMap.keySet();
                            for (String senderId : filterCartInfoKeySet) {
                                // Add to Bills
                                String billId = FirebaseDatabase.getInstance().getReference().push().getKey();
                                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                                Date date = new Date();
                                Bill bill = new Bill(GlobalConfig.choseAddressId, billId, formatter.format(date), "Confirm", false, userId, senderId, filterCartInfoPriceMap.get(senderId), filterCartInfoImageUrlMap.get(senderId));
                                FirebaseDatabase.getInstance().getReference().child("Bills").child(billId).setValue(bill).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            List<CartInfo> cartInfoList1 = filterCartInfoMap.get(senderId);
                                            for (CartInfo cartInfo : cartInfoList1) {
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
                                                            }
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });
                                            }
                                        }
                                    }
                                });
                                pushNotificationCartCompleteForSeller(bill);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    FirebaseDatabase.getInstance().getReference().child("Products").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot1) {
                            FirebaseDatabase.getInstance().getReference().child("Carts").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot2) {
                                    int totalAmount = 0;
                                    long totalPrice = 0;
                                    for (DataSnapshot ds : snapshot1.getChildren()) {
                                        Product product = ds.getValue(Product.class);
                                        for (CartInfo cartInfo : cartInfoList) {
                                            if (cartInfo.getProductId().equals(product.getProductId())) {
                                                totalAmount += cartInfo.getAmount();
                                                totalPrice += cartInfo.getAmount() * product.getProductPrice();
                                            }
                                        }
                                    }

                                    for (DataSnapshot ds : snapshot2.getChildren()) {
                                        Cart cart = ds.getValue(Cart.class);
                                        if (cart.getUserId().equals(userId)) {
                                            FirebaseDatabase.getInstance().getReference().child("Carts").child(cart.getCartId()).child("totalAmount").setValue(cart.getTotalAmount() - totalAmount);
                                            FirebaseDatabase.getInstance().getReference().child("Carts").child(cart.getCartId()).child("totalPrice").setValue(cart.getTotalPrice() - totalPrice);
                                        }
                                    }
                                    new SuccessfulToast(ProceedOrderActivity.this, "Order created successfully!").showToast();

                                    cartInfoList.clear();
                                    Intent intent = new Intent(ProceedOrderActivity.this, HomeActivity.class);
                                    setResult(RESULT_OK, intent);
                                    finish();
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

            }
        });

        binding.change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProceedOrderActivity.this, ChangeAddressActivity.class);
                intent.putExtra("userId", userId);
                changeAddressLauncher.launch(intent);
            }
        });
    }

    private boolean validateDate() {
        if (GlobalConfig.choseAddressId == null) {
            new FailToast(this, "You must choose delivery address!").showToast();
            return false;
        }

        return true;
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
        if (GlobalConfig.choseAddressId != null) {
            FirebaseDatabase.getInstance().getReference().child("Address").child(userId).child(GlobalConfig.choseAddressId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Address address = snapshot.getValue(Address.class);
                    binding.receiverName.setText(address.getReceiverName());
                    binding.detailAddress.setText(address.getDetailAddress());
                    binding.receiverPhoneNumber.setText(address.getReceiverPhoneNumber());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    private void initToolbar() {
        getWindow().setStatusBarColor(Color.parseColor("#E8584D"));
        getWindow().setNavigationBarColor(Color.parseColor("#E8584D"));
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle("Proceed order");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
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
        binding.totalPrice.setText(totalPriceDisplay);

        // Address
        FirebaseDatabase.getInstance().getReference().child("Address").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Address address = ds.getValue(Address.class);

                    if (address.getState().equals("default")) {
                        GlobalConfig.choseAddressId = address.getAddressId();
                        binding.receiverName.setText(address.getReceiverName());
                        binding.detailAddress.setText(address.getDetailAddress());
                        binding.receiverPhoneNumber.setText(address.getReceiverPhoneNumber());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void pushNotificationCartCompleteForSeller(Bill bill) {
        String title2 = "New order";
        String content2 = "Hurry up! There is a new order. Go to Delivery Manage for customer serving!";
        Notification notification2 = FirebaseNotificationHelper.createNotification(title2, content2, bill.getImageUrl(), "None", "None", bill.getBillId(), null);
        new FirebaseNotificationHelper(ProceedOrderActivity.this).addNotification(bill.getSenderId(), notification2, new FirebaseNotificationHelper.DataStatus() {
            @Override
            public void DataIsLoaded(List<Notification> notificationList,List<Notification> notificationListToNotify) {

            }

            @Override
            public void DataIsInserted() {

            }

            @Override
            public void DataIsUpdated() {

            }

            @Override
            public void DataIsDeleted() {

            }
        });
    }

}