package com.example.fooddeliveryapplication.Activities.Cart_PlaceOrder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fooddeliveryapplication.GlobalConfig;
import com.example.fooddeliveryapplication.Model.Address;
import com.example.fooddeliveryapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class UpdateAddAddressActivity extends AppCompatActivity {
    String userId;
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

        userId = getIntent().getStringExtra("userId");
        mode = getIntent().getStringExtra("mode");

        initToolbar();

        receiverName = findViewById(R.id.full_name);
        receiverPhoneNumber = findViewById(R.id.phone_number);
        detailAddress = findViewById(R.id.detail_address);
        setDefault = findViewById(R.id.set_default);
        updateAndComplete = findViewById(R.id.update_complete);

        if (mode.equals("add - default")) {
            updateAndComplete.setText("Complete");
            setDefault.setChecked(true);
            setDefault.setEnabled(false);
        }
        else if (mode.equals("add - non-default")) {
            updateAndComplete.setText("Complete");
        }
        else if (mode.equals("update")) {
            updateAndComplete.setText("Update");

            FirebaseDatabase.getInstance().getReference().child("Address").child(userId).child(GlobalConfig.updateAddressId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Address address = snapshot.getValue(Address.class);
                    receiverName.setText(address.getReceiverName());
                    receiverPhoneNumber.setText(address.getReceiverPhoneNumber());
                    detailAddress.setText(address.getDetailAddress());
                    if (address.getState().equals("default")) {
                        setDefault.setEnabled(false);
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
                if (validateAddressInfo()) {
                    if (updateAndComplete.getText().equals("Complete")) {
                        // Add default address
                        if (setDefault.isChecked()) {
                            String addressId = FirebaseDatabase.getInstance().getReference().push().getKey();
                            GlobalConfig.choseAddressId = addressId;

                            Address temp = new Address(addressId, detailAddress.getText().toString().trim(), "default",
                                    receiverName.getText().toString().trim(), receiverPhoneNumber.getText().toString().trim());

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

                                        Toast.makeText(UpdateAddAddressActivity.this, "Thêm địa chỉ mới thành công!", Toast.LENGTH_SHORT).show();

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

                            Address temp = new Address(addressId, detailAddress.getText().toString().trim(), "",
                                    receiverName.getText().toString().trim(), receiverPhoneNumber.getText().toString().trim());
                            FirebaseDatabase.getInstance().getReference().child("Address").child(userId).child(addressId).setValue(temp).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(UpdateAddAddressActivity.this, "Thêm địa chỉ mới thành công!", Toast.LENGTH_SHORT).show();

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
                        if (setDefault.isChecked()) {
                            Address temp = new Address(GlobalConfig.updateAddressId, detailAddress.getText().toString().trim(), "default",
                                    receiverName.getText().toString().trim(), receiverPhoneNumber.getText().toString().trim());
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

                                        Toast.makeText(UpdateAddAddressActivity.this, "Cập nhật địa chỉ thành công!", Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent();
                                        setResult(RESULT_OK, intent);
                                        finish();
                                    }
                                }
                            });
                        }
                        else {
                            Address temp = new Address(GlobalConfig.updateAddressId, detailAddress.getText().toString().trim(), "",
                                    receiverName.getText().toString().trim(), receiverPhoneNumber.getText().toString().trim());
                            FirebaseDatabase.getInstance().getReference().child("Address").child(userId).child(GlobalConfig.updateAddressId).setValue(temp).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(UpdateAddAddressActivity.this, "Cập nhật địa chỉ thành công!", Toast.LENGTH_SHORT).show();

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
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (mode.equals("update")) {
            getSupportActionBar().setTitle("Update address");
        }
        else {
            getSupportActionBar().setTitle("Add address");
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