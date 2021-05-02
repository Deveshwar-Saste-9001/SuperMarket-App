package com.example.sanjeevani;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import io.paperdb.Paper;

public class SplashActivity extends AppCompatActivity {
    private TextView welCome;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        welCome = findViewById(R.id.welcome);
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.blinkanimation);
        welCome.setAnimation(anim);
        Paper.init(this);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseUser CurrentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (CurrentUser == null) {
                    Intent mainintent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(mainintent);
                    finish();
                } else {
                    FirebaseFirestore.getInstance().collection("USERS").document(CurrentUser.getUid()).update("Last seen", FieldValue.serverTimestamp())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Intent Homeintent = new Intent(SplashActivity.this, HomeActivity.class);
                                        startActivity(Homeintent);
                                        finish();
                                    } else {
                                        String error = task.getException().getMessage();
                                        Toast.makeText(SplashActivity.this, error, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        }, 3000);
    }
}
