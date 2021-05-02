package com.example.sanjeevani.Fragments;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.sanjeevani.DBqueries;
import com.example.sanjeevani.MainActivity;
import com.example.sanjeevani.Model.MyOrderItemModel;
import com.example.sanjeevani.MyAddressesActivity;
import com.example.sanjeevani.R;
import com.example.sanjeevani.UpdateUserInfoActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import androidx.fragment.app.Fragment;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyAccountFragment extends Fragment {

    public MyAccountFragment() {
        // Required empty public constructor
    }

    public static final int MANAGE_ADDRESS = 1;
    private Button ViewAllAddress;

    private CircleImageView profileImage, currentOrderProduct;
    private TextView name, email, TVCurrentOrderStatus;
    private LinearLayout layoutContainer;
    private Dialog loadingDialog;
    private ImageView orderedIndicator, packedIndicator, shippedIndicator, deliveredIndicator;
    private ProgressBar O_P_Progress, P_S_Progress, S_D_Progress;
    private TextView recentOrderstitle;
    private LinearLayout recentOrderContainer;
    private TextView fullname, address, pincode;
    private Button signoutBtn;
    private FloatingActionButton settingsBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_account, container, false);
        profileImage = view.findViewById(R.id.profile_image_acc1);
        name = view.findViewById(R.id.user_name_acc1);
        email = view.findViewById(R.id.user_email_acc1);
        layoutContainer = view.findViewById(R.id.acc_Layout_Container);
        currentOrderProduct = view.findViewById(R.id.current_order_acc1);
        TVCurrentOrderStatus = view.findViewById(R.id.tv_current_order_status_acc1);

        orderedIndicator = view.findViewById(R.id.orderd_indictor_acc1);
        packedIndicator = view.findViewById(R.id.packed_indicator_acc1);
        shippedIndicator = view.findViewById(R.id.shiped_indicator_acc1);
        deliveredIndicator = view.findViewById(R.id.delivered_indicater_acc1);

        O_P_Progress = view.findViewById(R.id.ordered_acc1);
        P_S_Progress = view.findViewById(R.id.packed_acc1);
        S_D_Progress = view.findViewById(R.id.shipped_acc1);

        recentOrderstitle = view.findViewById(R.id.recent_orders_title_acc2);
        recentOrderContainer = view.findViewById(R.id.recent_order_container_acc1);

        fullname = view.findViewById(R.id.fullname_acc1);
        address = view.findViewById(R.id.address_acc1);
        pincode = view.findViewById(R.id.pincode_acc1);
        signoutBtn = view.findViewById(R.id.signoutbtn_myacc);
        settingsBtn = view.findViewById(R.id.setting_btn_acc1);

        loadingDialog = new Dialog(getContext());
        loadingDialog.setContentView(R.layout.loading_progress_bar);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getContext().getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.show();


        layoutContainer.getChildAt(1).setVisibility(View.GONE);
        loadingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                for (MyOrderItemModel myOrderItemModel : DBqueries.myOrderItemModelList) {
                    if (!myOrderItemModel.isCancellationRequested()) {
                        if (!myOrderItemModel.getOrderStatus().equals("Delivered") && !myOrderItemModel.getOrderStatus().equals("Cancelled")) {
                            layoutContainer.getChildAt(1).setVisibility(View.VISIBLE);
                            Glide.with(getContext()).load(myOrderItemModel.getProductImageOrder()).apply(new RequestOptions().placeholder(R.drawable.proandcatplaceholder)).into(currentOrderProduct);
                            TVCurrentOrderStatus.setText(myOrderItemModel.getOrderStatus());

                            switch (myOrderItemModel.getOrderStatus()) {
                                case "Ordered":
                                    orderedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                                    break;
                                case "Packed":
                                    orderedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                                    packedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                                    O_P_Progress.setProgress(100);
                                    break;
                                case "Shipped":
                                    orderedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                                    packedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                                    O_P_Progress.setProgress(100);
                                    shippedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                                    P_S_Progress.setProgress(100);
                                    break;
                                case "Out for Delivery":
                                    orderedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                                    packedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                                    O_P_Progress.setProgress(100);
                                    shippedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                                    P_S_Progress.setProgress(100);
                                    deliveredIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                                    S_D_Progress.setProgress(100);
                                    break;
                            }

                        }
                    }
                }
                int i = 0;
                for (MyOrderItemModel orderItemModel : DBqueries.myOrderItemModelList) {
                    if (i < 4) {
                        if (orderItemModel.getOrderStatus().equals("Delivered")) {
                            Glide.with(getContext()).load(orderItemModel.getProductImageOrder()).apply(new RequestOptions().placeholder(R.drawable.proandcatplaceholder)).into((ImageView) recentOrderContainer.getChildAt(i));
                            i++;
                        }
                    } else {
                        break;
                    }
                }
                if (i == 0) {
                    recentOrderstitle.setText("No recent Orders");
                }
                if (i < 3) {
                    for (int x = i; x < 4; x++) {
                        recentOrderContainer.getChildAt(x).setVisibility(View.GONE);
                    }
                }
                loadingDialog.show();
                loadingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        loadingDialog.setOnDismissListener(null);
                        if (DBqueries.addressesModelList.size() == 0) {
                            fullname.setText("No Address");
                            address.setText("-");
                            pincode.setText("-");
                        } else {
                            setAddress();
                        }


                    }
                });
                DBqueries.loadAddresses(getContext(), loadingDialog, false);
            }
        });
        DBqueries.loadOrders(getContext(), null, loadingDialog);

        ViewAllAddress = view.findViewById(R.id.view_all_btn_acc1);
        ViewAllAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myAddressintent = new Intent(getContext(), MyAddressesActivity.class);
                myAddressintent.putExtra("MODE", MANAGE_ADDRESS);
                startActivity(myAddressintent);
            }
        });

        signoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                DBqueries.clearData();
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent updateinfoIntent = new Intent(getContext(), UpdateUserInfoActivity.class);
                updateinfoIntent.putExtra("Name", name.getText());
                updateinfoIntent.putExtra("Email", email.getText());
                updateinfoIntent.putExtra("Photo", DBqueries.profile);
                startActivity(updateinfoIntent);

            }
        });


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        name.setText(DBqueries.fullname);
        email.setText(DBqueries.email);
        if (!DBqueries.profile.equals("")) {
            Glide.with(getContext()).load(DBqueries.profile).apply(new RequestOptions().placeholder(R.drawable.profile)).into(profileImage);
        } else {
            profileImage.setImageResource(R.drawable.profile);
        }
        if (!loadingDialog.isShowing()) {
            if (DBqueries.addressesModelList.size() == 0) {
                fullname.setText("No Address");
                address.setText("-");
                pincode.setText("-");
            } else {
                setAddress();
            }
        }
    }

    private void setAddress() {
        String nametext;
        String mobile;
        nametext = DBqueries.addressesModelList.get(DBqueries.selectedAddress).getName();
        mobile = DBqueries.addressesModelList.get(DBqueries.selectedAddress).getMobile();
        if (DBqueries.addressesModelList.get(DBqueries.selectedAddress).getAlternetMobile().equals("")) {
            fullname.setText(nametext + " - " + mobile);
        } else {
            fullname.setText(nametext + " - " + mobile + " or " + DBqueries.addressesModelList.get(DBqueries.selectedAddress).getAlternetMobile());
        }
        String flatNo = DBqueries.addressesModelList.get(DBqueries.selectedAddress).getFlatno();
        String locality = DBqueries.addressesModelList.get(DBqueries.selectedAddress).getLocality();
        String landMark = DBqueries.addressesModelList.get(DBqueries.selectedAddress).getLandMark();
        String city = DBqueries.addressesModelList.get(DBqueries.selectedAddress).getCity();
        String state = DBqueries.addressesModelList.get(DBqueries.selectedAddress).getState();
        if (landMark.equals("")) {
            address.setText(flatNo + ", " + locality + ", " + city + ", " + state);
        } else {
            address.setText(flatNo + ", " + locality + ", " + landMark + ", " + city + ", " + state);
        }
        pincode.setText(DBqueries.addressesModelList.get(DBqueries.selectedAddress).getPincode());
    }

}
