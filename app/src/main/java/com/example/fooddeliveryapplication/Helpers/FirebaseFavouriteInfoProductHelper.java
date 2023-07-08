package com.example.fooddeliveryapplication.Helpers;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FirebaseFavouriteInfoProductHelper {
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReferenceFavourite;


    public interface DataStatus{
        void DataIsLoaded(boolean isFavouriteExists, boolean isFavouriteDetailExists);
        void DataIsInserted();
        void DataIsUpdated();
        void DataIsDeleted();
    }

    public FirebaseFavouriteInfoProductHelper() {
        mDatabase = FirebaseDatabase.getInstance();
        mReferenceFavourite = mDatabase.getReference("Favorites");
    }
    public void readFavourite(String productId,String userId,final FirebaseFavouriteInfoProductHelper.DataStatus dataStatus)
    {
        mReferenceFavourite.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean isFavouriteExists = false;
                boolean isFavouriteDetailExists = false;
                if (snapshot.child(userId).exists())
                {
                    isFavouriteExists = true;
                    if (snapshot.child(userId).child(productId).exists())
                        isFavouriteDetailExists = true;
                }

                if (dataStatus != null) {
                    dataStatus.DataIsLoaded(isFavouriteExists,isFavouriteDetailExists);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void addFavourite(String userId,String productId, final FirebaseFavouriteInfoProductHelper.DataStatus dataStatus)
    {
        mReferenceFavourite.child(userId).child(productId).setValue(true)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        if (dataStatus != null) {
                            dataStatus.DataIsInserted();
                        }
                    }
                });
    }

    public void removeFavourite(String userId,String productId, final FirebaseFavouriteInfoProductHelper.DataStatus dataStatus)
    {
        mReferenceFavourite.child(userId).child(productId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                if (dataStatus != null)
                    dataStatus.DataIsDeleted();
                }
            });
    }
}