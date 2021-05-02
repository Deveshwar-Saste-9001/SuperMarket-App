package com.example.sanjeevani.Model;

import com.google.firebase.Timestamp;

import java.util.Date;

public class MyRewordModel {
    private String type;
    private String lowerLimit;
    private String upperLimit;
    private String discountORamount;
    private String rewordBody;
    private Date timestamp;
    private boolean alreadyUsed;
    private String coupenId;

    public MyRewordModel(String coupenId,String type, String lowerLimit, String upperLimit, String discountORamount, String rewordBody, Date timestamp,boolean alreadyUsed) {
        this.coupenId=coupenId;
        this.type = type;
        this.lowerLimit = lowerLimit;
        this.upperLimit = upperLimit;
        this.discountORamount = discountORamount;
        this.rewordBody = rewordBody;
        this.timestamp = timestamp;
        this.alreadyUsed=alreadyUsed;
    }

    public String getCoupenId() {
        return coupenId;
    }

    public void setCoupenId(String coupenId) {
        this.coupenId = coupenId;
    }

    public boolean isAlreadyUsed() {
        return alreadyUsed;
    }

    public void setAlreadyUsed(boolean alreadyUsed) {
        this.alreadyUsed = alreadyUsed;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLowerLimit() {
        return lowerLimit;
    }

    public void setLowerLimit(String lowerLimit) {
        this.lowerLimit = lowerLimit;
    }

    public String getUpperLimit() {
        return upperLimit;
    }

    public void setUpperLimit(String upperLimit) {
        this.upperLimit = upperLimit;
    }

    public String getDiscountORamount() {
        return discountORamount;
    }

    public void setDiscountORamount(String discount) {
        this.discountORamount = discount;
    }

    public String getRewordBody() {
        return rewordBody;
    }

    public void setRewordBody(String rewordBody) {
        this.rewordBody = rewordBody;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
