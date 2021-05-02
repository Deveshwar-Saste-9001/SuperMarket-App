package com.example.sanjeevani;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.sanjeevani.Adapter.CartAdapter;
import com.example.sanjeevani.Model.CartItemModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DeliveryActivity extends AppCompatActivity {
    private RecyclerView DeliveryRecyclerView;
    private Button ChangeAddress;
    public static final int SELECT_ADDRESS = 0;
    private TextView totalAmountBtn;

    public static List<CartItemModel> cartItemModelList;

    private TextView fullname;
    private String name, mobile;
    private TextView fullAddress;
    private TextView pincode;
    private Button continueBtn;

    public static Dialog loadingDialog;
    private Dialog paymentMethodDailog;
    private ImageButton upiPayment, COD_Btn;

    private ConstraintLayout orderConfirmationLayout;
    private ImageButton continueShoppingBtn;

    public static CartAdapter cartAdapter;

    private TextView orderID_tv;
    private String order_id;
    private boolean successResponse = false;

    public static boolean fromcart;
    private String paymentMethod = "UPI";

    public static boolean codOrderConfirmed = false;

    private FirebaseFirestore firebaseFirestore;

    //public static boolean allProductsAvailable;
    public static boolean getQtyIDs = true;

    private TextView cod_pay_indicator;
    private View divider;

    String GOOGLE_PAY_PACKAGE_NAME = "com.google.android.apps.nbu.paisa.user";


    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Toolbar toolbar = (Toolbar) findViewById(R.id.deliveryToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Delivery");

        DeliveryRecyclerView = findViewById(R.id.DeliveryRecyclerView);
        ChangeAddress = findViewById(R.id.change_or_add_adress1);
        totalAmountBtn = findViewById(R.id.total_deliver_amount1);
        fullname = findViewById(R.id.fullname_order1);
        fullAddress = findViewById(R.id.adress_order1);
        pincode = findViewById(R.id.pincode_order1);
        continueBtn = findViewById(R.id.cart_continue_btn1);

        orderConfirmationLayout = findViewById(R.id.order_confirmetion_layout);
        orderID_tv = findViewById(R.id.order_id_tv);
        continueShoppingBtn = findViewById(R.id.continue_shopping);

        loadingDialog = new Dialog(DeliveryActivity.this);
        loadingDialog.setContentView(R.layout.loading_progress_bar);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        paymentMethodDailog = new Dialog(DeliveryActivity.this);
        paymentMethodDailog.setContentView(R.layout.payment_method);
        paymentMethodDailog.setCancelable(true);
        paymentMethodDailog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background));
        paymentMethodDailog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        upiPayment = paymentMethodDailog.findViewById(R.id.UPIBtn);
        COD_Btn = paymentMethodDailog.findViewById(R.id.CODBtn);
        cod_pay_indicator = paymentMethodDailog.findViewById(R.id.cod_btn_tv);
        divider = paymentMethodDailog.findViewById(R.id.payment_devider);
        order_id = UUID.randomUUID().toString().substring(0, 28);


        firebaseFirestore = FirebaseFirestore.getInstance();
        getQtyIDs = true;

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        DeliveryRecyclerView.setLayoutManager(linearLayoutManager);

        cartAdapter = new CartAdapter(cartItemModelList, totalAmountBtn, false);
        DeliveryRecyclerView.setAdapter(cartAdapter);
        cartAdapter.notifyDataSetChanged();

        ChangeAddress.setVisibility(View.VISIBLE);

        ChangeAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getQtyIDs = false;
                Intent myAddressintent = new Intent(DeliveryActivity.this, MyAddressesActivity.class);
                myAddressintent.putExtra("MODE", SELECT_ADDRESS);
                startActivity(myAddressintent);

            }
        });

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean allProductAvailable = true;
                for (CartItemModel cartItemModel : cartItemModelList) {
                    if (cartItemModel.isQtyError()) {
                        allProductAvailable = false;
                        break;
                    }
                    if (cartItemModel.getType() == CartItemModel.CART_ITEM) {
                        if (!cartItemModel.isCod()) {
                            COD_Btn.setEnabled(false);
                            COD_Btn.setAlpha(0.5f);
                            cod_pay_indicator.setAlpha(0.5f);
                            break;
                        } else {
                            COD_Btn.setEnabled(true);
                            COD_Btn.setAlpha(1f);
                            cod_pay_indicator.setAlpha(1f);
                        }
                    }
                }
                if (allProductAvailable) {
                    paymentMethodDailog.show();
                }
            }
        });

        COD_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paymentMethod = "COD";
                placeOrderDetails();
            }
        });

        /////////////
        upiPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paymentMethod = "UPI";
                placeOrderDetails();

            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        ///accesssing qty

        if (getQtyIDs) {
            loadingDialog.show();
            for (int x = 0; x < cartItemModelList.size() - 1; x++) {
                for (int y = 0; y < cartItemModelList.get(x).getProductQuantity(); y++) {
                    final String quantityDocumentName = UUID.randomUUID().toString().substring(0, 20);
                    Map<String, Object> timeStamp = new HashMap<>();
                    timeStamp.put("time", FieldValue.serverTimestamp());

                    final int finalX = x;
                    final int finalY = y;
                    firebaseFirestore.collection("PRODUCTS").document(cartItemModelList.get(x).getProductIdcart()).collection("QUANTITY")
                            .document(quantityDocumentName).set(timeStamp)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {

                                        cartItemModelList.get(finalX).getQtyIDS().add(quantityDocumentName);
                                        if (finalY + 1 == cartItemModelList.get(finalX).getProductQuantity()) {

                                            firebaseFirestore.collection("PRODUCTS").document(cartItemModelList.get(finalX).getProductIdcart()).collection("QUANTITY")
                                                    .orderBy("time", Query.Direction.ASCENDING).limit(cartItemModelList.get(finalX).getStockQuantity()).get()
                                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                            if (task.isSuccessful()) {
                                                                List<String> serverQuantity = new ArrayList<>();
                                                                for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                                                                    serverQuantity.add(queryDocumentSnapshot.getId());
                                                                }
                                                                long AvailableQty = 0;
                                                                boolean noLongerAvailable = true;
                                                                for (String qtyid : cartItemModelList.get(finalX).getQtyIDS()) {
                                                                    cartItemModelList.get(finalX).setQtyError(false);
                                                                    if (!serverQuantity.contains(qtyid)) {
                                                                        if (noLongerAvailable) {
                                                                            cartItemModelList.get(finalX).setIn_Stock(false);
                                                                        } else {
                                                                            cartItemModelList.get(finalX).setQtyError(true);
                                                                            cartItemModelList.get(finalX).setMaxQuantity(AvailableQty);
                                                                            Toast.makeText(DeliveryActivity.this, "Sorry! all product may not available in required quantity.....", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    } else {
                                                                        AvailableQty++;
                                                                        noLongerAvailable = false;
                                                                    }
                                                                    cartAdapter.notifyDataSetChanged();
                                                                }

                                                            } else {
                                                                ////error
                                                                String error = task.getException().getMessage();
                                                                Toast.makeText(DeliveryActivity.this, error, Toast.LENGTH_SHORT).show();
                                                            }
                                                            loadingDialog.dismiss();
                                                        }
                                                    });
                                        }
                                    } else {
                                        loadingDialog.dismiss();
                                        String error = task.getException().getMessage();
                                        Toast.makeText(DeliveryActivity.this, error, Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                }
            }
        } else {
            getQtyIDs = true;
        }
        ///////////////////////////////////////////////////////////////////////////////
        name = DBqueries.addressesModelList.get(DBqueries.selectedAddress).getName();
        mobile = DBqueries.addressesModelList.get(DBqueries.selectedAddress).getMobile();
        if (DBqueries.addressesModelList.get(DBqueries.selectedAddress).getAlternetMobile().equals("")) {
            fullname.setText(name + " - " + mobile);
        } else {
            fullname.setText(name + " - " + mobile + " or " + DBqueries.addressesModelList.get(DBqueries.selectedAddress).getAlternetMobile());
        }
        String flatNo = DBqueries.addressesModelList.get(DBqueries.selectedAddress).getFlatno();
        String locality = DBqueries.addressesModelList.get(DBqueries.selectedAddress).getLocality();
        String landMark = DBqueries.addressesModelList.get(DBqueries.selectedAddress).getLandMark();
        String city = DBqueries.addressesModelList.get(DBqueries.selectedAddress).getCity();
        String state = DBqueries.addressesModelList.get(DBqueries.selectedAddress).getState();
        if (landMark.equals("")) {
            fullAddress.setText(flatNo + ", " + locality + ", " + city + ", " + state);
        } else {
            fullAddress.setText(flatNo + ", " + locality + ", " + landMark + ", " + city + ", " + state);
        }
        pincode.setText(DBqueries.addressesModelList.get(DBqueries.selectedAddress).getPincode());

        if (codOrderConfirmed) {
            showConfirmationLayout();
        }


    }

    @Override
    protected void onPause() {
        super.onPause();
        loadingDialog.dismiss();
        if (getQtyIDs) {

            for (int x = 0; x < cartItemModelList.size() - 1; x++) {
                if (!successResponse) {
                    for (final String qtyID : cartItemModelList.get(x).getQtyIDS()) {
                        final int finalX = x;
                        firebaseFirestore.collection("PRODUCTS").document(cartItemModelList.get(x).getProductIdcart()).collection("QUANTITY")
                                .document(qtyID).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                if (qtyID.equals(cartItemModelList.get(finalX).getQtyIDS().get(cartItemModelList.get(finalX).getQtyIDS().size() - 1))) {
                                    cartItemModelList.get(finalX).getQtyIDS().clear();
                                }
                            }
                        });

                    }
                } else {
                    cartItemModelList.get(x).getQtyIDS().clear();
                }

            }
        }


    }

    @Override
    public void onBackPressed() {
        if (successResponse) {
            finish();
            return;
        }
        super.onBackPressed();

    }

    private void showConfirmationLayout() {
        successResponse = true;
        codOrderConfirmed = false;
        getQtyIDs = false;
        for (int x = 0; x < cartItemModelList.size() - 1; x++) {
            for (String qtyID : cartItemModelList.get(x).getQtyIDS()) {
                firebaseFirestore.collection("PRODUCTS").document(cartItemModelList.get(x).getProductIdcart()).collection("QUANTITY").document(qtyID)
                        .update("USER_ID", FirebaseAuth.getInstance().getUid());
            }
        }
        if (HomeActivity.homeActivity != null) {
            HomeActivity.homeActivity.finish();
            HomeActivity.homeActivity = null;
            HomeActivity.showcart = false;
        } else {
            HomeActivity.resetHomeActivity = true;
        }
        if (ProductDetailsActivity.productDetailActivity != null) {
            ProductDetailsActivity.productDetailActivity.finish();
            ProductDetailsActivity.productDetailActivity = null;
        }


        ///////////////sent sms
        String SMS_API = "https://www.fast2sms.com/dev/bulk";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, SMS_API, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //////////////nothing
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //////////////nothing
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("authorization", "lgNrOsezCAP2vUcHTp9x4FoRjX0w5q7a1GDEdItKY6mfJuB8Syb538Q1JfHpdkEKhrsGXVlLiBmOA7ZM");
                return headers;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String Amount = totalAmountBtn.getText().toString().substring(3, totalAmountBtn.getText().length() - 2);
                Map<String, String> body = new HashMap<>();
                body.put("sender_id", "SMSIND");
                body.put("language", "english");
                body.put("route", "qt");
                body.put("numbers", mobile.substring(3, 13));
                body.put("message", "32557");
                body.put("variables", "{#BB#}|{#FF#}");
                body.put("variables_values", " " + Amount + "|" + order_id + " ");
                return body;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        RequestQueue requestQueue = Volley.newRequestQueue(DeliveryActivity.this);
        requestQueue.add(stringRequest);


        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, SMS_API, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //////////////nothing
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //////////////nothing
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("authorization", "lgNrOsezCAP2vUcHTp9x4FoRjX0w5q7a1GDEdItKY6mfJuB8Syb538Q1JfHpdkEKhrsGXVlLiBmOA7ZM");
                return headers;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String Amount = totalAmountBtn.getText().toString().substring(3, totalAmountBtn.getText().length() - 2);
                Map<String, String> body = new HashMap<>();
                body.put("sender_id", "SMSIND");
                body.put("language", "english");
                body.put("route", "qt");
                body.put("numbers", "7499415657");
                body.put("message", "32691");
                body.put("variables", "{#EE#}|{#CC#}|{#BB#}|{#FF#}");
                body.put("variables_values", " " + name + "|" + mobile + "|" + Amount + "|" + order_id + " ");
                return body;
            }
        };
        stringRequest2.setRetryPolicy(new DefaultRetryPolicy(
                5000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        RequestQueue requestQueue2 = Volley.newRequestQueue(DeliveryActivity.this);
        requestQueue2.add(stringRequest2);
        ///////////////////////////////////////////

        if (fromcart) {
            loadingDialog.show();

            Map<String, Object> updateCartList = new HashMap<>();
            long cartListSize = 0;
            final List<Integer> indexList = new ArrayList<>();

            for (int x = 0; x < DBqueries.cartList.size(); x++) {
                if (!cartItemModelList.get(x).isIn_Stock()) {
                    updateCartList.put("product_id_" + cartListSize, cartItemModelList.get(x).getProductIdcart());
                    cartListSize++;
                } else {
                    indexList.add(x);
                }

            }
            updateCartList.put("list_size", cartListSize);

            FirebaseFirestore.getInstance().collection("USERS").document(FirebaseAuth.getInstance().getUid())
                    .collection("USER_DATA").document("MY_CART")
                    .set(updateCartList).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        for (int x = 0; x < indexList.size(); x++) {
                            DBqueries.cartList.remove(indexList.get(x).intValue());
                            DBqueries.cartItemModelList.remove(indexList.get(x).intValue());
                            DBqueries.cartItemModelList.remove(DBqueries.cartItemModelList.size() - 1);
                        }
                    } else {
                        String error = task.getException().getMessage();
                        Toast.makeText(DeliveryActivity.this, error, Toast.LENGTH_SHORT).show();
                    }
                    loadingDialog.dismiss();
                }
            });


        }
        loadingDialog.dismiss();
        continueBtn.setEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        orderID_tv.setText("Order_ID :-" + order_id);
        orderConfirmationLayout.setVisibility(View.VISIBLE);
        continueShoppingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void placeOrderDetails() {
        loadingDialog.show();
        String userId = FirebaseAuth.getInstance().getUid();
        for (CartItemModel cartItemModel : cartItemModelList) {
            if (cartItemModel.getType() == CartItemModel.CART_ITEM) {
                Map<String, Object> orderDetails = new HashMap<>();
                orderDetails.put("ORDER_ID", order_id);
                orderDetails.put("Product_Id", cartItemModel.getProductIdcart());
                orderDetails.put("Product_Image", cartItemModel.getProductImagecart());
                orderDetails.put("Product_Title", cartItemModel.getProductTitle());
                orderDetails.put("User_Id", userId);
                orderDetails.put("Product_Quantity", cartItemModel.getProductQuantity());
                if (cartItemModel.getCuttedPrice() != null) {
                    orderDetails.put("Cutted_Price", cartItemModel.getCuttedPrice());
                } else {
                    orderDetails.put("Cutted_Price", "");
                }
                orderDetails.put("Product_Price", cartItemModel.getProductPrice());
                if (cartItemModel.getSelectedCoupenId() != null) {
                    orderDetails.put("Coupen_Id", cartItemModel.getSelectedCoupenId());
                } else {
                    orderDetails.put("Coupen_Id", "");
                }
                if (cartItemModel.getDiscountedPrice() != null) {
                    orderDetails.put("Discounted_price", cartItemModel.getDiscountedPrice());
                } else {
                    orderDetails.put("Discounted_price", "");
                }
                orderDetails.put("Ordered_Date", FieldValue.serverTimestamp());
                orderDetails.put("Packed_Date", FieldValue.serverTimestamp());
                orderDetails.put("Shipped_Date", FieldValue.serverTimestamp());
                orderDetails.put("Delivered_Date", FieldValue.serverTimestamp());
                orderDetails.put("Cancelled_Date", FieldValue.serverTimestamp());
                orderDetails.put("Order_Status", "Ordered");
                orderDetails.put("Payment_Method", paymentMethod);
                orderDetails.put("Address", fullAddress.getText());
                orderDetails.put("Full Name", fullname.getText());
                orderDetails.put("Pincode", pincode.getText());
                orderDetails.put("Free_Coupen", cartItemModel.getFreeCoupens());
                orderDetails.put("Delivery_Price", cartItemModelList.get(cartItemModelList.size() - 1).getDeliveryPrice());
                orderDetails.put("Cancellation_requested", false);

                firebaseFirestore.collection("ORDERS").document(order_id).collection("OrderItem")
                        .document(cartItemModel.getProductIdcart()).set(orderDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (!task.isSuccessful()) {
                            String error = task.getException().getMessage();
                            Toast.makeText(DeliveryActivity.this, error, Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            } else {
                Map<String, Object> orderDetails = new HashMap<>();
                orderDetails.put("Total_Item", cartItemModel.getTotalItem());
                orderDetails.put("ORDER_ID", order_id);
                orderDetails.put("time", FieldValue.serverTimestamp());
                orderDetails.put("Ordered_Date", FieldValue.serverTimestamp());
                orderDetails.put("Packed_Date", FieldValue.serverTimestamp());
                orderDetails.put("Shipped_Date", FieldValue.serverTimestamp());
                orderDetails.put("Delivered_Date", FieldValue.serverTimestamp());
                orderDetails.put("Cancelled_Date", FieldValue.serverTimestamp());
                orderDetails.put("Total_Item_Price", cartItemModel.getTotalItemPrice());
                orderDetails.put("Delivery_Price", cartItemModel.getDeliveryPrice());
                orderDetails.put("total_Amount", cartItemModel.getTotalAmount());
                orderDetails.put("Saved_Amount", cartItemModel.getSavedAmount());
                orderDetails.put("Payment_Method", paymentMethod);
                orderDetails.put("Address", fullAddress.getText());
                orderDetails.put("Full Name", fullname.getText());
                orderDetails.put("Pincode", pincode.getText());
                orderDetails.put("Payment_Status", "Not paid");
                orderDetails.put("Order_Status", "Cancelled");

                firebaseFirestore.collection("ORDERS").document(order_id).set(orderDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            if (paymentMethod.equals("UPI")) {
                                upi();
                            } else {
                                COD();
                            }
                        } else {
                            String error = task.getException().getMessage();
                            Toast.makeText(DeliveryActivity.this, error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }


        }
    }

    private void paytm() {
        paymentMethodDailog.dismiss();
        getQtyIDs = false;
        loadingDialog.show();
        if (ContextCompat.checkSelfPermission(DeliveryActivity.this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(DeliveryActivity.this, new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS}, 101);
        }

        final String M_id = "eygZGV52937469314213";
        final String customer_id = FirebaseAuth.getInstance().getUid();

        String url = "https://deveshwar.000webhostapp.com/paytm/generateChecksum.php";
        final String callBackUrl = "https://pguat.paytm.com/paytmchecksum/paytmCallback.jsp";


        RequestQueue requestQueue = Volley.newRequestQueue(DeliveryActivity.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.has("CHECKSUMHASH")) {
                        String CHECKSUMHASH = jsonObject.getString("CHECKSUMHASH");

                        PaytmPGService paytmPGService = PaytmPGService.getStagingService();
                        HashMap<String, String> paramMap = new HashMap<String, String>();
                        paramMap.put("MID", M_id);
                        paramMap.put("ORDER_ID", order_id);
                        paramMap.put("CUST_ID", customer_id);
                        paramMap.put("CHANNEL_ID", "WAP");
                        paramMap.put("TXN_AMOUNT", totalAmountBtn.getText().toString().substring(3, totalAmountBtn.getText().length() - 2));
                        paramMap.put("WEBSITE", "WEBSTAGING");
                        paramMap.put("INDUSTRY_TYPE_ID", "Retail");
                        paramMap.put("CALLBACK_URL", callBackUrl);
                        paramMap.put("CHECKSUMHASH", CHECKSUMHASH);

                        PaytmOrder paytmOrder = new PaytmOrder(paramMap);

                        paytmPGService.initialize(paytmOrder, null);
                        paytmPGService.startPaymentTransaction(DeliveryActivity.this, true, true, new PaytmPaymentTransactionCallback() {
                            @Override
                            public void onTransactionResponse(Bundle inResponse) {
                                //    Toast.makeText(getApplicationContext(), "Payment Transaction response " + inResponse.toString(), Toast.LENGTH_LONG).show();
                                if (inResponse.getString("STATUS").equals("TXN_SUCCESS")) {

                                    final Map<String, Object> updateStatus = new HashMap<>();
                                    updateStatus.put("Payment_Status", "paid");
                                    updateStatus.put("Order_Status", "Ordered");
                                    firebaseFirestore.collection("ORDERS").document(order_id).update(updateStatus).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Map<String, Object> userOrder = new HashMap<>();
                                                userOrder.put("order_id", order_id);
                                                userOrder.put("time", FieldValue.serverTimestamp());
                                                firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_ORDERS")
                                                        .document(order_id).set(userOrder).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            showConfirmationLayout();
                                                        } else {
                                                            Toast.makeText(DeliveryActivity.this, "failed to update user's OrderList", Toast.LENGTH_LONG).show();
                                                        }
                                                    }
                                                });
                                            } else {
                                                Toast.makeText(DeliveryActivity.this, "ORDER CANCELLED", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });

                                }

                            }

                            @Override
                            public void networkNotAvailable() {
                                Toast.makeText(getApplicationContext(), "Network connection error:check your internet connectivity", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void clientAuthenticationFailed(String inErrorMessage) {
                                Toast.makeText(getApplicationContext(), "Authentication failed : Server error" + inErrorMessage, Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void someUIErrorOccurred(String inErrorMessage) {
                                Toast.makeText(getApplicationContext(), "UI Error" + inErrorMessage, Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onErrorLoadingWebPage(int iniErrorCode, String inErrorMessage, String inFailingUrl) {
                                Toast.makeText(getApplicationContext(), "unable to load web page" + inErrorMessage, Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onBackPressedCancelTransaction() {
                                Toast.makeText(getApplicationContext(), "Transaction cancelled", Toast.LENGTH_LONG).show();

                            }

                            @Override
                            public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {
                                Toast.makeText(getApplicationContext(), "Transaction cancelled" + inResponse.toString(), Toast.LENGTH_LONG).show();
                            }
                        });


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                loadingDialog.dismiss();
                Toast.makeText(DeliveryActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
//                        return super.getParams();
                Map<String, String> paramMap = new HashMap<String, String>();
                paramMap.put("MID", M_id);
                paramMap.put("ORDER_ID", order_id);
                paramMap.put("CUST_ID", customer_id);
                paramMap.put("CHANNEL_ID", "WAP");
                paramMap.put("TXN_AMOUNT", totalAmountBtn.getText().toString().substring(3, totalAmountBtn.getText().length() - 2));
                paramMap.put("WEBSITE", "WEBSTAGING");
                paramMap.put("INDUSTRY_TYPE_ID", "Retail");
                paramMap.put("CALLBACK_URL", callBackUrl);

                return paramMap;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void upi() {
        paymentMethodDailog.dismiss();
        String Amount = totalAmountBtn.getText().toString().substring(3, totalAmountBtn.getText().length() - 2);
        String upi = "deveshwar.saste@ybl";
        String names = name;
        String note = "purchase";
        payUsingUpi(Amount, upi, names, note);
    }

    private void payUsingUpi(String amount, String upi, String name, String note) {
        Uri uri = Uri.parse("upi://pay").buildUpon()
                .appendQueryParameter("pa", upi)
                .appendQueryParameter("pn", name)
                .appendQueryParameter("tn", note)
                .appendQueryParameter("am", amount)
                .appendQueryParameter("cu", "INR")
                .build();

        Intent upiPayIntent = new Intent(Intent.ACTION_VIEW);
        upiPayIntent.setData(uri);
        upiPayIntent.setPackage(GOOGLE_PAY_PACKAGE_NAME);

        Intent chooser = Intent.createChooser(upiPayIntent, "Pay with");
        if (null != chooser.resolveActivity(getPackageManager())) {
            startActivityForResult(chooser, 0);
        } else {
            Toast.makeText(DeliveryActivity.this, "No UPI app found, please install one to continue", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:
                if ((resultCode == Activity.RESULT_OK) || resultCode == 11) {
                    if (data != null) {
                        String trtx = data.getStringExtra("response");
                        Log.d("UPI", "onActivityResult: " + trtx);
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add(trtx);
                        upiPaymentDataOperation(dataList);
                    } else {
                        Log.d("UPI", "onActivityResult: " + "Return data is null");
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add("nothing");
                        upiPaymentDataOperation(dataList);
                    }
                } else {
                    Log.d("UPI", "onActivityResult: " + "Return data is null");
                    ArrayList<String> dataList = new ArrayList<>();
                    dataList.add("nothing");
                    upiPaymentDataOperation(dataList);
                }
                break;
        }


    }

    private void upiPaymentDataOperation(ArrayList<String> data) {
        String str = data.get(0);
        Log.d("UPIPAY", "upiPaymentDataOperation: " + str);
        String paymentCancel = "";
        if (str == null) str = "discard";
        String status = "";
        String approvalRefNo = "";
        String response[] = str.split("&");
        for (int i = 0; i < response.length; i++) {
            String equalStr[] = response[i].split("=");
            if (equalStr.length >= 2) {
                if (equalStr[0].toLowerCase().equals("Status".toLowerCase())) {
                    status = equalStr[1].toLowerCase();
                } else if (equalStr[0].toLowerCase().equals("ApprovalRefNo".toLowerCase()) || equalStr[0].toLowerCase().equals("txnRef".toLowerCase())) {
                    approvalRefNo = equalStr[1];
                }
            } else {
                paymentCancel = "Payment cancelled by user";
            }
        }
        if (status.equals("success")) {
            Toast.makeText(DeliveryActivity.this, "Transaction successful", Toast.LENGTH_SHORT).show();
            Log.d("UPI", "responseStr: " + approvalRefNo);


            final Map<String, Object> updateStatus = new HashMap<>();
            updateStatus.put("Payment_Status", "paid");
            updateStatus.put("Transaction_ID", approvalRefNo);
            updateStatus.put("Order_Status", "Ordered");
            firebaseFirestore.collection("ORDERS").document(order_id).update(updateStatus).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Map<String, Object> userOrder = new HashMap<>();
                        userOrder.put("order_id", order_id);
                        userOrder.put("time", FieldValue.serverTimestamp());
                        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_ORDERS")
                                .document(order_id).set(userOrder).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    showConfirmationLayout();
                                } else {
                                    Toast.makeText(DeliveryActivity.this, "failed to update user's OrderList", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }

                }
            });
        } else if ("Payment cancelled by user".equals(paymentCancel)) {
            Toast.makeText(DeliveryActivity.this, "Payment cancel by user", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(DeliveryActivity.this, "Transaction failed please try again!", Toast.LENGTH_SHORT).show();
        }

    }

    private void COD() {
        getQtyIDs = false;
        paymentMethodDailog.dismiss();
        Intent otpIntent = new Intent(DeliveryActivity.this, OTPVerificationActivity.class);
        otpIntent.putExtra("mobileNo", mobile.substring(0, 13));
        otpIntent.putExtra("order_id", order_id);
        startActivity(otpIntent);
    }
}
