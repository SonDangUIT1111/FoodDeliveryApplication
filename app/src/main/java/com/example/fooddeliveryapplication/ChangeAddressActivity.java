package com.example.fooddeliveryapplication;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fooddeliveryapplication.Adapters.AddressAdapter;
import com.example.fooddeliveryapplication.Adapters.OrderProductAdapter;
import com.example.fooddeliveryapplication.Interfaces.IAddressAdapterListener;
import com.example.fooddeliveryapplication.Models.Address;
import com.example.fooddeliveryapplication.Models.CartInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChangeAddressActivity extends AppCompatActivity {
    private ImageView add1;
    private TextView add2;
    private RecyclerView recyclerViewAddresses;
    private AddressAdapter addressAdapter;
    private List<Address> addressList;
    private String choseAddressId;
    private ActivityResultLauncher<Intent> updateAddAddressLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_address);

        initToolbar();
        initUpdateAddAddressActivity();

        add1 = findViewById(R.id.add1);
        add2 = findViewById(R.id.add2);
        recyclerViewAddresses = findViewById(R.id.recycler_view_address);
        recyclerViewAddresses.setHasFixedSize(true);
        recyclerViewAddresses.setLayoutManager(new LinearLayoutManager(this));
        choseAddressId = getIntent().getStringExtra("choseAddressId");
        //Toast.makeText(this, choseAddressId, Toast.LENGTH_SHORT).show();
        addressList = new ArrayList<>();
        addressAdapter = new AddressAdapter(this, addressList, choseAddressId);
        addressAdapter.setAddressAdapterListener(new IAddressAdapterListener() {
            @Override
            public void onCheckedChanged(Address selectedAddress) {
                Intent intent = new Intent();
                intent.putExtra("addressId", selectedAddress.getAddressId());
                Toast.makeText(ChangeAddressActivity.this, selectedAddress.getAddressId(), Toast.LENGTH_SHORT).show();
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
                intent.putExtra("mode", "add");
                intent.putExtra("choseAddressId", choseAddressId);
                startActivity(intent);
            }
        });

        add2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChangeAddressActivity.this, UpdateAddAddressActivity.class);
                intent.putExtra("mode", "add");
                intent.putExtra("choseAddressId", choseAddressId);
                startActivity(intent);
            }
        });
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
        FirebaseDatabase.getInstance().getReference().child("Address").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                addressList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Address address = ds.getValue(Address.class);
                    if (address.getAddressId().equals(choseAddressId)) {
                        addressList.add(ds.getValue(Address.class));
                    }
                }
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Address address = ds.getValue(Address.class);
                    if (!address.getAddressId().equals(choseAddressId)) {
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