package com.example.fooddeliveryapplication.Model;

import java.util.List;

public class Comment {
    public String productId;
    public String productName;
    public List<CommentDetail> commentList;

    public Comment(String productId, String productName, List<CommentDetail> commentList) {
        this.productId = productId;
        this.productName = productName;
        this.commentList = commentList;
    }

    public Comment() {
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public List<CommentDetail> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<CommentDetail> commentList) {
        this.commentList = commentList;
    }
}
