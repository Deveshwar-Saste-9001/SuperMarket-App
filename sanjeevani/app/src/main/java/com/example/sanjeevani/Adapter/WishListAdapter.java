package com.example.sanjeevani.Adapter;

import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.sanjeevani.DBqueries;
import com.example.sanjeevani.Model.WishListModel;
import com.example.sanjeevani.ProductDetailsActivity;
import com.example.sanjeevani.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class WishListAdapter extends RecyclerView.Adapter<WishListAdapter.ViewHolder> {
    private List<WishListModel> wishListModelList;
    private Boolean wishlist;
    private int lastposition = -1;
    private boolean fromsearch;

    public boolean isFromsearch() {
        return fromsearch;
    }

    public void setFromsearch(boolean fromsearch) {
        this.fromsearch = fromsearch;
    }

    public WishListAdapter(List<WishListModel> wishListModelList, Boolean wishlist) {
        this.wishListModelList = wishListModelList;
        this.wishlist = wishlist;
    }

    public List<WishListModel> getWishListModelList() {
        return wishListModelList;
    }

    public void setWishListModelList(List<WishListModel> wishListModelList) {
        this.wishListModelList = wishListModelList;
    }

    @NonNull
    @Override
    public WishListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wish_list_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WishListAdapter.ViewHolder holder, int position) {
        String productId = wishListModelList.get(position).getProductId();
        String resource = wishListModelList.get(position).getProductImageWish();
        String title = wishListModelList.get(position).getProductTitleWish();
        long freeCoupens = wishListModelList.get(position).getFreeCoupensNo();
        String rating = wishListModelList.get(position).getRating();
        long totalRatings = wishListModelList.get(position).getTotalRating();
        String productprice = wishListModelList.get(position).getProductPriceWish();
        String cuttedprice = wishListModelList.get(position).getCuttedPriceWish();
        boolean payment = wishListModelList.get(position).isCOD();
        boolean inStock = wishListModelList.get(position).isInStock();
        holder.setData(productId, resource, title, freeCoupens, rating, totalRatings, productprice, cuttedprice, payment, position, inStock);

        if (lastposition < position) {
            //Animation animation= AnimationUtils.loadAnimation(holder.itemView.getContext(),R.anim.fade_in);
            holder.itemView.setAnimation(AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.fade_in));
            lastposition = position;
        }

    }

    @Override
    public int getItemCount() {
        return wishListModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView productIamgewish;
        private TextView producttitleWish;
        private TextView freeCoupensWish;
        private ImageView coupenIconWish;
        private TextView Ratingwish;
        private TextView totalRatings;
        private View pricecut;
        private TextView productPricewish;
        private TextView cuttedPriceWish;
        private TextView paymentMethod;
        private ImageButton deletBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productIamgewish = itemView.findViewById(R.id.product_image_wish1);
            producttitleWish = itemView.findViewById(R.id.product_title_wish1);
            freeCoupensWish = itemView.findViewById(R.id.free_coupen_wish1);
            coupenIconWish = itemView.findViewById(R.id.coupen_icon_wish1);
            Ratingwish = itemView.findViewById(R.id.tv_product_rating_miniView_wish1);
            totalRatings = itemView.findViewById(R.id.total_rating_wish1);
            pricecut = itemView.findViewById(R.id.pricecut_wish1);
            productPricewish = itemView.findViewById(R.id.product_price_wish1);
            cuttedPriceWish = itemView.findViewById(R.id.cutted_price_wish1);
            paymentMethod = itemView.findViewById(R.id.payment_method_wish1);
            deletBtn = itemView.findViewById(R.id.delete_btn_wish1);

        }

        private void setData(final String productid, String resource, String title, long freeCoupensWishNo, String avragerating, long totalRatingNo, String price, String cuttedprice, boolean COD, final int index, boolean instock) {

            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.drawable.proandcatplaceholder)).into(productIamgewish);

            producttitleWish.setText(title);
            if (freeCoupensWishNo != 0 && instock) {
                coupenIconWish.setVisibility(View.VISIBLE);
                if (freeCoupensWishNo == 1) {
                    freeCoupensWish.setText("Free " + freeCoupensWishNo + " Coupen");
                } else {
                    freeCoupensWish.setText("Free " + freeCoupensWishNo + " Coupens");
                }

            } else {
                coupenIconWish.setVisibility(View.INVISIBLE);
                freeCoupensWish.setVisibility(View.INVISIBLE);
            }
            LinearLayout rating = (LinearLayout) Ratingwish.getParent();
            if (instock) {
                rating.setVisibility(View.VISIBLE);
                Ratingwish.setVisibility(View.VISIBLE);
                totalRatings.setVisibility(View.VISIBLE);
                productPricewish.setTextColor(Color.parseColor("#000000"));
                cuttedPriceWish.setVisibility(View.VISIBLE);
                paymentMethod.setVisibility(View.VISIBLE);


                Ratingwish.setText(avragerating);
                totalRatings.setText("(" + totalRatingNo + ") Ratings");
                productPricewish.setText("Rs." + price + "/-");
                cuttedPriceWish.setText("Rs." + cuttedprice + "/-");
                if (COD) {
                    paymentMethod.setVisibility(View.VISIBLE);
                } else {
                    paymentMethod.setVisibility(View.INVISIBLE);
                }
            } else {

                rating.setVisibility(View.INVISIBLE);
                Ratingwish.setVisibility(View.INVISIBLE);
                totalRatings.setVisibility(View.INVISIBLE);
                productPricewish.setText("Out of stock");
                productPricewish.setTextColor(itemView.getContext().getResources().getColor(R.color.colorRed));
                cuttedPriceWish.setVisibility(View.INVISIBLE);
                paymentMethod.setVisibility(View.INVISIBLE);


            }

            if (wishlist) {
                deletBtn.setVisibility(View.VISIBLE);
                deletBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!ProductDetailsActivity.running_Wishlist_query) {
                            ProductDetailsActivity.running_Wishlist_query = true;
                            DBqueries.removeFromWishList(index, itemView.getContext());
                        }

                    }
                });
            } else {
                deletBtn.setVisibility(View.GONE);
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (fromsearch) {
                        ProductDetailsActivity.fromSearch = true;
                    }
                    Intent productintent = new Intent(itemView.getContext(), ProductDetailsActivity.class);
                    productintent.putExtra("productID", productid);
                    itemView.getContext().startActivity(productintent);
                }
            });


        }
    }
}
