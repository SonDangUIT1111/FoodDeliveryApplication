package com.example.fooddeliveryapplication.Model;

public class Comment {
   private String commentDetail;
   private String commentId;
   private String publisherId;
   private float rating;

    public Comment(String commentDetail, String commentId, String publisherId, float rating) {
        this.commentDetail = commentDetail;
        this.commentId = commentId;
        this.publisherId = publisherId;
        this.rating = rating;
    }

    public Comment() {
    }

    public String getCommentDetail() {
        return commentDetail;
    }

    public void setCommentDetail(String commentDetail) {
        this.commentDetail = commentDetail;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(String publisherId) {
        this.publisherId = publisherId;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
