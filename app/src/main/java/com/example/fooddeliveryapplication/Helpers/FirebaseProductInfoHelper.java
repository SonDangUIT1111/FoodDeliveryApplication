package com.example.fooddeliveryapplication.Helpers;

import androidx.annotation.NonNull;

import com.example.fooddeliveryapplication.Model.Comment;
import com.example.fooddeliveryapplication.Model.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FirebaseProductInfoHelper {
    private FirebaseDatabase mDatabase;
    private  DatabaseReference mReference;
    private String productId;
    private List<Comment> comments = new ArrayList<>();


    public interface DataStatus{
        void DataIsLoaded(List<Comment> comments, int countRate,List<String> keys);
        void DataIsInserted();
        void DataIsUpdated();
        void DataIsDeleted();

    }

    public interface DataStatusCountFavourite{
        void DataIsLoaded(int countFavourite);
        void DataIsInserted();
        void DataIsUpdated();
        void DataIsDeleted();

    }
    public interface DataStatusInformationOfProduct{
        void DataIsLoaded(Product product);
        void DataIsInserted();
        void DataIsUpdated();
        void DataIsDeleted();

    }
    public FirebaseProductInfoHelper(String productBranch){
        productId = productBranch;
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference();
    }


    public void readComments(final DataStatus dataStatus)
    {
        mReference.child("Comments").child(productId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                comments.clear();
                List<String> keys = new ArrayList<>();
                for (DataSnapshot keyNode : snapshot.getChildren())
                {
                    comments.add(keyNode.getValue(Comment.class));
                    keys.add(keyNode.getKey());
                }
                int rate = comments.size();
                if (dataStatus != null) {
                    dataStatus.DataIsLoaded(comments,rate,keys);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void countFavourite(final FirebaseProductInfoHelper.DataStatusCountFavourite dataStatusCountFavourite)
    {
        mReference.child("Favorites").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int count = 0;
                for (DataSnapshot keyNode: snapshot.getChildren())
                {
                    if (keyNode.child(productId).exists())
                    {
                        count++;
                    }
                }
                if (dataStatusCountFavourite != null) {
                    dataStatusCountFavourite.DataIsLoaded(count);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void readInformationById(final FirebaseProductInfoHelper.DataStatusInformationOfProduct dataStatusInformationOfProduct)
    {
        mReference.child("Products").child(productId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Product item = snapshot.getValue(Product.class);
                if (dataStatusInformationOfProduct != null) {
                    dataStatusInformationOfProduct.DataIsLoaded(item);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
