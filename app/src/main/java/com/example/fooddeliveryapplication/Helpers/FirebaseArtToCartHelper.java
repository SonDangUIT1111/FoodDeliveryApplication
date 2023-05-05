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
    String userId;
    String productId;

    public interface DataStatus{
        void DataIsLoaded(Cart cart,CartInfo cartInfo,boolean isExistsCart,boolean isExistsProduct);
        void DataIsInserted();
        void DataIsUpdated();
        void DataIsDeleted();
    }

    public FirebaseArtToCartHelper(String user, String product) {
        userId = user;
        productId = product;
        mDatabase = FirebaseDatabase.getInstance();
        mReferenceCart = mDatabase.getReference();
    }
    public FirebaseArtToCartHelper() {
        mDatabase = FirebaseDatabase.getInstance();
        mReferenceCart = mDatabase.getReference();
    }


    public void readCarts(final DataStatus dataStatus)
    {

        mReferenceCart.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean isExistsCart = false;
                boolean isExistsProduct = false;
                Cart cart = new Cart();
                CartInfo cartInfo = new CartInfo();
                for (DataSnapshot keyNode: snapshot.child("Carts").getChildren())
                {
                    if (keyNode.child("userId").getValue(String.class).equals(userId))
                    {
                        isExistsCart = true;
                        cart = keyNode.getValue(Cart.class);
                        break;
                    }
                }
                if (isExistsCart == true)
                {
                    for (DataSnapshot keyNode: snapshot.child("CartInfos").child(cart.getCartId()).getChildren())
                    {
                        if (keyNode.child("productId").getValue(String.class).equals(productId))
                        {
                            isExistsProduct = true;
                            cartInfo = keyNode.getValue(CartInfo.class);
                            break;
                        }
                    }
                }

                dataStatus.DataIsLoaded(cart,cartInfo,isExistsCart,isExistsProduct);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void addCarts(Cart cart,CartInfo cartInfo, final DataStatus dataStatus)
    {
        String key = mReferenceCart.child("Carts").push().getKey();
        cart.setCartId(key);
        mReferenceCart.child("Carts").child(key).setValue(cart)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        dataStatus.DataIsInserted();
                    }
                });
        String keyInfo = mReferenceCart.child("CartInfos").child(cart.getCartId()).push().getKey();
        cartInfo.setCartInfoId(keyInfo);
        mReferenceCart.child("CartInfos").child(cart.getCartId()).child(keyInfo).setValue(cartInfo)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        dataStatus.DataIsInserted();
                    }
                });
    }
    public void updateCart(Cart cart,CartInfo cartInfo,boolean isExistsProduct, final DataStatus dataStatus)
    {
        mReferenceCart.child("Carts").child(cart.getCartId()).setValue(cart).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                dataStatus.DataIsUpdated();
            }
        });
        if (isExistsProduct)
        {
            mReferenceCart.child("CartInfos").child(cart.getCartId()).child(cartInfo.getCartInfoId()).setValue(cartInfo)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            dataStatus.DataIsUpdated();
                        }
                    });
        }
        else {
            String key = mReferenceCart.child("CartInfos").child(cart.getCartId()).push().getKey();
            cartInfo.setCartInfoId(key);
            mReferenceCart.child("CartInfos").child(cart.getCartId()).child(key).setValue(cartInfo)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            dataStatus.DataIsInserted();
                        }
                    });
        }
    }
}
