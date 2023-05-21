package com.example.fooddeliveryapplication.Helpers;

import androidx.annotation.NonNull;

import com.example.fooddeliveryapplication.Model.Notification;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
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
                    notification.setRead(snap.child("isRead").getValue(Boolean.class));
                    notificationList.add(notification);
                }
                dataStatus.DataIsLoaded(notificationList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
