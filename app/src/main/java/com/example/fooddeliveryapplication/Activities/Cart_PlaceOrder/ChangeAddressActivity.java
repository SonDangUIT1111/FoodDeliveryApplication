package com.example.fooddeliveryapplication.Activities.Cart_PlaceOrder;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.example.fooddeliveryapplication.Adapters.AddressAdapter;
import com.example.fooddeliveryapplication.GlobalConfig;
import com.example.fooddeliveryapplication.Interfaces.IAddressAdapterListener;
import com.example.fooddeliveryapplication.Model.Address;
import com.example.fooddeliveryapplication.databinding.ActivityChangeAddressBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChangeAddressActivity extends AppCompatActivity {
    private String userId;
    private ActivityChangeAddressBinding binding;
    private AddressAdapter addressAdapter;
    private List<Address> addressList;
    private ActivityResultLauncher<Intent> updateAddAddressLauncher;
    static private final int UPDATE_ADDRESS_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChangeAddressBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userId = getIntent().getStringExtra("userId");

        initToolbar();
        initUpdateAddAddressActivity();

        binding.recyclerViewAddress.setHasFixedSize(true);
        binding.recyclerViewAddress.setLayoutManager(new LinearLayoutManager(this));
        addressList = new ArrayList<>();
        addressAdapter = new AddressAdapter(this, addressList, userId);
        addressAdapter.setAddressAdapterListener(new IAddressAdapterListener() {
            @Override
            public void onCheckedChanged(Address selectedAddress) {
                GlobalConfig.choseAddressId = selectedAddress.getAddressId();
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }

            @Override
            public void onDeleteAddress() {
                loadInfo();
            }
        });
        binding.recyclerViewAddress.setAdapter(addressAdapter);

        loadInfo();

        binding.add1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference().child("Address").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.getChildrenCount() == 0) {
                            Intent intent = new Intent(ChangeAddressActivity.this, UpdateAddAddressActivity.class);
                            intent.putExtra("userId", userId);
                            intent.putExtra("mode", "add - default");
                            updateAddAddressLauncher.launch(intent);
                        }
                        else {
                            Intent intent = new Intent(ChangeAddressActivity.this, UpdateAddAddressActivity.class);
                            intent.putExtra("userId", userId);
                            intent.putExtra("mode", "add - non-default");
                            updateAddAddressLauncher.launch(intent);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == UPDATE_ADDRESS_REQUEST_CODE) {
            loadInfo();
        }
    }

    private void initUpdateAddAddressActivity() {
        // Init launcher
        updateAddAddressLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                loadInfo();
            }
        });
    }

    private void loadInfo() {
        FirebaseDatabase.getInstance().getReference().child("Address").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                addressList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Address address = ds.getValue(Address.class);
                    if (address.getAddressId().equals(GlobalConfig.choseAddressId)) {
                        addressList.add(ds.getValue(Address.class));
                    }
                }
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Address address = ds.getValue(Address.class);
                    if (!address.getAddressId().equals(GlobalConfig.choseAddressId)) {
                        addressList.add(ds.getValue(Address.class));
                    }
                }
                addressAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initToolbar() {
        getWindow().setStatusBarColor(Color.parseColor("#E8584D"));
        getWindow().setNavigationBarColor(Color.parseColor("#E8584D"));
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle("Change address");
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
}