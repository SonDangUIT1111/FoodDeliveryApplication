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

import com.example.fooddeliveryapplication.CustomMessageBox.FailToast;
import com.example.fooddeliveryapplication.CustomMessageBox.SuccessfulToast;
import com.example.fooddeliveryapplication.Dialog.LoadingDialog;
import com.example.fooddeliveryapplication.Model.Cart;
import com.example.fooddeliveryapplication.Model.User;
import com.example.fooddeliveryapplication.databinding.FragmentSignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;


public class SignUpFragment extends Fragment {
    private FragmentSignUpBinding binding;
    private LoadingDialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSignUpBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (check()) {
                    String phone = binding.edtPhone.getText().toString();
                    String name = binding.edtName.getText().toString();
                    String email = binding.edtEmail.getText().toString();
                    String pass = binding.edtPass.getText().toString();
                    dialog = new LoadingDialog(getContext());
                    dialog.show();
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                User tmp = new User(task.getResult().getUser().getUid(), email, "", name, "01/01/2000", phone);
                                Cart cart = new Cart(FirebaseDatabase.getInstance().getReference().push().getKey(), 0, 0, task.getResult().getUser().getUid());

                                FirebaseDatabase.getInstance().getReference("Users").child(tmp.getUserId())
                                        .setValue(tmp).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    dialog.dismiss();
                                                    FirebaseDatabase.getInstance().getReference("Carts").child(cart.getCartId()).setValue(cart);
                                                    new SuccessfulToast(getContext(), "Create account successfully").showToast();
                                                } else {
                                                    dialog.dismiss();
                                                    new FailToast(getContext(),"Create account unsuccessfully").showToast();
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
        String phone= binding.edtPhone.getText().toString();
        String name= binding.edtName.getText().toString();
        String email= binding.edtEmail.getText().toString();
        String pass= binding.edtPass.getText().toString();
        if (phone.isEmpty()|| name.isEmpty()|| email.isEmpty()|| pass.isEmpty()) {
            createDialog("Điền đầy đủ thông tin").show();
            return false;
        }
        else if (!email.matches(String.valueOf(Patterns.EMAIL_ADDRESS))) {
            createDialog("Email không đúng định dạng").show();
            return false;
        }
        else if (!phone.matches("(03|05|07|08|09|01[2689])[0-9]{8}\\b")) {
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
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        return builder.create();
    }
}