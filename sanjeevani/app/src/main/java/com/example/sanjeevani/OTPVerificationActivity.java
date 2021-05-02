package com.example.sanjeevani;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class OTPVerificationActivity extends AppCompatActivity {

    private TextView mobileNumber;
    private EditText otp;
    private Button verifyBtn;
    private String userNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpverification);
        mobileNumber = findViewById(R.id.phone_number);
        otp = findViewById(R.id.OTP_for_verification);
        verifyBtn = findViewById(R.id.verify_Order_Btn);
        userNo = getIntent().getStringExtra("mobileNo");
        mobileNumber.setText("Verification code has been sent to " + userNo);

        Random random = new Random();
        final int OTP_Number = random.nextInt(999999 - 111111) + 111111;
        String SMS_API = "https://www.fast2sms.com/dev/bulk";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, SMS_API, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                verifyBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (otp.getText().toString().equals(String.valueOf(OTP_Number))) {

                            Map<String, Object> updateStatus = new HashMap<>();
                            updateStatus.put("Order_Status", "Ordered");
                            updateStatus.put("Payment_Status", "Unpaid-COD");
                            updateStatus.put("Transaction_ID", "");
                            final String order_id = getIntent().getStringExtra("order_id");
                            FirebaseFirestore.getInstance().collection("ORDERS").document(order_id).update(updateStatus).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Map<String, Object> userOrder = new HashMap<>();
                                        userOrder.put("order_id", order_id);
                                        userOrder.put("time", FieldValue.serverTimestamp());
                                        FirebaseFirestore.getInstance().collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_ORDERS")
                                                .document(order_id).set(userOrder).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    DeliveryActivity.codOrderConfirmed = true;
                                                    finish();
                                                } else {
                                                    Toast.makeText(OTPVerificationActivity.this, "failed to update user's OrderList", Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });

                                    } else {
                                        Toast.makeText(OTPVerificationActivity.this, "ORDER CANCELLED", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

                        } else {
                            Toast.makeText(OTPVerificationActivity.this, "OTP incorrect !", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                finish();
                Toast.makeText(OTPVerificationActivity.this, "failed to  send OTP verification code", Toast.LENGTH_SHORT).show();
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
                Map<String, String> body = new HashMap<>();
                body.put("sender_id", "SMSIND");
                body.put("language", "english");
                body.put("route", "qt");
                body.put("numbers", userNo.substring(3, 13));
                body.put("message", "32546");
                body.put("variables", "{#BB#}");
                body.put("variables_values", String.valueOf(OTP_Number));
                return body;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        RequestQueue requestQueue = Volley.newRequestQueue(OTPVerificationActivity.this);
        requestQueue.add(stringRequest);


    }
}
