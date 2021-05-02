package com.example.sanjeevani;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.transition.TransitionManager;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class ForgetPasswordActivity extends AppCompatActivity {

    private EditText registerEmial;
    private Button resetPasswordButton;
    private TextView GoBack;

    private ViewGroup emailIconContainer;
    private ImageView emailIcon;
    private TextView emailIconTextView;
    private ProgressBar forgetPassprogressBar;

    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        registerEmial = findViewById(R.id.forgot_password_email);
        resetPasswordButton = findViewById(R.id.ResetPassWordBtn);
        GoBack = findViewById(R.id.forget_password_goBack);
        emailIconContainer = findViewById(R.id.forget_pass_icon_container);
        emailIcon = findViewById(R.id.forget_pass_icon);
        emailIconTextView = findViewById(R.id.forget_pass_textview);
        forgetPassprogressBar = findViewById(R.id.forget_pass_progressBar);

        firebaseAuth = FirebaseAuth.getInstance();

        registerEmial.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        GoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransitionManager.beginDelayedTransition(emailIconContainer);
                emailIconTextView.setVisibility(View.GONE);

                TransitionManager.beginDelayedTransition(emailIconContainer);
                emailIcon.setVisibility(View.VISIBLE);
                forgetPassprogressBar.setVisibility(View.VISIBLE);


                resetPasswordButton.setEnabled(false);
                resetPasswordButton.setTextColor(Color.argb(50, 255, 255, 255));

                firebaseAuth.sendPasswordResetEmail(registerEmial.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    ScaleAnimation scaleAnimation = new ScaleAnimation(1,0,1,0,emailIcon.getWidth()/2,emailIcon.getHeight()/2);
                                    scaleAnimation.setDuration(100);
                                    scaleAnimation.setInterpolator(new AccelerateInterpolator());
                                    scaleAnimation.setRepeatMode(Animation.REVERSE);
                                    scaleAnimation.setRepeatCount(1);
                                    scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
                                        @Override
                                        public void onAnimationStart(Animation animation) {

                                        }

                                        @Override
                                        public void onAnimationEnd(Animation animation) {
                                            emailIconTextView.setText("Recovory email sent successfully! check your inbox");
                                            emailIconTextView.setTextColor(getResources().getColor(R.color.successGreen));

                                            TransitionManager.beginDelayedTransition(emailIconContainer);
                                            emailIconTextView.setVisibility(View.VISIBLE);

                                        }

                                        @Override
                                        public void onAnimationRepeat(Animation animation) {
                                            emailIcon.setImageResource(R.drawable.ic_mail_outline_black_24dp);
                                        }
                                    });
                                    emailIcon.startAnimation(scaleAnimation);


                                } else {
                                    String error = task.getException().getMessage();

                                    emailIconTextView.setText(error);
                                    emailIconTextView.setTextColor(getResources().getColor(R.color.colorRed));
                                    TransitionManager.beginDelayedTransition(emailIconContainer);
                                    emailIconTextView.setVisibility(View.VISIBLE);
                                    resetPasswordButton.setEnabled(true);
                                    resetPasswordButton.setTextColor(Color.rgb(255, 255, 255));
                                }
                                forgetPassprogressBar.setVisibility(View.GONE);
                            }
                        });
            }
        });


    }

    private void checkInputs() {
        if (TextUtils.isEmpty(registerEmial.getText())) {
            resetPasswordButton.setEnabled(false);
            resetPasswordButton.setTextColor(Color.argb(50, 255, 255, 255));
        } else {
            resetPasswordButton.setEnabled(true);
            resetPasswordButton.setTextColor(Color.rgb(255, 255, 255));

        }

    }

}
