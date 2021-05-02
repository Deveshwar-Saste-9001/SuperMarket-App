package com.example.sanjeevani.Model;

public class NotificationModel {
    private String notificationImage;
    private String notificationBody;
    private boolean Readed;

    public NotificationModel(String notificationImage, String notificationBody, boolean readed) {
        this.notificationImage = notificationImage;
        this.notificationBody = notificationBody;
        Readed = readed;
    }

    public String getNotificationImage() {
        return notificationImage;
    }

    public void setNotificationImage(String notificationImage) {
        this.notificationImage = notificationImage;
    }

    public String getNotificationBody() {
        return notificationBody;
    }

    public void setNotificationBody(String notificationBody) {
        this.notificationBody = notificationBody;
    }

    public boolean isReaded() {
        return Readed;
    }

    public void setReaded(boolean readed) {
        Readed = readed;
    }
}
