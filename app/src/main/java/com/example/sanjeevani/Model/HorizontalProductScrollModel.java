package com.example.sanjeevani.Model;

public class HorizontalProductScrollModel {
    private String ProductImage;
    private String Producttitle;
    private String Description;
    private String ProductPrice;
    private String productID;

    public HorizontalProductScrollModel(String ProductId, String productImage, String producttitle, String description, String productPrice) {

        this.productID = ProductId;
        this.ProductImage = productImage;
        this.Producttitle = producttitle;
        this.Description = description;
        this.ProductPrice = productPrice;

    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getProductImage() {
        return ProductImage;
    }

    public void setProductImage(String productImage) {
        ProductImage = productImage;
    }

    public String getProducttitle() {
        return Producttitle;
    }

    public void setProducttitle(String producttitle) {
        Producttitle = producttitle;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getProductPrice() {
        return ProductPrice;
    }

    public void setProductPrice(String productPrice) {
        ProductPrice = productPrice;
    }
}
