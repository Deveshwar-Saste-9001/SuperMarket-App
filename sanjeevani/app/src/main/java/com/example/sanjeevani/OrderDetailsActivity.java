package com.example.sanjeevani;

import android.app.Dialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.sanjeevani.Model.MyOrderItemModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class OrderDetailsActivity extends AppCompatActivity {

    private int Position;
    private TextView productTitle, price, quantity;
    private ImageView productImage;
    private ImageView orderedIndicator, packedIndicator, shippedIndicator, deliveredIndicator;
    private ProgressBar O_P_Progress, P_S_Progress, S_D_Progress;
    private TextView orderedtitle, packedtitle, shippedtitle, deliveredtitle;
    private TextView orderedDate, packedDate, shippedDate, deliveredDate;
    private TextView orderedbody, packedbody, shippedbody, deliveredbody;

    private LinearLayout rateNowContainer;
    private Dialog cancellDialog;
    private int rating;

    private TextView fullName, Address, Pincode;
    //
    private TextView totalItem, totalItemPrice, deliveryPrice, totalAmount, savedAmount;
    private Dialog loadingDialog;

    private SimpleDateFormat simpleDateFormat;
    private Button cancel_Btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarOrderDetail);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Order Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loadingDialog = new Dialog(OrderDetailsActivity.this);
        loadingDialog.setContentView(R.layout.loading_progress_bar);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.show();

        cancellDialog = new Dialog(OrderDetailsActivity.this);
        cancellDialog.setContentView(R.layout.cancel_order_confim);
        cancellDialog.setCancelable(true);
        cancellDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background));
        //cancellDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        Position = getIntent().getIntExtra("Position", -1);
        final MyOrderItemModel myOrderItemModel = DBqueries.myOrderItemModelList.get(Position);
        productTitle = findViewById(R.id.product_title_order_d1);
        price = findViewById(R.id.productPrice_order_d1);
        quantity = findViewById(R.id.product_qnt_order_d1);
        productImage = findViewById(R.id.productimage_order_d1);

        orderedIndicator = findViewById(R.id.order_dot_indicator1);
        packedIndicator = findViewById(R.id.pack_indicater1);
        shippedIndicator = findViewById(R.id.shipping_indicator1);
        deliveredIndicator = findViewById(R.id.deliverd_indicator1);

        O_P_Progress = findViewById(R.id.ordered_pack_progess1);
        P_S_Progress = findViewById(R.id.packed_shipping_progress1);
        S_D_Progress = findViewById(R.id.shipping_deliverd_progress1);

        orderedtitle = findViewById(R.id.ordered_title1);
        packedtitle = findViewById(R.id.packed_title1);
        shippedtitle = findViewById(R.id.order_shipped1);
        deliveredtitle = findViewById(R.id.order_deliverded1);

        orderedDate = findViewById(R.id.orderdatee1);
        packedDate = findViewById(R.id.packeded_date1);
        shippedDate = findViewById(R.id.shipped_date1);
        deliveredDate = findViewById(R.id.deliverded_date1);

        orderedbody = findViewById(R.id.ordered_body1);
        packedbody = findViewById(R.id.packded_body1);
        shippedbody = findViewById(R.id.shipped_body1);
        deliveredbody = findViewById(R.id.deliverded_body1);

        rateNowContainer = findViewById(R.id.order_detail_contaner1);
        fullName = findViewById(R.id.fullname_order1);
        Address = findViewById(R.id.adress_order1);
        Pincode = findViewById(R.id.pincode_order1);
//
        totalItem = findViewById(R.id.total_item1);
        totalItemPrice = findViewById(R.id.total_items_price1);
        deliveryPrice = findViewById(R.id.delivery_charge_price1);
        totalAmount = findViewById(R.id.total_price_delivery1);
        savedAmount = findViewById(R.id.Saved_Amount1);
        cancel_Btn = findViewById(R.id.cancel_Btn);

        productTitle.setText(myOrderItemModel.getProductTitleOrder());
        if (!myOrderItemModel.getDiscountedPrice().equals("")) {
            price.setText("Rs." + myOrderItemModel.getDiscountedPrice() + "/-");
        } else {
            price.setText("Rs." + myOrderItemModel.getProductPrice() + "/-");
        }
        quantity.setText("Qty " + String.valueOf(myOrderItemModel.getProductQuantity()));
        Glide.with(this).load(myOrderItemModel.getProductImageOrder()).into(productImage);

        simpleDateFormat = new SimpleDateFormat("EEE, dd MMM YYYY, hh:mm aa");

        switch (myOrderItemModel.getOrderStatus()) {
            case "Ordered":
                orderedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                orderedDate.setText(String.valueOf(simpleDateFormat.format(myOrderItemModel.getOrderedDate())));

                P_S_Progress.setVisibility(View.GONE);
                O_P_Progress.setVisibility(View.GONE);
                S_D_Progress.setVisibility(View.GONE);
                packedIndicator.setVisibility(View.GONE);
                packedtitle.setVisibility(View.GONE);
                packedbody.setVisibility(View.GONE);
                packedDate.setVisibility(View.GONE);
                shippedIndicator.setVisibility(View.GONE);
                shippedDate.setVisibility(View.GONE);
                shippedtitle.setVisibility(View.GONE);
                shippedbody.setVisibility(View.GONE);
                deliveredbody.setVisibility(View.GONE);
                deliveredDate.setVisibility(View.GONE);
                deliveredtitle.setVisibility(View.GONE);
                deliveredIndicator.setVisibility(View.GONE);

                break;
            case "Packed":
                orderedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                orderedDate.setText(String.valueOf(simpleDateFormat.format(myOrderItemModel.getOrderedDate())));
                packedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                packedDate.setText(String.valueOf(simpleDateFormat.format(myOrderItemModel.getPackedDate())));
                O_P_Progress.setProgress(100);

                P_S_Progress.setVisibility(View.GONE);
                S_D_Progress.setVisibility(View.GONE);
                shippedIndicator.setVisibility(View.GONE);
                shippedDate.setVisibility(View.GONE);
                shippedtitle.setVisibility(View.GONE);
                shippedbody.setVisibility(View.GONE);
                deliveredbody.setVisibility(View.GONE);
                deliveredDate.setVisibility(View.GONE);
                deliveredtitle.setVisibility(View.GONE);
                deliveredIndicator.setVisibility(View.GONE);
                break;
            case "Shipped":
                orderedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                orderedDate.setText(String.valueOf(simpleDateFormat.format(myOrderItemModel.getOrderedDate())));
                packedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                packedDate.setText(String.valueOf(simpleDateFormat.format(myOrderItemModel.getPackedDate())));
                O_P_Progress.setProgress(100);
                shippedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                shippedDate.setText(String.valueOf(simpleDateFormat.format(myOrderItemModel.getShippeddate())));
                P_S_Progress.setProgress(100);

                S_D_Progress.setVisibility(View.GONE);
                deliveredbody.setVisibility(View.GONE);
                deliveredDate.setVisibility(View.GONE);
                deliveredtitle.setVisibility(View.GONE);
                deliveredIndicator.setVisibility(View.GONE);
                break;
            case "Out for Delivery":
                orderedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                orderedDate.setText(String.valueOf(simpleDateFormat.format(myOrderItemModel.getOrderedDate())));

                packedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                packedDate.setText(String.valueOf(simpleDateFormat.format(myOrderItemModel.getPackedDate())));
                O_P_Progress.setProgress(100);

                shippedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                shippedDate.setText(String.valueOf(simpleDateFormat.format(myOrderItemModel.getShippeddate())));
                P_S_Progress.setProgress(100);

                deliveredIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                deliveredDate.setText(String.valueOf(simpleDateFormat.format(myOrderItemModel.getDeliveredDate())));
                S_D_Progress.setProgress(100);
                deliveredtitle.setText("Out for delivery");
                deliveredbody.setText("Your order is out for delivery");
                break;
            case "Delivered":
                orderedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                orderedDate.setText(String.valueOf(simpleDateFormat.format(myOrderItemModel.getOrderedDate())));

                packedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                packedDate.setText(String.valueOf(simpleDateFormat.format(myOrderItemModel.getPackedDate())));
                O_P_Progress.setProgress(100);

                shippedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                shippedDate.setText(String.valueOf(simpleDateFormat.format(myOrderItemModel.getShippeddate())));
                P_S_Progress.setProgress(100);

                deliveredIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                deliveredDate.setText(String.valueOf(simpleDateFormat.format(myOrderItemModel.getDeliveredDate())));
                S_D_Progress.setProgress(100);

                break;
            case "Cancelled":
                if (myOrderItemModel.getPackedDate().after(myOrderItemModel.getOrderedDate())) {

                    if (myOrderItemModel.getShippeddate().after(myOrderItemModel.getPackedDate())) {
                        orderedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                        orderedDate.setText(String.valueOf(simpleDateFormat.format(myOrderItemModel.getOrderedDate())));

                        packedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                        packedDate.setText(String.valueOf(simpleDateFormat.format(myOrderItemModel.getPackedDate())));
                        O_P_Progress.setProgress(100);

                        shippedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                        shippedDate.setText(String.valueOf(simpleDateFormat.format(myOrderItemModel.getShippeddate())));
                        P_S_Progress.setProgress(100);

                        deliveredIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorRed)));
                        deliveredDate.setText(String.valueOf(simpleDateFormat.format(myOrderItemModel.getCancelledDate())));
                        deliveredtitle.setText("Cancelled");
                        deliveredbody.setText("Your order has been cancelled");

                        S_D_Progress.setProgress(100);
                    } else {
                        orderedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                        orderedDate.setText(String.valueOf(simpleDateFormat.format(myOrderItemModel.getOrderedDate())));
                        packedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                        packedDate.setText(String.valueOf(simpleDateFormat.format(myOrderItemModel.getPackedDate())));
                        O_P_Progress.setProgress(100);
                        shippedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorRed)));
                        shippedDate.setText(String.valueOf(simpleDateFormat.format(myOrderItemModel.getCancelledDate())));
                        shippedtitle.setText("Cancelled");
                        shippedbody.setText("Your order has been cancelled");
                        P_S_Progress.setProgress(100);

                        S_D_Progress.setVisibility(View.GONE);
                        deliveredbody.setVisibility(View.GONE);
                        deliveredDate.setVisibility(View.GONE);
                        deliveredtitle.setVisibility(View.GONE);
                        deliveredIndicator.setVisibility(View.GONE);
                    }
                } else {
                    orderedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                    orderedDate.setText(String.valueOf(simpleDateFormat.format(myOrderItemModel.getOrderedDate())));
                    packedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorRed)));
                    packedDate.setText(String.valueOf(simpleDateFormat.format(myOrderItemModel.getCancelledDate())));
                    packedtitle.setText("Cancelled");
                    packedbody.setText("Your order has been cancelled");
                    O_P_Progress.setProgress(100);

                    P_S_Progress.setVisibility(View.GONE);
                    S_D_Progress.setVisibility(View.GONE);
                    shippedIndicator.setVisibility(View.GONE);
                    shippedDate.setVisibility(View.GONE);
                    shippedtitle.setVisibility(View.GONE);
                    shippedbody.setVisibility(View.GONE);
                    deliveredbody.setVisibility(View.GONE);
                    deliveredDate.setVisibility(View.GONE);
                    deliveredtitle.setVisibility(View.GONE);
                    deliveredIndicator.setVisibility(View.GONE);
                }
                break;

        }
//        rating = myOrderItemModel.getRating();
//        setRating(rating);
//        for (int x = 0; x < rateNowContainer.getChildCount(); x++) {
//            final int starposition = x;
//            rateNowContainer.getChildAt(x).setOnClickListener(new View.OnClickListener() {
//
//                @Override
//                public void onClick(View v) {
//                    setRating(starposition);
//                    final DocumentReference documentReference;
//                    documentReference = FirebaseFirestore.getInstance().collection("PRODUCTS")
//                            .document(myOrderItemModel.getProductId());
//                    FirebaseFirestore.getInstance().runTransaction(new Transaction.Function<Object>() {
//                        @Nullable
//                        @Override
//                        public Object apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
//                            DocumentSnapshot documentSnapshot = transaction.get(documentReference);
//
//                            if (rating != 0) {
//                                Long increase = documentSnapshot.getLong(starposition + 1 + "_star") + 1;
//                                Long decrease = documentSnapshot.getLong(rating + 1 + "_star") - 1;
//                                transaction.update(documentReference, starposition + 1 + "_star", increase);
//                                transaction.update(documentReference, rating + 1 + "_star", decrease);
//
//                            } else {
//                                Long increase = documentSnapshot.getLong(starposition + 1 + "_star") + 1;
//                                transaction.update(documentReference, starposition + 1 + "_star", increase);
//                            }
//
//                            return null;
//                        }
//                    }).addOnSuccessListener(new OnSuccessListener<Object>() {
//
//                        @Override
//                        public void onSuccess(Object object) {
//
//                            Map<String, Object> myrating = new HashMap<>();
//                            if (DBqueries.myRatedIds.contains(myOrderItemModel.getProductId())) {
//                                myrating.put("rating_" + DBqueries.myRatedIds.indexOf(myOrderItemModel.getProductId()), (long) starposition + 1);
//                            } else {
//                                myrating.put("list_size", (long) DBqueries.myRatedIds.size() + 1);
//                                myrating.put("product_id_" + DBqueries.myRatedIds.size(), myOrderItemModel.getProductId());
//                                myrating.put("rating_" + DBqueries.myRatedIds.size(), (long) starposition + 1);
//                            }
//                            FirebaseFirestore.getInstance().collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_RATINGS")
//                                    .update(myrating).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                @Override
//                                public void onComplete(@NonNull Task<Void> task) {
//                                    if (task.isSuccessful()) {
//                                        DBqueries.myOrderItemModelList.get(Position).setRating(starposition);
//                                        if (DBqueries.myRatedIds.contains(myOrderItemModel.getProductId())) {
//                                            DBqueries.myRating.set(DBqueries.myRatedIds.indexOf(myOrderItemModel.getProductId()), Long.parseLong(String.valueOf(starposition + 1)));
//                                        } else {
//                                            DBqueries.myRatedIds.add(myOrderItemModel.getProductId());
//                                            DBqueries.myRating.add(Long.parseLong(String.valueOf(starposition + 1)));
//                                        }
//                                    } else {
//                                        String error = task.getException().getMessage();
//                                        Toast.makeText(OrderDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
//                                    }
//                                }
//                            });
//
//                        }
//                    });
//                }
//            });
//        }

        if (myOrderItemModel.isCancellationRequested()) {
            cancel_Btn.setVisibility(View.VISIBLE);
            cancel_Btn.setEnabled(false);
            cancel_Btn.setText("Cancellation in process...");
            cancel_Btn.setTextColor(getResources().getColor(R.color.colorPrimary));
            cancel_Btn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffffff")));
        } else {
            if (myOrderItemModel.getOrderStatus().equals("Ordered") || myOrderItemModel.getOrderStatus().equals("Packed")) {
                cancel_Btn.setVisibility(View.VISIBLE);
                cancel_Btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cancellDialog.findViewById(R.id.no_btn).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                cancellDialog.dismiss();
                            }
                        });
                        cancellDialog.findViewById(R.id.yes_btn).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                cancellDialog.dismiss();
                                loadingDialog.show();
                                Map<String, Object> cancelOrder = new HashMap<>();
                                cancelOrder.put("OrderId", myOrderItemModel.getOrderId());
                                cancelOrder.put("ProductId", myOrderItemModel.getProductId());
                                cancelOrder.put("Order_Cancelled", false);

                                FirebaseFirestore.getInstance().collection("CANCELLED ORDERS").document().set(cancelOrder)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {

                                                    FirebaseFirestore.getInstance().collection("ORDERS").document(myOrderItemModel.getOrderId()).collection("OrderItem")
                                                            .document(myOrderItemModel.getProductId()).update("Cancellation_requested", true).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                myOrderItemModel.setCancellationRequested(true);
                                                                cancel_Btn.setVisibility(View.VISIBLE);
                                                                cancel_Btn.setEnabled(false);
                                                                cancel_Btn.setText("Cancellation in process...");
                                                                cancel_Btn.setTextColor(getResources().getColor(R.color.colorPrimary));
                                                                cancel_Btn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffffff")));
                                                            } else {
                                                                loadingDialog.dismiss();
                                                                String error = task.getException().getMessage();
                                                                Toast.makeText(OrderDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                                                            }
                                                            loadingDialog.dismiss();
                                                        }
                                                    });

                                                } else {
                                                    loadingDialog.dismiss();
                                                    String error = task.getException().getMessage();
                                                    Toast.makeText(OrderDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                                                }

                                            }
                                        });

                            }
                        });
                        cancellDialog.show();

                    }
                });
            }

        }

        fullName.setText(myOrderItemModel.getFullName());
        Address.setText(myOrderItemModel.getAddress());
        Pincode.setText(myOrderItemModel.getPincode());

        totalItem.setText("Price (" + myOrderItemModel.getProductQuantity() + " Items)");
        Long totalItemsPriceValue;

        if (!myOrderItemModel.getDiscountedPrice().equals("")) {
            totalItemsPriceValue = myOrderItemModel.getProductQuantity() * Long.valueOf(myOrderItemModel.getDiscountedPrice());
            totalItemPrice.setText("Rs." + totalItemsPriceValue + "/-");
        } else {
            totalItemsPriceValue = myOrderItemModel.getProductQuantity() * Long.valueOf(myOrderItemModel.getProductPrice());
            totalItemPrice.setText("Rs." + totalItemsPriceValue + "/-");
        }
        if (myOrderItemModel.getDeliveryPrice().equals("FREE")) {
            deliveryPrice.setText(myOrderItemModel.getDeliveryPrice());
            totalAmount.setText(totalItemPrice.getText());
        } else {
            deliveryPrice.setText("Rs." + myOrderItemModel.getDeliveryPrice() + "/-");
            totalAmount.setText("Rs." + (totalItemsPriceValue + Long.valueOf(myOrderItemModel.getDeliveryPrice())) + "/-");
        }

        if (!myOrderItemModel.getCuttedPrice().equals("")) {
            if (!myOrderItemModel.getDiscountedPrice().equals("")) {
                savedAmount.setText("You saved Rs." + myOrderItemModel.getProductQuantity() * (Long.valueOf(myOrderItemModel.getCuttedPrice()) - Long.valueOf(myOrderItemModel.getDiscountedPrice())) + "/- on this order ");
            } else {
                savedAmount.setText("You saved Rs." + myOrderItemModel.getProductQuantity() * (Long.valueOf(myOrderItemModel.getCuttedPrice()) - Long.valueOf(myOrderItemModel.getProductPrice())) + "/- on this order ");
            }
        } else {
            if (!myOrderItemModel.getDiscountedPrice().equals("")) {
                savedAmount.setText("You saved Rs." + myOrderItemModel.getProductQuantity() * (Long.valueOf(myOrderItemModel.getProductPrice()) - Long.valueOf(myOrderItemModel.getDiscountedPrice())) + "/- on this order ");
            } else {
                savedAmount.setText("You saved Rs.0/- on this order ");
            }
        }
        loadingDialog.dismiss();


    }

    private void setRating(int starposition) {
        int x;
        for (x = 0; x < rateNowContainer.getChildCount(); x++) {
            ImageView starbtn = (ImageView) rateNowContainer.getChildAt(x);
            starbtn.setImageTintList(ColorStateList.valueOf(Color.parseColor("#bebebe")));
            if (x <= starposition) {
                starbtn.setImageTintList(ColorStateList.valueOf(Color.parseColor("#ffbb00")));
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
