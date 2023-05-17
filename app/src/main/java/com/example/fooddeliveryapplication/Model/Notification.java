package com.example.fooddeliveryapplication.Model;

public class Notification {
    String title;
    String content;
    String imageURL;
    String time;
    boolean isRead;

    public Notification(String title, String content, String imageURL, String time, boolean isRead) {
        this.title = title;
        this.content = content;
        this.imageURL = imageURL;
        this.time = time;
        this.isRead = isRead;
    }

    public Notification() {
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
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }
}
