package com.example.sanjeevani.Model;

public class SliderModel {
    private String banners;
    private String backcolor;

    public SliderModel(String banners, String backcolor) {
        this.banners = banners;
        this.backcolor = backcolor;
    }

    public String getBanners() {
        return banners;
    }

    public void setBanners(String banners) {
        this.banners = banners;
    }

    public String getBackcolor() {
        return backcolor;
    }

    public void setBackcolor(String backcolor) {
        this.backcolor = backcolor;
    }
}
