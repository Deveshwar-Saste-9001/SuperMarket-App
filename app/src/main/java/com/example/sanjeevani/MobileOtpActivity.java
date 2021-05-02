package com.example.sanjeevani;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sanjeevani.Model.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MobileOtpActivity extends AppCompatActivity {

    public PhoneAuthProvider.OnVerificationStateChangedCallbacks mcallbacks;

    private Button GetVerificationCode, VerifyButton;
    public EditText InputPhone, InputOtp;
    public String mVerificationId;
    public PhoneAuthProvider.ForceResendingToken mResendToken;

    private LinearLayout mobile_container;
    private LinearLayout otp_message_contaner;
    private LinearLayout otp_container;
    private TextView go_back_otp;
    private TextView otp_messege;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_otp);

        GetVerificationCode = (Button) findViewById(R.id.user_OTP_login);
        VerifyButton = (Button) findViewById(R.id.verify_login);
        InputOtp = (EditText) findViewById(R.id.Mobile_OTP);
        InputPhone = (EditText) findViewById(R.id.login_MOBILE22);
        mAuth = FirebaseAuth.getInstance();
        mobile_container=findViewById(R.id.mobile_Container);
        otp_message_contaner=findViewById(R.id.otp_massage);
        otp_container=findViewById(R.id.otp_container);
        go_back_otp=findViewById(R.id.go_back_mobile_otp);
        otp_messege=findViewById(R.id.otp_send_messege);

        go_back_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent homeIntent=new Intent(MobileOtpActivity.this,MainActivity.class);
                startActivity(homeIntent);
                finish();
            }
        });





        GetVerificationCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetVerificationCode.setEnabled(false);
                final String phoneNumber = "+91" + InputPhone.getText().toString();
                if (TextUtils.isEmpty(phoneNumber)) {
                    Toast.makeText(MobileOtpActivity.this, "Please enter your Number", Toast.LENGTH_SHORT).show();

                } else {
                    final DatabaseReference RootRef;
                    RootRef = FirebaseDatabase.getInstance().getReference();

                    RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.child("Users").child("+91" + InputPhone.getText().toString()).exists()) {
                                GetVerificationCode.setEnabled(true);
                                TransitionManager.beginDelayedTransition(mobile_container);
                                GetVerificationCode.setVisibility(View.VISIBLE);
                                InputPhone.setVisibility(View.VISIBLE);
                                mobile_container.setVisibility(View.VISIBLE);

                                TransitionManager.beginDelayedTransition(otp_message_contaner);
                                otp_messege.setText("Mobile Number Already Exists");
                                otp_messege.setTextColor(getResources().getColor(R.color.colorRed));
                                otp_messege.setVisibility(View.VISIBLE);
                                otp_message_contaner.setVisibility(View.VISIBLE);
                                InputOtp.setVisibility(View.GONE);
                                VerifyButton.setVisibility(View.GONE);
                                otp_container.setVisibility(View.GONE);

                                //Toast.makeText(MobileOtpActivity.this, "Mobile Number Already Exists", Toast.LENGTH_SHORT).show();
                            } else {
                                PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNumber, 60, TimeUnit.SECONDS, MobileOtpActivity.this, mcallbacks);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
            }
        });
        mcallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                // signInWithPhoneAuthCredential(phoneAuthCredential);

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {

//                TransitionManager.beginDelayedTransition(otp_message_contaner);
                GetVerificationCode.setVisibility(View.VISIBLE);
                InputPhone.setVisibility(View.VISIBLE);
                otp_message_contaner.setVisibility(View.VISIBLE);

                TransitionManager.beginDelayedTransition(otp_message_contaner);
                otp_messege.setText(e.toString());
                otp_messege.setTextColor(getResources().getColor(R.color.colorRed));
                otp_messege.setVisibility(View.VISIBLE);
                otp_message_contaner.setVisibility(View.VISIBLE);

                otp_container.setVisibility(View.GONE);
                InputOtp.setVisibility(View.GONE);
                VerifyButton.setVisibility(View.GONE);
                GetVerificationCode.setEnabled(true);
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {


                mVerificationId = verificationId;
                mResendToken = token;
                mobile_container.setVisibility(View.INVISIBLE);
                GetVerificationCode.setVisibility(View.INVISIBLE);
                InputPhone.setVisibility(View.INVISIBLE);

                TransitionManager.beginDelayedTransition(otp_message_contaner);
                otp_messege.setText("OTP has been sent to you on your mobile phone. please enter it below");
                otp_messege.setTextColor(getResources().getColor(R.color.successGreen));
                otp_messege.setVisibility(View.VISIBLE);
                otp_message_contaner.setVisibility(View.VISIBLE);

                TransitionManager.beginDelayedTransition(otp_container);
                InputOtp.setVisibility(View.VISIBLE);
                VerifyButton.setVisibility(View.VISIBLE);
                otp_container.setVisibility(View.VISIBLE);
            }
        };

        VerifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VerifyButton.setEnabled(false);
                GetVerificationCode.setVisibility(View.INVISIBLE);
                InputPhone.setVisibility(View.INVISIBLE);

                String varificationCode = InputOtp.getText().toString();
                if (TextUtils.isEmpty(varificationCode)) {
                    VerifyButton.setEnabled(true);
                    Toast.makeText(MobileOtpActivity.this, "Enter VerificationCode", Toast.LENGTH_SHORT).show();
                } else {
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, varificationCode);
                    signInWithPhoneAuthCredential(credential);
                }
            }
        });
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            sendUsertoNext();
                        } else {
                            VerifyButton.setEnabled(true);
                            String message = task.getException().toString();
                            Toast.makeText(MobileOtpActivity.this, "error:" + message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void sendUsertoNext() {
        GetVerificationCode.setEnabled(true);
        VerifyButton.setEnabled(true);
        String Mobile = InputPhone.getText().toString();
        Intent intent = new Intent(MobileOtpActivity.this, ResisterActivity.class);
        intent.putExtra("Mobile", Mobile);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent homeIntent=new Intent(MobileOtpActivity.this,MainActivity.class);
        startActivity(homeIntent);
        finish();
    }
}
