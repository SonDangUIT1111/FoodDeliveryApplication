package com.example.fooddeliveryapplication.Model;

public class Notification {
    String notificationId;
    String title;
    String content;
    String imageURL;
    String time;
    boolean read;
    boolean notified;
    String productId;
    String billId;
    String confirmId;

    public Notification(String notificationId, String title, String content, String imageURL, String time, boolean read, boolean notified, String productId, String billId, String confirmId) {
        this.notificationId = notificationId;
        this.title = title;
        this.content = content;
        this.imageURL = imageURL;
        this.time = time;
        this.read = read;
        this.notified = notified;
        this.productId = productId;
        this.billId = billId;
        this.confirmId = confirmId;
    }

    public Notification() {
    }

    public String getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public boolean isNotified() {
        return notified;
    }

    public void setNotified(boolean notified) {
        this.notified = notified;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public String getConfirmId() {
        return confirmId;
    }

    public void setConfirmId(String confirmId) {
        this.confirmId = confirmId;
    }


}

