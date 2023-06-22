package com.example.fooddeliveryapplication.Activities.Home;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.Manifest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.fooddeliveryapplication.Activities.Cart_PlaceOrder.CartActivity;
import com.example.fooddeliveryapplication.Activities.Cart_PlaceOrder.EmptyCartActivity;
import com.example.fooddeliveryapplication.Activities.MyShop.MyShopActivity;
import com.example.fooddeliveryapplication.Activities.Order.OrderActivity;
import com.example.fooddeliveryapplication.Fragments.Home.FavoriteFragment;
import com.example.fooddeliveryapplication.Fragments.Home.HistoryFragment;
import com.example.fooddeliveryapplication.Fragments.Home.HomeFragment;
import com.example.fooddeliveryapplication.Fragments.NotificationFragment;
import com.example.fooddeliveryapplication.Helpers.FirebaseNotificationHelper;
import com.example.fooddeliveryapplication.Helpers.FirebaseUserInfoHelper;
import com.example.fooddeliveryapplication.Model.Cart;
import com.example.fooddeliveryapplication.Model.Notification;
import com.example.fooddeliveryapplication.Model.User;
import com.example.fooddeliveryapplication.R;
import com.example.fooddeliveryapplication.databinding.ActivityHomeBinding;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;


import java.util.Collections;
import java.util.List;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private String userId;
    private FirebaseUser firebaseUser;
    FirebaseDatabase database= FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = database.getReference();
    ActivityHomeBinding binding;
    private LinearLayout layoutMain;

    private static final int NOTIFICATION_PERMISSION_CODE = 10023;
    private static final int STORAGE_PERMISSION_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // request permission here
        checkPermission("android.permission.POST_NOTIFICATIONS",NOTIFICATION_PERMISSION_CODE);
        checkPermission("android.permission.WRITE_EXTERNAL_STORAGE",STORAGE_PERMISSION_CODE);
        //----------
        initUI();
        //----------------------

        //----------------------


        loadInformationForNavigationBar();

    }




    private void initUI() {
        binding.navigationLeft.bringToFront();
        createActionBar();

        layoutMain=binding.layoutMain;
        getSupportFragmentManager()
                .beginTransaction()
                .add(layoutMain.getId(),new HomeFragment(userId))
                .commit();
        setEventNavigationBottom();
        setCartNavigation();
        binding.navigationLeft.setNavigationItemSelectedListener(this);

    }
    
    private void setCartNavigation()
    {
        binding.toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.cart_menu:
                        FirebaseDatabase.getInstance().getReference().child("Carts").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot ds : snapshot.getChildren()) {
                                        Cart cart = ds.getValue(Cart.class);
                                        if (cart.getUserId().equals(userId)) {
                                            FirebaseDatabase.getInstance().getReference().child("CartInfos").child(cart.getCartId()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    if (snapshot.getChildrenCount() == 0) {

                                                        startActivity(new Intent(HomeActivity.this, EmptyCartActivity.class));
                                                        return;
                                                    }
                                                    else {
                                                        Intent intent = new Intent(HomeActivity.this,CartActivity.class);
                                                        intent.putExtra("userId",userId);
                                                        startActivity(intent);
                                                        return;
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
                        return true;
                }
                return true;
            }
        });
    }
    private void setEventNavigationBottom() {
        binding.bottomNavigation.show(2,true);
        binding.bottomNavigation.add(new MeowBottomNavigation.Model(1,R.drawable.ic_favourite));
        binding.bottomNavigation.add(new MeowBottomNavigation.Model(2,R.drawable.ic_home));
        binding.bottomNavigation.add(new MeowBottomNavigation.Model(3,R.drawable.notification_icon));

        binding.bottomNavigation.setOnClickMenuListener(new Function1<MeowBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(MeowBottomNavigation.Model model) {
                Fragment fragment;
                View headerView = binding.navigationLeft.getHeaderView(0);
                ConstraintLayout headerOfNavigationLeft = (ConstraintLayout) headerView.findViewById(R.id.headerOfNavigationLeft);

                switch (model.getId())
                {
                    case 1:
                        fragment=new FavoriteFragment(userId);
                        getSupportFragmentManager().beginTransaction()
                                .replace(layoutMain.getId(),fragment)
                                .commit();
                        getWindow().setNavigationBarColor(Color.parseColor("#F64280"));
                        binding.bottomNavigation.setBackgroundBottomColor(Color.parseColor("#F64280"));
                        getWindow().setStatusBarColor(Color.parseColor("#F64280"));
                        headerOfNavigationLeft.setBackgroundColor(Color.parseColor("#F64280"));
                        break;
                    case 2:
                        fragment=new HomeFragment(userId);
                        getSupportFragmentManager().beginTransaction()
                                .replace(layoutMain.getId(),fragment)
                                .commit();
                        getWindow().setNavigationBarColor(Color.parseColor("#E8584D"));
                        binding.bottomNavigation.setBackgroundBottomColor(Color.parseColor("#E8584D"));
                        getWindow().setStatusBarColor(Color.parseColor("#E8584D"));
                        headerOfNavigationLeft.setBackgroundColor(Color.parseColor("#E8584D"));
                        break;
                    case 3:
                        fragment = new NotificationFragment(userId);
                        getSupportFragmentManager().beginTransaction()
                                .replace(layoutMain.getId(),fragment)
                                .commit();
                        getWindow().setNavigationBarColor(Color.parseColor("#00B7FF"));
                        binding.bottomNavigation.setBackgroundBottomColor(Color.parseColor("#00B7FF"));
                        getWindow().setStatusBarColor(Color.parseColor("#00B7FF"));
                        headerOfNavigationLeft.setBackgroundColor(Color.parseColor("#00B7FF"));
                        break;
                }
                return null;
            }
        });

        binding.bottomNavigation.setOnShowListener(new Function1<MeowBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(MeowBottomNavigation.Model model) {
                switch (model.getId())
                {
                    case 1:
                        Fragment fragment;
                        fragment=new FavoriteFragment(userId);
                        getSupportFragmentManager().beginTransaction()
                                .replace(layoutMain.getId(),fragment)
                                .commit();
                        getWindow().setNavigationBarColor(Color.parseColor("#F64280"));
                        binding.bottomNavigation.setBackgroundBottomColor(Color.parseColor("#F64280"));
                        getWindow().setStatusBarColor(Color.parseColor("#F64280"));
                        View headerView = binding.navigationLeft.getHeaderView(0);
                        ConstraintLayout headerOfNavigationLeft = (ConstraintLayout) headerView.findViewById(R.id.headerOfNavigationLeft);
                        headerOfNavigationLeft.setBackgroundColor(Color.parseColor("#F64280"));
                        break;
                }
                return null;
            }
        });
        binding.bottomNavigation.setOnShowListener(new Function1<MeowBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(MeowBottomNavigation.Model model) {
                switch (model.getId())
                {
                    case 2:
                        Fragment fragment;
                        fragment=new HomeFragment(userId);
                        getSupportFragmentManager().beginTransaction()
                                .replace(layoutMain.getId(),fragment)
                                .commit();
                        getWindow().setNavigationBarColor(Color.parseColor("#E8584D"));
                        binding.bottomNavigation.setBackgroundBottomColor(Color.parseColor("#E8584D"));
                        getWindow().setStatusBarColor(Color.parseColor("#E8584D"));
                        View headerView = binding.navigationLeft.getHeaderView(0);
                        ConstraintLayout headerOfNavigationLeft = (ConstraintLayout) headerView.findViewById(R.id.headerOfNavigationLeft);
                        headerOfNavigationLeft.setBackgroundColor(Color.parseColor("#E8584D"));
                        break;
                }
                return null;
            }
        });
        binding.bottomNavigation.setOnShowListener(new Function1<MeowBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(MeowBottomNavigation.Model model) {
                switch (model.getId())
                {
                    case 3:
                        Fragment fragment;
                        fragment = new NotificationFragment(userId);
                        getSupportFragmentManager().beginTransaction()
                                .replace(layoutMain.getId(),fragment)
                                .commit();
                        getWindow().setNavigationBarColor(Color.parseColor("#00B7FF"));
                        binding.bottomNavigation.setBackgroundBottomColor(Color.parseColor("#00B7FF"));
                        getWindow().setStatusBarColor(Color.parseColor("#00B7FF"));
                        View headerView = binding.navigationLeft.getHeaderView(0);
                        ConstraintLayout headerOfNavigationLeft = (ConstraintLayout) headerView.findViewById(R.id.headerOfNavigationLeft);
                        headerOfNavigationLeft.setBackgroundColor(Color.parseColor("#00B7FF"));
                        break;
                }
                return null;
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

    // Function to check and request permission.
    public void checkPermission(String permission, int requestCode)
    {
        if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_DENIED) {

            // Requesting the permission
            ActivityCompat.requestPermissions(this, new String[] { permission }, requestCode);
        }
        else {

        }
    }

    // This function is called when the user accepts or decline the permission.
    // Request Code is used to check which permission called this function.
    // This request code is provided when the user is prompt for permission.

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode,
                permissions,
                grantResults);

        if (requestCode == NOTIFICATION_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            }
            else {

            }
        }
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            }
            else {

            }
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.profileMenu:
                Intent intent = new Intent(this,ProfileActivity.class);
                intent.putExtra("userId",userId);
                startActivity(intent);
                break;
            case R.id.orderMenu:
                Intent intent1 = new Intent(this, OrderActivity.class);
                intent1.putExtra("userId",userId);
                startActivity(intent1);
                break;
            case R.id.myShopMenu:
                Intent intent2 = new Intent(this, MyShopActivity.class);
                intent2.putExtra("userId",userId);
                startActivity(intent2);
                break;
            case R.id.logoutMenu:
                AlertDialog.Builder builder=new AlertDialog.Builder(this);
                builder.setTitle("Notice");
                builder.setMessage("Thoát ứng dụng?");
                builder.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        System.exit(0);
                    }
                });
                builder.create().show();
                break;
        }
        binding.drawLayoutHome.close();
        return true;

    }
    public void loadInformationForNavigationBar()
    {
        // load number of notification not read in bottom navigation bar
        new FirebaseNotificationHelper(this).readNotification(userId, new FirebaseNotificationHelper.DataStatus() {
            @Override
            public void DataIsLoaded(List<Notification> notificationList,List<Notification> notificationListToNotify) {
                int count = 0;
                for (int i = 0;i<notificationList.size();i++)
                {
                    if (notificationList.get(i).isRead() == false)
                    {
                        count++;
                    }
                }
                if (count > 0 )
                {
                    binding.bottomNavigation.setCount(3, String.valueOf(count));
                }
                else if (count == 0)
                {
                    binding.bottomNavigation.clearCount(3);
                }

                for (int i = 0;i<notificationListToNotify.size(); i++)
                {
                    new FirebaseNotificationHelper(HomeActivity.this).notificationPush(notificationListToNotify.get(i),userId);
                }
            }

            @Override
            public void DataIsInserted() {

            }

            @Override
            public void DataIsUpdated() {

            }

            @Override
            public void DataIsDeleted() {

            }
        });

        new FirebaseUserInfoHelper(this).readUserInfo(userId, new FirebaseUserInfoHelper.DataStatus() {
            @Override
            public void DataIsLoaded(User user) {
                View headerView = binding.navigationLeft.getHeaderView(0);
                ShapeableImageView imgAvatarInNavigationBar = (ShapeableImageView) headerView.findViewById(R.id.imgAvatarInNavigationBar);
                TextView txtNameInNavigationBar = (TextView) headerView.findViewById(R.id.txtNameInNavigationBar);
                txtNameInNavigationBar.setText("Hi, "+ user.getUserName());
                Glide.with(HomeActivity.this)
                        .load(user.getAvatarURL())
                        .into(imgAvatarInNavigationBar);
            }

            @Override
            public void DataIsInserted() {

            }

            @Override
            public void DataIsUpdated() {

            }

            @Override
            public void DataIsDeleted() {

            }
        });



    }
}