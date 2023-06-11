package com.example.fooddeliveryapplication.Activities.Home;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;

import com.example.fooddeliveryapplication.Adapters.Cart_PlaceOrder.CartActivity;
import com.example.fooddeliveryapplication.Adapters.Cart_PlaceOrder.EmptyCartActivity;
import com.example.fooddeliveryapplication.Activities.MyShop.MyShopActivity;
import com.example.fooddeliveryapplication.Activities.Order.OrderActivity;
import com.example.fooddeliveryapplication.Fragments.Home.FavoriteFragment;
import com.example.fooddeliveryapplication.Fragments.Home.HistoryFragment;
import com.example.fooddeliveryapplication.Fragments.Home.HomeFragment;
import com.example.fooddeliveryapplication.Fragments.NotificationFragment;
import com.example.fooddeliveryapplication.Helpers.FirebaseNotificationHelper;
import com.example.fooddeliveryapplication.Model.Cart;
import com.example.fooddeliveryapplication.Model.Notification;
import com.example.fooddeliveryapplication.R;
import com.example.fooddeliveryapplication.databinding.ActivityHomeBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;


import java.util.List;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private String userId;
    private FirebaseUser firebaseUser;
    FirebaseDatabase database= FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = database.getReference();
    ActivityHomeBinding binding;
    private LinearLayout layoutMain;

    private static final int NOTIFICATION_PERMISSION_CODE = 10023;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Todo take information of user just login here using firebaseuser.getuid() whatever ...
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // request permission here
        checkPermission("android.permission.POST_NOTIFICATIONS",NOTIFICATION_PERMISSION_CODE);
        //----------
        initUI();
        //----------------------

        //----------------------


        // load number of notification not read in bottom navigation bar
        new FirebaseNotificationHelper(this).readNotification(userId, new FirebaseNotificationHelper.DataStatus() {
            @Override
            public void DataIsLoaded(List<Notification> notificationList) {
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
                    binding.navigationBottom.showBadge(R.id.notification_menu,count);
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
    }




    private void initUI() {
        binding.navigationLeft.bringToFront();
        createActionBar();

        layoutMain=binding.layoutMain;
        getSupportFragmentManager()
                .beginTransaction()
                .add(layoutMain.getId(),new HomeFragment(userId))
                .commit();
        binding.navigationBottom.setItemSelected(R.id.home_menu,true);
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
                                                }
                                                else {
                                                    Intent intent = new Intent(HomeActivity.this,CartActivity.class);
                                                    intent.putExtra("userId",userId);
                                                    startActivity(intent);
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
                    case R.id.notification_menu:
                        fragment = new NotificationFragment(userId);
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
        // there something other permission require here

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
}