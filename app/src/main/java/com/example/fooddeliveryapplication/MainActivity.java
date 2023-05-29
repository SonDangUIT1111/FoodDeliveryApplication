package com.example.fooddeliveryapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.fooddeliveryapplication.Adapters.CartProductAdapter;
import com.example.fooddeliveryapplication.Fragments.ProfileFragment;
import com.example.fooddeliveryapplication.Interfaces.IAdapterItemListener;
import com.example.fooddeliveryapplication.Models.Cart;
import com.example.fooddeliveryapplication.Models.CartInfo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private FirebaseUser firebaseUser;
    private BottomNavigationView bottomNavigationView;
    private Fragment selectorFragment;
    private ImageView cart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Login user for test purpose, will be deleted later
        FirebaseAuth.getInstance().signInWithEmailAndPassword("21522549@gm.uit.edu.vn", "12345678").addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(MainActivity.this, "Login successfully!", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "Login failed!", Toast.LENGTH_SHORT).show();
            }
        });

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        cart = findViewById(R.id.cart);

        // Navigation item selected
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        // TODO - Open HomeFragment
                        break;
                    case R.id.nav_favorite:
                        // TODO - Open FavoriteFragment
                        break;
                    case R.id.nav_profile:
                        selectorFragment = new ProfileFragment();
                        break;
                    case R.id.nav_history:
                        // TODO - Open HistoryFragment
                        break;
                }

                if (selectorFragment != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectorFragment).commit();
                }

                return true;
            }
        });

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference().child("Carts").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            Cart cart = ds.getValue(Cart.class);
                            if (cart.getUserId().equals(firebaseUser.getUid())) {
                                FirebaseDatabase.getInstance().getReference().child("CartInfos").child(cart.getCartId()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.getChildrenCount() == 0) {
                                            startActivity(new Intent(MainActivity.this, EmptyCartActivity.class));
                                        }
                                        else {
                                            startActivity(new Intent(MainActivity.this, CartActivity.class));
                                        }
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }
}