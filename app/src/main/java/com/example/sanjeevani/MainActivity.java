package com.example.sanjeevani;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sanjeevani.Model.Users;
import com.example.sanjeevani.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity
{
    private Button joinNowButton,LoginButton;
private TextView Skip_btn;
    private ProgressDialog LodingBar;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Skip_btn=(TextView)findViewById(R.id.skip_btn);
        joinNowButton=(Button)findViewById(R.id.main_join_butn);
        LoginButton=(Button)findViewById(R.id.main_login_butn);
        LodingBar =new ProgressDialog(this);
        Animation anim = AnimationUtils.loadAnimation(this,R.anim.blinkanimation);
        Skip_btn.setAnimation(anim);



        Paper.init(this);

        LoginButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent=new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        joinNowButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent=new Intent(MainActivity.this,MobileOtpActivity.class);
                startActivity(intent);
                finish();


            }
        });
        Skip_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        String UserPhoneKey= Paper.book().read(Prevalent.UserPhoneKey);
        String UserPasswordKey=Paper.book().read(Prevalent.UserPasswordKey);

        if (UserPhoneKey !=null && UserPasswordKey !=null)
        {
            if(!TextUtils.isEmpty(UserPhoneKey) && !TextUtils.isEmpty(UserPasswordKey))
            {
                AllowAccess(UserPhoneKey,UserPasswordKey);

            }
        }

    }

    private void AllowAccess(final String Mobile, final String Password) {

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("Users").child(Mobile).exists()) {
                    Users usersData = dataSnapshot.child("Users").child(Mobile).getValue(Users.class);

                    if (usersData.getMobile().equals(Mobile)) {
                        if (usersData.getPassword().equals(Password)) {

                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                            startActivity(intent);

                        } else {

                            Toast.makeText(MainActivity.this, "Password was incorrect", Toast.LENGTH_SHORT).show();
                        }

                    }


                } else {
                    Toast.makeText(MainActivity.this, "This user does not exist", Toast.LENGTH_SHORT).show();


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
