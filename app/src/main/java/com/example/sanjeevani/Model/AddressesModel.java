package com.example.sanjeevani.Model;

public class AddressesModel {

    private boolean selected;
    private String city;
    private String locality;
    private String landMark;
    private String flatno;
    private String pincode;
    private String state;
    private String name;
    private String mobile;
    private String alternetMobile;

    public AddressesModel(boolean selected, String city, String locality, String landMark, String flatno, String pincode, String state, String name, String mobile, String alternetMobile) {
        this.selected = selected;
        this.city = city;
        this.locality = locality;
        this.landMark = landMark;
        this.flatno = flatno;
        this.pincode = pincode;
        this.state = state;
        this.name = name;
        this.mobile = mobile;
        this.alternetMobile = alternetMobile;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getLandMark() {
        return landMark;
    }

    public void setLandMark(String landMark) {
        this.landMark = landMark;
    }

    public String getFlatno() {
        return flatno;
    }

    public void setFlatno(String flatno) {
        this.flatno = flatno;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAlternetMobile() {
        return alternetMobile;
    }

    public void setAlternetMobile(String alternetMobile) {
        this.alternetMobile = alternetMobile;
    }
}
