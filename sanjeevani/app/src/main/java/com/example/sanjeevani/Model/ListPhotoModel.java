package com.example.sanjeevani.Model;

public class ListPhotoModel {

    private String sender;
    private String receiver;
    private String message;
    private String messageId;

    public ListPhotoModel() {
    }

    public ListPhotoModel(String sender, String receiver, String message, String messageId) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.messageId = messageId;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }
}
