package com.example.sanjeevani;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.sanjeevani.Fragments.AboutUsFragment;
import com.example.sanjeevani.Fragments.ContactUsFragment;
import com.example.sanjeevani.Fragments.DevloperFragment;
import com.example.sanjeevani.Fragments.HomeFragment;
import com.example.sanjeevani.Fragments.MyAccountFragment;
import com.example.sanjeevani.Fragments.MyCartFragment;
import com.example.sanjeevani.Fragments.MyOrdersFargment;
import com.example.sanjeevani.Fragments.MyRewordFragment;
import com.example.sanjeevani.Fragments.MyWishLayout;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;


public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FrameLayout frameLayout;
    private static final int HOME_FRAGMENT = 0;
    private static final int CART_FRAGMENT = 1;
    private static final int ORDERS_FRAGMENT = 2;
    private static final int MY_WISHLIST = 3;
    private static final int MY_REWORDS = 4;
    private static final int MY_ACCOUNT = 5;
    private static final int CONTACT_US = 6;
    private static final int ABOUT_US = 7;
    private static final int DEVELOPER = 8;

    public static Boolean showcart = false;
    public static boolean resetHomeActivity = false;
    private NavigationView navigationView;
    private FloatingActionButton fab;
    private int currentFragment = -1;
    private ImageView actionBarlogo;
    private Window window;
    private Toolbar toolbar;
    private Dialog signInDialog;
    private FirebaseUser CurrentUser;
    private int scrollFlags;
    private AppBarLayout.LayoutParams params;
    public static Activity homeActivity;

    private CircleImageView profileView;
    private TextView FullName, email;
    private ImageView addProfile;


    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        actionBarlogo = findViewById(R.id.actionBarLogo);
        setSupportActionBar(toolbar);
        window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        params = (AppBarLayout.LayoutParams) toolbar.getLayoutParams();
        scrollFlags = params.getScrollFlags();


        CurrentUser = FirebaseAuth.getInstance().getCurrentUser();

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CurrentUser != null) {
                    gotoFragment("My Cart", new MyCartFragment(), CART_FRAGMENT);
                } else {
                    signInDialog.show();
                }
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);


        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);

        frameLayout = findViewById(R.id.main_framelayout);

        profileView = navigationView.getHeaderView(0).findViewById(R.id.Profile_Photo_home);
        FullName = navigationView.getHeaderView(0).findViewById(R.id.UserName_Home);
        addProfile = navigationView.getHeaderView(0).findViewById(R.id.addProfile_Home);
        email = navigationView.getHeaderView(0).findViewById(R.id.email_Home);

        if (showcart) {
            homeActivity = this;
            drawer.setDrawerLockMode(1);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            gotoFragment("My Cart", new MyCartFragment(), -2);
            fab.hide();
        } else {
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();
            setFragment(new HomeFragment(), HOME_FRAGMENT);

        }
        signInDialog = new Dialog(HomeActivity.this);
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
                ResisterActivity.disableRegCloseBtn = true;
                Intent signInIntent = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(signInIntent);

            }
        });
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInDialog.dismiss();
                LoginActivity.disableCloseBtn = true;
                ResisterActivity.disableRegCloseBtn = true;
                Intent signupIntent = new Intent(HomeActivity.this, MobileOtpActivity.class);
                startActivity(signupIntent);


            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        DBqueries.checkNotification(true, null);
    }

    @Override
    protected void onStart() {
        super.onStart();
        CurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (CurrentUser == null) {
            navigationView.getMenu().getItem(navigationView.getMenu().size() - 1).setEnabled(false);
        } else {
//            if (DBqueries.cartList.size() == 0) {
//                DBqueries.loadCartList(HomeActivity.this, new Dialog(HomeActivity.this), false, new TextView(HomeActivity.this));
//            }
            if (DBqueries.email == null) {
                FirebaseFirestore.getInstance().collection("USERS").document(CurrentUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DBqueries.email = task.getResult().getString("Email");
                            DBqueries.usernamemobile = task.getResult().getString("Mobile");
                            DBqueries.fullname = task.getResult().getString("Name");
                            DBqueries.profile = task.getResult().getString("profile");

                            FullName.setText(DBqueries.fullname);
                            email.setText(DBqueries.email);
                            if (DBqueries.profile.equals("")) {

                                addProfile.setVisibility(View.VISIBLE);
                            } else {
                                addProfile.setVisibility(View.INVISIBLE);
                                Glide.with(HomeActivity.this).load(DBqueries.profile).apply(new RequestOptions().placeholder(R.drawable.profile)).into(profileView);
                            }
                        } else {
                            String error = task.getException().getMessage();
                            Toast.makeText(HomeActivity.this, error, Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            } else {
                FullName.setText(DBqueries.fullname);
                email.setText(DBqueries.email);
                if (DBqueries.profile.equals("")) {
                    profileView.setImageResource(R.drawable.profile);
                    addProfile.setVisibility(View.VISIBLE);
                } else {
                    addProfile.setVisibility(View.INVISIBLE);
                    Glide.with(HomeActivity.this).load(DBqueries.profile).apply(new RequestOptions().placeholder(R.drawable.profile)).into(profileView);
                }
            }
            navigationView.getMenu().getItem(navigationView.getMenu().size() - 1).setEnabled(true);
        }
        if (resetHomeActivity) {
            resetHomeActivity = false;
            actionBarlogo.setVisibility(View.VISIBLE);
            setFragment(new HomeFragment(), HOME_FRAGMENT);
            navigationView.getMenu().getItem(0).setChecked(true);
        }
        invalidateOptionsMenu();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (currentFragment == HOME_FRAGMENT) {
                super.onBackPressed();
                currentFragment = -1;
            } else {
                if (showcart) {
                    showcart = false;
                    homeActivity = null;
                    finish();
                } else {
                    invalidateOptionsMenu();
                    actionBarlogo.setVisibility(View.VISIBLE);
                    setFragment(new HomeFragment(), HOME_FRAGMENT);
                    navigationView.getMenu().getItem(0).setChecked(true);
                }
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (currentFragment == HOME_FRAGMENT) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getMenuInflater().inflate(R.menu.home, menu);

            MenuItem notifyitem = menu.findItem(R.id.action_Notification);

            notifyitem.setActionView(R.layout.badge_layout);
            ImageView badgeIcon = notifyitem.getActionView().findViewById(R.id.badge_icon);
            badgeIcon.setImageResource(R.drawable.ic_notifications_black_24dp);
            TextView badgeCount = notifyitem.getActionView().findViewById(R.id.badge_count);
            badgeCount.setText(String.valueOf(DBqueries.cartList.size()));

            if (CurrentUser != null) {
                DBqueries.checkNotification(false, badgeCount);
            }
            notifyitem.getActionView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (CurrentUser != null) {
                        Intent notificationIntent = new Intent(HomeActivity.this, NotificationActivity.class);
                        startActivity(notificationIntent);
                    } else {
                        signInDialog.show();
                    }
                }
            });

        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatemen
        if (id == R.id.action_Search) {
            Intent searchIntent = new Intent(this, SearchActivity.class);
            startActivity(searchIntent);
            return true;
        } else if (id == R.id.action_Notification) {
            if (CurrentUser == null) {
                signInDialog.show();
            } else {
                Intent notificationIntent = new Intent(this, NotificationActivity.class);
                startActivity(notificationIntent);
            }
            return true;
        } else if (id == R.id.action_Logout) {
            if (CurrentUser != null) {
                Paper.book().destroy();
                FirebaseAuth.getInstance().signOut();
                DBqueries.clearData();
                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
            return true;
        } else if (id == android.R.id.home) {
            showcart = false;
            homeActivity = null;
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    MenuItem menuItem;

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        menuItem = item;

        if (CurrentUser != null) {
            drawer.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
                @Override
                public void onDrawerClosed(View drawerView) {
                    super.onDrawerClosed(drawerView);
                    int id = menuItem.getItemId();
                    if (id == R.id.nav_Home) {
                        invalidateOptionsMenu();
                        actionBarlogo.setVisibility(View.VISIBLE);
                        setFragment(new HomeFragment(), HOME_FRAGMENT);

                    } else if (id == R.id.nav_cart) {
                        gotoFragment("My Cart", new MyCartFragment(), CART_FRAGMENT);

                    } else if (id == R.id.nav_orders) {
                        gotoFragment("My Order", new MyOrdersFargment(), ORDERS_FRAGMENT);

                    } else if (id == R.id.nav_rewords) {
                        gotoFragment("My Reword", new MyRewordFragment(), MY_REWORDS);

                    } else if (id == R.id.nav_wishlist) {
                        gotoFragment("My WishList", new MyWishLayout(), MY_WISHLIST);

                    } else if (id == R.id.nav_account) {
                        gotoFragment("My Account", new MyAccountFragment(), MY_ACCOUNT);

                    } else if (id == R.id.nav_contact) {
                        gotoFragment("Contact Us", new ContactUsFragment(), CONTACT_US);
                    } else if (id == R.id.nav_about) {
                        gotoFragment("About Us", new AboutUsFragment(), ABOUT_US);
                    } else if (id == R.id.nav_developer) {
                        gotoFragment("Developer", new DevloperFragment(), DEVELOPER);
                    }
                    drawer.removeDrawerListener(this);
                }
            });
            return true;
        } else {
            signInDialog.show();
            return false;
        }

    }

    private void gotoFragment(String Title, Fragment fragment, int FragmentNo) {
        invalidateOptionsMenu();
        actionBarlogo.setVisibility(View.GONE);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(Title);
        setFragment(fragment, FragmentNo);
        if (FragmentNo == CART_FRAGMENT || showcart) {
            navigationView.getMenu().getItem(1).setChecked(true);
            params.setScrollFlags(0);
        } else {
            params.setScrollFlags(scrollFlags);
        }
        if (FragmentNo == CONTACT_US) {
            //navigationView.getMenu().getItem(1).setChecked(true);
            params.setScrollFlags(0);
        }
        if (FragmentNo == ABOUT_US) {
            //navigationView.getMenu().getItem(1).setChecked(true);
            params.setScrollFlags(0);
        }
        if (FragmentNo == DEVELOPER) {
            //navigationView.getMenu().getItem(1).setChecked(true);
            params.setScrollFlags(0);
        }


    }

    private void setFragment(Fragment fragment, int fragmentNo) {
        if (fragmentNo != currentFragment) {
            if (fragmentNo == MY_REWORDS) {
                window.setStatusBarColor(getResources().getColor(R.color.voilate));
                toolbar.setBackgroundColor(getResources().getColor(R.color.voilate));
                fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.voilate)));
            } else {
                window.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
                toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
            }
            currentFragment = fragmentNo;
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(frameLayout.getId(), fragment);
            fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
            fragmentTransaction.commit();
            if (currentFragment == HOME_FRAGMENT) {
                fab.show();
                getSupportActionBar().setDisplayShowTitleEnabled(false);
            } else {
                fab.hide();
            }

        }
    }
}
