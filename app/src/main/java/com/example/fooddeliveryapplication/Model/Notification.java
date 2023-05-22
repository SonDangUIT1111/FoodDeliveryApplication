package com.example.fooddeliveryapplication.Model;

public class Notification {
    String notificationId;
    String title;
    String content;
    String imageURL;
    String time;
    boolean read;

    public Notification(String notificationId, String title, String content, String imageURL, String time, boolean isRead) {
        this.notificationId = notificationId;
        this.title = title;
        this.content = content;
        this.imageURL = imageURL;
        this.time = time;
        this.read = isRead;
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

    public void setRead(boolean isRead) {
        read = isRead;
    }
}
