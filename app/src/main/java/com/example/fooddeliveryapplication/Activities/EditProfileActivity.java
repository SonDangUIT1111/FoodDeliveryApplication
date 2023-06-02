package com.example.fooddeliveryapplication.Activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.fooddeliveryapplication.Model.User;
import com.example.fooddeliveryapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class EditProfileActivity extends AppCompatActivity {
    private ImageView profileImage;
    private EditText userName;
    private EditText email;
    private EditText phoneNumber;
    private EditText birthDate;
    private TextView changePhoto;
    private ImageView datePicker;
    private Button update;
    private DatePickerDialog datePickerDialog;
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private Intent imagePickerIntent;
    private AlertDialog.Builder builder;
    private String imageUrl;

    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");

        initToolbar();
        initDatePickerDialog();
        initImagePickerActivity();
        initDialogBuilder();

        profileImage = findViewById(R.id.profile_image);
        userName = findViewById(R.id.user_name);
        email = findViewById(R.id.email);
        phoneNumber = findViewById(R.id.phone_number);
        birthDate = findViewById(R.id.birth_date);
        changePhoto = findViewById(R.id.change_photo);
        datePicker = findViewById(R.id.date_picker);
        update = findViewById(R.id.update);

        getInfo();

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImagePicker();
            }
        });

        changePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImagePicker();
            }
        });

        userName.addTextChangedListener(new TextWatcher() {
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

                        update.setEnabled(!(user.getUserName().equals(userName.getText().toString()) &&
                                user.getEmail().equals(email.getText().toString()) &&
                                user.getPhoneNumber().equals(phoneNumber.getText().toString()) &&
                                user.getBirthDate().equals(birthDate.getText().toString())));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        email.addTextChangedListener(new TextWatcher() {
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

                        update.setEnabled(!(user.getUserName().equals(userName.getText().toString()) &&
                                user.getEmail().equals(email.getText().toString()) &&
                                user.getPhoneNumber().equals(phoneNumber.getText().toString()) &&
                                user.getBirthDate().equals(birthDate.getText().toString())));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        phoneNumber.addTextChangedListener(new TextWatcher() {
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

                        update.setEnabled(!(user.getUserName().equals(userName.getText().toString()) &&
                                user.getEmail().equals(email.getText().toString()) &&
                                user.getPhoneNumber().equals(phoneNumber.getText().toString()) &&
                                user.getBirthDate().equals(birthDate.getText().toString())));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        birthDate.addTextChangedListener(new TextWatcher() {
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

                        update.setEnabled(!(user.getUserName().equals(userName.getText().toString()) &&
                                user.getEmail().equals(email.getText().toString()) &&
                                user.getPhoneNumber().equals(phoneNumber.getText().toString()) &&
                                user.getBirthDate().equals(birthDate.getText().toString())));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateInfo();
            }
        });
    }

    private void initDialogBuilder() {
        builder = new AlertDialog.Builder(EditProfileActivity.this);
        builder.setMessage("Save changes?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

                updateInfo();
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                finish();
            }
        });
    }

    private void updateInfo() {
        if (email.getText().toString().equals("")) {
            Toast.makeText(this, "Email must not be empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (phoneNumber.getText().toString().equals("")) {
            Toast.makeText(this, "Phone number must not be empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (userName.getText().toString().equals("")) {
            Toast.makeText(this, "User name must not be empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        HashMap<String, Object> map = new HashMap<>();
        map.put("avatarURL", imageUrl);
        map.put("birthDate", birthDate.getText().toString());
        map.put("email", email.getText().toString());
        map.put("phoneNumber", phoneNumber.getText().toString());
        map.put("userName", userName.getText().toString());

        FirebaseDatabase.getInstance().getReference().child("Users").child(userId).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(EditProfileActivity.this, "Updated successfully!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Todo: update in authetification
//        firebaseUser.updateEmail(email.getText().toString());

        finish();
    }

    private void initImagePickerActivity() {
        // Init intent
        imagePickerIntent = new Intent();
        imagePickerIntent.setType("image/");
        imagePickerIntent.setAction(Intent.ACTION_GET_CONTENT);

        // Init launcher
        imagePickerLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                Uri imageUri = result.getData().getData();

                uploadImage(imageUri);
            }
        });
    }

    private void openImagePicker() {
        imagePickerLauncher.launch(imagePickerIntent);
    }

    private void getInfo() {
        FirebaseDatabase.getInstance().getReference().child("Users").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                Glide.with(getApplicationContext()).load(user.getAvatarURL()).placeholder(R.drawable.profile_image).into(profileImage);
                userName.setText(user.getUserName());
                email.setText(user.getEmail());
                phoneNumber.setText(user.getPhoneNumber());
                birthDate.setText(user.getBirthDate());
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
                            imageUrl = uri.toString();
                            Glide.with(EditProfileActivity.this).load(imageUrl).placeholder(R.drawable.profile_image).into(profileImage);

                            pd.dismiss();

                            update.setEnabled(true);
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
                birthDate.setText(getDMY(date, month, year));
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
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Edit Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (update.isEnabled()) {
                    AlertDialog alert = builder.create();
                    alert.show();
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