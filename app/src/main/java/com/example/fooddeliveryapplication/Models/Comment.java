package com.example.fooddeliveryapplication.Models;

public class Comment {
    String commentDetail;
    String commentId;
    String publisherId;
    double rating;

    public Comment() {
    }

    public Comment(String commentDetail, String commentId, String publisherId, double rating) {
        this.commentDetail = commentDetail;
        this.commentId = commentId;
        this.publisherId = publisherId;
        this.rating = rating;
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

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}
