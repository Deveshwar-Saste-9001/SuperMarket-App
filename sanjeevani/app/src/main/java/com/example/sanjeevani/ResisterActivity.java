package com.example.sanjeevani;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class ResisterActivity extends AppCompatActivity {
    FirebaseAuth auth;
    String verificationCode;
    private DatabaseReference RootRef;
    public static boolean disableRegCloseBtn = false;


    private Button createAccountButton, getOtpbtn;
    private EditText InputName, InputPhoneNumber, InputPassword, InputEmail, TnputOtp;
    private ProgressDialog LodingBar;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;


    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resister);

        createAccountButton = (Button) findViewById(R.id.user_create);
        //  getOtpbtn =(Button)findViewById(R.id.user_send_otp) ;
        InputName = (EditText) findViewById(R.id.res_name);
        InputPhoneNumber = (EditText) findViewById(R.id.res_mobile223);
        InputEmail = (EditText) findViewById(R.id.res_email);
        //TnputOtp=(EditText) findViewById(R.id.User_OTP_TEXT) ;
        InputPassword = (EditText) findViewById(R.id.res_pass);
        LodingBar = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();


        InputPhoneNumber.setText("+91" + getIntent().getStringExtra("Mobile"));


        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateAccount();
            }


        });


    }

    private void CreateAccount() {
        String Name = InputName.getText().toString();
        String Mobile = InputPhoneNumber.getText().toString();
        String Email = InputEmail.getText().toString();
        String Password = InputPassword.getText().toString();


        if (TextUtils.isEmpty(Name)) {
            Toast.makeText(this, "Enter your name...", Toast.LENGTH_SHORT).show();

        } else if (TextUtils.isEmpty(Mobile)) {
            Toast.makeText(this, "Enter your mobile number...", Toast.LENGTH_SHORT).show();

        } else if (TextUtils.isEmpty(Email)) {
            Toast.makeText(this, "Enter your email...", Toast.LENGTH_SHORT).show();

        } else if (TextUtils.isEmpty(Password)) {
            Toast.makeText(this, "Enter your password...", Toast.LENGTH_SHORT).show();

        } else {
            LodingBar.setTitle("Create Account");
            LodingBar.setMessage("please wait,While we are checking the credential");
            LodingBar.setCanceledOnTouchOutside(false);
            LodingBar.show();

            ValidatePhoneNumber(Name, Mobile, Password, Email);
        }

    }

    private void ValidatePhoneNumber(final String Name, final String Mobile, final String Password, final String Email) {

        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!(dataSnapshot.child("Users").child(Mobile).exists())) {
                    final HashMap<String, Object> userdataMap = new HashMap<>();
                    userdataMap.put("Mobile", Mobile);
                    userdataMap.put("Name", Name);
                    userdataMap.put("Email", Email);
                    userdataMap.put("Password", Password);

                    CheckEmailAndPassword(Mobile, Name, Email, Password);


                } else {
                    Toast.makeText(ResisterActivity.this, "this mobile number" + Mobile + "alredy exist", Toast.LENGTH_SHORT).show();
                    LodingBar.dismiss();
                    Toast.makeText(ResisterActivity.this, "Please try again using another number", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ResisterActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void CheckEmailAndPassword(final String Mobile, final String Name, final String Email, final String Password) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";
        if (InputEmail.getText().toString().matches(emailPattern)) {
            firebaseAuth.createUserWithEmailAndPassword(InputEmail.getText().toString(), InputPassword.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                final HashMap<String, Object> userdataMap = new HashMap<>();
                                userdataMap.put("Mobile", Mobile);
                                userdataMap.put("Name", Name);
                                userdataMap.put("Email", Email);
                                userdataMap.put("profile", "");
                                userdataMap.put("id", firebaseAuth.getUid());
                                userdataMap.put("Password", Password);

                                firebaseFirestore.collection("USERS").document(firebaseAuth.getUid())
                                        .set(userdataMap)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    CollectionReference UserDataReference = firebaseFirestore.collection("USERS").document(firebaseAuth.getUid()).collection("USER_DATA");

//////////////////MAPS
                                                    Map<String, Object> wishlistMap = new HashMap<>();
                                                    wishlistMap.put("list_size", (long) 0);

                                                    Map<String, Object> ratingsMap = new HashMap<>();
                                                    ratingsMap.put("list_size", (long) 0);

                                                    Map<String, Object> cartMap = new HashMap<>();
                                                    cartMap.put("list_size", (long) 0);

                                                    Map<String, Object> MyAddressMap = new HashMap<>();
                                                    MyAddressMap.put("list_size", (long) 0);

                                                    Map<String, Object> notificationMap = new HashMap<>();
                                                    notificationMap.put("list_size", (long) 0);

                                                    //////
                                                    final List<String> documentsNames = new ArrayList<>();
                                                    documentsNames.add("MY_WISHLIST");
                                                    documentsNames.add("MY_RATINGS");
                                                    documentsNames.add("MY_CART");
                                                    documentsNames.add("MY_ADDRESSES");
                                                    documentsNames.add("MY_NOTIFICATIONS");

                                                    List<Map<String, Object>> documentsFields = new ArrayList<>();
                                                    documentsFields.add(wishlistMap);
                                                    documentsFields.add(ratingsMap);
                                                    documentsFields.add(cartMap);
                                                    documentsFields.add(MyAddressMap);
                                                    documentsFields.add(notificationMap);

                                                    for (int x = 0; x < documentsNames.size(); x++) {
                                                        final int finalX = x;
                                                        UserDataReference.document(documentsNames.get(x))
                                                                .set(documentsFields.get(x)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    if (finalX == documentsNames.size() - 1) {
                                                                        RootRef.child("Users").child(Mobile).updateChildren(userdataMap)
                                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                    @Override
                                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                                        if (task.isSuccessful()) {
                                                                                            final HashMap<String, Object> userdata = new HashMap<>();
                                                                                            userdata.put("Name", Name);
                                                                                            userdata.put("Profile", "default");
                                                                                            userdata.put("id", firebaseAuth.getUid());
                                                                                            DatabaseReference newRef = FirebaseDatabase.getInstance().getReference("UserList");
                                                                                            newRef.child(firebaseAuth.getUid()).updateChildren(userdata);
                                                                                            if (disableRegCloseBtn) {
                                                                                                Toast.makeText(ResisterActivity.this, "Congratulation your account created", Toast.LENGTH_SHORT).show();
                                                                                                disableRegCloseBtn = false;
                                                                                                finish();
                                                                                            } else {
                                                                                                Toast.makeText(ResisterActivity.this, "Congratulation your account created", Toast.LENGTH_SHORT).show();
                                                                                                Intent intent = new Intent(ResisterActivity.this, HomeActivity.class);
                                                                                                startActivity(intent);
                                                                                                finish();
                                                                                                disableRegCloseBtn = false;
                                                                                            }
                                                                                            LodingBar.dismiss();
                                                                                            InputName.setText("");
                                                                                            InputPhoneNumber.setText("");
                                                                                            InputPassword.setText("");
                                                                                            InputEmail.setText("");
                                                                                        } else {
                                                                                            LodingBar.dismiss();
                                                                                            disableRegCloseBtn = false;
                                                                                            Toast.makeText(ResisterActivity.this, "Network Error Please Try Again", Toast.LENGTH_SHORT).show();
                                                                                        }
                                                                                    }
                                                                                });
                                                                    }
                                                                } else {
                                                                    LodingBar.dismiss();
                                                                    Toast.makeText(ResisterActivity.this, "Network Error Please Try Again", Toast.LENGTH_SHORT).show();

                                                                }

                                                            }
                                                        });
                                                    }

                                                } else {
                                                    LodingBar.dismiss();
                                                    Toast.makeText(ResisterActivity.this, "Network Error Please Try Again", Toast.LENGTH_SHORT).show();
                                                }
                                            }


                                        });


                            } else {
                                LodingBar.dismiss();
                                String error = task.getException().getMessage();
                                Toast.makeText(ResisterActivity.this, error, Toast.LENGTH_SHORT).show();

                            }
                        }
                    });


        } else {
            LodingBar.dismiss();
            Toast.makeText(ResisterActivity.this, "Please Enter Valid Email Address", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (FirebaseAuth.getInstance().getUid() != null) {
            FirebaseAuth.getInstance().signOut();
        }
        InputName.setText("");
        InputPhoneNumber.setText("");
        InputPassword.setText("");
        InputEmail.setText("");

        Intent homeIntent = new Intent(ResisterActivity.this, MainActivity.class);
        startActivity(homeIntent);
        finish();
    }


}

