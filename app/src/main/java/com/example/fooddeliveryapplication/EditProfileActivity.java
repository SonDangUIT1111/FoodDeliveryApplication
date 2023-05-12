package com.example.fooddeliveryapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.fooddeliveryapplication.Models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class EditProfileActivity extends AppCompatActivity {
    private ImageView profileImage;
    private EditText userName;
    private EditText email;
    private EditText phoneNumber;
    private EditText birthDate;
    private TextView changePhoto;
    private ImageView datePicker;
    private DatePickerDialog datePickerDialog;

    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        initToolbar();
        initDatePickerDialog();

        profileImage = findViewById(R.id.profile_image);
        userName = findViewById(R.id.user_name);
        email = findViewById(R.id.email);
        phoneNumber = findViewById(R.id.phone_number);
        birthDate = findViewById(R.id.birth_date);
        changePhoto = findViewById(R.id.change_photo);
        datePicker = findViewById(R.id.date_picker);

        getInfo();

        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show();
            }
        });
    }

    private void getInfo() {
        FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                Glide.with(EditProfileActivity.this).load(user.getAvatarURL()).placeholder(R.drawable.profile_image).into(profileImage);
                userName.setText(user.getUserName());
                email.setText(user.getEmail());
                phoneNumber.setText(user.getPhoneNumber());
                birthDate.setText(user.getBirthDate());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initDatePickerDialog() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                month++;
                birthDate.setText(getDMY(date, month, year));
            }
        };

        int style = AlertDialog.BUTTON_NEGATIVE;

        FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                String txtBirthDate = user.getBirthDate();
                int date = Integer.parseInt(txtBirthDate.substring(0, 2));
                int month = Integer.parseInt(txtBirthDate.substring(3, 5));
                int year = Integer.parseInt(txtBirthDate.substring(6));

                datePickerDialog = new DatePickerDialog(EditProfileActivity.this, style, dateSetListener, year, month - 1, date);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Edit Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private String getDMY(int date, int month, int year) {
        if ((date >= 1 && date <= 9) && (month >= 1 && month <= 9))
            return "0" + date + "/" + "0" + month + "/" + year;

        if (date >= 1 && date <= 9)
            return "0" + date + "/" + month + "/" + year;

        if (month >= 1 && month <= 9)
            return date + "/" + "0" + month + "/" + year;

        return date + "/" + month + "/" + year;
    }
}