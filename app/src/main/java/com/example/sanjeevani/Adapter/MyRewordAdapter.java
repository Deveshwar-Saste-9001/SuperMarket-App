package com.example.sanjeevani.Adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sanjeevani.Model.CartItemModel;
import com.example.sanjeevani.Model.MyRewordModel;
import com.example.sanjeevani.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyRewordAdapter extends RecyclerView.Adapter<MyRewordAdapter.ViewHolder> {

    private List<MyRewordModel> myRewordModelList;
    private Boolean useminilayout;
    private RecyclerView coupensRecyclerView;
    private LinearLayout SelectedCoupen;
    private String productOriginalPrice;
    private TextView discountedPrice;
    private TextView coupenTitle;
    private TextView coupenExpiryDate;
    private TextView coupenBody;
    private int cartItemPosition = -1;
    private List<CartItemModel> cartItemModelList;

    public MyRewordAdapter(List<MyRewordModel> myRewordModelList, Boolean useminilayout) {
        this.myRewordModelList = myRewordModelList;
        this.useminilayout = useminilayout;
    }

    public MyRewordAdapter(List<MyRewordModel> myRewordModelList, Boolean useminilayout, RecyclerView coupensRecyclerView, LinearLayout SelectedCoupen, String productOriginalPrice, TextView coupenTitle, TextView coupenExpiryDate, TextView coupenBody, TextView discountedPrice) {
        this.myRewordModelList = myRewordModelList;
        this.useminilayout = useminilayout;
        this.coupensRecyclerView = coupensRecyclerView;
        this.SelectedCoupen = SelectedCoupen;
        this.productOriginalPrice = productOriginalPrice;
        this.coupenTitle = coupenTitle;
        this.coupenExpiryDate = coupenExpiryDate;
        this.coupenBody = coupenBody;
        this.discountedPrice = discountedPrice;

    }

    public MyRewordAdapter(int cartItemPosition, List<MyRewordModel> myRewordModelList, Boolean useminilayout, RecyclerView coupensRecyclerView, LinearLayout SelectedCoupen, String productOriginalPrice, TextView coupenTitle, TextView coupenExpiryDate, TextView coupenBody, TextView discountedPrice, List<CartItemModel> cartItemModelList) {
        this.cartItemPosition = cartItemPosition;
        this.myRewordModelList = myRewordModelList;
        this.useminilayout = useminilayout;
        this.coupensRecyclerView = coupensRecyclerView;
        this.SelectedCoupen = SelectedCoupen;
        this.productOriginalPrice = productOriginalPrice;
        this.coupenTitle = coupenTitle;
        this.coupenExpiryDate = coupenExpiryDate;
        this.coupenBody = coupenBody;
        this.discountedPrice = discountedPrice;
        this.cartItemModelList = cartItemModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (useminilayout) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mini_rewords_item_layout, parent, false);
            return new ViewHolder(view);

        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reword_item_layout, parent, false);
            return new ViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String type = myRewordModelList.get(position).getType();
        Date validity = myRewordModelList.get(position).getTimestamp();
        String body = myRewordModelList.get(position).getRewordBody();
        String lowerLimit = myRewordModelList.get(position).getLowerLimit();
        String upperLimit = myRewordModelList.get(position).getUpperLimit();
        String discountOrAmount = myRewordModelList.get(position).getDiscountORamount();
        boolean alreadyUsed = myRewordModelList.get(position).isAlreadyUsed();
        String coupenId = myRewordModelList.get(position).getCoupenId();
        holder.setData(coupenId, type, validity, body, upperLimit, lowerLimit, discountOrAmount, alreadyUsed);

    }

    @Override
    public int getItemCount() {
        return myRewordModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView Rewordtitle;
        private TextView RewordExpiryDate;
        private TextView RewordBody;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Rewordtitle = itemView.findViewById(R.id.coupen_title_reword1);
            RewordExpiryDate = itemView.findViewById(R.id.coupen_validity_reword1);
            RewordBody = itemView.findViewById(R.id.coupen_body_reword1);
        }

        private void setData(final String coupenId, final String type, final Date validity, final String body, final String upperLimit, final String lowerLimit, final String discOrAmount, final boolean alreadyUsed) {


            if (type.equals("Discount")) {
                Rewordtitle.setText(type);
            } else {
                Rewordtitle.setText("FLAT Rs." + discOrAmount + "/- OFF");
            }
            final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM YYYY");
            if (alreadyUsed) {
                RewordExpiryDate.setText("Already used");
                RewordExpiryDate.setTextColor(itemView.getContext().getResources().getColor(R.color.colorRed));
                RewordBody.setTextColor(Color.parseColor("#50ffffff"));
                Rewordtitle.setTextColor(Color.parseColor("#50ffffff"));
            } else {
                RewordExpiryDate.setTextColor(itemView.getContext().getResources().getColor(R.color.coupencolor));
                RewordExpiryDate.setText("till " + simpleDateFormat.format(validity));
                RewordBody.setTextColor(Color.parseColor("#ffffff"));
                Rewordtitle.setTextColor(Color.parseColor("#ffffff"));

            }
            RewordBody.setText(body);


            if (useminilayout) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!alreadyUsed) {
                            coupenTitle.setText(type);
                            coupenExpiryDate.setText("till " + simpleDateFormat.format(validity));
                            coupenBody.setText(body);

                            if (Long.valueOf(productOriginalPrice) > Long.valueOf(lowerLimit) && Long.valueOf(productOriginalPrice) < Long.valueOf(upperLimit)) {
                                if (type.equals("Discount")) {
                                    Long discountAmount = Long.valueOf(productOriginalPrice) * Long.valueOf(discOrAmount) / 100;
                                    discountedPrice.setText("Rs." + String.valueOf(Long.valueOf(productOriginalPrice) - discountAmount) + "/-");
                                } else {
                                    discountedPrice.setText("Rs." + String.valueOf(Long.valueOf(productOriginalPrice) - Long.valueOf(discOrAmount)) + "/-");
                                }
                                if (cartItemPosition != -1) {
                                    cartItemModelList.get(cartItemPosition).setSelectedCoupenId(coupenId);
                                }

                            } else {
                                if (cartItemPosition != -1) {
                                    cartItemModelList.get(cartItemPosition).setSelectedCoupenId(null);
                                }

                                Toast.makeText(itemView.getContext(), " Sorry ! Product does not matches the coupen terms", Toast.LENGTH_SHORT).show();
                                discountedPrice.setText("Invalid");
                            }
                            if (coupensRecyclerView.getVisibility() == View.GONE) {
                                coupensRecyclerView.setVisibility(View.VISIBLE);
                                SelectedCoupen.setVisibility(View.GONE);
                            } else {
                                coupensRecyclerView.setVisibility(View.GONE);
                                SelectedCoupen.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                });
            }

        }
    }
}
