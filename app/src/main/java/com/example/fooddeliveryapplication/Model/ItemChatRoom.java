package com.example.fooddeliveryapplication.Model;

public class ItemChatRoom {
    private User receiver;
    private Message lastMessage;

    public ItemChatRoom(User receiver) {
        this.receiver=receiver;
    }

    public ItemChatRoom(User receiver, Message lastMessage) {
        this.receiver = receiver;
        this.lastMessage = lastMessage;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        receiver = receiver;
    }

    public Message getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(Message lastMessage) {
        this.lastMessage = lastMessage;
    }
}