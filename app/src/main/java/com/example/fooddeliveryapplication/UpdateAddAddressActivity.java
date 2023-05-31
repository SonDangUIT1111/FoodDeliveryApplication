package com.example.fooddeliveryapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fooddeliveryapplication.Models.Address;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class UpdateAddAddressActivity extends AppCompatActivity {
    private EditText receiverName;
    private EditText receiverPhoneNumber;
    private EditText detailAddress;
    private SwitchCompat setDefault;
    private Button updateAndComplete;
    private String mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_add_address);

        mode = getIntent().getStringExtra("mode");

        initToolbar();

        receiverName = findViewById(R.id.full_name);
        receiverPhoneNumber = findViewById(R.id.phone_number);
        detailAddress = findViewById(R.id.detail_address);
        setDefault = findViewById(R.id.set_default);
        updateAndComplete = findViewById(R.id.update_complete);

        if (mode.equals("add")) {
            updateAndComplete.setText("Complete");
        }
        else {
            updateAndComplete.setText("Update");

            FirebaseDatabase.getInstance().getReference().child("Address").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(GlobalConfig.updateAddressId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Address address = snapshot.getValue(Address.class);
                    receiverName.setText(address.getReceiverName());
                    receiverPhoneNumber.setText(address.getReceiverPhoneNumber());
                    detailAddress.setText(address.getDetailAddress());
                    if (address.getState().equals("default")) {
                        setDefault.setChecked(true);
                    }
                    else {
                        setDefault.setChecked(false);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        updateAndComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (updateAndComplete.getText().equals("Complete")) {
                    if (validateAddressInfo()) {
                        if (setDefault.isChecked()) {
                            String addressId = FirebaseDatabase.getInstance().getReference().push().getKey();
                            GlobalConfig.choseAddressId = addressId;

                            FirebaseDatabase.getInstance().getReference().child("Address").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot ds : snapshot.getChildren()) {
                                        Address address = ds.getValue(Address.class);
                                        if (!address.getAddressId().equals(addressId)) {
                                            FirebaseDatabase.getInstance().getReference().child("Address").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(address.getAddressId()).child("state").setValue("");
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                            HashMap<String, Object> map = new HashMap<>();
                            map.put("addressId", addressId);
                            map.put("detailAddress", detailAddress.getText().toString());
                            map.put("receiverName", receiverName.getText().toString());
                            map.put("receiverPhoneNumber", receiverPhoneNumber.getText().toString());
                            map.put("state", "default");
                            FirebaseDatabase.getInstance().getReference().child("Address").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(addressId).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        GlobalConfig.choseAddressId = addressId;
                                        Intent intent = new Intent();
                                        setResult(RESULT_OK, intent);
                                        finish();
                                    }
                                }
                            });
                        }
                        else {
                            String addressId = FirebaseDatabase.getInstance().getReference().push().getKey();
                            GlobalConfig.choseAddressId = addressId;

                            HashMap<String, Object> map = new HashMap<>();
                            map.put("addressId", addressId);
                            map.put("detailAddress", detailAddress.getText().toString());
                            map.put("receiverName", receiverName.getText().toString());
                            map.put("receiverPhoneNumber", receiverPhoneNumber.getText().toString());
                            map.put("state", "");
                            FirebaseDatabase.getInstance().getReference().child("Address").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(addressId).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        GlobalConfig.choseAddressId = addressId;
                                        Intent intent = new Intent();
                                        setResult(RESULT_OK, intent);
                                        finish();
                                    }
                                }
                            });
                        }
                    }
                }
                else if (updateAndComplete.getText().equals("Update")) {
                    if (validateAddressInfo()) {
                        if (setDefault.isChecked()) {
                            FirebaseDatabase.getInstance().getReference().child("Address").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot ds : snapshot.getChildren()) {
                                        Address address = ds.getValue(Address.class);
                                        if (!address.getAddressId().equals(GlobalConfig.updateAddressId)) {
                                            FirebaseDatabase.getInstance().getReference().child("Address").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(address.getAddressId()).child("state").setValue("");
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                            HashMap<String, Object> map = new HashMap<>();
                            map.put("detailAddress", detailAddress.getText().toString());
                            map.put("receiverName", receiverName.getText().toString());
                            map.put("receiverPhoneNumber", receiverPhoneNumber.getText().toString());
                            map.put("state", "default");
                            FirebaseDatabase.getInstance().getReference().child("Address").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(GlobalConfig.updateAddressId).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        finish();
                                    }
                                }
                            });
                        }
                        else {
                            HashMap<String, Object> map = new HashMap<>();
                            map.put("detailAddress", detailAddress.getText().toString());
                            map.put("receiverName", receiverName.getText().toString());
                            map.put("receiverPhoneNumber", receiverPhoneNumber.getText().toString());
                            map.put("state", "");
                            FirebaseDatabase.getInstance().getReference().child("Address").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(GlobalConfig.updateAddressId).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        finish();
                                    }
                                }
                            });
                        }
                    }
                }
            }
        });
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (mode.equals("add")) {
            getSupportActionBar().setTitle("Add new address");
        }
        else if (mode.equals("update")) {
            getSupportActionBar().setTitle("Update address");
        }
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

    private boolean validateAddressInfo() {
        if (receiverName.getText().toString().equals("")) {
            Toast.makeText(this, "Receiver name must not be empty!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (receiverPhoneNumber.getText().toString().equals("")) {
            Toast.makeText(this, "Receiver phone number must not be empty!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (detailAddress.getText().toString().equals("")) {
            Toast.makeText(this, "Detail address must not be empty", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}