package com.example.sanjeevani.Adapter;

import android.app.Dialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.sanjeevani.DBqueries;
import com.example.sanjeevani.DeliveryActivity;
import com.example.sanjeevani.HomeActivity;
import com.example.sanjeevani.Model.CartItemModel;
import com.example.sanjeevani.Model.MyRewordModel;
import com.example.sanjeevani.ProductDetailsActivity;
import com.example.sanjeevani.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CartAdapter extends RecyclerView.Adapter {

    private List<CartItemModel> cartItemModelList;
    private int lastposition = -1;
    private TextView ccarttotalAmountbtmn;
    private boolean showDeletBtn;

    public CartAdapter(List<CartItemModel> cartItemModelList, TextView cartTotalAmount, boolean showDeletBtn) {
        this.cartItemModelList = cartItemModelList;
        this.ccarttotalAmountbtmn = cartTotalAmount;
        this.showDeletBtn = showDeletBtn;
    }

    @Override
    public int getItemViewType(int position) {
        switch (cartItemModelList.get(position).getType()) {
            case 0:
                return CartItemModel.CART_ITEM;
            case 1:
                return CartItemModel.TOTAL_AMOUNT;
            default:
                return -1;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case CartItemModel.CART_ITEM:
                View itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_layout, parent, false);
                return new CartItemViewHolder(itemview);

            case CartItemModel.TOTAL_AMOUNT:
                View totalview = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_total_amount_layout, parent, false);
                return new CartTotalAmountViewHolder(totalview);

            default:
                return null;

        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (cartItemModelList.get(position).getType()) {
            case CartItemModel.CART_ITEM:
                String productId = cartItemModelList.get(position).getProductIdcart();
                String resourse = cartItemModelList.get(position).getProductImagecart();
                String title = cartItemModelList.get(position).getProductTitle();
                long freecoupens = cartItemModelList.get(position).getFreeCoupens();
                String productPrice = cartItemModelList.get(position).getProductPrice();
                String cuttedPrice = cartItemModelList.get(position).getCuttedPrice();
                long offersApplide = cartItemModelList.get(position).getOffersApplied();
                boolean instock = cartItemModelList.get(position).isIn_Stock();
                long proQty = cartItemModelList.get(position).getProductQuantity();
                long maxQty = cartItemModelList.get(position).getMaxQuantity();
                boolean qtyError = cartItemModelList.get(position).isQtyError();
                List<String> qtyIds = cartItemModelList.get(position).getQtyIDS();
                long stockQuantity = cartItemModelList.get(position).getStockQuantity();
                boolean cod = cartItemModelList.get(position).isCod();

                ((CartItemViewHolder) holder).setItemDetail(productId, resourse, title, freecoupens, productPrice, cuttedPrice, offersApplide, position, instock, String.valueOf(proQty), maxQty, qtyError, qtyIds, stockQuantity, cod);
                break;
            case CartItemModel.TOTAL_AMOUNT:
                String deliveryprice;
                int totalItems = 0;
                int totalItemPrice = 0;
                int totalamount;
                int saveedamount = 0;
                for (int x = 0; x < cartItemModelList.size(); x++) {
                    if (cartItemModelList.get(x).getType() == CartItemModel.CART_ITEM && cartItemModelList.get(x).isIn_Stock()) {
                        int quantity = Integer.parseInt(String.valueOf(cartItemModelList.get(x).getProductQuantity()));
                        totalItems = totalItems + quantity;
                        if (TextUtils.isEmpty(cartItemModelList.get(x).getSelectedCoupenId())) {
                            totalItemPrice = totalItemPrice + Integer.parseInt(cartItemModelList.get(x).getProductPrice()) * quantity;
                        } else {
                            totalItemPrice = totalItemPrice + Integer.parseInt(cartItemModelList.get(x).getDiscountedPrice()) * quantity;
                        }
                        if (!TextUtils.isEmpty(cartItemModelList.get(x).getCuttedPrice())) {
                            saveedamount = saveedamount + (Integer.parseInt(cartItemModelList.get(x).getCuttedPrice()) - Integer.parseInt(cartItemModelList.get(x).getProductPrice())) * quantity;
                            if (!TextUtils.isEmpty(cartItemModelList.get(x).getSelectedCoupenId())) {
                                saveedamount = saveedamount + (Integer.parseInt(cartItemModelList.get(x).getProductPrice()) - Integer.parseInt(cartItemModelList.get(x).getDiscountedPrice())) * quantity;
                            }
                        } else {
                            if (!TextUtils.isEmpty(cartItemModelList.get(x).getSelectedCoupenId())) {
                                saveedamount = saveedamount + (Integer.parseInt(cartItemModelList.get(x).getProductPrice()) - Integer.parseInt(cartItemModelList.get(x).getDiscountedPrice())) * quantity;
                            }
                        }
                    }
                }
                if (totalItemPrice > 500) {
                    deliveryprice = "FREE";
                    totalamount = totalItemPrice;
                } else {
                    deliveryprice = "60";
                    totalamount = totalItemPrice + 60;
                }

                cartItemModelList.get(position).setTotalItem(totalItems);
                cartItemModelList.get(position).setTotalItemPrice(totalItemPrice);
                cartItemModelList.get(position).setDeliveryPrice(deliveryprice);
                cartItemModelList.get(position).setTotalAmount(totalamount);
                cartItemModelList.get(position).setSavedAmount(saveedamount);
                ((CartTotalAmountViewHolder) holder).settotalAmount(totalItems, totalItemPrice, deliveryprice, totalamount, saveedamount);
                break;

            default:
                return;
        }
        if (lastposition < position) {
            holder.itemView.setAnimation(AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.fade_in));
            lastposition = position;
        }

    }

    @Override
    public int getItemCount() {
        return cartItemModelList.size();
    }

    class CartItemViewHolder extends RecyclerView.ViewHolder {
        private ImageView productImage;
        private TextView productTitle;
        private ImageView freeCoupenicon;
        private TextView FreeCoupens;
        private TextView productPrice;
        private TextView cuttedPrice;
        private TextView offersApplied;
        private TextView coupensApplied;
        private TextView ProductQty;

        private LinearLayout coupen_redem_layout;

        private LinearLayout removeCartBtn;
        private Button RedemBtn;

        private TextView Rewordtitle1;
        private TextView RewordExpiryDate1;
        private TextView RewordBody1;
        private RecyclerView coupensRecyclerView;
        private LinearLayout SelectedCoupen;
        private TextView discountprice;
        private TextView originalPrice;
        private Button removeCoupenBtn;
        private Button applyCoupenBtn;
        private LinearLayout applyOrRemoveContainer;
        private TextView footerText;
        private String productOriginalPrice;
        private TextView coupenRedemptionBody;
        private ImageView codIdicator;


        public CartItemViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.product_cart_image1);
            productTitle = itemView.findViewById(R.id.product_cart_title1);

            FreeCoupens = itemView.findViewById(R.id.tv_free_cart_coupen1);
            freeCoupenicon = itemView.findViewById(R.id.Free_coupen_cart_icon1);
            productPrice = itemView.findViewById(R.id.product_cart_price1);
            cuttedPrice = itemView.findViewById(R.id.cutted_price_cart1);
            offersApplied = itemView.findViewById(R.id.Offers_cart_applied1);
            coupensApplied = itemView.findViewById(R.id.coupens_applied_cart1);
            ProductQty = itemView.findViewById(R.id.product_quantity_cart1);
            RedemBtn = itemView.findViewById(R.id.coupen_redeem_Btn1_cart);
            removeCartBtn = itemView.findViewById(R.id.Remove_item_btn_cart1);
            coupen_redem_layout = itemView.findViewById(R.id.cuupen_Redem_layout1_cart);
            coupenRedemptionBody = itemView.findViewById(R.id.tv_coupen_redimption1);
            codIdicator = itemView.findViewById(R.id.COD_indicator_cart);

        }

        private void setItemDetail(final String productID, String resource, String title, long freeCoupensnumber, final String Price, String cuttedpricetext, long offersapplidnumber, final int position, boolean inStock, final String Quantity, final long maxQty, final boolean qtyError, final List<String> qtyIds, final long stockQuantity, boolean cod) {


            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.drawable.proandcatplaceholder)).into(productImage);
            productTitle.setText(title);
            final Dialog coupenDialog = new Dialog(itemView.getContext());
            coupenDialog.setContentView(R.layout.coupen_redime_dailog);
            coupenDialog.setCancelable(false);
            coupenDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            if (cod) {
                codIdicator.setVisibility(View.VISIBLE);
            } else {
                codIdicator.setVisibility(View.INVISIBLE);
            }

            if (inStock) {
                if (freeCoupensnumber > 0) {
                    freeCoupenicon.setVisibility(View.VISIBLE);
                    FreeCoupens.setVisibility(View.VISIBLE);
                    coupen_redem_layout.setVisibility(View.VISIBLE);
                    if (freeCoupensnumber == 1) {
                        FreeCoupens.setText("Free" + freeCoupensnumber + "Coupen");
                    } else {
                        FreeCoupens.setText("Free" + freeCoupensnumber + "Coupens");
                    }
                } else {
                    coupen_redem_layout.setVisibility(View.GONE);
                    freeCoupenicon.setVisibility(View.INVISIBLE);
                    FreeCoupens.setVisibility(View.INVISIBLE);
                }
                productPrice.setText("Rs. " + Price + "/-");
                productPrice.setTextColor(Color.parseColor("#000000"));
                cuttedPrice.setText("Rs. " + cuttedpricetext + "/-");


                ////////////////////////coupen dialog


                ImageView togleRecyclerView = coupenDialog.findViewById(R.id.toggle_recyclerView1);
                coupensRecyclerView = coupenDialog.findViewById(R.id.Coupens_recicler_view1);
                SelectedCoupen = coupenDialog.findViewById(R.id.selected_coupen_contaner1);

                Rewordtitle1 = coupenDialog.findViewById(R.id.coupen_title_reword1);
                RewordExpiryDate1 = coupenDialog.findViewById(R.id.coupen_validity_reword1);
                RewordBody1 = coupenDialog.findViewById(R.id.coupen_body_reword1);
                originalPrice = coupenDialog.findViewById(R.id.original_price_redem1);
                discountprice = coupenDialog.findViewById(R.id.diecounted_price1);
                removeCoupenBtn = coupenDialog.findViewById(R.id.remove_coupenBtn);
                applyCoupenBtn = coupenDialog.findViewById(R.id.apply_coupenBtn);
                footerText = coupenDialog.findViewById(R.id.footer_Text);


                applyOrRemoveContainer = coupenDialog.findViewById(R.id.apply_or_removeBtn_container);

                footerText.setVisibility(View.GONE);
                applyOrRemoveContainer.setVisibility(View.VISIBLE);

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(itemView.getContext());
                linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
                coupensRecyclerView.setLayoutManager(linearLayoutManager);

                /////////for coupen dialog
                originalPrice.setText(productPrice.getText());
                productOriginalPrice = Price;
                MyRewordAdapter myRewordAdapter = new MyRewordAdapter(position, DBqueries.myRewordModelList, true, coupensRecyclerView, SelectedCoupen, productOriginalPrice, Rewordtitle1, RewordExpiryDate1, RewordBody1, discountprice, cartItemModelList);
                coupensRecyclerView.setAdapter(myRewordAdapter);
                myRewordAdapter.notifyDataSetChanged();

                applyCoupenBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!TextUtils.isEmpty(cartItemModelList.get(position).getSelectedCoupenId())) {
                            for (MyRewordModel rewordModel : DBqueries.myRewordModelList) {
                                if (rewordModel.getCoupenId().equals(cartItemModelList.get(position).getSelectedCoupenId())) {
                                    rewordModel.setAlreadyUsed(true);
                                    coupen_redem_layout.setBackground(itemView.getContext().getResources().getDrawable(R.drawable.reword_gredint_background));
                                    coupenRedemptionBody.setText(rewordModel.getRewordBody());
                                    RedemBtn.setText("Coupen");
                                }
                            }
                            coupensApplied.setVisibility(View.VISIBLE);
                            cartItemModelList.get(position).setDiscountedPrice(discountprice.getText().toString().substring(3, discountprice.getText().length() - 2));
                            productPrice.setText(discountprice.getText().toString());
                            String offerDiscountedAmount = String.valueOf(Long.valueOf(Price) - Long.valueOf(discountprice.getText().toString().substring(3, discountprice.getText().length() - 2)));
                            coupensApplied.setText("Coupen applied -Rs." + offerDiscountedAmount + "/-");
                            notifyItemChanged(cartItemModelList.size() - 1);
                            coupenDialog.dismiss();
                        }
                    }
                });

                removeCoupenBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for (MyRewordModel rewordModel : DBqueries.myRewordModelList) {
                            if (rewordModel.getCoupenId().equals(cartItemModelList.get(position).getSelectedCoupenId())) {
                                rewordModel.setAlreadyUsed(false);
                            }
                        }
                        Rewordtitle1.setText("coupen");
                        RewordBody1.setText("validity");

                        RewordExpiryDate1.setText("Tap the icon on the top right corner to select your coupen.");
                        coupensApplied.setVisibility(View.INVISIBLE);
                        coupen_redem_layout.setBackgroundColor(itemView.getContext().getResources().getColor(R.color.coupenRed));
                        coupenRedemptionBody.setText("Apply your coupen here");
                        RedemBtn.setText("Redem");
                        productPrice.setText("Rs." + Price + "/-");
                        cartItemModelList.get(position).setSelectedCoupenId(null);
                        notifyItemChanged(cartItemModelList.size() - 1);
                        coupenDialog.dismiss();
                    }
                });


                togleRecyclerView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showRecyclerView();
                    }
                });


                if (!TextUtils.isEmpty(cartItemModelList.get(position).getSelectedCoupenId())) {
                    for (MyRewordModel rewordModel : DBqueries.myRewordModelList) {
                        if (rewordModel.getCoupenId().equals(cartItemModelList.get(position).getSelectedCoupenId())) {
                            coupen_redem_layout.setBackground(itemView.getContext().getResources().getDrawable(R.drawable.reword_gredint_background));
                            coupenRedemptionBody.setText(rewordModel.getRewordBody());

                            RedemBtn.setText("Coupen");

                            RewordBody1.setText(rewordModel.getRewordBody());
                            if (rewordModel.getType().equals("Discount")) {
                                Rewordtitle1.setText(rewordModel.getType());
                            } else {
                                Rewordtitle1.setText("FLAT Rs." + rewordModel.getDiscountORamount() + "/- OFF");
                            }
                            final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM YYYY");
                            RewordExpiryDate1.setText("till " + simpleDateFormat.format(rewordModel.getTimestamp()));
                        }
                    }
                    discountprice.setText("Rs." + cartItemModelList.get(position).getDiscountedPrice() + "/-");
                    coupensApplied.setVisibility(View.VISIBLE);
                    productPrice.setText("Rs." + cartItemModelList.get(position).getDiscountedPrice() + "/-");
                    String offerDiscountedAmount = String.valueOf(Long.valueOf(Price) - Long.valueOf(cartItemModelList.get(position).getDiscountedPrice()));
                    coupensApplied.setText("Coupen applied -Rs." + offerDiscountedAmount + "/-");
                } else {
                    coupensApplied.setVisibility(View.INVISIBLE);
                    coupen_redem_layout.setBackgroundColor(itemView.getContext().getResources().getColor(R.color.coupenRed));
                    coupenRedemptionBody.setText("Apply your coupen here");
                    RedemBtn.setText("Redem");
                }
/////
                // //////
                ProductQty.setText("Qty: " + Quantity);

                if (!showDeletBtn) {
                    if (qtyError) {
                        ProductQty.setTextColor(itemView.getContext().getResources().getColor(R.color.colorRed));
                        ProductQty.setBackgroundTintList(ColorStateList.valueOf(itemView.getContext().getResources().getColor(R.color.colorRed)));
                    } else {
                        ProductQty.setTextColor(itemView.getContext().getResources().getColor(android.R.color.black));
                        ProductQty.setBackgroundTintList(ColorStateList.valueOf(itemView.getContext().getResources().getColor(android.R.color.black)));
                    }
                }

                ProductQty.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Dialog QtyDialog = new Dialog(itemView.getContext());
                        QtyDialog.setContentView(R.layout.qty_item_layout);
                        QtyDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        QtyDialog.setCancelable(false);

                        final EditText quantityNo = QtyDialog.findViewById(R.id.qty_chnage_text_dia1);
                        quantityNo.setHint("Max " + String.valueOf(maxQty));
                        Button cancleBtn = QtyDialog.findViewById(R.id.cancle_btn_dia1);
                        Button saveBtn = QtyDialog.findViewById(R.id.save_btn_dia1);

                        cancleBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                QtyDialog.dismiss();
                            }
                        });
                        saveBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (!TextUtils.isEmpty(quantityNo.getText())) {
                                    if (Long.parseLong(quantityNo.getText().toString()) <= maxQty && Long.parseLong(quantityNo.getText().toString()) != 0) {
                                        if (itemView.getContext() instanceof HomeActivity) {
                                            cartItemModelList.get(position).setProductQuantity(Long.valueOf(quantityNo.getText().toString()));
                                        } else {
                                            if (DeliveryActivity.fromcart) {
                                                cartItemModelList.get(position).setProductQuantity(Long.valueOf(quantityNo.getText().toString()));
                                            } else {
                                                DeliveryActivity.cartItemModelList.get(position).setProductQuantity(Long.valueOf(quantityNo.getText().toString()));
                                            }
                                        }
                                        ProductQty.setText("Qty: " + quantityNo.getText().toString());
                                        notifyItemChanged(cartItemModelList.size() - 1);
                                        if (!showDeletBtn) {
                                            DeliveryActivity.loadingDialog.show();
                                            DeliveryActivity.cartItemModelList.get(position).setQtyError(false);
                                            final int initialQuantity = Integer.parseInt(Quantity);
                                            final int finalQuantity = Integer.parseInt(quantityNo.getText().toString());
                                            final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
                                            if (finalQuantity > initialQuantity) {

                                                for (int y = 0; y < finalQuantity - initialQuantity; y++) {
                                                    final String quantityDocumentName = UUID.randomUUID().toString().substring(0, 20);
                                                    Map<String, Object> timeStamp = new HashMap<>();
                                                    timeStamp.put("time", FieldValue.serverTimestamp());

                                                    final int finalY = y;
                                                    firebaseFirestore.collection("PRODUCTS").document(productID).collection("QUANTITY")
                                                            .document(quantityDocumentName).set(timeStamp)
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    qtyIds.add(quantityDocumentName);

                                                                    if (finalY + 1 == finalQuantity - initialQuantity) {

                                                                        firebaseFirestore.collection("PRODUCTS").document(productID).collection("QUANTITY")
                                                                                .orderBy("time", Query.Direction.ASCENDING).limit(stockQuantity).get()
                                                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                                    @Override
                                                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                                        if (task.isSuccessful()) {
                                                                                            List<String> serverQuantity = new ArrayList<>();
                                                                                            for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                                                                                                serverQuantity.add(queryDocumentSnapshot.getId());
                                                                                            }
                                                                                            long availableQty = 0;
                                                                                            for (String qtyid : qtyIds) {
                                                                                                if (!serverQuantity.contains(qtyid)) {
                                                                                                    DeliveryActivity.cartItemModelList.get(position).setQtyError(true);
                                                                                                    DeliveryActivity.cartItemModelList.get(position).setMaxQuantity(availableQty);
                                                                                                    Toast.makeText(itemView.getContext(), "Sorry! all product may not available in required quantity.....", Toast.LENGTH_SHORT).show();
                                                                                                } else {
                                                                                                    availableQty++;
                                                                                                }
                                                                                                DeliveryActivity.cartAdapter.notifyDataSetChanged();
                                                                                            }
                                                                                        } else {
                                                                                            ////error
                                                                                            String error = task.getException().getMessage();
                                                                                            Toast.makeText(itemView.getContext(), error, Toast.LENGTH_SHORT).show();
                                                                                        }
                                                                                        DeliveryActivity.loadingDialog.dismiss();
                                                                                    }
                                                                                });
                                                                    }
                                                                }
                                                            });
                                                }
                                            } else if (initialQuantity > finalQuantity) {
                                                for (int x = 0; x < initialQuantity - finalQuantity; x++) {
                                                    final String qtyID = qtyIds.get(qtyIds.size() - 1 - x);
                                                    final int finalX = x;
                                                    firebaseFirestore.collection("PRODUCTS").document(productID).collection("QUANTITY")
                                                            .document(qtyID).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            qtyIds.remove(qtyID);
                                                            DeliveryActivity.cartAdapter.notifyDataSetChanged();
                                                            if (finalX + 1 < initialQuantity - finalQuantity) {
                                                                DeliveryActivity.loadingDialog.dismiss();
                                                            }
                                                        }
                                                    });
                                                }
                                            }
                                        }
                                    } else {
                                        Toast.makeText(itemView.getContext(), "Max quantity: " + maxQty, Toast.LENGTH_SHORT).show();
                                    }
                                }
                                QtyDialog.dismiss();
                            }
                        });
                        QtyDialog.show();
                    }
                });
                coupensApplied.setVisibility(View.VISIBLE);
                if (offersapplidnumber > 0) {
                    offersApplied.setVisibility(View.VISIBLE);
                    String offerDiscountedAmount = String.valueOf(Long.valueOf(cuttedpricetext) - Long.valueOf(Price));
                    offersApplied.setText("Offer applied - Rs." + offerDiscountedAmount + "/-");
                } else {
                    offersApplied.setVisibility(View.INVISIBLE);
                }
            } else {
                productPrice.setText("out of Stock");
                productPrice.setTextColor(itemView.getContext().getResources().getColor(R.color.colorRed));
                cuttedPrice.setText("");
                coupen_redem_layout.setVisibility(View.GONE);
                ProductQty.setVisibility(View.INVISIBLE);
                freeCoupenicon.setVisibility(View.INVISIBLE);
                FreeCoupens.setVisibility(View.INVISIBLE);
                offersApplied.setVisibility(View.INVISIBLE);
                coupensApplied.setVisibility(View.INVISIBLE);
            }

            if (showDeletBtn) {
                removeCartBtn.setVisibility(View.VISIBLE);
            } else {
                removeCartBtn.setVisibility(View.GONE);
            }

            RedemBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (MyRewordModel rewordModel : DBqueries.myRewordModelList) {
                        if (rewordModel.getCoupenId().equals(cartItemModelList.get(position).getSelectedCoupenId())) {
                            rewordModel.setAlreadyUsed(false);
                        }
                    }
                    coupenDialog.show();
                }
            });
            ////////////////////////

            removeCartBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!TextUtils.isEmpty(cartItemModelList.get(position).getSelectedCoupenId())) {
                        for (MyRewordModel rewordModel : DBqueries.myRewordModelList) {
                            if (rewordModel.getCoupenId().equals(cartItemModelList.get(position).getSelectedCoupenId())) {
                                rewordModel.setAlreadyUsed(false);
                            }
                        }
                    }


                    if (!ProductDetailsActivity.running_cart_query) {
                        ProductDetailsActivity.running_cart_query = true;
                        DBqueries.removeFromCart(position, itemView.getContext(), ccarttotalAmountbtmn);
                    }


                }
            });


        }

        private void showRecyclerView() {
            if (coupensRecyclerView.getVisibility() == View.GONE) {
                coupensRecyclerView.setVisibility(View.VISIBLE);
                SelectedCoupen.setVisibility(View.GONE);
            } else {
                coupensRecyclerView.setVisibility(View.GONE);
                SelectedCoupen.setVisibility(View.VISIBLE);
            }

        }
    }

    class CartTotalAmountViewHolder extends RecyclerView.ViewHolder {
        private TextView TotalItems;
        private TextView TotalPrice;
        private TextView DeliveryPrice;
        private TextView TotalAmount;
        private TextView saveAmount;

        public CartTotalAmountViewHolder(@NonNull View itemView) {
            super(itemView);
            TotalItems = itemView.findViewById(R.id.total_item1);
            TotalPrice = itemView.findViewById(R.id.total_items_price1);
            DeliveryPrice = itemView.findViewById(R.id.delivery_charge_price1);
            TotalAmount = itemView.findViewById(R.id.total_price_delivery1);
            saveAmount = itemView.findViewById(R.id.Saved_Amount1);
        }

        private void settotalAmount(int totalitemtxt, int totalpricetxt, String deliveripricetxt, int totalamounttxt, int saveamounttxt) {
            TotalItems.setText("Price " + totalitemtxt + " items");
            TotalPrice.setText("Rs. " + totalpricetxt + "/-");
            if (deliveripricetxt.equals("FREE")) {
                DeliveryPrice.setText(deliveripricetxt);
            } else {
                DeliveryPrice.setText("Rs. " + deliveripricetxt + "/-");
            }
            TotalAmount.setText("Rs." + totalamounttxt + "/-");
            ccarttotalAmountbtmn.setText("Rs." + totalamounttxt + "/-");
            saveAmount.setText("You saved Rs." + saveamounttxt + "/- on this order.");
            LinearLayout parent = (LinearLayout) ccarttotalAmountbtmn.getParent().getParent();
            if (totalpricetxt == 0) {
                if (DeliveryActivity.fromcart) {
                    cartItemModelList.remove(cartItemModelList.size() - 1);
                    DeliveryActivity.cartItemModelList.remove(cartItemModelList.size() - 1);
                }
                if (showDeletBtn) {
                    cartItemModelList.remove(cartItemModelList.size() - 1);
                }
                parent.setVisibility(View.GONE);
            } else {
                parent.setVisibility(View.VISIBLE);
            }

        }
    }
}
