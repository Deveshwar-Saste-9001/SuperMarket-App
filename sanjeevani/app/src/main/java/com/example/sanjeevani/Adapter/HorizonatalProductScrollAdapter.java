package com.example.sanjeevani.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.sanjeevani.Model.HorizontalProductScrollModel;
import com.example.sanjeevani.ProductDetailsActivity;
import com.example.sanjeevani.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HorizonatalProductScrollAdapter extends RecyclerView.Adapter<HorizonatalProductScrollAdapter.ViewHolder> {

    private List<HorizontalProductScrollModel> horizontalProductScrollModelList;

    public HorizonatalProductScrollAdapter(List<HorizontalProductScrollModel> horizontalProductScrollModelList) {
        this.horizontalProductScrollModelList = horizontalProductScrollModelList;
    }

    @NonNull
    @Override
    public HorizonatalProductScrollAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_scroll_item_layout, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HorizonatalProductScrollAdapter.ViewHolder holder, int position) {
        String Resource = horizontalProductScrollModelList.get(position).getProductImage();
        String title = horizontalProductScrollModelList.get(position).getProducttitle();
        String description = horizontalProductScrollModelList.get(position).getDescription();
        String price = horizontalProductScrollModelList.get(position).getProductPrice();
        String productid = horizontalProductScrollModelList.get(position).getProductID();
        holder.setProductData(productid, Resource, title, description, price);


    }

    @Override
    public int getItemCount() {
        if (horizontalProductScrollModelList.size() > 8) {
            return 8;
        } else {
            return horizontalProductScrollModelList.size();
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView productImage;
        private TextView productTitle;
        private TextView productDescription;
        private TextView productPrice;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.product_Scroll_image);
            productTitle = itemView.findViewById(R.id.product_scroll_title1);
            productDescription = itemView.findViewById(R.id.product_scroll_offer1);
            productPrice = itemView.findViewById(R.id.product_scroll_price1);


        }

        private void setProductData(final String productID, String resource, String title, String description, String price) {
            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.drawable.proandcatplaceholder)).into(productImage);
            productTitle.setText(title);
            productDescription.setText(description);
            productPrice.setText("Rs. " + price + "/-");
            if (!title.equals("")) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(itemView.getContext(), ProductDetailsActivity.class);
                        intent.putExtra("productID", productID);
                        itemView.getContext().startActivity(intent);
                    }
                });
            }
        }
    }
}
