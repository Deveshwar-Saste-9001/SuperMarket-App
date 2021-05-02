package com.example.sanjeevani.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.sanjeevani.Model.HorizontalProductScrollModel;
import com.example.sanjeevani.ProductDetailsActivity;
import com.example.sanjeevani.R;

import java.util.List;

public class GridProductLayoutAdapter extends BaseAdapter {

    List<HorizontalProductScrollModel> gridProductModelList;

    public GridProductLayoutAdapter(List<HorizontalProductScrollModel> gridProductScrollModelList) {
        this.gridProductModelList = gridProductScrollModelList;
    }

    @Override
    public int getCount() {
        return gridProductModelList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        View view;
        if (convertView == null) {

            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_scroll_item_layout, null);
            ImageView productImage = view.findViewById(R.id.product_Scroll_image);
            TextView productTitle = view.findViewById(R.id.product_scroll_title1);
            TextView productDescription = view.findViewById(R.id.product_scroll_offer1);
            TextView productPrice = view.findViewById(R.id.product_scroll_price1);


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent productDetailintent = new Intent(parent.getContext(), ProductDetailsActivity.class);
                    productDetailintent.putExtra("productID",gridProductModelList.get(position).getProductID());
                    parent.getContext().startActivity(productDetailintent);
                }
            });

            Glide.with(parent.getContext()).load(gridProductModelList.get(position).getProductImage()).apply(new RequestOptions().placeholder(R.drawable.proandcatplaceholder)).into(productImage);
            productTitle.setText(gridProductModelList.get(position).getProducttitle());
            productDescription.setText(gridProductModelList.get(position).getDescription());
            productPrice.setText("Rs. "+gridProductModelList.get(position).getProductPrice()+" /-");

        } else {
            view = convertView;
        }
        return view;

    }
}
