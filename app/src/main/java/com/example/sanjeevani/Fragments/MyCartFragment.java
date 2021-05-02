package com.example.sanjeevani.Fragments;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sanjeevani.Adapter.CartAdapter;
import com.example.sanjeevani.DBqueries;
import com.example.sanjeevani.DeliveryActivity;
import com.example.sanjeevani.Model.CartItemModel;
import com.example.sanjeevani.Model.MyRewordModel;
import com.example.sanjeevani.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyCartFragment extends Fragment {


    public MyCartFragment() {
        // Required empty public constructor
    }

    private TextView cartEmpty;
    private RecyclerView cartItemsRecyclerView;
    private Button cartContineBtn;
    private Dialog loadingDialog;
    private TextView totalAmountbtn;
    public static CartAdapter cartAdapter;

    @SuppressLint("WrongConstant")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_cart, container, false);

        loadingDialog = new Dialog(getContext());
        loadingDialog.setContentView(R.layout.loading_progress_bar);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getContext().getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.show();

        totalAmountbtn = view.findViewById(R.id.total_cart_amount1);
        cartEmpty = view.findViewById(R.id.cart_empatyTextView);

        cartItemsRecyclerView = view.findViewById(R.id.Cart_item_RrcyclerView);
        cartContineBtn = view.findViewById(R.id.cart_continue_btn1);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        cartItemsRecyclerView.setLayoutManager(linearLayoutManager);
        cartAdapter = new CartAdapter(DBqueries.cartItemModelList, totalAmountbtn, true);
        cartItemsRecyclerView.setAdapter(cartAdapter);
        cartAdapter.notifyDataSetChanged();


        cartContineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeliveryActivity.cartItemModelList = new ArrayList<>();
                DeliveryActivity.fromcart = true;
                for (int x = 0; x < DBqueries.cartItemModelList.size(); x++) {
                    CartItemModel cartItemModel = DBqueries.cartItemModelList.get(x);
                    if (cartItemModel.isIn_Stock()) {
                        DeliveryActivity.cartItemModelList.add(cartItemModel);
                    }

                }
                DeliveryActivity.cartItemModelList.add(new CartItemModel(CartItemModel.TOTAL_AMOUNT));
                loadingDialog.show();
                if (DBqueries.addressesModelList.size() == 0) {
                    DBqueries.loadAddresses(getContext(), loadingDialog, true);
                } else {
                    loadingDialog.dismiss();
                    Intent DeliveryIntent = new Intent(getContext(), DeliveryActivity.class);
                    startActivity(DeliveryIntent);
                }
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        cartAdapter.notifyDataSetChanged();
        if (DBqueries.myRewordModelList.size() == 0) {
            loadingDialog.show();
            DBqueries.loadRewords(getContext(), loadingDialog, false);
        }
        if (DBqueries.cartItemModelList.size() == 0) {
            DBqueries.cartList.clear();
            DBqueries.loadCartList(getContext(), loadingDialog, true, totalAmountbtn);

        } else {
            cartAdapter = new CartAdapter(DBqueries.cartItemModelList, totalAmountbtn, true);
            cartItemsRecyclerView.setAdapter(cartAdapter);
            cartAdapter.notifyDataSetChanged();
            if (DBqueries.cartItemModelList.get(DBqueries.cartItemModelList.size() - 1).getType() == CartItemModel.TOTAL_AMOUNT) {
                LinearLayout parent = (LinearLayout) totalAmountbtn.getParent().getParent();
                parent.setVisibility(View.VISIBLE);
            }
            loadingDialog.dismiss();
        }


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        for (CartItemModel cartItemModel : DBqueries.cartItemModelList) {
            if (!TextUtils.isEmpty(cartItemModel.getSelectedCoupenId())) {
                for (MyRewordModel rewordModel : DBqueries.myRewordModelList) {
                    if (rewordModel.getCoupenId().equals(cartItemModel.getSelectedCoupenId())) {
                        rewordModel.setAlreadyUsed(false);
                    }
                }
                cartItemModel.setSelectedCoupenId(null);
                if (MyRewordFragment.myRewordAdapter != null) {
                    MyRewordFragment.myRewordAdapter.notifyDataSetChanged();
                }
            }
        }

    }
}
