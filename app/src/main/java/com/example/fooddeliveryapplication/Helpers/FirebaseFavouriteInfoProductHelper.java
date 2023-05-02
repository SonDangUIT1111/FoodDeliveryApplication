package com.example.fooddeliveryapplication.Helpers;

import androidx.annotation.NonNull;

import com.example.fooddeliveryapplication.Model.Cart;
import com.example.fooddeliveryapplication.Model.Favourite;
import com.example.fooddeliveryapplication.Model.FavouriteDetail;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FirebaseFavouriteInfoProductHelper {
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReferenceFavourite;


    public interface DataStatus{
        void DataIsLoaded(Favourite favourite,String keyFavourite,String keyFavouriteDetail,boolean isFavouriteExists, boolean isFavouriteDetailExists);
        void DataIsInserted();
        void DataIsUpdated();
        void DataIsDeleted();
    }

    public FirebaseFavouriteInfoProductHelper() {
        mDatabase = FirebaseDatabase.getInstance();
        mReferenceFavourite = mDatabase.getReference("Favourites");
    }
    public void readFavourite(String productId,String userId,final FirebaseFavouriteInfoProductHelper.DataStatus dataStatus)
    {
        mReferenceFavourite.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean isFavouriteExists = false;
                boolean isFavouriteDetailExists = false;
                String keyFavourite = "";
                String keyFavouriteDetail = "";
                Favourite favourite = new Favourite();
                for (DataSnapshot keyNode : snapshot.getChildren())
                {
                    if (keyNode.child("userId").getValue(String.class).equals(userId))
                    {
                        List<FavouriteDetail> details = new ArrayList<>();
                        DataSnapshot snapshotList = keyNode.child("favouriteList");
                        for (DataSnapshot keyChildNode : snapshotList.getChildren())
                        {
                            if (keyChildNode.child("productId").getValue(String.class).equals(productId)) {
                                isFavouriteDetailExists = true;
                                keyFavouriteDetail = keyChildNode.getKey();
                            }
                            FavouriteDetail detail = keyChildNode.getValue(FavouriteDetail.class);
                            details.add(detail);
                        }
                        favourite.favouriteList = details;
                        favourite.userName = keyNode.child("userName").getValue(String.class);
                        favourite.userId = keyNode.child("userId").getValue(String.class);
                        keyFavourite = keyNode.getKey();
                        isFavouriteExists = true;
                        break;
                    }
                    else continue;
                }
                dataStatus.DataIsLoaded(favourite,keyFavourite,keyFavouriteDetail,isFavouriteExists,isFavouriteDetailExists);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void addFavourite(Favourite favourite, final FirebaseArtToCartHelper.DataStatus dataStatus)
    {
        String key = mReferenceFavourite.push().getKey();
        mReferenceFavourite.child(key).setValue(favourite)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        dataStatus.DataIsInserted();
                    }
                });
    }

    public void updateFavourite(String keyFavourite,Favourite favourite, final FirebaseArtToCartHelper.DataStatus dataStatus)
    {
        mReferenceFavourite.child(keyFavourite).setValue(favourite).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                dataStatus.DataIsUpdated();
            }
        });
    }
}
