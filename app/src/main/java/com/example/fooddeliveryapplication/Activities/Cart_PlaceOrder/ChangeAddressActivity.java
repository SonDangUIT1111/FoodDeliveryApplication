package com.example.fooddeliveryapplication.Activities.Cart_PlaceOrder;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fooddeliveryapplication.Adapters.AddressAdapter;
import com.example.fooddeliveryapplication.GlobalConfig;
import com.example.fooddeliveryapplication.Interfaces.IAddressAdapterListener;
import com.example.fooddeliveryapplication.Model.Address;
import com.example.fooddeliveryapplication.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChangeAddressActivity extends AppCompatActivity {
    String userId;
    private ImageView add1;
    private RecyclerView recyclerViewAddresses;
    private AddressAdapter addressAdapter;
    private List<Address> addressList;
    private ActivityResultLauncher<Intent> updateAddAddressLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_address);

        userId = getIntent().getStringExtra("userId");

        initToolbar();
        initUpdateAddAddressActivity();

        add1 = findViewById(R.id.add1);
        recyclerViewAddresses = findViewById(R.id.recycler_view_address);
        recyclerViewAddresses.setHasFixedSize(true);
        recyclerViewAddresses.setLayoutManager(new LinearLayoutManager(this));
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
        });
        recyclerViewAddresses.setAdapter(addressAdapter);

        loadInfo();

        add1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChangeAddressActivity.this, UpdateAddAddressActivity.class);
                intent.putExtra("userId",userId);
                intent.putExtra("mode", "add");
                updateAddAddressLauncher.launch(intent);
            }
        });
    }

    private void initUpdateAddAddressActivity() {
        // Init launcher
        updateAddAddressLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                addressAdapter.notifyDataSetChanged();
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
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Change address");
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
}