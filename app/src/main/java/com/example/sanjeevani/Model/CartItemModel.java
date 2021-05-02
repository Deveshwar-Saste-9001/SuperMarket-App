package com.example.sanjeevani.Model;

import java.util.ArrayList;
import java.util.List;

public class CartItemModel {
    public static final int CART_ITEM = 0;
    public static final int TOTAL_AMOUNT = 1;

    private int Type;

    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
    }

    ///cartitem
    private String productIdcart;
    private String productImagecart;
    private String productTitle;
    private long freeCoupens;
    private String productPrice;
    private String cuttedPrice;
    private long productQuantity;
    private long maxQuantity;
    private long stockQuantity;
    private long OffersApplied;
    private long coupensApplied;
    private boolean in_Stock;
    private List<String> qtyIDS;
    private boolean qtyError;
    private String selectedCoupenId;
    private String discountedPrice;
    private boolean cod;

    public CartItemModel(boolean cod, int type, String productId, String productImagecart, String productTitle, long freeCoupens, String productPrice, String cuttedPrice, long productQuantity, long offersApplied, long coupensApplied, boolean in_stock, long maxQuantity, long stockQuantity) {
        Type = type;
        this.productIdcart = productId;
        this.productImagecart = productImagecart;
        this.productTitle = productTitle;
        this.freeCoupens = freeCoupens;
        this.productPrice = productPrice;
        this.cuttedPrice = cuttedPrice;
        this.productQuantity = productQuantity;
        OffersApplied = offersApplied;
        this.coupensApplied = coupensApplied;
        this.in_Stock = in_stock;
        this.maxQuantity = maxQuantity;
        this.stockQuantity = stockQuantity;
        qtyIDS = new ArrayList<>();
        qtyError = false;
        this.cod = cod;
    }

    public boolean isCod() {
        return cod;
    }

    public void setCod(boolean cod) {
        this.cod = cod;
    }

    public String getDiscountedPrice() {
        return discountedPrice;
    }

    public void setDiscountedPrice(String discountedPrice) {
        this.discountedPrice = discountedPrice;
    }

    public String getSelectedCoupenId() {
        return selectedCoupenId;
    }

    public void setSelectedCoupenId(String selectedCoupenId) {
        this.selectedCoupenId = selectedCoupenId;
    }

    public boolean isQtyError() {
        return qtyError;
    }

    public void setQtyError(boolean qtyError) {
        this.qtyError = qtyError;
    }

    public long getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(long stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public List<String> getQtyIDS() {
        return qtyIDS;
    }

    public void setQtyIDS(List<String> qtyIDS) {
        this.qtyIDS = qtyIDS;
    }

    public long getMaxQuantity() {
        return maxQuantity;
    }

    public void setMaxQuantity(long maxQuantity) {
        this.maxQuantity = maxQuantity;
    }

    public boolean isIn_Stock() {
        return in_Stock;
    }

    public void setIn_Stock(boolean in_Stock) {
        this.in_Stock = in_Stock;
    }

    public String getProductIdcart() {
        return productIdcart;
    }

    public void setProductIdcart(String productIdcart) {
        this.productIdcart = productIdcart;
    }

    public String getProductImagecart() {
        return productImagecart;
    }

    public void setProductImagecart(String productImagecart) {
        this.productImagecart = productImagecart;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public long getFreeCoupens() {
        return freeCoupens;
    }

    public void setFreeCoupens(long freeCoupens) {
        this.freeCoupens = freeCoupens;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getCuttedPrice() {
        return cuttedPrice;
    }

    public void setCuttedPrice(String cuttedPrice) {
        this.cuttedPrice = cuttedPrice;
    }

    public long getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(long productQuantity) {
        this.productQuantity = productQuantity;
    }

    public long getOffersApplied() {
        return OffersApplied;
    }

    public void setOffersApplied(long offersApplied) {
        OffersApplied = offersApplied;
    }

    public long getCoupensApplied() {
        return coupensApplied;
    }

    public void setCoupensApplied(long coupensApplied) {
        this.coupensApplied = coupensApplied;
    }

    ///////cart total

    private int totalItem, totalItemPrice, totalAmount, savedAmount;
    private String deliveryPrice;

    public CartItemModel(int type) {
        Type = type;
    }

    public int getTotalItem() {
        return totalItem;
    }

    public void setTotalItem(int totalItem) {
        this.totalItem = totalItem;
    }

    public int getTotalItemPrice() {
        return totalItemPrice;
    }

    public void setTotalItemPrice(int totalItemPrice) {
        this.totalItemPrice = totalItemPrice;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getSavedAmount() {
        return savedAmount;
    }

    public void setSavedAmount(int savedAmount) {
        this.savedAmount = savedAmount;
    }

    public String getDeliveryPrice() {
        return deliveryPrice;
    }

    public void setDeliveryPrice(String deliveryPrice) {
        this.deliveryPrice = deliveryPrice;
    }
    //////
}
