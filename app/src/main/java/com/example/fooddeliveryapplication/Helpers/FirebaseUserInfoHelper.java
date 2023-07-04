package com.example.fooddeliveryapplication.Helpers;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.fooddeliveryapplication.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class FirebaseUserInfoHelper {
    private Context mContext;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    public interface DataStatus{
        void DataIsLoaded(User user);
        void DataIsInserted();
        void DataIsUpdated();
        void DataIsDeleted();
    }
    public FirebaseUserInfoHelper(Context context) {
        mContext = context;
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference();
    }
    public void readUserInfo(String userId, final FirebaseUserInfoHelper.DataStatus dataStatus)
    {
        mReference.child("Users").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (dataStatus != null) {
                    dataStatus.DataIsLoaded(user);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
