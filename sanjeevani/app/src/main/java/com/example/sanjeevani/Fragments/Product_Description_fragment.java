package com.example.sanjeevani.Fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sanjeevani.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class Product_Description_fragment extends Fragment {


    public Product_Description_fragment() {
        // Required empty public constructor
    }
    private TextView descriptionBody;
    public String body;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_product__description_fragment, container, false);
        descriptionBody=view.findViewById(R.id.tv_product_Description_text1);
        descriptionBody.setText(body);
        return view;
    }

}
