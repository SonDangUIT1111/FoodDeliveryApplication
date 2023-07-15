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
                    if (notificationList.get(i).isNotified() == false) {
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
//    public void notificationPush(Notification notification) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
////            NotificationChannel channel = new NotificationChannel("1","notification", NotificationManager.IMPORTANCE_HIGH);
////            NotificationManager notificationManager = mContext.getSystemService(NotificationManager.class);
////            notificationManager.createNotificationChannel(channel);
//
//        }
//        final String CHANNEL_ID = "1";
//
//
//        Bitmap largeIcon = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.bkg);
//
//
//
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, CHANNEL_ID)
//                .setSmallIcon(R.drawable.bkg)
//                .setContentTitle("Food services")
//                .setContentText(notification.getTitle())
//                .setStyle(new NotificationCompat.BigTextStyle()
//                        .setBigContentTitle(notification.getTitle())
//                        .bigText(notification.getContent()))
//                .setLargeIcon(largeIcon)
//                .setColor(Color.RED)
//                .setAutoCancel(true)
//                .setPriority(NotificationCompat.PRIORITY_HIGH);
//
//        Intent intent = new Intent();
//        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
//        if (!notification.isRead())
//        {
//            notification.setRead(true);
//            new FirebaseNotificationHelper(mContext).updateNotification(userId, notification, new FirebaseNotificationHelper.DataStatus() {
//                @Override
//                public void DataIsLoaded(List<Notification> notificationList, List<Notification> notificationListToNotify) {
//
//                }
//
//                @Override
//                public void DataIsInserted() {
//
//                }
//
//                @Override
//                public void DataIsUpdated() {
//
//                }
//
//                @Override
//                public void DataIsDeleted() {
//
//                }
//            });
//        }
//
//        if (!notification.getBillId().equals("None"))
//        {
//            Bill bill = new Bill();
//            bill.setBillId(notification.getBillId());
//            intent = new Intent(mContext, OrderDetailActivity.class);
//            intent.putExtra("Bill", bill);
//            intent.putExtra("userId",userId);
//            mContext.startActivity(intent);
//        }
//        else if (!notification.getProductId().equals("None"))
//        {
//            final String[] userName = new String[1];
//            FirebaseDatabase.getInstance().getReference().child("Users").child(userId).addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    userName[0] = snapshot.child("userName").getValue(String.class);
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });
//            new FirebaseProductInfoHelper(notification.getProductId()).readInformationById(new FirebaseProductInfoHelper.DataStatusInformationOfProduct() {
//                @Override
//                public void DataIsLoaded(Product item) {
//                    Intent intent = new Intent(mContext, ProductInfoActivity.class);
//                    intent.putExtra("productId", item.getProductId());
//                    intent.putExtra("productName", item.getProductName());
//                    intent.putExtra("productPrice", item.getProductPrice());
//                    intent.putExtra("productImage1", item.getProductImage1());
//                    intent.putExtra("productImage2", item.getProductImage2());
//                    intent.putExtra("productImage3", item.getProductImage3());
//                    intent.putExtra("productImage4", item.getProductImage4());
//                    intent.putExtra("ratingStar", item.getRatingStar());
//                    intent.putExtra("productDescription", item.getDescription());
//                    intent.putExtra("publisherId", item.getPublisherId());
//                    intent.putExtra("sold", item.getSold());
//                    intent.putExtra("productType", item.getProductType());
//                    intent.putExtra("remainAmount", item.getRemainAmount());
//                    intent.putExtra("ratingAmount", item.getRatingAmount());
//                    intent.putExtra("state", item.getState());
//                    intent.putExtra("userId", userId);
//                    intent.putExtra("userName", userName);
//                    mContext.startActivity(intent);
//                }
//
//                @Override
//                public void DataIsInserted() {
//
//                }
//
//                @Override
//                public void DataIsUpdated() {
//
//                }
//
//                @Override
//                public void DataIsDeleted() {
//
//                }
//            });
//        }
//        else if (!notification.getConfirmId().equals("None"))
//        {
//            intent = new Intent(mContext, DeliveryManagementActivity.class);
//            intent.putExtra("userId",userId);
//            mContext.startActivity(intent);
//        }
//        else if (notification.getPublisher() != null) {
//            intent = new Intent(mContext, ChatDetailActivity.class);
//            intent.setAction("chatActivity");
//            intent.putExtra("publisher", notification.getPublisher());
//            mContext.startActivity(intent);
//        }
//
//        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_MUTABLE);
//        builder.setContentIntent(pendingIntent);
//
//        NotificationManager notificationManager = mContext.getSystemService(NotificationManager.class);
//        notificationManager.notify(getNotificationId(), builder.build());
//    }

    public void notificationPush(Notification notification) {
        String channelID = "CHANNEL_ID_NOTIFICATION";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, channelID);
        Bitmap largeIcon = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.bkg);
        builder.setSmallIcon(R.drawable.bkg)
                .setContentTitle("Food services")
                .setContentText(notification.getTitle())
                .setStyle(new NotificationCompat.BigTextStyle().setBigContentTitle(notification.getTitle()).bigText(notification.getContent()))
                .setLargeIcon(largeIcon)
                .setColor(Color.RED)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        if (!notification.isRead()) {
            notification.setRead(true);
            new FirebaseNotificationHelper(mContext).updateNotification(userId, notification, new FirebaseNotificationHelper.DataStatus() {
                @Override
                public void DataIsLoaded(List<Notification> notificationList, List<Notification> notificationListToNotify) {

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

        if (!notification.getBillId().equals("None")) {
            Bill bill = new Bill();
            bill.setBillId(notification.getBillId());
            intent = new Intent(mContext, OrderDetailActivity.class);
            intent.putExtra("Bill", bill);
            intent.putExtra("userId",userId);
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
                    Intent intent = new Intent(mContext, ProductInfoActivity.class);
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
            intent = new Intent(mContext, DeliveryManagementActivity.class);
            intent.putExtra("userId",userId);
        }
        else if (notification.getPublisher() != null) {
            intent = new Intent(mContext, ChatDetailActivity.class);
            intent.setAction("chatActivity");
            intent.putExtra("publisher", notification.getPublisher());
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_MUTABLE);
        builder.setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = notificationManager.getNotificationChannel(channelID);
            if (notificationChannel != null) {
                int importance = NotificationManager.IMPORTANCE_HIGH;
                notificationChannel = new NotificationChannel(channelID, "Some description", importance);
                notificationChannel.setLightColor(Color.GREEN);
                notificationChannel.enableVibration(true);
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }

        notificationManager.notify(0, builder.build());
    }

    public static Notification createNotification(String title, String content, String imageNotificationURL, String productId, String billId, String confirmId, User publisher)
    {
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