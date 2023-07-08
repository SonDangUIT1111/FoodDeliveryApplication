package com.example.fooddeliveryapplication.Helpers;

import androidx.annotation.NonNull;

import com.example.fooddeliveryapplication.Model.Product;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class FirebaseFavouriteUserHelper {
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReferenceFavourite;
    private ArrayList<String> keyProducts;
    private ArrayList<Product> favouriteList;


    public interface DataStatus{
        void DataIsLoaded(ArrayList<Product> favouriteProducts,ArrayList<String> keys);
        void DataIsInserted();
        void DataIsUpdated();
        void DataIsDeleted();
    }

    public FirebaseFavouriteUserHelper() {
        mDatabase = FirebaseDatabase.getInstance();
        mReferenceFavourite = mDatabase.getReference();
    }
    public void readFavouriteList(String userId,final FirebaseFavouriteUserHelper.DataStatus dataStatus)
    {
        mReferenceFavourite.child("Favorites").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                keyProducts = new ArrayList<>();
                favouriteList = new ArrayList<>();
                for (DataSnapshot keyNode: snapshot.getChildren())
                {
                    keyProducts.add(keyNode.getKey());
                }
                readProductInfo(keyProducts,dataStatus);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void readProductInfo(ArrayList<String> keys, FirebaseFavouriteUserHelper.DataStatus dataStatus)
    {
        mReferenceFavourite.child("Products").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (int i = 0; i < keys.size(); i++) {
                    Product product = snapshot.child(keys.get(i)).getValue(Product.class);
                    favouriteList.add(product);
                }

                if (dataStatus != null) {
                    dataStatus.DataIsLoaded(favouriteList,keyProducts);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
