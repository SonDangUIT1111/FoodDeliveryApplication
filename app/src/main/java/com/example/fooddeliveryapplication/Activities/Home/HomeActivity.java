package com.example.fooddeliveryapplication.Activities.Home;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;

import com.example.fooddeliveryapplication.Fragments.Home.FavoriteFragment;
import com.example.fooddeliveryapplication.Fragments.Home.HistoryFragment;
import com.example.fooddeliveryapplication.Fragments.Home.HomeFragment;
import com.example.fooddeliveryapplication.R;
import com.example.fooddeliveryapplication.databinding.ActivityHomeBinding;

import com.example.fooddeliveryapplication.databinding.LayoutNavigationHeaderBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class HomeActivity extends AppCompatActivity {
    private String userId;
    FirebaseDatabase database= FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = database.getReference();
    ActivityHomeBinding binding;
    private LinearLayout layoutMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Todo take information of user just login here
        userId = "randomUserId1";
        //----------
        //----------------------
        initUI();
        //----------------------
    }




    private void initUI() {
        createActionBar();

        layoutMain=binding.layoutMain;
        getSupportFragmentManager()
                .beginTransaction()
                .add(layoutMain.getId(),new HomeFragment(userId))
                .commit();

        setEventNavigationBottom();
    }

    private void setEventNavigationBottom() {
        binding.navigationBottom.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                Fragment fragment;
                switch (i) {
                    case R.id.favorite_menu:
                        fragment=new FavoriteFragment(userId);
                        break;
                    case R.id.history_menu:
                        fragment=new HistoryFragment();
                        break;
                    default:
                        fragment=new HomeFragment(userId);
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