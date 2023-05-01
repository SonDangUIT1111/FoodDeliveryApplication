package com.example.fooddeliveryapplication.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.fooddeliveryapplication.Model.Comment;
import com.example.fooddeliveryapplication.Model.CommentDetail;
import com.example.fooddeliveryapplication.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class CommentRecyclerViewAdapter extends RecyclerView.Adapter<CommentRecyclerViewAdapter.ViewHolder> {
    private Context mContext;
    private List<CommentDetail> commentList;
    private List<String> mKeys;


    public CommentRecyclerViewAdapter(Context mContext, List<CommentDetail> commentList, List<String> mKeys) {
        this.mContext = mContext;
        this.commentList = commentList;
        this.mKeys = mKeys;

    }

    @NonNull
    @Override
    public CommentRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.comment_list_item,parent,false);
        return new CommentRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentRecyclerViewAdapter.ViewHolder holder, int position) {
        CommentDetail comment = commentList.get(position);
        holder.txtRecCommentUsername.setText(comment.getPublisherName());
        holder.txtRecCommentComment.setText(comment.getCommentDetail());
        holder.recCommentRatingBar.setRating(comment.getRatingStar());
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView txtRecCommentUsername;
        public TextView txtRecCommentComment;
        public RatingBar recCommentRatingBar;
        public String key;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtRecCommentUsername = itemView.findViewById(R.id.txtRecCommentUsername);
            txtRecCommentComment = itemView.findViewById(R.id.txtRecCommentComment);
            recCommentRatingBar = itemView.findViewById(R.id.recCommentRatingBar);
        }
    }
}
