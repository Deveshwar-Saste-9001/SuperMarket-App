package com.example.sanjeevani.Fragments;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sanjeevani.Adapter.MyOrderAdapter;
import com.example.sanjeevani.DBqueries;
import com.example.sanjeevani.R;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyOrdersFargment extends Fragment {

    public MyOrdersFargment() {
        // Required empty public constructor
    }

    private RecyclerView myOrderrecyclerView;
    public static MyOrderAdapter myOrderAdapter;
    private Dialog loadingDialog;

    @SuppressLint("WrongConstant")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_orders_fargment, container, false);

        loadingDialog = new Dialog(getContext());
        loadingDialog.setContentView(R.layout.loading_progress_bar);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getContext().getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.show();

        myOrderrecyclerView = view.findViewById(R.id.myOrdersRecyclerView);


        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        myOrderrecyclerView.setLayoutManager(layoutManager);

        myOrderAdapter = new MyOrderAdapter(DBqueries.myOrderItemModelList);
        myOrderrecyclerView.setAdapter(myOrderAdapter);
        myOrderAdapter.notifyDataSetChanged();

        DBqueries.loadOrders(getContext(), myOrderAdapter,loadingDialog);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        myOrderAdapter.notifyDataSetChanged();
    }
}
