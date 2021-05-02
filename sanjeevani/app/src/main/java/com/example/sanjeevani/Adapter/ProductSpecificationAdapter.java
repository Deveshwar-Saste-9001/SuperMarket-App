package com.example.sanjeevani.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sanjeevani.Model.ProductSpecificationModel;
import com.example.sanjeevani.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProductSpecificationAdapter extends RecyclerView.Adapter<ProductSpecificationAdapter.ViewHolder> {

    private List<ProductSpecificationModel> productSpecificationModelList;

    public ProductSpecificationAdapter(List<ProductSpecificationModel> productSpecificationModelList) {
        this.productSpecificationModelList = productSpecificationModelList;
    }

    @NonNull
    @Override
    public ProductSpecificationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case ProductSpecificationModel.SPECIFICATION_TITLE:
                TextView Title = new TextView(parent.getContext());
                Title.setTypeface(null, Typeface.BOLD);
                Title.setTextColor(Color.parseColor("#000000"));
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(setDp(16, parent.getContext()), setDp(16, parent.getContext()), setDp(16, parent.getContext()), setDp(8, parent.getContext()));
                Title.setLayoutParams(layoutParams);
                return new ViewHolder(Title);


            case ProductSpecificationModel.SPECIFICATION_BODY:
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_specification_item_layout, parent, false);
                return new ViewHolder(view);

            default:
                return null;

        }

    }

    @Override
    public int getItemViewType(int position) {
        switch (productSpecificationModelList.get(position).getType()) {
            case 0:
                return ProductSpecificationModel.SPECIFICATION_TITLE;

            case 1:
                return ProductSpecificationModel.SPECIFICATION_BODY;
            default:
                return -1;

        }
    }

    @Override
    public void onBindViewHolder(@NonNull ProductSpecificationAdapter.ViewHolder holder, int position) {
        switch (productSpecificationModelList.get(position).getType()) {
            case ProductSpecificationModel.SPECIFICATION_TITLE:
                holder.setTitle(productSpecificationModelList.get(position).getTitle());
                break;

            case ProductSpecificationModel.SPECIFICATION_BODY:
                String FeatureTitle = productSpecificationModelList.get(position).getFeatureName();
                String FeatureDetail = productSpecificationModelList.get(position).getFeatureValue();
                holder.setFeatures(FeatureTitle, FeatureDetail);
                break;

            default:
                return;

        }


    }

    @Override
    public int getItemCount() {
        return productSpecificationModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView featureName;
        private TextView featureValue;
        private TextView Title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

        }

        private void setTitle(String titleText) {
            Title = (TextView) itemView;
            Title.setText(titleText);
        }

        private void setFeatures(String featuretitle, String featuredetail) {
            featureName = itemView.findViewById(R.id.feature_name1);
            featureValue = itemView.findViewById(R.id.feature_value1);
            featureName.setText(featuretitle);
            featureValue.setText(featuredetail);
        }
    }

    private int setDp(int dp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());

    }
}
