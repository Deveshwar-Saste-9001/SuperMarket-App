package com.example.sanjeevani;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.sanjeevani.Adapter.MyRewordAdapter;
import com.example.sanjeevani.Adapter.ProductDetailsAdaptor;
import com.example.sanjeevani.Adapter.ProductImagesAdapter;
import com.example.sanjeevani.Model.CartItemModel;
import com.example.sanjeevani.Model.ProductSpecificationModel;
import com.example.sanjeevani.Model.WishListModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.sanjeevani.HomeActivity.showcart;

public class ProductDetailsActivity extends AppCompatActivity {
    public static boolean running_Wishlist_query = false;
    public static boolean running_rating_query = false;
    public static boolean running_cart_query = false;

    public static boolean fromSearch = false;

    private ViewPager productImagesViewPager;
    private TextView ProductTitleDe;
    private TextView avarageRatingmini;
    private TextView totalRatingMini;
    private TextView prodcutPrice;

    private String productOriginalPrice;
    private TextView cuttedPrice;
    private TextView CODTextindicator;
    private ImageView CODindicator;
    private TextView rewordTitle;
    private ConstraintLayout rewordContainer;
    private TextView rewordBody;


    private ConstraintLayout productDetalilstabcontainer;
    private ConstraintLayout producDetailsonlycontainer;
    private ViewPager productDetailViewPager;
    private TabLayout viewPagerIndicator;
    private Dialog signInDialog;

    private List<ProductSpecificationModel> productSpecificationModelList = new ArrayList<>();
    private String productDescripton1;
    private String productotherDetails;
    private TextView productonlydescriptionbody;
    private TabLayout productDetailsTablayout;

    public static LinearLayout RatenowContaner;
    public static int initialRating;
    private TextView totalRating;
    private TextView avarageRating;
    private LinearLayout ratingsNumbercontaner;
    private TextView totalRatingsfugure;
    private LinearLayout ratingProgressBarContaner;


    private Button BuyNowBtn;
    private LinearLayout addTocartBtn;
    public static MenuItem cartItem;


    private Button coupenRedemBtn;
    private LinearLayout coupenRedimlayout;

    private boolean inStock = false;


    //////////////

    private TextView Rewordtitle1;
    private TextView RewordExpiryDate1;
    private TextView RewordBody1;
    private RecyclerView coupensRecyclerView;
    private LinearLayout SelectedCoupen;
    private TextView discountprice;
    private TextView originalPrice;

    //////////

    public static boolean ADDEDTOWISHLIST = false;
    public static boolean ALREADY_ADDED_TO_CART = false;

    private FirebaseFirestore firebaseFirestore;

    public static String productID;

    private Dialog loadingDialog;
    public static FloatingActionButton addtoWishList;
    private DocumentSnapshot documentSnapshot;
    private FirebaseUser CurrentUser;

    public static Activity productDetailActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar4);
        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle("Product");
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        initialRating = -1;

        loadingDialog = new Dialog(ProductDetailsActivity.this);
        loadingDialog.setContentView(R.layout.loading_progress_bar);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.show();

        ////////////////////////coupen dialog
        final Dialog coupenDialog = new Dialog(ProductDetailsActivity.this);
        coupenDialog.setContentView(R.layout.coupen_redime_dailog);
        coupenDialog.setCancelable(true);
        coupenDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        ImageView togleRecyclerView = coupenDialog.findViewById(R.id.toggle_recyclerView1);
        coupensRecyclerView = coupenDialog.findViewById(R.id.Coupens_recicler_view1);
        SelectedCoupen = coupenDialog.findViewById(R.id.selected_coupen_contaner1);

        Rewordtitle1 = coupenDialog.findViewById(R.id.coupen_title_reword1);
        RewordExpiryDate1 = coupenDialog.findViewById(R.id.coupen_validity_reword1);
        RewordBody1 = coupenDialog.findViewById(R.id.coupen_body_reword1);


        originalPrice = coupenDialog.findViewById(R.id.original_price_redem1);
        discountprice = coupenDialog.findViewById(R.id.diecounted_price1);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ProductDetailsActivity.this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        coupensRecyclerView.setLayoutManager(linearLayoutManager);


        togleRecyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRecyclerView();
            }
        });

////////////////////////

        addtoWishList = (FloatingActionButton) findViewById(R.id.AddtoWishListButton1);
        addtoWishList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CurrentUser == null) {
                    signInDialog.show();
                } else {
//
                    if (!running_Wishlist_query) {
                        running_Wishlist_query = true;
                        if (ADDEDTOWISHLIST) {
                            int index = DBqueries.WishListlist.indexOf(productID);
                            DBqueries.removeFromWishList(index, ProductDetailsActivity.this);
                            addtoWishList.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#9e9e9e")));
                        } else {
                            addtoWishList.setSupportImageTintList(getResources().getColorStateList(R.color.colorPrimary));
                            Map<String, Object> addProduct = new HashMap<>();
                            addProduct.put("product_id_" + String.valueOf(DBqueries.WishListlist.size()), productID);
                            addProduct.put("list_size", (long) DBqueries.WishListlist.size() + 1);

                            firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA")
                                    .document("MY_WISHLIST").update(addProduct).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        if (DBqueries.wishListModelList.size() != 0) {

                                            DBqueries.wishListModelList.add(new WishListModel(productID
                                                    , documentSnapshot.get("product_image_1").toString()
                                                    , documentSnapshot.get("product_title").toString()
                                                    , (long) documentSnapshot.get("free_coupens")
                                                    , documentSnapshot.get("avarage_rating").toString()
                                                    , (long) documentSnapshot.get("total_rating")
                                                    , documentSnapshot.get("product_price").toString()
                                                    , documentSnapshot.get("cutted_price").toString()
                                                    , (boolean) documentSnapshot.get("COD")
                                                    , inStock));
                                        }
                                        ADDEDTOWISHLIST = true;
                                        DBqueries.WishListlist.add(productID);
                                        Toast.makeText(ProductDetailsActivity.this, "Product Added to wishlist successfully", Toast.LENGTH_SHORT).show();
                                    } else {
                                        addtoWishList.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#9e9e9e")));
                                        String error = task.getException().getMessage();
                                        Toast.makeText(ProductDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                                    }
                                    running_Wishlist_query = false;

                                }
                            });
                        }
                    }
                }
            }
        });
        productImagesViewPager = findViewById(R.id.Product_images_viewPager1);
        viewPagerIndicator = findViewById(R.id.View_pager_indicator1);
        productDetailViewPager = findViewById(R.id.product_detail_Viewpager1);
        productDetailsTablayout = findViewById(R.id.Product_detail_Des_TabLayout1);
        BuyNowBtn = findViewById(R.id.buy_now_btn1);
        coupenRedemBtn = findViewById(R.id.coupen_redeem_Btn1);
        ProductTitleDe = findViewById(R.id.Product_title_detail1);
        avarageRatingmini = findViewById(R.id.tv_product_rating_miniView_wish1);
        totalRatingMini = findViewById(R.id.Total_rating_miniView1);
        prodcutPrice = findViewById(R.id.Product_Price_miniview1);
        cuttedPrice = findViewById(R.id.Cutted_Price_Product1);
        CODindicator = findViewById(R.id.CODindicator1);
        CODTextindicator = findViewById(R.id.tv_Cod_indicator1);
        rewordTitle = findViewById(R.id.Reword_title_product1);
        rewordBody = findViewById(R.id.reword_body_mini1);
        avarageRating = findViewById(R.id.avarage_rating1);
        producDetailsonlycontainer = findViewById(R.id.product_details_only_container1);
        productDetalilstabcontainer = findViewById(R.id.product_detail_tab_container1);
        productonlydescriptionbody = findViewById(R.id.Product_Details_only_body1);
        totalRating = findViewById(R.id.Total_Ratings1);
        ratingsNumbercontaner = findViewById(R.id.ratings_numbers_container1);
        totalRatingsfugure = findViewById(R.id.total_ratings_figure1);
        ratingProgressBarContaner = findViewById(R.id.ratings_progress_bar_container1);
        rewordContainer = findViewById(R.id.Reword_layout_container1);
        addTocartBtn = findViewById(R.id.Addtocartbtn);
        coupenRedimlayout = findViewById(R.id.cuupen_Redem_layout1);


        firebaseFirestore = FirebaseFirestore.getInstance();
        final List<String> productImages = new ArrayList<>();

        productID = getIntent().getStringExtra("productID");

        firebaseFirestore.collection("PRODUCTS").document(productID)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    documentSnapshot = task.getResult();

                    firebaseFirestore.collection("PRODUCTS").document(productID).collection("QUANTITY")
                            .orderBy("time", Query.Direction.ASCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (long x = 1; x < (long) documentSnapshot.get("no_of_product_images"); x++) {
                                    productImages.add(documentSnapshot.get("product_image_1").toString());
                                }
                                ProductImagesAdapter productImagesAdapter = new ProductImagesAdapter(productImages);
                                productImagesViewPager.setAdapter(productImagesAdapter);
                                ProductTitleDe.setText(documentSnapshot.get("product_title").toString());
                                avarageRatingmini.setText(documentSnapshot.get("avarage_rating").toString());
                                avarageRating.setText(documentSnapshot.get("avarage_rating").toString());
                                totalRatingMini.setText("(" + (long) documentSnapshot.get("total_rating") + ") Ratings");
                                prodcutPrice.setText("Rs." + documentSnapshot.get("product_price").toString() + "/-");
                                productOriginalPrice = documentSnapshot.get("product_price").toString();
                                /////////for coupen dialog
                                originalPrice.setText(prodcutPrice.getText());
                                MyRewordAdapter myRewordAdapter = new MyRewordAdapter(DBqueries.myRewordModelList, true, coupensRecyclerView, SelectedCoupen, productOriginalPrice, Rewordtitle1, RewordExpiryDate1, RewordBody1, discountprice);
                                coupensRecyclerView.setAdapter(myRewordAdapter);
                                myRewordAdapter.notifyDataSetChanged();
                                //////////////
                                cuttedPrice.setText("Rs." + documentSnapshot.get("cutted_price").toString() + "/-");

                                if ((boolean) documentSnapshot.get("COD")) {
                                    CODindicator.setVisibility(View.VISIBLE);
                                    CODTextindicator.setVisibility(View.VISIBLE);

                                } else {
                                    CODindicator.setVisibility(View.INVISIBLE);
                                    CODTextindicator.setVisibility(View.INVISIBLE);
                                }
                                if ((long) documentSnapshot.get("free_coupens") > (long) 0) {
                                    rewordContainer.setVisibility(View.VISIBLE);
                                    rewordTitle.setText((long) documentSnapshot.get("free_coupens") + documentSnapshot.get("free_coupen_title").toString());
                                    rewordBody.setText(documentSnapshot.get("free_coupen_body").toString());
                                } else {
                                    rewordContainer.setVisibility(View.GONE);
                                }
                                if ((boolean) documentSnapshot.get("use_tab_layout")) {
                                    productDetalilstabcontainer.setVisibility(View.VISIBLE);
                                    producDetailsonlycontainer.setVisibility(View.GONE);
                                    productDescripton1 = documentSnapshot.get("product_description").toString();
                                    productSpecificationModelList = new ArrayList<>();
                                    for (long i = 1; i < (long) documentSnapshot.get("total_spec_titles") + 1; i++) {

                                        productSpecificationModelList.add(new ProductSpecificationModel(0, documentSnapshot.get("spec_title_" + i).toString()));

                                        for (long j = 1; j < (long) documentSnapshot.get("spec_title_" + i + "_total_fields") + 1; j++) {

                                            productSpecificationModelList.add(new ProductSpecificationModel(1, documentSnapshot.get("spec_title_" + i + "_field_" + j + "_name").toString(), documentSnapshot.get("spec_title_" + i + "_field_" + j + "_value").toString()));

                                        }
                                    }
                                    productotherDetails = documentSnapshot.get("product_other_detail").toString();
                                } else {
                                    productDetalilstabcontainer.setVisibility(View.GONE);
                                    producDetailsonlycontainer.setVisibility(View.VISIBLE);
                                    productonlydescriptionbody.setText(documentSnapshot.get("product_description").toString());
                                }
                                totalRating.setText("(" + (long) documentSnapshot.get("total_rating") + ") Ratings");
                                for (int x = 0; x < 5; x++) {
                                    TextView ratingnumber = (TextView) ratingsNumbercontaner.getChildAt(x);
                                    ratingnumber.setText(String.valueOf((long) documentSnapshot.get((5 - x) + "_star")));
                                    ProgressBar progressBar = (ProgressBar) ratingProgressBarContaner.getChildAt(x);
                                    int maxProgress = Integer.parseInt(String.valueOf((long) documentSnapshot.get("total_rating")));
                                    progressBar.setMax(maxProgress);
                                    progressBar.setProgress(Integer.parseInt(String.valueOf((long) documentSnapshot.get((5 - x) + "_star"))));
                                }
                                totalRatingsfugure.setText(String.valueOf((long) documentSnapshot.get("total_rating")));
                                productDetailViewPager.setAdapter(new ProductDetailsAdaptor(getSupportFragmentManager(), productDetailsTablayout.getTabCount(), productDescripton1, productotherDetails, productSpecificationModelList));

                                if (CurrentUser != null) {
                                    if (DBqueries.myRating.size() == 0) {
                                        DBqueries.loadRatingList(ProductDetailsActivity.this);
                                    }
                                    if (DBqueries.cartList.size() == 0) {
                                        DBqueries.loadCartList(ProductDetailsActivity.this, loadingDialog, false, new TextView(ProductDetailsActivity.this));
                                    }
                                    if (DBqueries.myRewordModelList.size() == 0) {
                                        DBqueries.loadRewords(ProductDetailsActivity.this, loadingDialog, false);
                                    }
                                    if (DBqueries.WishListlist.size() == 0) {
                                        DBqueries.loadWishList(ProductDetailsActivity.this, loadingDialog, false);
                                    }
                                    if (DBqueries.cartList.size() != 0 && DBqueries.myRewordModelList.size() != 0 && DBqueries.WishListlist.size() != 0) {
                                        loadingDialog.dismiss();
                                    }
                                } else {
                                    loadingDialog.dismiss();
                                }
                                if (DBqueries.myRatedIds.contains(productID)) {
                                    int index = DBqueries.myRatedIds.indexOf(productID);
                                    initialRating = Integer.parseInt(String.valueOf(DBqueries.myRating.get(index))) - 1;
                                    setRating(initialRating);
                                }
                                if (DBqueries.cartList.contains(productID)) {
                                    ALREADY_ADDED_TO_CART = true;
                                } else {
                                    ALREADY_ADDED_TO_CART = false;
                                }
                                if (DBqueries.WishListlist.contains(productID)) {
                                    ADDEDTOWISHLIST = true;
                                    addtoWishList.setSupportImageTintList(getResources().getColorStateList(R.color.colorPrimary));
                                } else {
                                    ADDEDTOWISHLIST = false;
                                    addtoWishList.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#9e9e9e")));
                                }


                                if (task.getResult().getDocuments().size() < (long) documentSnapshot.get("stock_quantity")) {
                                    inStock = true;
                                    BuyNowBtn.setVisibility(View.VISIBLE);
                                    addTocartBtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (CurrentUser == null) {
                                                signInDialog.show();
                                            } else {
                                                if (!running_cart_query) {
                                                    running_cart_query = true;
                                                    if (ALREADY_ADDED_TO_CART) {
                                                        running_cart_query = false;
                                                        Toast.makeText(ProductDetailsActivity.this, "Already Added to Cart", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        Map<String, Object> addProduct = new HashMap<>();
                                                        addProduct.put("product_id_" + String.valueOf(DBqueries.cartList.size()), productID);
                                                        addProduct.put("list_size", (long) DBqueries.cartList.size() + 1);

                                                        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA")
                                                                .document("MY_CART").update(addProduct).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    if (DBqueries.cartItemModelList.size() != 0) {
                                                                        DBqueries.cartItemModelList.add(0, new CartItemModel((boolean) documentSnapshot.get("COD"), CartItemModel.CART_ITEM
                                                                                , productID
                                                                                , documentSnapshot.get("product_image_1").toString()
                                                                                , documentSnapshot.get("product_title").toString()
                                                                                , (long) documentSnapshot.get("free_coupens")
                                                                                , documentSnapshot.get("product_price").toString()
                                                                                , documentSnapshot.get("cutted_price").toString()
                                                                                , (long) 1
                                                                                , (long) documentSnapshot.get("offers_applied")
                                                                                , (long) 1
                                                                                , inStock
                                                                                , (long) documentSnapshot.get("max_quantity")
                                                                                , (long) documentSnapshot.get("stock_quantity")));
                                                                    }

//                                        if (DBqueries.cartList.size() == 1) {
//                                            DBqueries.cartItemModelList.add(new CartItemModel(CartItemModel.TOTAL_AMOUNT));
//                                        }

                                                                    ALREADY_ADDED_TO_CART = true;
                                                                    DBqueries.cartList.add(productID);
                                                                    Toast.makeText(ProductDetailsActivity.this, "Product Added to cart successfully", Toast.LENGTH_SHORT).show();
                                                                    invalidateOptionsMenu();
                                                                    running_cart_query = false;
                                                                } else {
                                                                    running_cart_query = false;
                                                                    String error = task.getException().getMessage();
                                                                    Toast.makeText(ProductDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });
                                                    }
                                                }

                                            }
                                        }
                                    });
                                } else {
                                    inStock = false;
                                    BuyNowBtn.setVisibility(View.GONE);
                                    TextView outofStock = (TextView) addTocartBtn.getChildAt(0);
                                    outofStock.setText("Out of Stock");
                                    outofStock.setTextColor(getResources().getColor(R.color.colorRed));
                                    outofStock.setCompoundDrawables(null, null, null, null);
                                }
                            } else {
                                String error = task.getException().getMessage();
                                Toast.makeText(ProductDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                } else {
                    loadingDialog.dismiss();
                    String error = task.getException().getMessage();
                    Toast.makeText(ProductDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                }


            }
        });

        viewPagerIndicator.setupWithViewPager(productImagesViewPager, true);


        productDetailViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(productDetailsTablayout));
        productDetailsTablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                productDetailViewPager.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {


            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
/////////////////////Rating layout

        RatenowContaner = findViewById(R.id.Rate_now_contaner1);
        for (int x = 0; x < RatenowContaner.getChildCount(); x++) {
            final int starposition = x;
            RatenowContaner.getChildAt(x).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (CurrentUser == null) {
                        signInDialog.show();
                    } else {
                        if (starposition != initialRating) {
                            if (!running_rating_query) {
                                running_rating_query = true;
                                setRating(starposition);
                                Map<String, Object> updateRating = new HashMap<>();
                                if (DBqueries.myRatedIds.contains(productID)) {
                                    TextView oldrating = (TextView) ratingsNumbercontaner.getChildAt(5 - initialRating - 1);
                                    TextView finalrating = (TextView) ratingsNumbercontaner.getChildAt(5 - starposition - 1);
                                    updateRating.put(initialRating + 1 + "_star", Long.parseLong(oldrating.getText().toString()) - 1);
                                    updateRating.put(starposition + 1 + "_star", Long.parseLong(finalrating.getText().toString()) + 1);
                                    updateRating.put("avarage_rating", calculateAvarageRating(starposition - initialRating, true));
                                } else {
                                    updateRating.put(starposition + 1 + "_star", (long) documentSnapshot.get(starposition + 1 + "_star") + 1);
                                    updateRating.put("avarage_rating", calculateAvarageRating((long) starposition + 1, false));
                                    updateRating.put("total_rating", (long) documentSnapshot.get("total_rating") + 1);
                                }
                                firebaseFirestore.collection("PRODUCTS").document(productID).update(updateRating).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Map<String, Object> myrating = new HashMap<>();
                                            if (DBqueries.myRatedIds.contains(productID)) {
                                                myrating.put("rating_" + DBqueries.myRatedIds.indexOf(productID), (long) starposition + 1);
                                            } else {
                                                myrating.put("list_size", (long) DBqueries.myRatedIds.size() + 1);
                                                myrating.put("product_id_" + DBqueries.myRatedIds.size(), productID);
                                                myrating.put("rating_" + DBqueries.myRatedIds.size(), (long) starposition + 1);
                                            }
                                            firebaseFirestore.collection("USERS").document(CurrentUser.getUid()).collection("USER_DATA").document("MY_RATINGS")
                                                    .update(myrating).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        if (DBqueries.myRatedIds.contains(productID)) {
                                                            DBqueries.myRating.set(DBqueries.myRatedIds.indexOf(productID), (long) starposition + 1);
                                                            TextView oldrating = (TextView) ratingsNumbercontaner.getChildAt(5 - initialRating - 1);
                                                            oldrating.setText(String.valueOf(Integer.parseInt(oldrating.getText().toString()) - 1));
                                                            TextView finalrating = (TextView) ratingsNumbercontaner.getChildAt(5 - starposition - 1);
                                                            finalrating.setText(String.valueOf(Integer.parseInt(finalrating.getText().toString()) + 1));
                                                        } else {
                                                            DBqueries.myRatedIds.add(productID);
                                                            DBqueries.myRating.add((long) starposition + 1);
                                                            TextView rating = (TextView) ratingsNumbercontaner.getChildAt(5 - starposition - 1);
                                                            rating.setText(String.valueOf(Integer.parseInt(rating.getText().toString()) + 1));
                                                            totalRatingMini.setText("(" + ((long) documentSnapshot.get("total_rating") + 1) + ") Ratings");
                                                            totalRating.setText("(" + ((long) documentSnapshot.get("total_rating") + 1) + ") Rated");
                                                            totalRatingsfugure.setText(String.valueOf((long) documentSnapshot.get("total_rating") + 1));
                                                            Toast.makeText(ProductDetailsActivity.this, "Thank you! for Rating", Toast.LENGTH_SHORT).show();
                                                        }

                                                        for (int x = 0; x < 5; x++) {
                                                            TextView ratingnumber = (TextView) ratingsNumbercontaner.getChildAt(x);
                                                            ProgressBar progressBar = (ProgressBar) ratingProgressBarContaner.getChildAt(x);
                                                            int maxProgress = Integer.parseInt(String.valueOf(totalRatingsfugure.getText().toString()));
                                                            progressBar.setMax(maxProgress);
                                                            progressBar.setProgress(Integer.parseInt(ratingnumber.getText().toString()));
                                                        }
                                                        initialRating = starposition;
                                                        avarageRating.setText(calculateAvarageRating(0, true));
                                                        avarageRatingmini.setText(calculateAvarageRating(0, true));
                                                        if (DBqueries.WishListlist.contains(productID) && DBqueries.wishListModelList.size() != 0) {
                                                            int index = DBqueries.WishListlist.indexOf(productID);
                                                            DBqueries.wishListModelList.get(index).setRating(avarageRating.getText().toString());
                                                            DBqueries.wishListModelList.get(index).setTotalRating(Long.parseLong(totalRatingsfugure.getText().toString()));
                                                        }
                                                    } else {
                                                        setRating(initialRating);
                                                        String error = task.getException().getMessage();
                                                        Toast.makeText(ProductDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                                                    }
                                                    running_rating_query = false;
                                                }
                                            });
                                        } else {
                                            setRating(initialRating);
                                            running_rating_query = false;
                                            String error = task.getException().getMessage();
                                            Toast.makeText(ProductDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }
                    }
                }
            });
        }
//////////////////////////ratinglyout
        BuyNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CurrentUser == null) {
                    signInDialog.show();
                } else {

                    DeliveryActivity.fromcart = false;
                    loadingDialog.show();
                    productDetailActivity = ProductDetailsActivity.this;
                    DeliveryActivity.cartItemModelList = new ArrayList<>();
                    DeliveryActivity.cartItemModelList.add(new CartItemModel((boolean) documentSnapshot.get("COD")
                            , CartItemModel.CART_ITEM
                            , productID
                            , documentSnapshot.get("product_image_1").toString()
                            , documentSnapshot.get("product_title").toString()
                            , (long) documentSnapshot.get("free_coupens")
                            , documentSnapshot.get("product_price").toString()
                            , documentSnapshot.get("cutted_price").toString()
                            , (long) 1
                            , (long) documentSnapshot.get("offers_applied")
                            , (long) 1
                            , inStock
                            , (long) documentSnapshot.get("max_quantity")
                            , (long) documentSnapshot.get("stock_quantity")));

                    DeliveryActivity.cartItemModelList.add(new CartItemModel(CartItemModel.TOTAL_AMOUNT));
                    if (DBqueries.addressesModelList.size() == 0) {
                        DBqueries.loadAddresses(ProductDetailsActivity.this, loadingDialog, true);
                    } else {
                        loadingDialog.dismiss();
                        Intent DeliveryIntent = new Intent(ProductDetailsActivity.this, DeliveryActivity.class);
                        startActivity(DeliveryIntent);
                    }

                }

            }
        });


        coupenRedemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                coupenDialog.show();
            }
        });
/////////////////////////sign
        signInDialog = new Dialog(ProductDetailsActivity.this);
        signInDialog.setContentView(R.layout.signindialog);
        signInDialog.setCancelable(true);
        signInDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final Button signinBtn = signInDialog.findViewById(R.id.sign_in_dialogbtn1);
        Button signUpBtn = signInDialog.findViewById(R.id.sign_up_dialogbtn1);
        signinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInDialog.dismiss();
                LoginActivity.disableCloseBtn = true;
                Intent signInIntent = new Intent(ProductDetailsActivity.this, LoginActivity.class);
                startActivity(signInIntent);
            }
        });
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInDialog.dismiss();
                LoginActivity.disableCloseBtn = true;
                Intent signupIntent = new Intent(ProductDetailsActivity.this, MobileOtpActivity.class);
                startActivity(signupIntent);
            }
        });
        /////////////////

    }

    @Override
    protected void onStart() {
        super.onStart();

        CurrentUser = FirebaseAuth.getInstance().getCurrentUser();
//        if (CurrentUser == null) {
//            coupenRedimlayout.setVisibility(View.GONE);
//        } else {
//            coupenRedimlayout.setVisibility(View.VISIBLE);
//        }
        if (CurrentUser != null) {
            if (DBqueries.myRating.size() == 0) {
                DBqueries.loadRatingList(ProductDetailsActivity.this);
            }
            if (DBqueries.cartList.size() == 0) {
                DBqueries.loadCartList(ProductDetailsActivity.this, loadingDialog, false, new TextView(ProductDetailsActivity.this));
            }
            if (DBqueries.myRewordModelList.size() == 0) {
                DBqueries.loadRewords(ProductDetailsActivity.this, loadingDialog, false);
            }
            if (DBqueries.WishListlist.size() == 0) {
                DBqueries.loadWishList(ProductDetailsActivity.this, loadingDialog, false);
            }
            if (DBqueries.cartList.size() != 0 && DBqueries.myRewordModelList.size() != 0 && DBqueries.WishListlist.size() != 0) {
                loadingDialog.dismiss();
            }
        } else {
            loadingDialog.dismiss();
        }
        if (DBqueries.myRatedIds.contains(productID)) {
            int index = DBqueries.myRatedIds.indexOf(productID);
            initialRating = Integer.parseInt(String.valueOf(DBqueries.myRating.get(index))) - 1;
            setRating(initialRating);

        }
        if (DBqueries.cartList.contains(productID)) {
            ALREADY_ADDED_TO_CART = true;
        } else {
            ALREADY_ADDED_TO_CART = false;
        }
        if (DBqueries.WishListlist.contains(productID)) {
            ADDEDTOWISHLIST = true;
            addtoWishList.setSupportImageTintList(getResources().getColorStateList(R.color.colorPrimary));
        } else {
            ADDEDTOWISHLIST = false;
            addtoWishList.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#9e9e9e")));
        }
        invalidateOptionsMenu();
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


    public static void setRating(int starposition) {

        for (int x = 0; x < RatenowContaner.getChildCount(); x++) {
            ImageView starbtn = (ImageView) RatenowContaner.getChildAt(x);
            starbtn.setImageTintList(ColorStateList.valueOf(Color.parseColor("#bebebe")));
            if (x <= starposition) {
                starbtn.setImageTintList(ColorStateList.valueOf(Color.parseColor("#ffbb00")));
            }
        }
    }

    private String calculateAvarageRating(long currentUserRating, boolean update) {
        Double totalStars = Double.valueOf(0);
        for (int x = 1; x < 6; x++) {
            TextView ratingNo = (TextView) ratingsNumbercontaner.getChildAt(5 - x);
            totalStars = totalStars + (Long.parseLong(ratingNo.getText().toString()) * x);
        }
        totalStars = totalStars + currentUserRating;
        if (update) {
            return String.valueOf(totalStars / Long.parseLong(totalRatingsfugure.getText().toString())).substring(0, 3);
        } else {
            return String.valueOf(totalStars / (Long.parseLong(totalRatingsfugure.getText().toString()) + 1)).substring(0, 3);
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_and_cart_icon, menu);

        cartItem = menu.findItem(R.id.action_cart3);

        if (CurrentUser != null) {
            if (DBqueries.cartList.size() > 0) {
                cartItem.setActionView(R.layout.badge_layout);
                ImageView badgeIcon = cartItem.getActionView().findViewById(R.id.badge_icon);
                badgeIcon.setImageResource(R.drawable.ic_shopping_cart_black_24dp);
                TextView badgeCount = cartItem.getActionView().findViewById(R.id.badge_count);
                badgeCount.setVisibility(View.VISIBLE);
                if (DBqueries.cartList.size() < 99) {
                    badgeCount.setText(String.valueOf(DBqueries.cartList.size()));
                } else {
                    badgeCount.setText(String.valueOf(99));
                }
                cartItem.getActionView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (CurrentUser != null) {
                            Intent cartIntent = new Intent(ProductDetailsActivity.this, HomeActivity.class);
                            showcart = true;
                            startActivity(cartIntent);
                        } else {
                            signInDialog.show();
                        }
                    }
                });
            } else {
                cartItem.setActionView(null);
            }
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_Search4) {
            if (fromSearch) {
                finish();
            } else {
                Intent searchIntent = new Intent(this, SearchActivity.class);
                startActivity(searchIntent);
            }

            return true;
        } else if (id == R.id.action_cart3) {
            if (CurrentUser != null) {
                Intent cartIntent = new Intent(ProductDetailsActivity.this, HomeActivity.class);
                showcart = true;
                startActivity(cartIntent);
            } else {
                signInDialog.show();
            }
            return true;
        } else if (id == android.R.id.home) {
            finish();
            productDetailActivity = null;
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        productDetailActivity = null;
        super.onBackPressed();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        fromSearch = false;
    }
}
