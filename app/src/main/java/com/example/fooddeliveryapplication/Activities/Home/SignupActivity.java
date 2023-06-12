package com.example.fooddeliveryapplication.Activities.Home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fooddeliveryapplication.Dialog.LoadingDialog;
import com.example.fooddeliveryapplication.Model.User;
import com.example.fooddeliveryapplication.R;
import com.example.fooddeliveryapplication.databinding.ActivitySignupBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SignupActivity extends AppCompatActivity {

    ActivitySignupBinding binding;
    private LoadingDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (check()==true) {
                    String phone=binding.edtPhone.getText().toString();
                    String name=binding.edtName.getText().toString();
                    String email=binding.edtEmail.getText().toString();
                    String pass=binding.edtPass.getText().toString();
                    dialog=new LoadingDialog(SignupActivity.this);
                    dialog.show();
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                User tmp=new User(name,task.getResult().getUser().getUid(),email,"",name,"",phone,
                                        new SimpleDateFormat("dd/MM/yyyy").format(new Date()),"");
                                FirebaseDatabase.getInstance().getReference("Users").child(tmp.getUserId())
                                        .setValue(tmp).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    dialog.dismiss();
                                                    Toast.makeText(SignupActivity.this,"Tạo tài khoản thành công",Toast.LENGTH_SHORT);
                                                    finish();
                                                } else {
                                                    dialog.dismiss();
                                                    Toast.makeText(SignupActivity.this,"Không thành thành công",Toast.LENGTH_SHORT);
                                                    finish();
                                                }
                                            }
                                        });
                            } else {
                                createDialog("Email đã tồn tại").show();
                                dialog.dismiss();
                            }
                        }
                    });
                }
            }
        });
        binding.txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public boolean check() {
        String phone=binding.edtPhone.getText().toString();
        String name=binding.edtName.getText().toString();
        String email=binding.edtEmail.getText().toString();
        String pass=binding.edtPass.getText().toString();
        if (phone.isEmpty()|| name.isEmpty()|| email.isEmpty()|| pass.isEmpty()) {
                createDialog("Điền đầy đủ thông tin").show();
                return false;
        }  else if (!email.matches(String.valueOf(Patterns.EMAIL_ADDRESS))) {
            createDialog("Email không đúng định dạng").show();
            return false;
        } else if (!phone.matches("(03|05|07|08|09|01[2689])[0-9]{8}\\b")) {
            createDialog("Số điện thoại không hợp lệ").show();
            return false;
        }
        return true;
    }


    public AlertDialog createDialog(String message) {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setTitle("Notice");
        builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        return builder.create();
    }
}