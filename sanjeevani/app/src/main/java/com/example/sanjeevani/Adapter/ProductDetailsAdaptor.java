package com.example.sanjeevani.Adapter;



import com.example.sanjeevani.Fragments.Product_Description_fragment;
import com.example.sanjeevani.Fragments.Product_Specification_Fragment;
import com.example.sanjeevani.Model.ProductSpecificationModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class ProductDetailsAdaptor extends FragmentPagerAdapter {
    private int totalTab;
    private String productDescription;
    private String productOtherDetails;
    private List<ProductSpecificationModel> productSpecificationModelList;

    public ProductDetailsAdaptor(@NonNull FragmentManager fm, int totalTab,String productDescription, String productOtherDetails, List<ProductSpecificationModel> productSpecificationModelList) {
        super(fm);
        this.totalTab = totalTab;
        this.productDescription = productDescription;
        this.productOtherDetails = productOtherDetails;
        this.productSpecificationModelList = productSpecificationModelList;
    }


    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                Product_Description_fragment productDescriptionFragment1 = new Product_Description_fragment();
                productDescriptionFragment1.body=productDescription;
                return productDescriptionFragment1;

            case 1:
                Product_Specification_Fragment productSpecificationFragment = new Product_Specification_Fragment();
                productSpecificationFragment.productSpecificationModelList=productSpecificationModelList;
                return productSpecificationFragment;

            case 2:
                Product_Description_fragment productDescriptionFragment2 = new Product_Description_fragment();
                productDescriptionFragment2.body=productOtherDetails;
                return productDescriptionFragment2;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 0;
    }
}
