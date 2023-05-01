package com.example.fooddeliveryapplication.Model;

public class CommentDetail {
    String publisherId;
    String publisherName;
    String commentId;
    String commentDetail;
    Float ratingStar;

    public CommentDetail() {
    }

    public CommentDetail(String publisherId, String publisherName, String commentId, String commentDetail, Float ratingStar) {
        this.publisherId = publisherId;
        this.publisherName = publisherName;
        this.commentId = commentId;
        this.commentDetail = commentDetail;
        this.ratingStar = ratingStar;
    }

    public String getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(String publisherId) {
        this.publisherId = publisherId;
    }

    public String getPublisherName() {
        return publisherName;
    }

    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getCommentDetail() {
        return commentDetail;
    }

    public void setCommentDetail(String commentDetail) {
        this.commentDetail = commentDetail;
    }

    public Float getRatingStar() {
        return ratingStar;
    }

    public void setRatingStar(Float ratingStar) {
        this.ratingStar = ratingStar;
    }
}
