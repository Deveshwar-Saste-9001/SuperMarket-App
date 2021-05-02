package com.example.sanjeevani.Fragments;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sanjeevani.Adapter.WishListAdapter;
import com.example.sanjeevani.DBqueries;
import com.example.sanjeevani.Model.WishListModel;
import com.example.sanjeevani.ProductDetailsActivity;
import com.example.sanjeevani.R;

import java.util.ArrayList;
import java.util.List;

import static com.example.sanjeevani.DBqueries.wishListModelList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyWishLayout extends Fragment {


    public MyWishLayout() {
        // Required empty public constructor
    }
    private RecyclerView wishlistRecyclerView;
    private Dialog loadingDialog;
    public static  WishListAdapter wishListAdapter;



    @SuppressLint("WrongConstant")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_my_wish_layout, container, false);
        wishlistRecyclerView=view.findViewById(R.id.mywishlsitRecyclerView);

        loadingDialog = new Dialog(getContext());
        loadingDialog.setContentView(R.layout.loading_progress_bar);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getContext().getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.show();

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        wishlistRecyclerView.setLayoutManager(linearLayoutManager);

        if(DBqueries.wishListModelList.size()==0){
            DBqueries.WishListlist.clear();
            DBqueries.loadWishList(getContext(),loadingDialog,true);
        }else {
            loadingDialog.dismiss();
        }
        wishListAdapter=new WishListAdapter(DBqueries.wishListModelList,true);
        wishlistRecyclerView.setAdapter(wishListAdapter);
        wishListAdapter.notifyDataSetChanged();
        return view;
    }

}
