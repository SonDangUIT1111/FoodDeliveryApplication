package com.example.fooddeliveryapplication.Adapters.ProductInfomation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.fooddeliveryapplication.Model.Comment;
import com.example.fooddeliveryapplication.databinding.ItemCommentListBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CommentRecyclerViewAdapter extends RecyclerView.Adapter<CommentRecyclerViewAdapter.ViewHolder> {
    private Context mContext;
    private List<Comment> commentList;
    private List<String> mKeys;

    public CommentRecyclerViewAdapter(Context mContext, List<Comment> commentList, List<String> mKeys) {
        this.mContext = mContext;
        this.commentList = commentList;
        this.mKeys = mKeys;
    }

    @NonNull
    @Override
    public CommentRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CommentRecyclerViewAdapter.ViewHolder(ItemCommentListBinding.inflate(LayoutInflater.from(mContext), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CommentRecyclerViewAdapter.ViewHolder holder, int position) {
        // find name from id
        Comment comment = commentList.get(position);
        final String[] userName = {""};

        FirebaseDatabase.getInstance().getReference().child("Users").child(comment.getPublisherId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("userName").getValue(String.class);
                holder.binding.txtRecCommentUsername.setText(name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        holder.binding.txtRecCommentUsername.setText(userName[0]);
        holder.binding.txtRecCommentComment.setText(comment.getCommentDetail());
        holder.binding.recCommentRatingBar.setRating(comment.getRating());
    }

    @Override
    public int getItemCount() {
        return commentList == null ? 0 : commentList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final ItemCommentListBinding binding;

        public ViewHolder(@NonNull ItemCommentListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
