package com.example.fooddeliveryapplication.Helpers;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.fooddeliveryapplication.Activities.Home.ChatDetailActivity;
import com.example.fooddeliveryapplication.Activities.Order.OrderDetailActivity;
import com.example.fooddeliveryapplication.Activities.OrderSellerManagement.DeliveryManagementActivity;
import com.example.fooddeliveryapplication.Activities.ProductInformation.ProductInfoActivity;
import com.example.fooddeliveryapplication.Model.Bill;
import com.example.fooddeliveryapplication.Model.Notification;
import com.example.fooddeliveryapplication.Model.Product;
import com.example.fooddeliveryapplication.Model.User;
import com.example.fooddeliveryapplication.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class FirebaseNotificationHelper {
    private Context mContext;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private List<Notification> notificationList = new ArrayList<>();
    private List<Notification> notificationListToNotify = new ArrayList<>();

    public interface DataStatus{
        void DataIsLoaded(List<Notification> notificationList,List<Notification> notificationListToNotify);
        void DataIsInserted();
        void DataIsUpdated();
        void DataIsDeleted();
    }

    public FirebaseNotificationHelper(Context context) {
        mContext = context;
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference();
    }

    public void readNotification (String userId, final FirebaseNotificationHelper.DataStatus dataStatus)
    {
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                notificationList.clear();
                notificationListToNotify.clear();
                DataSnapshot snapchild = snapshot.child("Notification").child(userId);
                for (DataSnapshot snap:snapchild.getChildren())
                {
                    Notification notification = snap.getValue(Notification.class);
                    notificationList.add(notification);
                }

                // sort notification by date
                Collections.sort(notificationList, new Comparator<Notification>() {
                    @Override
                    public int compare(Notification o1, Notification o2) {
                        if (o1.getTime() == null || o2.getTime() == null)
                            return 0;
                        return o2.getTime().compareTo(o1.getTime());
                    }
                });
                for (int i = 0;i < notificationList.size(); i++)
                {
                    if (!notificationList.get(i).isNotified()) {
                        notificationListToNotify.add(notificationList.get(i));
                        mReference.child("Notification").child(userId).child(notificationList.get(i).getNotificationId()).child("notified").setValue(true)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {

                                    }
                                });
                    }
                }

                if (dataStatus != null) {
                    dataStatus.DataIsLoaded(notificationList, notificationListToNotify);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void addNotification(String userId,Notification notification, final FirebaseNotificationHelper.DataStatus dataStatus) {
        String key = mReference.child("Notification").child(userId).push().getKey();
        notification.setNotificationId(key);
        mReference.child("Notification").child(userId).child(key).setValue(notification)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        if (dataStatus != null) {
                            dataStatus.DataIsInserted();
                        }
                    }
                });
    }
    public void updateNotification(String userId,Notification notification, final FirebaseNotificationHelper.DataStatus dataStatus) {
        mReference.child("Notification").child(userId).child(notification.getNotificationId()).setValue(notification)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        if (dataStatus != null) {
                            dataStatus.DataIsUpdated();
                        }
                    }
                });
    }

    public static Notification createNotification(String title, String content, String imageNotificationURL, String productId, String billId, String confirmId, User publisher) {
        Notification notification = new Notification();
        notification.setRead(false);
        notification.setNotified(false);
        notification.setContent(content);
        notification.setImageURL(imageNotificationURL);
        notification.setTitle(title);
        notification.setProductId(productId);
        notification.setBillId(billId);
        notification.setConfirmId(confirmId);
        notification.setPublisher(publisher);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String currentDateAndTime = sdf.format(new Date());
        notification.setTime(currentDateAndTime);

        return notification;
    }
}