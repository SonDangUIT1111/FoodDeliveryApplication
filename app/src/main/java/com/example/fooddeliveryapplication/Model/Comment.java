package com.example.fooddeliveryapplication.Model;

public class Comment {
    public String commentDetail;
    public String commentId;
    public String publisherId;
    public float ratingStar;

    public Comment() {
    }

    public Comment(String commentDetail, String commentId, String publisherId, float ratingStar) {
        this.commentDetail = commentDetail;
        this.commentId = commentId;
        this.publisherId = publisherId;
        this.ratingStar = ratingStar;
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

    public float getRatingStar() {
        return ratingStar;
    }

    public void setRatingStar(float ratingStar) {
        this.ratingStar = ratingStar;
    }
}
