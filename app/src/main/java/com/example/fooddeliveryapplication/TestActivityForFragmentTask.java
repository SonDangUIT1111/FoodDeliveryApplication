package com.example.fooddeliveryapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.fooddeliveryapplication.Adapters.AdapterTest;
import com.example.fooddeliveryapplication.Helpers.FirebaseNotificationHelper;
import com.example.fooddeliveryapplication.Model.Notification;
import com.google.android.material.tabs.TabLayout;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TestActivityForFragmentTask extends AppCompatActivity {

    String userId;

    TabLayout tabLayoutTest;
    ViewPager2 viewPagerTest;
    AdapterTest adapterTest;
    Button btnAddNotification;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_for_fragment_task);
        //get input
        userId = "randomUserId1";

        // find view by id
        tabLayoutTest = (TabLayout) findViewById(R.id.tabLayoutTest);
        viewPagerTest = (ViewPager2) findViewById(R.id.viewPagerTest);
        btnAddNotification = (Button) findViewById(R.id.btnAddNotification);
        adapterTest = new AdapterTest(this,userId);
        viewPagerTest.setAdapter(adapterTest);




        // connect tab layout with view pager
        tabLayoutTest.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPagerTest.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
        viewPagerTest.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayoutTest.getTabAt(position).select();
            }
        });

        btnAddNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Notification notification = new Notification();
                notification.setRead(false);
                notification.setContent("New content");
                notification.setImageURL("https://img.freepik.com/free-photo/wide-angle-shot-single-tree-growing-clouded-sky-during-sunset-surrounded-by-grass_181624-22807.jpg?w=2000");
                notification.setTitle("New title");
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                String currentDateAndTime = sdf.format(new Date());
                notification.setTime(currentDateAndTime);
                new FirebaseNotificationHelper(TestActivityForFragmentTask.this).addNotification(userId, notification, new FirebaseNotificationHelper.DataStatus() {
                    @Override
                    public void DataIsLoaded(List<Notification> notificationList) {}

                    @Override
                    public void DataIsInserted() {
                        Toast.makeText(TestActivityForFragmentTask.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void DataIsUpdated() {}

                    @Override
                    public void DataIsDeleted() {}
                });
            }
        });
    }

}