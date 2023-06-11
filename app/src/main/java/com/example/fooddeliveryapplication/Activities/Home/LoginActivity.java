package com.example.fooddeliveryapplication.Activities.Home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fooddeliveryapplication.Activities.Order.OrderActivity;
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

public class LoginActivity extends AppCompatActivity {
    ActivityResultLauncher launcher;
    String TAG="firebase - LOGIN";
    EditText edtUserNameLogin,edtPasswordLogin;
    Button btnLogin;
    public static final int CODE_SUCCESS=100001;
    private static User currentUser=new User();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getLauncher();
        anhxa();
        TextView signup = findViewById(R.id.donthaveaccText);
        btnLogin.setOnClickListener(view -> {
            if (edtPasswordLogin.getText().toString().isEmpty()||edtUserNameLogin.getText().toString().isEmpty()) {
                Toast.makeText(LoginActivity.this, "Ô Trống", Toast.LENGTH_SHORT).show();
            } else {

                FirebaseAuth.getInstance().signInWithEmailAndPassword(edtUserNameLogin.getText().toString(), edtPasswordLogin.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String idCurrentUser=FirebaseAuth.getInstance().getCurrentUser().getUid();
                            FirebaseDatabase.getInstance().getReference("Users").child(idCurrentUser).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    currentUser=snapshot.getValue(User.class);
                                    Log.d(TAG, "đăng nhập thành công", task.getException());
                                    Toast.makeText(LoginActivity.this, "Thành công", Toast.LENGTH_SHORT).show();
                                    Intent intent=new Intent(LoginActivity.this, HomeActivity.class);
                                    startActivity(intent);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                }
                            });


                        } else {
                            Log.w(TAG, "đăng nhập thất bại", task.getException());
                            Toast.makeText(LoginActivity.this, "Sai mật khẩu hoặc email", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        TextView txtForgot=findViewById(R.id.forgotpassText);
        txtForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this,ForgotActivity.class);
                startActivity(intent);
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                launcher.launch(intent);
            }
        });
    }

    private void anhxa() {
        edtPasswordLogin=findViewById(R.id.edtPasswordLogin);
        edtUserNameLogin=findViewById(R.id.edtEmail);
        btnLogin=findViewById(R.id.btnReset);
    }



    public void getLauncher() {
        launcher =registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),result -> {
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