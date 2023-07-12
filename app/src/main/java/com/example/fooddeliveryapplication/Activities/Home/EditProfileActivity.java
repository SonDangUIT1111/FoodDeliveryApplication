package com.example.fooddeliveryapplication.Activities.Home;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.DatePicker;

import com.bumptech.glide.Glide;
import com.example.fooddeliveryapplication.CustomMessageBox.CustomAlertDialog;
import com.example.fooddeliveryapplication.CustomMessageBox.FailToast;
import com.example.fooddeliveryapplication.CustomMessageBox.SuccessfulToast;
import com.example.fooddeliveryapplication.Model.User;
import com.example.fooddeliveryapplication.R;
import com.example.fooddeliveryapplication.databinding.ActivityEditProfileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Objects;

public class EditProfileActivity extends AppCompatActivity {
    private ActivityEditProfileBinding binding;
    private DatePickerDialog datePickerDialog;
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private String imageUrl;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getWindow().setStatusBarColor(Color.parseColor("#E8584D"));
        getWindow().setNavigationBarColor(Color.parseColor("#E8584D"));

        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");

        initToolbar();
        initDatePickerDialog();
        initImagePickerActivity();

        getInfo();

        binding.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImagePicker();
            }
        });

        binding.changePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImagePicker();
            }
        });

        binding.userName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                FirebaseDatabase.getInstance().getReference().child("Users").child(userId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user = snapshot.getValue(User.class);

                        binding.update.setEnabled(!(user.getUserName().equals(binding.userName.getText().toString()) &&
                                user.getEmail().equals(binding.email.getText().toString()) &&
                                user.getPhoneNumber().equals(binding.phoneNumber.getText().toString()) &&
                                user.getBirthDate().equals(binding.birthDate.getText().toString())));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        binding.email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                FirebaseDatabase.getInstance().getReference().child("Users").child(userId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user = snapshot.getValue(User.class);

                        binding.update.setEnabled(!(user.getUserName().equals(binding.userName.getText().toString()) &&
                                user.getEmail().equals(binding.email.getText().toString()) &&
                                user.getPhoneNumber().equals(binding.phoneNumber.getText().toString()) &&
                                user.getBirthDate().equals(binding.birthDate.getText().toString())));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        binding.phoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                FirebaseDatabase.getInstance().getReference().child("Users").child(userId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user = snapshot.getValue(User.class);

                        binding.update.setEnabled(!(user.getUserName().equals(binding.userName.getText().toString()) &&
                                user.getEmail().equals(binding.email.getText().toString()) &&
                                user.getPhoneNumber().equals(binding.phoneNumber.getText().toString()) &&
                                user.getBirthDate().equals(binding.birthDate.getText().toString())));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        binding.birthDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                FirebaseDatabase.getInstance().getReference().child("Users").child(userId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user = snapshot.getValue(User.class);

                        binding.update.setEnabled(!(user.getUserName().equals(binding.userName.getText().toString()) &&
                                user.getEmail().equals(binding.email.getText().toString()) &&
                                user.getPhoneNumber().equals(binding.phoneNumber.getText().toString()) &&
                                user.getBirthDate().equals(binding.birthDate.getText().toString())));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        binding.datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show();
            }
        });

        binding.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateInfo();
            }
        });
    }

    private void updateInfo() {
        String emailTxt = binding.email.getText().toString().trim();
        String phoneNumberTxt = binding.phoneNumber.getText().toString().trim();
        String userNameTxt = binding.userName.getText().toString().trim();

        if (emailTxt.equals("")) {
            new FailToast(this, "Email must not be empty!").showToast();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(emailTxt).matches()) {
            new FailToast(this, "Invalid email!!").showToast();
            return;
        }

        if (phoneNumberTxt.equals("")) {
            new FailToast(this, "Phone number must not be empty!").showToast();
            return;
        }

        if (userNameTxt.equals("")) {
            new FailToast(this, "User name must not be empty!").showToast();
            return;
        }

        HashMap<String, Object> map = new HashMap<>();
        map.put("avatarURL", imageUrl);
        map.put("birthDate", binding.birthDate.getText().toString());
        map.put("email", binding.email.getText().toString());
        map.put("phoneNumber", binding.phoneNumber.getText().toString());
        map.put("userName", binding.userName.getText().toString());

        FirebaseDatabase.getInstance().getReference().child("Users").child(userId).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    new SuccessfulToast(EditProfileActivity.this, "Updated successfully!").showToast();
                }
            }
        });

        //Todo: not update in auth due to link
//        firebaseUser.updateEmail(email.getText().toString());

        finish();
    }

    private void deleteOldImage() {
        if (!Objects.equals(imageUrl, "")) {
            FirebaseStorage.getInstance().getReferenceFromUrl(imageUrl).delete();
        }
    }

    private void initImagePickerActivity() {
        // Init launcher
        imagePickerLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                Uri imageUri = result.getData().getData();

                uploadImage(imageUri);
            }
        });
    }

    private void openImagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        imagePickerLauncher.launch(intent);
    }

    private void getInfo() {
        FirebaseDatabase.getInstance().getReference().child("Users").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                Glide.with(getApplicationContext()).load(user.getAvatarURL()).placeholder(R.drawable.default_avatar).into(binding.profileImage);
                binding.userName.setText(user.getUserName());
                binding.email.setText(user.getEmail());
                binding.phoneNumber.setText(user.getPhoneNumber());
                binding.birthDate.setText(user.getBirthDate());
                imageUrl = user.getAvatarURL();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    void uploadImage(Uri imageUri) {
        ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Uploading");
        pd.show();

        if (imageUri != null) {
            StorageReference fileRef = FirebaseStorage.getInstance().getReference().child("Users").child(System.currentTimeMillis() + "." + getFileExtension(imageUri));

            fileRef.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            deleteOldImage();

                            imageUrl = uri.toString();
                            Glide.with(EditProfileActivity.this).load(imageUrl).placeholder(R.drawable.profile_image).into(binding.profileImage);

                            pd.dismiss();

                            binding.update.setEnabled(true);
                        }
                    });
                }
            });
        }
    }

    private void initDatePickerDialog() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                month++;
                binding.birthDate.setText(getDMY(date, month, year));
            }
        };

        int style = AlertDialog.BUTTON_NEGATIVE;

        FirebaseDatabase.getInstance().getReference().child("Users").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                String txtBirthDate = user.getBirthDate();
                String[] dateSplit = cutDay(txtBirthDate);
                int date = Integer.parseInt(dateSplit[0]);
                int month = Integer.parseInt(dateSplit[1]);
                int year = Integer.parseInt(dateSplit[2]);

                datePickerDialog = new DatePickerDialog(EditProfileActivity.this, style, dateSetListener, year, month - 1, date);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initToolbar() {
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle("Edit Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.update.isEnabled()) {
                    new CustomAlertDialog(EditProfileActivity.this,"Save changes?");
                    CustomAlertDialog.binding.btnYes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            CustomAlertDialog.alertDialog.dismiss();
                            updateInfo();
                        }
                    });
                    CustomAlertDialog.binding.btnNo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            CustomAlertDialog.alertDialog.dismiss();
                            finish();
                        }
                    });
                    CustomAlertDialog.showAlertDialog();
                }
                else {
                    finish();
                }
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

    private String getFileExtension(Uri uri) {
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(getContentResolver().getType(uri));
    }

    private String[] cutDay(String date)
    {
        String[] arrOfStr = date.split("/");
        return arrOfStr;
    }
}
