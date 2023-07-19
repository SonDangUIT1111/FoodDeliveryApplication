package com.example.fooddeliveryapplication.Activities.Home;

import static android.icu.util.ULocale.getName;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.fooddeliveryapplication.Activities.Cart_PlaceOrder.CartActivity;
import com.example.fooddeliveryapplication.Activities.Cart_PlaceOrder.EmptyCartActivity;
import com.example.fooddeliveryapplication.Activities.MyShop.MyShopActivity;
import com.example.fooddeliveryapplication.Activities.Order.OrderActivity;
import com.example.fooddeliveryapplication.Activities.Order.OrderDetailActivity;
import com.example.fooddeliveryapplication.Activities.OrderSellerManagement.DeliveryManagementActivity;
import com.example.fooddeliveryapplication.Activities.ProductInformation.ProductInfoActivity;
import com.example.fooddeliveryapplication.CustomMessageBox.CustomAlertDialog;
import com.example.fooddeliveryapplication.CustomMessageBox.SuccessfulToast;
import com.example.fooddeliveryapplication.Fragments.Home.FavoriteFragment;
import com.example.fooddeliveryapplication.Fragments.Home.HomeFragment;
import com.example.fooddeliveryapplication.Fragments.NotificationFragment;
import com.example.fooddeliveryapplication.Helpers.FirebaseNotificationHelper;
import com.example.fooddeliveryapplication.Helpers.FirebaseProductInfoHelper;
import com.example.fooddeliveryapplication.Helpers.FirebaseUserInfoHelper;
import com.example.fooddeliveryapplication.Model.Bill;
import com.example.fooddeliveryapplication.Model.Cart;
import com.example.fooddeliveryapplication.Model.Notification;
import com.example.fooddeliveryapplication.Model.Product;
import com.example.fooddeliveryapplication.Model.User;
import com.example.fooddeliveryapplication.R;
import com.example.fooddeliveryapplication.databinding.ActivityHomeBinding;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private String userId;
    private ActivityHomeBinding binding;
    private LinearLayout layoutMain;
    private Fragment selectionFragment;

    private static final int NOTIFICATION_PERMISSION_CODE = 10023;
    private static final int STORAGE_PERMISSION_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Request permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(HomeActivity.this,
                    Manifest.permission.POST_NOTIFICATIONS) !=
                    PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 101);
            }

            if (ContextCompat.checkSelfPermission(HomeActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                    PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 102);
            }

            if (ContextCompat.checkSelfPermission(HomeActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) !=
                    PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 103);
            }
        }

        initUI();
        loadInformationForNavigationBar();
    }


    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    private void initUI() {
        getWindow().setStatusBarColor(Color.parseColor("#E8584D"));
        getWindow().setNavigationBarColor(Color.parseColor("#E8584D"));
        binding.navigationLeft.bringToFront();
        createActionBar();

        layoutMain=binding.layoutMain;
        getSupportFragmentManager()
                .beginTransaction()
                .replace(layoutMain.getId(),new HomeFragment(userId))
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
                    case R.id.message_menu:
                        Intent intent=new Intent(HomeActivity.this,ChatActivity.class);
                        intent.putExtra("userId",userId);
                        startActivity(intent);
                        break;
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
        binding.bottomNavigation.show(2, true);
        binding.bottomNavigation.add(new MeowBottomNavigation.Model(1, R.drawable.ic_favourite));
        binding.bottomNavigation.add(new MeowBottomNavigation.Model(2, R.drawable.ic_home));
        binding.bottomNavigation.add(new MeowBottomNavigation.Model(3, R.drawable.notification_icon));

        binding.bottomNavigation.setOnClickMenuListener(model -> {
            switch (model.getId()) {
                case 1:
                    selectionFragment = new FavoriteFragment(userId);
                    break;
                case 2:
                    selectionFragment = new HomeFragment(userId);
                    break;
                case 3:
                    selectionFragment = new NotificationFragment(userId);
                    break;
            }

            if (selectionFragment != null) {
                getSupportFragmentManager().beginTransaction().replace(layoutMain.getId(), selectionFragment).commit();
            }

            return null;
        });

        binding.bottomNavigation.setOnShowListener(model -> {
            switch (model.getId()) {
                case 1:
                    selectionFragment = new FavoriteFragment(userId);
                    break;
                case 2:
                    selectionFragment = new HomeFragment(userId);
                    break;
                case 3:
                    selectionFragment = new NotificationFragment(userId);
                    break;
            }

            if (selectionFragment != null) {
                getSupportFragmentManager().beginTransaction().replace(layoutMain.getId(), selectionFragment).commit();
            }

            return null;
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
        if (item.getItemId() == android.R.id.home) {
            binding.drawLayoutHome.openDrawer(GravityCompat.START);
        }

        return super.onOptionsItemSelected(item);
    }

    // Function to check and request permission.
    public void checkPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_DENIED) {
            // Requesting the permission
            ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
        } else {
            // Permission already granted
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
                new CustomAlertDialog(HomeActivity.this,"Do you want to logout?");
                CustomAlertDialog.binding.btnYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new SuccessfulToast(HomeActivity.this, "Logout successfully!").showToast();
                        CustomAlertDialog.alertDialog.dismiss();
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(HomeActivity.this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                        finish();
                    }
                });
                CustomAlertDialog.binding.btnNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CustomAlertDialog.alertDialog.dismiss();
                    }
                });
                CustomAlertDialog.showAlertDialog();
                break;
        }
        binding.drawLayoutHome.close();
        return true;

    }
    public void loadInformationForNavigationBar() {
        // load number of notification not read in bottom navigation bar
        new FirebaseNotificationHelper(this).readNotification(userId, new FirebaseNotificationHelper.DataStatus() {
            @Override
            public void DataIsLoaded(List<Notification> notificationList, List<Notification> notificationListToNotify) {
                int count = 0;
                for (int i = 0; i<notificationList.size(); i++) {
                    if (!notificationList.get(i).isRead()) {
                        count++;
                    }
                }
                if (count > 0) {
                    binding.bottomNavigation.setCount(3, String.valueOf(count));
                }
                else if (count == 0) {
                    binding.bottomNavigation.clearCount(3);
                }

                for (int i = 0; i < notificationListToNotify.size(); i++) {
                    makeNotification(notificationListToNotify.get(i));
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
                txtNameInNavigationBar.setText("Hi, " + getLastName(user.getUserName()));
                Glide.with(HomeActivity.this).load(user.getAvatarURL()).placeholder(R.drawable.default_avatar).into(imgAvatarInNavigationBar);
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

    private String getLastName(String userName) {
        userName = userName.trim();
        String[] output = userName.split(" ");
        return output[output.length - 1];
    }

    private void makeNotification(Notification notification) {
        String channelId = "CHANNEL_ID_NOTIFICATION";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channelId);
        Bitmap largeIcon = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.bkg);
        builder.setSmallIcon(R.drawable.bkg)
                .setContentTitle("Food services")
                .setContentText(notification.getTitle())
                .setStyle(new NotificationCompat.BigTextStyle()
                        .setBigContentTitle(notification.getTitle())
                        .bigText(notification.getContent()))
                .setLargeIcon(largeIcon)
                .setColor(Color.RED)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        if (!notification.getBillId().equals("None")) {
            Bill bill = new Bill();
            bill.setBillId(notification.getBillId());
            Intent intent = new Intent(getApplicationContext(), OrderDetailActivity.class);
            intent.putExtra("Bill", bill);
            intent.putExtra("userId", userId);
            intent.putExtra("notification", notification);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_MUTABLE);
            builder.setContentIntent(pendingIntent);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel notificationChannel = notificationManager.getNotificationChannel(channelId);
                if (notificationChannel == null) {
                    int importance = NotificationManager.IMPORTANCE_HIGH;
                    notificationChannel = new NotificationChannel(channelId, "Some description", importance);
                    notificationChannel.setLightColor(Color.GREEN);
                    notificationChannel.enableVibration(true);
                    notificationManager.createNotificationChannel(notificationChannel);
                }
            }

            notificationManager.notify(0, builder.build());
        }
        else if (!notification.getProductId().equals("None")) {
            final String[] userName = new String[1];
            FirebaseDatabase.getInstance().getReference().child("Users").child(userId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    userName[0] = snapshot.child("userName").getValue(String.class);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            new FirebaseProductInfoHelper(notification.getProductId()).readInformationById(new FirebaseProductInfoHelper.DataStatusInformationOfProduct() {
                @Override
                public void DataIsLoaded(Product item) {
                    Intent intent = new Intent(getApplicationContext(), ProductInfoActivity.class);
                    intent.putExtra("productId", item.getProductId());
                    intent.putExtra("productName", item.getProductName());
                    intent.putExtra("productPrice", item.getProductPrice());
                    intent.putExtra("productImage1", item.getProductImage1());
                    intent.putExtra("productImage2", item.getProductImage2());
                    intent.putExtra("productImage3", item.getProductImage3());
                    intent.putExtra("productImage4", item.getProductImage4());
                    intent.putExtra("ratingStar", item.getRatingStar());
                    intent.putExtra("productDescription", item.getDescription());
                    intent.putExtra("publisherId", item.getPublisherId());
                    intent.putExtra("sold", item.getSold());
                    intent.putExtra("productType", item.getProductType());
                    intent.putExtra("remainAmount", item.getRemainAmount());
                    intent.putExtra("ratingAmount", item.getRatingAmount());
                    intent.putExtra("state", item.getState());
                    intent.putExtra("userId", userId);
                    intent.putExtra("userName", userName);
                    intent.putExtra("notification", notification);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_MUTABLE);
                    builder.setContentIntent(pendingIntent);
                    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        NotificationChannel notificationChannel = notificationManager.getNotificationChannel(channelId);
                        if (notificationChannel == null) {
                            int importance = NotificationManager.IMPORTANCE_HIGH;
                            notificationChannel = new NotificationChannel(channelId, "Some description", importance);
                            notificationChannel.setLightColor(Color.GREEN);
                            notificationChannel.enableVibration(true);
                            notificationManager.createNotificationChannel(notificationChannel);
                        }
                    }

                    notificationManager.notify(0, builder.build());
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
        else if (!notification.getConfirmId().equals("None")) {
            Intent intent = new Intent(getApplicationContext(), DeliveryManagementActivity.class);
            intent.putExtra("userId", userId);
            intent.putExtra("notification", notification);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_MUTABLE);
            builder.setContentIntent(pendingIntent);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel notificationChannel = notificationManager.getNotificationChannel(channelId);
                if (notificationChannel == null) {
                    int importance = NotificationManager.IMPORTANCE_HIGH;
                    notificationChannel = new NotificationChannel(channelId, "Some description", importance);
                    notificationChannel.setLightColor(Color.GREEN);
                    notificationChannel.enableVibration(true);
                    notificationManager.createNotificationChannel(notificationChannel);
                }
            }

            notificationManager.notify(0, builder.build());
        }
        else if (notification.getPublisher() != null) {
            Intent intent = new Intent(getApplicationContext(), ChatDetailActivity.class);
            intent.setAction("homeActivity");
            intent.putExtra("notification", notification);
            intent.putExtra("publisher", notification.getPublisher());
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel notificationChannel = notificationManager.getNotificationChannel(channelId);
                if (notificationChannel == null) {
                    int importance = NotificationManager.IMPORTANCE_HIGH;
                    notificationChannel = new NotificationChannel(channelId, "Some description", importance);
                    notificationChannel.setLightColor(Color.GREEN);
                    notificationChannel.enableVibration(true);
                    notificationManager.createNotificationChannel(notificationChannel);
                }
            }

            notificationManager.notify(0, builder.build());
        }
    }
}