package com.example.fooddeliveryapplication.Fragments.Home;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.fooddeliveryapplication.Dialog.LoadingDialog;
import com.example.fooddeliveryapplication.Model.User;
import com.example.fooddeliveryapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;


public class SignUpFragment extends Fragment {

    private LoadingDialog dialog;
    View view;
    Button btnSignUp;
    TextInputEditText edtPhone;
    TextInputEditText edtName;
    TextInputEditText edtEmail;
    TextInputEditText edtPass;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_sign_up, container, false);
        btnSignUp = view.findViewById(R.id.btnSignUp);
        edtPhone = view.findViewById(R.id.edtPhone);
        edtName = view.findViewById(R.id.edtName);
        edtEmail = view.findViewById(R.id.edtEmail);
        edtPass = view.findViewById(R.id.edtPass);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (check()==true) {
                    String phone= edtPhone.getText().toString();
                    String name= edtName.getText().toString();
                    String email= edtEmail.getText().toString();
                    String pass= edtPass.getText().toString();
                    dialog=new LoadingDialog(getContext());
                    dialog.show();
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                User tmp=new User(name,task.getResult().getUser().getUid(),email,"https://t4.ftcdn.net/jpg/01/18/03/35/360_F_118033506_uMrhnrjBWBxVE9sYGTgBht8S5liVnIeY.jpg",name,"1/1/2000",phone,
                                        new SimpleDateFormat("dd/MM/yyyy").format(new Date()),"");
                                FirebaseDatabase.getInstance().getReference("Users").child(tmp.getUserId())
                                        .setValue(tmp).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    dialog.dismiss();
                                                    Toast.makeText(getContext(),"Tạo tài khoản thành công",Toast.LENGTH_SHORT);
                                                } else {
                                                    dialog.dismiss();
                                                    Toast.makeText(getContext(),"Không thành thành công",Toast.LENGTH_SHORT);
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

        return view;
    }

    public boolean check() {
        String phone= edtPhone.getText().toString();
        String name= edtName.getText().toString();
        String email= edtEmail.getText().toString();
        String pass= edtPass.getText().toString();
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
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
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