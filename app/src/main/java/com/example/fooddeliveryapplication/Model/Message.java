package com.example.fooddeliveryapplication.Model;

public class Message {
    String idMessage;
    String content;
    String senderId;
    long timeStamp;
    boolean isSeen;

    public Message() {
    }

    public String getIdMessage() {
        return idMessage;
    }

    public void setIdMessage(String idMessage) {
        this.idMessage = idMessage;
    }

    public Message(String content, String senderId, long timeStamp, boolean isSeen) {
        this.content = content;
        this.senderId = senderId;
        this.timeStamp = timeStamp;
        this.isSeen = isSeen;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public boolean isSeen() {
        return isSeen;
    }

    public void setSeen(boolean seen) {
        isSeen = seen;
    }
}
