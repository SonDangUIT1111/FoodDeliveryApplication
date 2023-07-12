package com.example.fooddeliveryapplication.Activities.Cart_PlaceOrder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.fooddeliveryapplication.CustomMessageBox.FailToast;
import com.example.fooddeliveryapplication.CustomMessageBox.SuccessfulToast;
import com.example.fooddeliveryapplication.GlobalConfig;
import com.example.fooddeliveryapplication.Model.Address;
import com.example.fooddeliveryapplication.databinding.ActivityUpdateAddAddressBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UpdateAddAddressActivity extends AppCompatActivity {
    private ActivityUpdateAddAddressBinding binding;
    private String userId;
    private String mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateAddAddressBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userId = getIntent().getStringExtra("userId");
        mode = getIntent().getStringExtra("mode");

        initToolbar();

        if (mode.equals("add - default")) {
            binding.updateComplete.setText("Complete");
            binding.setDefault.setChecked(true);
            binding.setDefault.setEnabled(false);
        }
        else if (mode.equals("add - non-default")) {
            binding.updateComplete.setText("Complete");
        }
        else if (mode.equals("update")) {
            binding.updateComplete.setText("Update");

            FirebaseDatabase.getInstance().getReference().child("Address").child(userId).child(GlobalConfig.updateAddressId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Address address = snapshot.getValue(Address.class);
                    binding.fullName.setText(address.getReceiverName());
                    binding.phoneNumber.setText(address.getReceiverPhoneNumber());
                    binding.detailAddress.setText(address.getDetailAddress());
                    if (address.getState().equals("default")) {
                        binding.setDefault.setEnabled(false);
                        binding.setDefault.setChecked(true);
                    }
                    else {
                        binding.setDefault.setChecked(false);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        binding.updateComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateAddressInfo()) {
                    if (binding.updateComplete.getText().equals("Complete")) {
                        // Add default address
                        if (binding.setDefault.isChecked()) {
                            String addressId = FirebaseDatabase.getInstance().getReference().push().getKey();
                            GlobalConfig.choseAddressId = addressId;

                            Address temp = new Address(addressId, binding.detailAddress.getText().toString().trim(), "default",
                                    binding.fullName.getText().toString().trim(), binding.phoneNumber.getText().toString().trim());

                            FirebaseDatabase.getInstance().getReference().child("Address").child(userId).child(addressId).setValue(temp).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseDatabase.getInstance().getReference().child("Address").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                for (DataSnapshot ds : snapshot.getChildren()) {
                                                    Address address = ds.getValue(Address.class);
                                                    if (!address.getAddressId().equals(addressId)) {
                                                        FirebaseDatabase.getInstance().getReference().child("Address").child(userId).child(address.getAddressId()).child("state").setValue("");
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });

                                        new SuccessfulToast(UpdateAddAddressActivity.this,"Added new address!").showToast();

                                        GlobalConfig.choseAddressId = addressId;
                                        Intent intent = new Intent();
                                        setResult(RESULT_OK, intent);
                                        finish();
                                    }
                                }
                            });
                        }
                        // Add normal address
                        else {
                            String addressId = FirebaseDatabase.getInstance().getReference().push().getKey();
                            GlobalConfig.choseAddressId = addressId;

                            Address temp = new Address(addressId, binding.detailAddress.getText().toString().trim(), "",
                                    binding.fullName.getText().toString().trim(), binding.phoneNumber.getText().toString().trim());
                            FirebaseDatabase.getInstance().getReference().child("Address").child(userId).child(addressId).setValue(temp).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        new SuccessfulToast(UpdateAddAddressActivity.this, "Added new address!").showToast();

                                        GlobalConfig.choseAddressId = addressId;
                                        Intent intent = new Intent();
                                        setResult(RESULT_OK, intent);
                                        finish();
                                    }
                                }
                            });
                        }
                    }
                    else {
                        // Update to default address
                        if (binding.setDefault.isChecked()) {
                            Address temp = new Address(GlobalConfig.updateAddressId, binding.detailAddress.getText().toString().trim(), "default",
                                    binding.fullName.getText().toString().trim(), binding.phoneNumber.getText().toString().trim());
                            FirebaseDatabase.getInstance().getReference().child("Address").child(userId).child(GlobalConfig.updateAddressId).setValue(temp).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseDatabase.getInstance().getReference().child("Address").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                for (DataSnapshot ds : snapshot.getChildren()) {
                                                    Address address = ds.getValue(Address.class);
                                                    if (!address.getAddressId().equals(GlobalConfig.updateAddressId)) {
                                                        FirebaseDatabase.getInstance().getReference().child("Address").child(userId).child(address.getAddressId()).child("state").setValue("");
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });

                                        new SuccessfulToast(UpdateAddAddressActivity.this, "Updated chose address!").showToast();

                                        Intent intent = new Intent();
                                        setResult(RESULT_OK, intent);
                                        finish();
                                    }
                                }
                            });
                        }
                        else {
                            Address temp = new Address(GlobalConfig.updateAddressId, binding.detailAddress.getText().toString().trim(), "",
                                    binding.fullName.getText().toString().trim(), binding.phoneNumber.getText().toString().trim());
                            FirebaseDatabase.getInstance().getReference().child("Address").child(userId).child(GlobalConfig.updateAddressId).setValue(temp).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        new SuccessfulToast(UpdateAddAddressActivity.this,"Updated chose address!").showToast();

                                        Intent intent = new Intent();
                                        setResult(RESULT_OK, intent);
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
        getWindow().setStatusBarColor(Color.parseColor("#E8584D"));
        getWindow().setNavigationBarColor(Color.parseColor("#E8584D"));
        setSupportActionBar(binding.toolbar);
        if (mode.equals("update")) {
            getSupportActionBar().setTitle("Update address");
        }
        else {
            getSupportActionBar().setTitle("Add address");
        }
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

    private boolean validateAddressInfo() {
        if (binding.fullName.getText().toString().equals("")) {
            new FailToast(this, "Receiver name must not be empty!").showToast();
            return false;
        }

        if (binding.phoneNumber.getText().toString().equals("")) {
            new FailToast(this, "Receiver phone number must not be empty!").showToast();
            return false;
        }

        if (binding.detailAddress.getText().toString().equals("")) {
            new FailToast(this, "Detail address must not be empty!").showToast();
            return false;
        }

        return true;
    }
}