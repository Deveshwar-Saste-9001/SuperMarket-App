package com.example.sanjeevani.Fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.sanjeevani.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class DevloperFragment extends Fragment {


    public DevloperFragment() {
        // Required empty public constructor
    }

    private AdView adView;
    private CircleImageView circleImageView;

    private ImageView instagram, facebook, twitter;
    private String GOOGLE_PAY_PACKAGE_NAME = "com.google.android.apps.nbu.paisa.user";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_devloper, container, false);

        adView = new AdView(getContext());
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId("ca-app-pub-4247910596564740~4337195756");
        MobileAds.initialize(getContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        adView = view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);


        instagram = view.findViewById(R.id.dev_instagram);
        facebook = view.findViewById(R.id.dev_facebook);
        twitter = view.findViewById(R.id.dev_twitter);


        instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://instagram.com/_u/er.deveshwar_saste_patil");
                Intent insta = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(insta);

            }
        });
        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri facebookuri = Uri.parse("https://www.facebook.com/devya.saste/");
                Intent facebook = new Intent(Intent.ACTION_VIEW, facebookuri);
                startActivity(facebook);
            }
        });

        circleImageView = view.findViewById(R.id.circleImageView);
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Amount = "1";
                String upi = "8408895912@ybl";
                String name = "Deveshwar";
                String note = "purches";
                payUsingUpi(Amount, upi, name, note);
            }
        });

        return view;
    }

    private void payUsingUpi(String amount, String upi, String name, String note) {
        Uri uri = Uri.parse("upi://pay").buildUpon()
                .appendQueryParameter("pa", upi)
                .appendQueryParameter("pn", name)
                .appendQueryParameter("tn", note)
                .appendQueryParameter("am", amount)
                .appendQueryParameter("cu", "INR")
                .build();

        Intent upiPayIntent = new Intent(Intent.ACTION_VIEW);
        upiPayIntent.setData(uri);
        upiPayIntent.setPackage(GOOGLE_PAY_PACKAGE_NAME);

        Intent chooser = Intent.createChooser(upiPayIntent, "Pay with");
        if (null != chooser.resolveActivity(getActivity().getPackageManager())) {
            startActivityForResult(chooser, 0);
        } else {
            Toast.makeText(getContext(), "No UPI app found, please install one to continue", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:
                if ((resultCode == RESULT_OK) || resultCode == 11) {
                    if (data != null) {
                        String trtx = data.getStringExtra("response");
                        Log.d("UPI", "onActivityResult: " + trtx);
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add(trtx);
                        upiPaymentDataOperation(dataList);
                    } else {
                        Log.d("UPI", "onActivityResult: " + "Return data is null");
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add("nothing");
                        upiPaymentDataOperation(dataList);
                    }
                } else {
                    Log.d("UPI", "onActivityResult: " + "Return data is null");
                    ArrayList<String> dataList = new ArrayList<>();
                    dataList.add("nothing");
                    upiPaymentDataOperation(dataList);
                }
                break;
        }

//        Log.d("data", data.toString());
//        String status = null;
//        if (data != null) {
//            status = data.getStringExtra("Status").toLowerCase();
//        }
//        if ((RESULT_OK == resultCode) && status.equals("success")) {
//            Toast.makeText(getContext(), "Transaction successful", Toast.LENGTH_SHORT).show();
//        } else {
//            Toast.makeText(getContext(), "Transaction failed please try again!", Toast.LENGTH_SHORT).show();
//        }


    }

    private void upiPaymentDataOperation(ArrayList<String> data) {
        String str = data.get(0);
        Log.d("UPIPAY", "upiPaymentDataOperation: " + str);
        String paymentCancel = "";
        if (str == null) str = "discard";
        String status = "";
        String approvalRefNo = "";
        String response[] = str.split("&");
        for (int i = 0; i < response.length; i++) {
            String equalStr[] = response[i].split("=");
            if (equalStr.length >= 2) {
                if (equalStr[0].toLowerCase().equals("Status".toLowerCase())) {
                    status = equalStr[1].toLowerCase();
                } else if (equalStr[0].toLowerCase().equals("ApprovalRefNo".toLowerCase()) || equalStr[0].toLowerCase().equals("txnRef".toLowerCase())) {
                    approvalRefNo = equalStr[1];
                }
            } else {
                paymentCancel = "Payment cancelled by user";
            }
        }
        if (status.equals("success")) {
            Toast.makeText(getContext(), "Transaction successful", Toast.LENGTH_SHORT).show();
            Log.d("UPI", "responseStr: " + approvalRefNo);


        } else if ("Payment cancelled by user".equals(paymentCancel)) {
            Toast.makeText(getContext(), "Payment cancel by user", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(getContext(), "Transaction failed please try again!", Toast.LENGTH_SHORT).show();
        }

    }
}
