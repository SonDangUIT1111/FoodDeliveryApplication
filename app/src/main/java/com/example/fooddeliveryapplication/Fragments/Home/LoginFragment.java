package com.example.fooddeliveryapplication.Fragments.Home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.fooddeliveryapplication.Activities.Home.ForgotActivity;
import com.example.fooddeliveryapplication.Activities.Home.HomeActivity;
import com.example.fooddeliveryapplication.Model.User;
import com.example.fooddeliveryapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class LoginFragment extends Fragment {
    ActivityResultLauncher launcher;
    String TAG="firebase - LOGIN";
    EditText edtUserNameLogin,edtPasswordLogin;
    Button btnLogin;
    public static final int CODE_SUCCESS=100001;
    private static User currentUser=new User();
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_login, container, false);

        getLauncher();
        anhxa();
        btnLogin.setOnClickListener(view -> {
            if (edtPasswordLogin.getText().toString().isEmpty()||edtUserNameLogin.getText().toString().isEmpty()) {
                Toast.makeText(getContext(), "Ô Trống", Toast.LENGTH_SHORT).show();
            } else {

                FirebaseAuth.getInstance().signInWithEmailAndPassword(edtUserNameLogin.getText().toString(),edtPasswordLogin.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String idCurrentUser=FirebaseAuth.getInstance().getCurrentUser().getUid();
                            FirebaseDatabase.getInstance().getReference("Users").child(idCurrentUser).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    currentUser=snapshot.getValue(User.class);
                                    Log.d(TAG, "đăng nhập thành công", task.getException());
                                    Toast.makeText(getContext(), "Thành công", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(getContext(), "Sai mật khẩu hoặc email", Toast.LENGTH_SHORT).show();
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

    private void anhxa() {
        edtPasswordLogin= view.findViewById(R.id.edtPasswordLogin);
        edtUserNameLogin= view.findViewById(R.id.edtEmail);
        btnLogin= view.findViewById(R.id.btnReset);
    }



    public void getLauncher() {
        launcher =registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode()==CODE_SUCCESS) {
                Intent tmp=result.getData();
                currentUser= (User) tmp.getSerializableExtra("User");
                edtUserNameLogin.setText(currentUser.getEmail().toString());
            }
        });
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public void login(String email,String pass) {

    }
}