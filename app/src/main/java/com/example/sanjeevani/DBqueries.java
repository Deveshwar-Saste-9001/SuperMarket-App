package com.example.sanjeevani;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sanjeevani.Adapter.CategoryAdapter;
import com.example.sanjeevani.Adapter.HomePageAdapter;
import com.example.sanjeevani.Adapter.MyOrderAdapter;
import com.example.sanjeevani.Fragments.HomeFragment;
import com.example.sanjeevani.Fragments.MyCartFragment;
import com.example.sanjeevani.Fragments.MyOrdersFargment;
import com.example.sanjeevani.Fragments.MyRewordFragment;
import com.example.sanjeevani.Fragments.MyWishLayout;
import com.example.sanjeevani.Model.AddressesModel;
import com.example.sanjeevani.Model.CartItemModel;
import com.example.sanjeevani.Model.CategoryModel;
import com.example.sanjeevani.Model.HomePageModel;
import com.example.sanjeevani.Model.HorizontalProductScrollModel;
import com.example.sanjeevani.Model.MyOrderItemModel;
import com.example.sanjeevani.Model.MyRewordModel;
import com.example.sanjeevani.Model.NotificationModel;
import com.example.sanjeevani.Model.SliderModel;
import com.example.sanjeevani.Model.WishListModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBqueries {

    public static FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    public static String email, fullname, profile, usernamemobile;

    public static List<CategoryModel> categoryModelList = new ArrayList<CategoryModel>();

    public static List<List<HomePageModel>> lists = new ArrayList<>();
    public static List<String> loadedCategoriesNames = new ArrayList<>();

    public static List<String> WishListlist = new ArrayList<>();
    public static List<WishListModel> wishListModelList = new ArrayList<>();

    public static List<String> myRatedIds = new ArrayList<>();
    public static List<Long> myRating = new ArrayList<>();

    public static List<String> cartList = new ArrayList<>();
    public static List<CartItemModel> cartItemModelList = new ArrayList<>();

    public static int selectedAddress = -1;
    public static List<AddressesModel> addressesModelList = new ArrayList<>();

    public static List<MyRewordModel> myRewordModelList = new ArrayList<>();

    public static List<MyOrderItemModel> myOrderItemModelList = new ArrayList<>();

    public static List<NotificationModel> notificationModelList = new ArrayList<>();
    private static ListenerRegistration registration;


    public static void loadCategories(final RecyclerView categoryRecyclerView, final Context context) {
        categoryModelList.clear();
        firebaseFirestore.collection("CATEGORIES").orderBy("index").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                categoryModelList.add(new CategoryModel(documentSnapshot.get("icon").toString(), documentSnapshot.get("categoryName").toString()));
                            }
                            CategoryAdapter categoryAdapter = new CategoryAdapter(categoryModelList);
                            categoryRecyclerView.setAdapter(categoryAdapter);
                            categoryAdapter.notifyDataSetChanged();
                        } else {
                            String error = task.getException().getMessage();
                            Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public static void loadFragmentData(final RecyclerView homePageRecyclerView, final Context context, final int index, String categoryName) {

        firebaseFirestore.collection("CATEGORIES")
                .document(categoryName).collection("TOP_DEALS").orderBy("index").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                if ((long) documentSnapshot.get("view_type") == 0) {
                                    List<SliderModel> sliderModelList = new ArrayList<>();
                                    long no_of_banners = (long) documentSnapshot.get("no_of_banner");
                                    for (long x = 1; x < no_of_banners + 1; x++) {
                                        sliderModelList.add(new SliderModel(documentSnapshot.get("banner" + x).toString(), documentSnapshot.get("banner" + x + "_background").toString()));
                                    }
                                    lists.get(index).add(new HomePageModel(0, sliderModelList));

                                } else if ((long) documentSnapshot.get("view_type") == 1) {
                                    lists.get(index).add(new HomePageModel(1, documentSnapshot.get("strip_ad_banner").toString(), documentSnapshot.get("background").toString()));


                                } else if ((long) documentSnapshot.get("view_type") == 2) {
                                    List<WishListModel> ViewAllProductList = new ArrayList<>();
                                    List<HorizontalProductScrollModel> horizontalProductScrollModelList = new ArrayList<>();
                                    long no_of_products = (long) documentSnapshot.get("no_of_products");
                                    for (long x = 1; x < no_of_products + 1; x++) {

                                        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(documentSnapshot.get("product_ID_" + x).toString()
                                                , " "
                                                , " "
                                                , " "
                                                , " "));


                                        ViewAllProductList.add(new WishListModel(documentSnapshot.get("product_ID_" + x).toString()
                                                , " "
                                                , " "
                                                , 0
                                                , " "
                                                , 0
                                                , " "
                                                , " "
                                                , false
                                                , false));

                                    }
                                    lists.get(index).add(new HomePageModel(2, documentSnapshot.get("layout_title").toString(), documentSnapshot.get("layout_background").toString(), horizontalProductScrollModelList, ViewAllProductList));

                                } else if ((long) documentSnapshot.get("view_type") == 3) {
                                    List<HorizontalProductScrollModel> GridProductlayoutModelList = new ArrayList<>();
                                    long no_of_products = (long) documentSnapshot.get("no_of_products");
                                    for (long x = 1; x < no_of_products + 1; x++) {
                                        GridProductlayoutModelList.add(new HorizontalProductScrollModel(documentSnapshot.get("product_ID_" + x).toString()
                                                , " "
                                                , " "
                                                , " "
                                                , " "));
                                    }
                                    lists.get(index).add(new HomePageModel(3, documentSnapshot.get("layout_title").toString(), documentSnapshot.get("layout_background").toString(), GridProductlayoutModelList));
                                }

                            }
                            HomePageAdapter homePageAdapter = new HomePageAdapter(lists.get(index));
                            homePageRecyclerView.setAdapter(homePageAdapter);
                            homePageAdapter.notifyDataSetChanged();
                            HomeFragment.swipeRefreshLayout.setRefreshing(false);
                        } else {
                            String error = task.getException().getMessage();
                            Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }

    public static void loadWishList(final Context context, final Dialog dialog, final boolean loadProductData) {
        WishListlist.clear();
        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_WISHLIST")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    for (long x = 0; x < (long) task.getResult().get("list_size"); x++) {
                        WishListlist.add(task.getResult().get("product_id_" + x).toString());
                        if (DBqueries.WishListlist.contains(ProductDetailsActivity.productID)) {
                            ProductDetailsActivity.ADDEDTOWISHLIST = true;
                            if (ProductDetailsActivity.addtoWishList != null) {
                                ProductDetailsActivity.addtoWishList.setSupportImageTintList(context.getResources().getColorStateList(R.color.colorPrimary));
                            }
                        } else {
                            ProductDetailsActivity.ADDEDTOWISHLIST = false;
                            if (ProductDetailsActivity.addtoWishList != null) {
                                ProductDetailsActivity.addtoWishList.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#9e9e9e")));
                            }
                        }
                        if (loadProductData) {
                            wishListModelList.clear();
                            final String productId = task.getResult().get("product_id_" + x).toString();
                            firebaseFirestore.collection("PRODUCTS").document(productId)
                                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        final DocumentSnapshot documentSnapshot = task.getResult();

                                        FirebaseFirestore.getInstance().collection("PRODUCTS").document(productId).collection("QUANTITY")
                                                .orderBy("time", Query.Direction.ASCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    if (task.getResult().getDocuments().size() < (long) documentSnapshot.get("stock_quantity")) {
                                                        wishListModelList.add(new WishListModel(productId
                                                                , documentSnapshot.get("product_image_1").toString()
                                                                , documentSnapshot.get("product_title").toString()
                                                                , (long) documentSnapshot.get("free_coupens")
                                                                , documentSnapshot.get("avarage_rating").toString()
                                                                , (long) documentSnapshot.get("total_rating")
                                                                , documentSnapshot.get("product_price").toString()
                                                                , documentSnapshot.get("cutted_price").toString()
                                                                , (boolean) documentSnapshot.get("COD"),
                                                                true));
                                                    } else {
                                                        wishListModelList.add(new WishListModel(productId
                                                                , documentSnapshot.get("product_image_1").toString()
                                                                , documentSnapshot.get("product_title").toString()
                                                                , (long) documentSnapshot.get("free_coupens")
                                                                , documentSnapshot.get("avarage_rating").toString()
                                                                , (long) documentSnapshot.get("total_rating")
                                                                , documentSnapshot.get("product_price").toString()
                                                                , documentSnapshot.get("cutted_price").toString()
                                                                , (boolean) documentSnapshot.get("COD"),
                                                                false));

                                                    }
                                                    MyWishLayout.wishListAdapter.notifyDataSetChanged();

                                                } else {
                                                    String error = task.getException().getMessage();
                                                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });


                                    } else {
                                        String error = task.getException().getMessage();
                                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                } else {
                    String error = task.getException().getMessage();
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });
    }

    public static void removeFromWishList(final int index, final Context context) {

        final String removedProductID = WishListlist.get(index);
        WishListlist.remove(index);
        Map<String, Object> updateWishList = new HashMap<>();
        for (int x = 0; x < WishListlist.size(); x++) {
            updateWishList.put("product_id_" + x, WishListlist.get(x));
        }
        updateWishList.put("list_size", (long) WishListlist.size());

        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid())
                .collection("USER_DATA").document("MY_WISHLIST")
                .set(updateWishList).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    if (wishListModelList.size() != 0) {
                        wishListModelList.remove(index);
                        MyWishLayout.wishListAdapter.notifyDataSetChanged();
                    }
                    ProductDetailsActivity.ADDEDTOWISHLIST = false;
                    Toast.makeText(context, "Removed successfully", Toast.LENGTH_SHORT).show();
                } else {
                    if (ProductDetailsActivity.addtoWishList != null) {
                        ProductDetailsActivity.addtoWishList.setSupportImageTintList(context.getResources().getColorStateList(R.color.colorPrimary));
                    }
                    WishListlist.add(index, removedProductID);
                    String error = task.getException().getMessage();
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                }
                ProductDetailsActivity.running_Wishlist_query = false;
            }
        });
    }

    public static void loadRatingList(final Context context) {

        if (!ProductDetailsActivity.running_rating_query) {
            ProductDetailsActivity.running_rating_query = true;
            myRatedIds.clear();
            myRating.clear();
            firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_RATINGS")
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        List<String> orderProdcutIds = new ArrayList<>();
                        for (int y = 0; y < myOrderItemModelList.size(); y++) {
                            orderProdcutIds.add(myOrderItemModelList.get(y).getProductId());
                        }
                        for (long x = 0; x < (long) task.getResult().get("list_size"); x++) {
                            myRatedIds.add(task.getResult().get("product_id_" + x).toString());
                            myRating.add((long) task.getResult().get("rating_" + x));
                            if (task.getResult().get("product_id_" + x).toString().equals(ProductDetailsActivity.productID)) {
                                ProductDetailsActivity.initialRating = Integer.parseInt(String.valueOf((long) task.getResult().get("rating_" + x))) - 1;
                                if (ProductDetailsActivity.RatenowContaner != null) {
                                    ProductDetailsActivity.setRating(ProductDetailsActivity.initialRating);
                                }
                            }
                            if (orderProdcutIds.contains(task.getResult().get("product_id_" + x).toString())) {
                                myOrderItemModelList.get(orderProdcutIds.indexOf(task.getResult().get("product_id_" + x).toString())).setRating(Integer.parseInt(String.valueOf((long) task.getResult().get("rating_" + x))) - 1);
                            }
                        }
                        if (MyOrdersFargment.myOrderAdapter != null) {
                            MyOrdersFargment.myOrderAdapter.notifyDataSetChanged();
                        }

                    } else {
                        String error = task.getException().getMessage();
                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                    }
                    ProductDetailsActivity.running_rating_query = false;
                }
            });
        }


    }

    public static void loadCartList(final Context context, final Dialog dialog, final boolean loadProductData, final TextView totalAmountbtn) {
        cartList.clear();
        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_CART")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    final long listsize = (long) task.getResult().get("list_size");
                    for (long x = 0; x < (long) task.getResult().get("list_size"); x++) {
                        cartList.add(task.getResult().get("product_id_" + x).toString());
                        if (cartList.contains(ProductDetailsActivity.productID)) {
                            ProductDetailsActivity.ALREADY_ADDED_TO_CART = true;
                        } else {
                            ProductDetailsActivity.ALREADY_ADDED_TO_CART = false;
                        }
                        if (loadProductData) {
                            cartItemModelList.clear();
                            final String productId = task.getResult().get("product_id_" + x).toString();
                            final long finalX = x;
                            firebaseFirestore.collection("PRODUCTS").document(productId)
                                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        final DocumentSnapshot documentSnapshot = task.getResult();
                                        FirebaseFirestore.getInstance().collection("PRODUCTS").document(productId).collection("QUANTITY")
                                                .orderBy("time", Query.Direction.ASCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    int index = 0;
                                                    if (cartList.size() >= 2) {
                                                        index = cartList.size() - 2;
                                                    }
                                                    if (task.getResult().getDocuments().size() < (long) documentSnapshot.get("stock_quantity")) {
                                                        cartItemModelList.add(new CartItemModel((boolean) documentSnapshot.get("COD"), CartItemModel.CART_ITEM
                                                                , productId
                                                                , documentSnapshot.get("product_image_1").toString()
                                                                , documentSnapshot.get("product_title").toString()
                                                                , (long) documentSnapshot.get("free_coupens")
                                                                , documentSnapshot.get("product_price").toString()
                                                                , documentSnapshot.get("cutted_price").toString()
                                                                , (long) 1
                                                                , (long) documentSnapshot.get("offers_applied")
                                                                , (long) 0
                                                                , true
                                                                , (long) documentSnapshot.get("max_quantity")
                                                                , (long) documentSnapshot.get("stock_quantity")));
                                                    } else {
                                                        cartItemModelList.add(new CartItemModel((boolean) documentSnapshot.get("COD"), CartItemModel.CART_ITEM
                                                                , productId
                                                                , documentSnapshot.get("product_image_1").toString()
                                                                , documentSnapshot.get("product_title").toString()
                                                                , (long) documentSnapshot.get("free_coupens")
                                                                , documentSnapshot.get("product_price").toString()
                                                                , documentSnapshot.get("cutted_price").toString()
                                                                , (long) 1
                                                                , (long) documentSnapshot.get("offers_applied")
                                                                , (long) 0
                                                                , false
                                                                , (long) documentSnapshot.get("max_quantity")
                                                                , (long) documentSnapshot.get("stock_quantity")));
                                                    }
                                                    if (cartList.size() == 0) {
                                                        cartItemModelList.clear();
                                                    }
                                                    if (finalX == (long) (listsize - (long)1)) {
                                                        cartItemModelList.add(new CartItemModel(CartItemModel.TOTAL_AMOUNT));
                                                        LinearLayout parent = (LinearLayout) totalAmountbtn.getParent().getParent();
                                                        parent.setVisibility(View.GONE);
                                                    }
                                                    MyCartFragment.cartAdapter.notifyDataSetChanged();
                                                } else {
                                                    String error = task.getException().getMessage();
                                                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    } else {
                                        String error = task.getException().getMessage();
                                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }

                } else {
                    String error = task.getException().getMessage();
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });

    }

    public static void removeFromCart(final int index, final Context context, final TextView totalAmountbtn) {

        final String removedProductID = cartList.get(index);
        cartList.remove(index);
        Map<String, Object> updateCartList = new HashMap<>();
        for (int x = 0; x < cartList.size(); x++) {
            updateCartList.put("product_id_" + x, cartList.get(x));
        }
        updateCartList.put("list_size", (long) cartList.size());

        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid())
                .collection("USER_DATA").document("MY_CART")
                .set(updateCartList).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    if (cartItemModelList.size() != 0) {
                        cartItemModelList.remove(index);
                        MyCartFragment.cartAdapter.notifyDataSetChanged();
                    }
                    if (cartList.size() == 0) {
                        if (DBqueries.cartItemModelList.get(DBqueries.cartItemModelList.size() - 1).getType() == CartItemModel.TOTAL_AMOUNT) {
                            LinearLayout parent = (LinearLayout) totalAmountbtn.getParent().getParent();
                            parent.setVisibility(View.GONE);
                        }
                        cartItemModelList.clear();
                    }
                    if (ProductDetailsActivity.cartItem != null) {
                        ProductDetailsActivity.cartItem.setActionView(null);
                    }

                    Toast.makeText(context, "Removed successfully", Toast.LENGTH_SHORT).show();
                } else {
                    cartList.add(index, removedProductID);
                    String error = task.getException().getMessage();
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                }
                ProductDetailsActivity.running_cart_query = false;
            }
        });
    }

    public static void loadAddresses(final Context context, final Dialog loadingDialog, final boolean gotoDelivery) {

        addressesModelList.clear();
        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_ADDRESSES")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    Intent deliveryIntent;
                    if ((long) task.getResult().get("list_size") == 0) {
                        deliveryIntent = new Intent(context, AddAdressActivity.class);
                        deliveryIntent.putExtra("INTENT", "deliveryIntent");
                    } else {
                        for (long x = 1; x < (long) task.getResult().get("list_size") + 1; x++) {
                            addressesModelList.add(new AddressesModel(task.getResult().getBoolean("selected_" + x)
                                    , task.getResult().getString("city_" + x)
                                    , task.getResult().getString("locality_" + x)
                                    , task.getResult().getString("landmark_" + x)
                                    , task.getResult().getString("flat_no_" + x)
                                    , task.getResult().getString("pincode_" + x)
                                    , task.getResult().getString("state_" + x)
                                    , task.getResult().getString("fullname_" + x)
                                    , task.getResult().getString("mobile_no_" + x)
                                    , task.getResult().getString("alter_mobile_no_" + x)
                            ));
                            if ((boolean) task.getResult().get("selected_" + x)) {
                                selectedAddress = Integer.parseInt(String.valueOf(x - 1));
                            }
                        }
                        deliveryIntent = new Intent(context, DeliveryActivity.class);
                    }
                    if (gotoDelivery) {
                        context.startActivity(deliveryIntent);
                    }
                } else {
                    String error = task.getException().getMessage();
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                }
                loadingDialog.dismiss();
            }
        });

    }

    public static void loadRewords(final Context context, final Dialog loadingDialog, final boolean onRewordFragment) {
        myRewordModelList.clear();

        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            final Date lastSeenDate = task.getResult().getDate("Last seen");

                            firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_REWARDS")
                                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                            if (documentSnapshot.get("type").toString().equals("Discount") && lastSeenDate.before(documentSnapshot.getDate("validity"))) {
                                                myRewordModelList.add(new MyRewordModel(documentSnapshot.getId()
                                                        , documentSnapshot.get("type").toString()
                                                        , documentSnapshot.get("lower_limit").toString()
                                                        , documentSnapshot.get("upper_limit").toString()
                                                        , documentSnapshot.get("percentage").toString()
                                                        , documentSnapshot.get("body").toString()
                                                        , (Date) documentSnapshot.get("validity")
                                                        , (boolean) documentSnapshot.get("already_used")
                                                ));
                                            } else if (documentSnapshot.get("type").toString().equals("Flat Rs. OFF") && lastSeenDate.before(documentSnapshot.getDate("validity"))) {
                                                myRewordModelList.add(new MyRewordModel(documentSnapshot.getId()
                                                        , documentSnapshot.get("type").toString()
                                                        , documentSnapshot.get("lower_limit").toString()
                                                        , documentSnapshot.get("upper_limit").toString()
                                                        , documentSnapshot.get("amount").toString()
                                                        , documentSnapshot.get("body").toString()
                                                        , (Date) documentSnapshot.get("validity")
                                                        , (boolean) documentSnapshot.get("already_used")
                                                ));
                                            }
                                        }
                                        if (onRewordFragment) {
                                            MyRewordFragment.myRewordAdapter.notifyDataSetChanged();
                                        }
                                    } else {
                                        String error = task.getException().getMessage();
                                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                                    }
                                    loadingDialog.dismiss();
                                }
                            });

                        } else {
                            loadingDialog.dismiss();
                            String error = task.getException().getMessage();
                            Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }

    public static void loadOrders(final Context context, @Nullable final MyOrderAdapter myOrderAdapter, final Dialog loadingDialog) {
        myOrderItemModelList.clear();
        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_ORDERS").orderBy("time", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {
                        firebaseFirestore.collection("ORDERS").document(documentSnapshot.getString("order_id")).collection("OrderItem").get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (DocumentSnapshot orderItems : task.getResult().getDocuments()) {

                                                myOrderItemModelList.add(new MyOrderItemModel(orderItems.getString("Product_Id")
                                                        , orderItems.getString("Product_Title")
                                                        , orderItems.getString("Product_Image")
                                                        , orderItems.getString("Order_Status")
                                                        , orderItems.getString("Address")
                                                        , orderItems.getString("Coupen_Id")
                                                        , orderItems.getString("Cutted_Price")
                                                        , orderItems.getDate("Ordered_Date")
                                                        , orderItems.getDate("Packed_Date")
                                                        , orderItems.getDate("Shipped_Date")
                                                        , orderItems.getDate("Delivered_Date")
                                                        , orderItems.getDate("Cancelled_Date")
                                                        , orderItems.getString("Discounted_price")
                                                        , orderItems.getLong("Free_Coupen")
                                                        , orderItems.getString("Full Name")
                                                        , orderItems.getString("ORDER_ID")
                                                        , orderItems.getString("Payment_Method")
                                                        , orderItems.getString("Pincode")
                                                        , orderItems.getString("Product_Price")
                                                        , orderItems.getLong("Product_Quantity")
                                                        , orderItems.getString("User_Id")
                                                        , orderItems.getString("Delivery_Price")
                                                        , orderItems.getBoolean("Cancellation_requested")
                                                ));
                                            }
                                            loadRatingList(context);
                                            if (myOrderAdapter != null) {
                                                myOrderAdapter.notifyDataSetChanged();
                                            }
                                        } else {
                                            String error = task.getException().getMessage();
                                            Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                                        }
                                        loadingDialog.dismiss();

                                    }
                                });
                    }

                } else {
                    loadingDialog.dismiss();
                    String error = task.getException().getMessage();
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    public static void checkNotification(boolean remove, @Nullable final TextView notifyCount) {

        if (remove) {
            registration.remove();
        } else {
            registration = firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_NOTIFICATIONS")
                    .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {

                            if (documentSnapshot != null && documentSnapshot.exists()) {
                                notificationModelList.clear();
                                int unread = 0;
                                for (long x = 0; x < (long) documentSnapshot.get("list_size"); x++) {
                                    notificationModelList.add(0, new NotificationModel(documentSnapshot.getString("Noti_Image_" + x), documentSnapshot.getString("Noti_Body_" + x), documentSnapshot.getBoolean("Readed_" + x)));
                                    if (!documentSnapshot.getBoolean("Readed_" + x)) {
                                        unread++;
                                        if (notifyCount != null) {
                                            if (unread > 0) {
                                                notifyCount.setVisibility(View.VISIBLE);
                                                if (unread < 99) {
                                                    notifyCount.setText(String.valueOf(unread));
                                                } else {
                                                    notifyCount.setText("99");
                                                }
                                            } else {
                                                notifyCount.setVisibility(View.INVISIBLE);
                                            }
                                        }
                                    }
                                }
                                if (NotificationActivity.notificationAdapter != null) {
                                    NotificationActivity.notificationAdapter.notifyDataSetChanged();
                                }

                            }

                        }
                    });
        }

    }


    public static void clearData() {
        categoryModelList.clear();
        lists.clear();
        loadedCategoriesNames.clear();
        WishListlist.clear();
        wishListModelList.clear();
        myRatedIds.clear();
        myRating.clear();
        cartList.clear();
        cartItemModelList.clear();
        addressesModelList.clear();
        myRewordModelList.clear();
        myOrderItemModelList.clear();

    }

}

