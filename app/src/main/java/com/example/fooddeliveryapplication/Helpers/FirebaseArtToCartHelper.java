package com.example.fooddeliveryapplication.Helpers;

import androidx.annotation.NonNull;

import com.example.fooddeliveryapplication.Model.Cart;
import com.example.fooddeliveryapplication.Model.CartInfo;
import com.example.fooddeliveryapplication.Model.Comment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FirebaseArtToCartHelper {
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReferenceCart;
    private List<Cart> carts = new ArrayList<>();

    public interface DataStatus{
        void DataIsLoaded(List<Cart> carts, List<String> keys);
        void DataIsInserted();
        void DataIsUpdated();
        void DataIsDeleted();
    }

    public FirebaseArtToCartHelper() {
        mDatabase = FirebaseDatabase.getInstance();
        mReferenceCart = mDatabase.getReference("Carts");
    }
    public FirebaseArtToCartHelper(String path) {
        mDatabase = FirebaseDatabase.getInstance();
        mReferenceCart = mDatabase.getReference(path);
    }


    public void readCarts(final DataStatus dataStatus)
    {
        mReferenceCart.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                carts.clear();
                List<String> keys = new ArrayList<>();
                for (DataSnapshot keyNode : snapshot.getChildren())
                {
                    keys.add(keyNode.getKey());
                    Cart cart = keyNode.getValue(Cart.class);
                    carts.add(cart);

                }
                dataStatus.DataIsLoaded(carts,keys);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void addCarts(Cart cart, final DataStatus dataStatus)
    {
        String key = mReferenceCart.push().getKey();
        mReferenceCart.child(key).setValue(cart)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        dataStatus.DataIsInserted();
                    }
                });
    }
}
