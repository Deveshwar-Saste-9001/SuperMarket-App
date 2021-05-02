package com.example.sanjeevani.Model;

import java.util.ArrayList;

public class WishListModel {
    private String productId;
    private String ProductImageWish;
    private String ProductTitleWish;
    private long freeCoupensNo;
    private String Rating;
    private long TotalRating;
    private String productPriceWish;
    private String CuttedPriceWish;
    private boolean COD;
    private boolean inStock;
    private ArrayList<String> tags;

    public WishListModel(String productId, String productImageWish, String productTitleWish, long freeCoupensNo, String rating, long totalRating, String productPriceWish, String cuttedPriceWish, boolean COD, boolean inStock) {
        this.productId = productId;
        ProductImageWish = productImageWish;
        ProductTitleWish = productTitleWish;
        this.freeCoupensNo = freeCoupensNo;
        Rating = rating;
        TotalRating = totalRating;
        this.productPriceWish = productPriceWish;
        CuttedPriceWish = cuttedPriceWish;
        this.COD = COD;
        this.inStock = inStock;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public boolean isInStock() {
        return inStock;
    }

    public void setInStock(boolean inStock) {
        this.inStock = inStock;
    }

    public String getProductId() {
        return productId;
    }


    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductImageWish() {
        return ProductImageWish;
    }

    public void setProductImageWish(String productImageWish) {
        ProductImageWish = productImageWish;
    }

    public String getProductTitleWish() {
        return ProductTitleWish;
    }

    public void setProductTitleWish(String productTitleWish) {
        ProductTitleWish = productTitleWish;
    }

    public long getFreeCoupensNo() {
        return freeCoupensNo;
    }

    public void setFreeCoupensNo(long freeCoupensNo) {
        this.freeCoupensNo = freeCoupensNo;
    }

    public String getRating() {
        return Rating;
    }

    public void setRating(String rating) {
        Rating = rating;
    }

    public long getTotalRating() {
        return TotalRating;
    }

    public void setTotalRating(long totalRating) {
        TotalRating = totalRating;
    }

    public String getProductPriceWish() {
        return productPriceWish;
    }

    public void setProductPriceWish(String productPriceWish) {
        this.productPriceWish = productPriceWish;
    }

    public String getCuttedPriceWish() {
        return CuttedPriceWish;
    }

    public void setCuttedPriceWish(String cuttedPriceWish) {
        CuttedPriceWish = cuttedPriceWish;
    }

    public boolean isCOD() {
        return COD;
    }

    public void setCOD(boolean COD) {
        this.COD = COD;
    }
}