package com.example.fooddeliveryapplication.Helpers;

import android.provider.ContactsContract;

import androidx.annotation.NonNull;

import com.example.fooddeliveryapplication.Model.Comment;
import com.example.fooddeliveryapplication.Model.CommentDetail;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FirebaseProductInfoHelper {
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReferenceComment;
    private String productId;
    private List<CommentDetail> comments = new ArrayList<>();


    public interface DataStatus{
        void DataIsLoaded(List<CommentDetail> comments, List<String> keys);
        void DataIsInserted();
        void DataIsUpdated();
        void DataIsDeleted();
    }
    public FirebaseProductInfoHelper(String productBranch){
        productId = productBranch;
        mDatabase = FirebaseDatabase.getInstance();
        mReferenceComment = mDatabase.getReference("/Comments");
    }

    public void readComments(final DataStatus dataStatus)
    {
        mReferenceComment.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                comments.clear();
                List<String> keys = new ArrayList<>();
                for (DataSnapshot keyNode : snapshot.getChildren())
                {
                    keys.add(keyNode.getKey());
                    if (keyNode.child("productId").getValue(String.class).equals(productId))
                    {
                        DataSnapshot snapshotList = keyNode.child("commentList");
                        for (DataSnapshot keyChildNode : snapshotList.getChildren())
                        {
                            CommentDetail detail = keyChildNode.getValue(CommentDetail.class);
                            comments.add(detail);
                        }
                        break;
                    }
                    else continue;
                }
                dataStatus.DataIsLoaded(comments,keys);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
