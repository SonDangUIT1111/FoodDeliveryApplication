package com.example.fooddeliveryapplication.Helpers;

import androidx.annotation.NonNull;

import com.example.fooddeliveryapplication.Model.Notification;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class FirebaseNotification {
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private List<Notification> notificationList = new ArrayList<>();

    public interface DataStatus{
        void DataIsLoaded(List<Notification> notificationList);
        void DataIsInserted();
        void DataIsUpdated();
        void DataIsDeleted();
    }

    public FirebaseNotification() {
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference();
    }

    public void readNotification(String userId, final FirebaseNotification.DataStatus dataStatus)
    {
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                notificationList.clear();
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
                dataStatus.DataIsLoaded(notificationList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void addNotification(String userId,Notification notification, final FirebaseNotification.DataStatus dataStatus)
    {
        String key = mReference.child("Notification").child(userId).push().getKey();
        notification.setNotificationId(key);
        mReference.child("Notification").child(userId).child(key).setValue(notification)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        dataStatus.DataIsInserted();
                    }
                });
    }
    public void updateNotification(String userId,Notification notification, final FirebaseNotification.DataStatus dataStatus)
    {
        mReference.child("Notification").child(userId).child(notification.getNotificationId()).setValue(notification)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        dataStatus.DataIsUpdated();
                    }
                });
    }
}
