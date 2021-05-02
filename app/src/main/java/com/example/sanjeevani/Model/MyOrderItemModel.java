package com.example.sanjeevani.Model;

import java.util.Date;

public class MyOrderItemModel {
    private String productId;
    private String productTitleOrder;
    private String productImageOrder;

    private String orderStatus;
    private String address;
    private String coupenId;
    private String cuttedPrice;
    private Date orderedDate;
    private Date packedDate;
    private Date shippeddate;
    private Date deliveredDate;
    private Date cancelledDate;
    private String discountedPrice;
    private Long freeCoupens;
    private String fullName;
    private String orderId;
    private String paymentMethod;
    private String pincode;
    private String productPrice;
    private Long productQuantity;
    private String User_Id;
    private String deliveryPrice;
    private boolean cancellationRequested;

    private int rating = 0;


    public MyOrderItemModel(String productId, String productTitleOrder, String productImageOrder, String orderStatus, String address, String coupenId, String cuttedPrice, Date orderedDate, Date packedDate, Date shippeddate, Date deliveredDate, Date cancelledDate, String discountedPrice, Long freeCoupens, String fullName, String orderId, String paymentMethod, String pincode, String productPrice, Long productQuantity, String user_Id, String deliveryPrice, boolean cancellationRequested) {
        this.productId = productId;
        this.productTitleOrder = productTitleOrder;
        this.productImageOrder = productImageOrder;
        this.orderStatus = orderStatus;
        this.address = address;
        this.coupenId = coupenId;
        this.cuttedPrice = cuttedPrice;
        this.orderedDate = orderedDate;
        this.packedDate = packedDate;
        this.shippeddate = shippeddate;
        this.deliveredDate = deliveredDate;
        this.cancelledDate = cancelledDate;
        this.discountedPrice = discountedPrice;
        this.freeCoupens = freeCoupens;
        this.fullName = fullName;
        this.orderId = orderId;
        this.paymentMethod = paymentMethod;
        this.pincode = pincode;
        this.productPrice = productPrice;
        this.productQuantity = productQuantity;
        User_Id = user_Id;
        this.deliveryPrice = deliveryPrice;
        this.cancellationRequested = cancellationRequested;
    }

    public boolean isCancellationRequested() {
        return cancellationRequested;
    }

    public void setCancellationRequested(boolean cancellationRequested) {
        this.cancellationRequested = cancellationRequested;
    }

    public String getDeliveryPrice() {
        return deliveryPrice;
    }

    public void setDeliveryPrice(String deliveryPrice) {
        this.deliveryPrice = deliveryPrice;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductTitleOrder() {
        return productTitleOrder;
    }

    public void setProductTitleOrder(String productTitleOrder) {
        this.productTitleOrder = productTitleOrder;
    }

    public String getProductImageOrder() {
        return productImageOrder;
    }

    public void setProductImageOrder(String productImageOrder) {
        this.productImageOrder = productImageOrder;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCoupenId() {
        return coupenId;
    }

    public void setCoupenId(String coupenId) {
        this.coupenId = coupenId;
    }

    public String getCuttedPrice() {
        return cuttedPrice;
    }

    public void setCuttedPrice(String cuttedPrice) {
        this.cuttedPrice = cuttedPrice;
    }

    public Date getOrderedDate() {
        return orderedDate;
    }

    public void setOrderedDate(Date orderedDate) {
        this.orderedDate = orderedDate;
    }

    public Date getPackedDate() {
        return packedDate;
    }

    public void setPackedDate(Date packedDate) {
        this.packedDate = packedDate;
    }

    public Date getShippeddate() {
        return shippeddate;
    }

    public void setShippeddate(Date shippeddate) {
        this.shippeddate = shippeddate;
    }

    public Date getDeliveredDate() {
        return deliveredDate;
    }

    public void setDeliveredDate(Date deliveredDate) {
        this.deliveredDate = deliveredDate;
    }

    public Date getCancelledDate() {
        return cancelledDate;
    }

    public void setCancelledDate(Date cancelledDate) {
        this.cancelledDate = cancelledDate;
    }

    public String getDiscountedPrice() {
        return discountedPrice;
    }

    public void setDiscountedPrice(String discountedPrice) {
        this.discountedPrice = discountedPrice;
    }

    public Long getFreeCoupens() {
        return freeCoupens;
    }

    public void setFreeCoupens(Long freeCoupens) {
        this.freeCoupens = freeCoupens;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public Long getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(Long productQuantity) {
        this.productQuantity = productQuantity;
    }

    public String getUser_Id() {
        return User_Id;
    }

    public void setUser_Id(String user_Id) {
        User_Id = user_Id;
    }
}
