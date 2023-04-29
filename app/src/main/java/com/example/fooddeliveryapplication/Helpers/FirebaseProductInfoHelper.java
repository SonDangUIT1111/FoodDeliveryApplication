package com.example.fooddeliveryapplication.Helpers;

import androidx.annotation.NonNull;

import com.example.fooddeliveryapplication.Model.Comment;
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
    private List<Comment> comments = new ArrayList<>();


    public interface DataStatus{
        void DataIsLoaded(List<Comment> comments, List<String> keys);
        void DataIsInserted();
        void DataIsUpdated();
        void DataIsDeleted();
    }
    public FirebaseProductInfoHelper(String productBranch){
        mDatabase = FirebaseDatabase.getInstance();
        mReferenceComment = mDatabase.getReference("/Comments/"+productBranch);
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
                    Comment comment = keyNode.getValue(Comment.class);
                    comments.add(comment);

                }
                dataStatus.DataIsLoaded(comments,keys);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
