package com.example.sanjeevani;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.example.sanjeevani.Fragments.UpdateInfoFragment;
import com.example.sanjeevani.Fragments.UpdatePasswordfragment;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class UpdateUserInfoActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private FrameLayout frameLayout;
    private UpdateInfoFragment updateInfoFragment;
    private UpdatePasswordfragment updatePasswordfragment;
    private String name;
    private String email;
    private String photo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_info);

        tabLayout = findViewById(R.id.tab_userInfo);
        frameLayout = findViewById(R.id.userinfo_frameLayout);

        updateInfoFragment = new UpdateInfoFragment();
        updatePasswordfragment = new UpdatePasswordfragment();

        name = getIntent().getStringExtra("Name");
        email = getIntent().getStringExtra("Email");
        photo = getIntent().getStringExtra("Photo");

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    setFragment(updateInfoFragment, true);
                }
                if (tab.getPosition() == 1) {
                    setFragment(updatePasswordfragment, true);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        tabLayout.getTabAt(0).select();
        setFragment(updateInfoFragment, true);


    }

    private void setFragment(Fragment fragment, boolean setbundle) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (setbundle) {
            Bundle bundle = new Bundle();
            bundle.putString("Name", name);
            bundle.putString("Email", email);
            bundle.putString("Photo", photo);
            fragment.setArguments(bundle);
        } else {
            Bundle bundle = new Bundle();
            bundle.putString("Email", email);
            fragment.setArguments(bundle);
        }
        fragmentTransaction.replace(frameLayout.getId(), fragment);
        fragmentTransaction.commit();
    }


}
