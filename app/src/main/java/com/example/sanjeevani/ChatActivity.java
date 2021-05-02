package com.example.sanjeevani;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.sanjeevani.Fragments.ChatFragment;
import com.example.sanjeevani.Fragments.UploadListFragment;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class ChatActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    //     private FrameLayout frameLayout;
    private ChatFragment chatFragment;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Toolbar toolbar = (Toolbar) findViewById(R.id.deliveryToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Chat");

        tabLayout = findViewById(R.id.tab_chat);
        viewPager = findViewById(R.id.chat_viewPager);

        MyFragmentViewPagerAdapter myFragmentViewPageerAdapter = new MyFragmentViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(myFragmentViewPageerAdapter);
        tabLayout.setupWithViewPager(viewPager);
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

    class MyFragmentViewPagerAdapter extends FragmentPagerAdapter {


        public MyFragmentViewPagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {

            if (position == 0) {
                return new ChatFragment();
            }
            if (position == 1) {
                return new UploadListFragment();
            }
            return null;

        }

        @Override
        public int getCount() {
            return 2;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0) {
                return "Chat with us";
            }
            if (position == 1) {
                return "Upload your List";
            }
            return super.getPageTitle(position);
        }

    }
}
