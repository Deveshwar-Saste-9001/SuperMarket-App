package com.example.sanjeevani.Model;

public class ChatModel {

    private String sender;
    private String receiver;
    private String message;
    private String messageId;

    public ChatModel(String sender, String receiver, String message, String messasgeId) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.messageId = messasgeId;

    }
    public ChatModel() {
    }
    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
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
}
