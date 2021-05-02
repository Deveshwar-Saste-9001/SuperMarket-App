package com.example.sanjeevani.Fragments;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sanjeevani.Adapter.MyRewordAdapter;
import com.example.sanjeevani.DBqueries;
import com.example.sanjeevani.R;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyRewordFragment extends Fragment {


    public MyRewordFragment() {
        // Required empty public constructor
    }

    private RecyclerView myRewordRecyclerView;
    private Dialog loadingDialog;
    public static MyRewordAdapter myRewordAdapter;

    @SuppressLint("WrongConstant")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_reword, container, false);

        loadingDialog = new Dialog(getContext());
        loadingDialog.setContentView(R.layout.loading_progress_bar);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getContext().getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.show();

        myRewordRecyclerView = view.findViewById(R.id.myRewordsRecyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        myRewordRecyclerView.setLayoutManager(layoutManager);
        myRewordAdapter = new MyRewordAdapter(DBqueries.myRewordModelList, false);
        myRewordRecyclerView.setAdapter(myRewordAdapter);
        if (DBqueries.myRewordModelList.size() == 0) {
            DBqueries.loadRewords(getContext(), loadingDialog, true);
        } else {
            loadingDialog.dismiss();
        }
        myRewordAdapter.notifyDataSetChanged();

        return view;
    }

}
