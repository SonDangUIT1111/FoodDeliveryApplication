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

    public interface DataStatus{
        void DataIsLoaded(Cart cart, String keyCart, String keyProduct,boolean isExistsCart,boolean isExistsProduct);
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


    public void readCarts(String userId,String productId,final DataStatus dataStatus)
    {

        mReferenceCart.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String keyProduct = "";
                String keyCart = "";
                Cart cart = new Cart();
                List<CartInfo> cartInfos = new ArrayList<>();
                boolean isExistsCart = false;
                boolean isExistsProduct = false;
                for (DataSnapshot keyNode : snapshot.getChildren())
                {
                    if (keyNode.child("userId").getValue(String.class).equals(userId))
                    {
                        DataSnapshot snapShotList = keyNode.child("cartInfos");
                        for (DataSnapshot snapShotChild :snapShotList.getChildren()){
                            if (snapShotChild.child("productId").getValue(String.class).equals(productId))
                            {
                                keyProduct = snapShotChild.getKey();
                                isExistsProduct = true;
                            }
                            CartInfo cartInfo = snapShotChild.getValue(CartInfo.class);
                            cartInfos.add(cartInfo);
                        }
                        isExistsCart = true;
                        keyCart = keyNode.getKey();
                        cart.cartInfos = cartInfos;
                        cart.cartId = keyNode.child("cartId").getValue(String.class);
                        cart.userName = keyNode.child("userName").getValue(String.class);
                        cart.userId = keyNode.child("userId").getValue(String.class);
                        cart.totalPrice = keyNode.child("totalPrice").getValue(int.class);
                        cart.totalAmount = keyNode.child("totalAmount").getValue(int.class);
                        break;
                    }
                }
                dataStatus.DataIsLoaded(cart,keyCart,keyProduct,isExistsCart,isExistsProduct);
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
    public void updateCart(String keyCart,Cart cart, final DataStatus dataStatus)
    {
        mReferenceCart.child(keyCart).setValue(cart).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                dataStatus.DataIsUpdated();
            }
        });
    }
}
