package com.example.sanjeevani;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.sanjeevani.Model.AddressesModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddAdressActivity extends AppCompatActivity {
    private Button savebtn;
    private EditText city;
    private EditText locality;
    private EditText flatno;
    private EditText pincode;
    private EditText landMark;
    private EditText name;
    private EditText mobile;
    private EditText alternetMobile;
    private EditText State;
    private EditText floorNo;
    private Dialog loadingDialog;

    private boolean updateAddress = false;
    private AddressesModel addressesModel;
    private int position;

    //private String[] stateList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_adress);
        Toolbar toolbar = (Toolbar) findViewById(R.id.Add_Address_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add Address");

        loadingDialog = new Dialog(this);
        loadingDialog.setContentView(R.layout.loading_progress_bar);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        savebtn = findViewById(R.id.save_botton_add1);

        city = (EditText) findViewById(R.id.city_adress1);
        locality = findViewById(R.id.Area_address1);
        flatno = findViewById(R.id.flat_no_address1);
        pincode = findViewById(R.id.pincode_adress1);
        landMark = findViewById(R.id.landmark_adress1);
        name = findViewById(R.id.name_adress1);
        mobile = findViewById(R.id.mobile_address1);
        alternetMobile = findViewById(R.id.alter_mobile_adress1);
        State = findViewById(R.id.state_adress1);
        floorNo = findViewById(R.id.flowerNo);

        if (getIntent().getStringExtra("INTENT").equals("update_address")) {
            updateAddress = true;
            position = getIntent().getIntExtra("INDEX", -1);
            addressesModel = DBqueries.addressesModelList.get(position);

            city.setText(addressesModel.getCity());
            locality.setText(addressesModel.getLocality());
            flatno.setText(addressesModel.getFlatno());
            pincode.setText(addressesModel.getPincode());
            landMark.setText(addressesModel.getLandMark());
            name.setText(addressesModel.getName());
            mobile.setText(addressesModel.getMobile().substring(3, 13));
            if (addressesModel.getAlternetMobile().equals("")) {
                alternetMobile.setText(addressesModel.getAlternetMobile().substring(3, 13));
            }
            State.setText(addressesModel.getState());
            savebtn.setText("Update");

        } else {
            position = DBqueries.addressesModelList.size();
        }


        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!TextUtils.isEmpty(city.getText())) {
                    if (!TextUtils.isEmpty(locality.getText())) {
                        if (!TextUtils.isEmpty(flatno.getText())) {
                            if (!TextUtils.isEmpty(pincode.getText()) && pincode.getText().length() == 6) {
                                if (!TextUtils.isEmpty(name.getText())) {
                                    if (!TextUtils.isEmpty(mobile.getText()) && mobile.getText().length() == 10) {
                                        loadingDialog.show();
                                        Map<String, Object> addAddress = new HashMap<>();
                                        if (!TextUtils.isEmpty(floorNo.getText())) {
                                            addAddress.put("flat_no_" + String.valueOf(position + 1), floorNo.getText().toString()+flatno.getText().toString());
                                        } else {

                                            addAddress.put("flat_no_" + String.valueOf(position + 1), "NA"+flatno.getText().toString());


                                        }
                                        addAddress.put("city_" + String.valueOf(position + 1), city.getText().toString());
                                        addAddress.put("locality_" + String.valueOf(position + 1), locality.getText().toString());
                                        addAddress.put("flat_no_" + String.valueOf(position + 1), flatno.getText().toString());
                                        addAddress.put("landmark_" + String.valueOf(position + 1), landMark.getText().toString());
                                        addAddress.put("pincode_" + String.valueOf(position + 1), pincode.getText().toString());
                                        addAddress.put("state_" + String.valueOf(position + 1), State.getText().toString());
                                        addAddress.put("fullname_" + String.valueOf(position + 1), name.getText().toString());
                                        addAddress.put("mobile_no_" + String.valueOf(position + 1), "+91" + mobile.getText().toString());
                                        if (!TextUtils.isEmpty(alternetMobile.getText())) {
                                            addAddress.put("alter_mobile_no_" + String.valueOf(position + 1), "+91" + alternetMobile.getText().toString());
                                        } else {
                                            addAddress.put("alter_mobile_no_" + String.valueOf(position + 1), alternetMobile.getText().toString());
                                        }
                                        if (!updateAddress) {
                                            addAddress.put("list_size", (long) DBqueries.addressesModelList.size() + 1);
                                            if (getIntent().getStringExtra("INTENT").equals("manage")) {
                                                if (DBqueries.addressesModelList.size() == 0) {
                                                    addAddress.put("selected_" + String.valueOf(position + 1), true);
                                                } else {
                                                    addAddress.put("selected_" + String.valueOf(position + 1), false);
                                                }
                                            } else {
                                                addAddress.put("selected_" + String.valueOf(position + 1), true);
                                            }
                                            if (DBqueries.addressesModelList.size() > 0) {
                                                addAddress.put("selected_" + (DBqueries.selectedAddress + 1), false);
                                            }
                                        }
                                        FirebaseFirestore.getInstance().collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA")
                                                .document("MY_ADDRESSES")
                                                .update(addAddress).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    if (!updateAddress) {
                                                        if (DBqueries.addressesModelList.size() > 0) {
                                                            DBqueries.addressesModelList.get(DBqueries.selectedAddress).setSelected(false);
                                                        }
                                                        DBqueries.addressesModelList.add(new AddressesModel(true, city.getText().toString(), locality.getText().toString(), landMark.getText().toString(), flatno.getText().toString(), pincode.getText().toString(), State.getText().toString(), name.getText().toString(), "+91" + mobile.getText().toString(), "+91" + alternetMobile.getText().toString()));
                                                        if (getIntent().getStringExtra("INTENT").equals("manage")) {
                                                            if (DBqueries.addressesModelList.size() == 0) {
                                                                DBqueries.selectedAddress = DBqueries.addressesModelList.size() - 1;
                                                            }
                                                        } else {
                                                            DBqueries.selectedAddress = DBqueries.addressesModelList.size() - 1;
                                                        }

                                                    } else {
                                                        DBqueries.addressesModelList.set(position, new AddressesModel(true, city.getText().toString(), locality.getText().toString(), landMark.getText().toString(), flatno.getText().toString(), pincode.getText().toString(), State.getText().toString(), name.getText().toString(), "+91" + mobile.getText().toString(), "+91" + alternetMobile.getText().toString()));

                                                    }
                                                    if (getIntent().getStringExtra("INTENT").equals("deliveryIntent")) {
                                                        Intent deliveryIntent = new Intent(AddAdressActivity.this, DeliveryActivity.class);
                                                        startActivity(deliveryIntent);
                                                    } else {
                                                        MyAddressesActivity.refreshItem(DBqueries.selectedAddress, DBqueries.addressesModelList.size() - 1);
                                                    }
                                                    finish();
                                                } else {
                                                    String error = task.getException().getMessage();
                                                    Toast.makeText(AddAdressActivity.this, error, Toast.LENGTH_SHORT).show();
                                                }
                                                loadingDialog.dismiss();
                                            }
                                        });


                                    } else {
                                        mobile.requestFocus();
                                        Toast.makeText(AddAdressActivity.this, "Please provide Valid mobile number", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    name.requestFocus();
                                    Toast.makeText(AddAdressActivity.this, "Please Enter your Name ", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                pincode.requestFocus();
                                Toast.makeText(AddAdressActivity.this, "Please provide valid pincode", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            flatno.requestFocus();
                        }
                    } else {
                        locality.requestFocus();
                    }
                } else {
                    city.requestFocus();
                }
//
            }
        });
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

}
