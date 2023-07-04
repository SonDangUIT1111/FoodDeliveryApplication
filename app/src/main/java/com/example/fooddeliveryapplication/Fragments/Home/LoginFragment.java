package com.example.fooddeliveryapplication.Fragments.Home;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.fooddeliveryapplication.Activities.Home.ForgotActivity;
import com.example.fooddeliveryapplication.Activities.Home.HomeActivity;
import com.example.fooddeliveryapplication.CustomMessageBox.FailToast;
import com.example.fooddeliveryapplication.Model.User;
import com.example.fooddeliveryapplication.R;
import com.example.fooddeliveryapplication.databinding.FragmentLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class LoginFragment extends Fragment {
    private FragmentLoginBinding binding;
    private ActivityResultLauncher launcher;
    private static final String TAG="firebase - LOGIN";
    public static final int CODE_SUCCESS = 100001;
    private static User currentUser = new User();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        getLauncher();

        binding.btnReset.setOnClickListener(view1 -> {
            if (binding.edtPasswordLogin.getText().toString().isEmpty() || binding.edtEmail.getText().toString().isEmpty()) {
                new FailToast().showToast(getContext(),"Please fill all the information");
            } else {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(binding.edtEmail.getText().toString(),binding.edtPasswordLogin.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String idCurrentUser=FirebaseAuth.getInstance().getCurrentUser().getUid();
                            FirebaseDatabase.getInstance().getReference("Users").child(idCurrentUser).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    currentUser=snapshot.getValue(User.class);
                                    Log.d(TAG, "đăng nhập thành công", task.getException());
                                    Intent intent=new Intent(getContext(), HomeActivity.class);
                                    startActivity(intent);
                                    requireActivity().finish();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                }
                            });


                        } else {
                            Log.w(TAG, "đăng nhập thất bại", task.getException());
                            new FailToast().showToast(getContext(),"Wrong email or password");
                        }
                    }
                });
            }
        });

        TextView txtForgot= view.findViewById(R.id.forgotpassText);
        txtForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(), ForgotActivity.class);
                startActivity(intent);
            }
        });


        return view;
    }

//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        binding = null;
//    }

    public void getLauncher() {
        launcher =registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode()==CODE_SUCCESS) {
                Intent tmp=result.getData();
                currentUser = (User) tmp.getSerializableExtra("User");
                binding.edtEmail.setText(currentUser.getEmail().toString());
            }
        });
    }
}