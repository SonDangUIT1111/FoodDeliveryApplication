package com.example.fooddeliveryapplication.Activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;

import com.example.fooddeliveryapplication.Fragment.FavoriteFragment;
import com.example.fooddeliveryapplication.Fragment.HistoryFragment;
import com.example.fooddeliveryapplication.Fragment.HomeFragment;
import com.example.fooddeliveryapplication.Model.Food;
import com.example.fooddeliveryapplication.Model.Products;
import com.example.fooddeliveryapplication.Model.User;
import com.example.fooddeliveryapplication.R;
import com.example.fooddeliveryapplication.databinding.ActivityHomeBinding;

import com.example.fooddeliveryapplication.databinding.LayoutNavigationHeaderBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;


import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
    FirebaseDatabase database= FirebaseDatabase.getInstance();
    DatabaseReference datbaseReference=database.getReference();



   ActivityHomeBinding binding;


    private LinearLayout layoutMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //----------------------
        initUI();
        //----------------------
    }




    private void initUI() {
        //Khởi tạo actionbar
        createActionBar();
        //Khởi tạo homeFrg
        layoutMain=binding.layoutMain;
        getSupportFragmentManager()
                .beginTransaction()
                .add(layoutMain.getId(),new HomeFragment())
                .commit();
        //Set sự kiện cho bottom navigation
        setEventNavigationBottom();
    }

    private void setEventNavigationBottom() {
        binding.navigationBottom.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                Fragment fragment;
                switch (i) {
                    case R.id.home_menu:
                        fragment=new HomeFragment();
                        break;
                    case R.id.favorite_menu:
                        fragment=new FavoriteFragment();
                        break;
                    case R.id.history_menu:
                        fragment=new HistoryFragment();
                        break;
                    default:
                        fragment=new HomeFragment();
                }

                getSupportFragmentManager().beginTransaction()
                        .replace(layoutMain.getId(),fragment)
                        .commit();
            }
        });
    }


    private void createActionBar() {
        setSupportActionBar(binding.toolbar);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.menu_icon);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home_top,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==android.R.id.home) {
            binding.drawLayoutHome.openDrawer(GravityCompat.START);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding=null;
    }
}