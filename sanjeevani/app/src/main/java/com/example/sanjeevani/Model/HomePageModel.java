package com.example.sanjeevani.Model;

import java.util.List;

public class HomePageModel {
    public static final int BANNER_SLIDER = 0;
    public static final int STRIP_ADS_BANNER = 1;
    public static final int HORIZONTAL_SCROLL = 2;
    public static final int GRID_VIEW_LAYOUT = 3;

    private int type;
    private String Backgroundcolor;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getBackgroundcolor() {
        return Backgroundcolor;
    }

    public void setBackgroundcolor(String backgroundcolor) {
        Backgroundcolor = backgroundcolor;
    }


    ///////banner slider
    private List<SliderModel> SliderModelList;

    //////////strip ads

    public HomePageModel(int type, List<SliderModel> sliderModelList) {
        SliderModelList = sliderModelList;
        this.type = type;
    }

    public List<SliderModel> getSliderModelList() {
        return SliderModelList;
    }

    public void setSliderModelList(List<SliderModel> sliderModelList) {
        SliderModelList = sliderModelList;
    }

    /////////////////////bannerslider
    private String resource;


    public HomePageModel(int type, String resource, String backgroundcolor) {
        this.type = type;
        this.resource = resource;
        Backgroundcolor = backgroundcolor;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }


    /////////////////Horizontal
    private String title;
    private List<HorizontalProductScrollModel> horizontalProductScrollModelList;
    private List<WishListModel> ViewAllproductList;


    //////////////////////////horizontal
    public HomePageModel(int type, String title, String backgroundcolor, List<HorizontalProductScrollModel> horizontalProductScrollModelList, List<WishListModel> ViewAllproductList) {
        this.type = type;
        this.title = title;
        this.Backgroundcolor = backgroundcolor;
        this.horizontalProductScrollModelList = horizontalProductScrollModelList;
        this.ViewAllproductList = ViewAllproductList;
    }

    public List<WishListModel> getViewAllproductList() {
        return ViewAllproductList;
    }

    public void setViewAllproductList(List<WishListModel> viewAllproductList) {
        ViewAllproductList = viewAllproductList;
    }
//////////////////////////horizontal

    //////////////////grid

    public HomePageModel(int type, String title, String backgroundcolor, List<HorizontalProductScrollModel> horizontalProductScrollModelList) {
        this.type = type;
        this.title = title;
        this.Backgroundcolor = backgroundcolor;
        this.horizontalProductScrollModelList = horizontalProductScrollModelList;
    }
    //////////////////grid

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<HorizontalProductScrollModel> getHorizontalProductScrollModelList() {
        return horizontalProductScrollModelList;
    }

    public void setHorizontalProductScrollModelList(List<HorizontalProductScrollModel> horizontalProductScrollModelList) {
        this.horizontalProductScrollModelList = horizontalProductScrollModelList;
    }



    //////////x
}
