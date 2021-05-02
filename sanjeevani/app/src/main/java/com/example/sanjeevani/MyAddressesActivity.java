package com.example.sanjeevani;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sanjeevani.Adapter.AddressesAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import static com.example.sanjeevani.DeliveryActivity.SELECT_ADDRESS;

public class MyAddressesActivity extends AppCompatActivity {

    private int previousAddress;

    private RecyclerView AddressesRecyclerView;
    private static AddressesAdapter addressesAdapter;
    private Button deleverHereBtn;
    private TextView addressSaved;
    private LinearLayout addNewAddressBtn;
    private Dialog loadingDialog;
    private int Mode;


    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_addresses);
        Toolbar toolbar = (Toolbar) findViewById(R.id.Selectaddtoolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("My Address");

        loadingDialog = new Dialog(this);
        loadingDialog.setContentView(R.layout.loading_progress_bar);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                addressSaved.setText(String.valueOf(DBqueries.addressesModelList.size()) + " Saved addresses");
            }
        });

        previousAddress = DBqueries.selectedAddress;

        AddressesRecyclerView = findViewById(R.id.adress_myad_reciclerview1);
        deleverHereBtn = findViewById(R.id.deliver_here_btn_myad1);
        addNewAddressBtn = findViewById(R.id.add_new_address_btn_myad1);
        addressSaved = findViewById(R.id.Address_save_myad1);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        AddressesRecyclerView.setLayoutManager(linearLayoutManager);
        Mode = getIntent().getIntExtra("MODE", -1);

        if (Mode == SELECT_ADDRESS) {
            deleverHereBtn.setVisibility(View.VISIBLE);
        } else {
            deleverHereBtn.setVisibility(View.GONE);
        }

        addressesAdapter = new AddressesAdapter(DBqueries.addressesModelList, Mode, loadingDialog);
        AddressesRecyclerView.setAdapter(addressesAdapter);
        ((SimpleItemAnimator) AddressesRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        addressesAdapter.notifyDataSetChanged();

        addNewAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addAddressIntent = new Intent(MyAddressesActivity.this, AddAdressActivity.class);
                if (Mode != SELECT_ADDRESS) {
                    addAddressIntent.putExtra("INTENT", "manage");
                } else {
                    addAddressIntent.putExtra("INTENT", "null");
                }
                startActivity(addAddressIntent);
            }
        });


        deleverHereBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DBqueries.selectedAddress != previousAddress) {
                    final int previeousindex = previousAddress;

                    loadingDialog.show();
                    Map<String, Object> updateSelection = new HashMap<>();
                    updateSelection.put("selected_" + String.valueOf(previousAddress + 1), false);
                    updateSelection.put("selected_" + String.valueOf(DBqueries.selectedAddress + 1), true);

                    previousAddress = DBqueries.selectedAddress;
                    FirebaseFirestore.getInstance().collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA")
                            .document("MY_ADDRESSES").update(updateSelection).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                finish();
                            } else {
                                previousAddress = previeousindex;
                                String error = task.getException().getMessage();
                                Toast.makeText(MyAddressesActivity.this, error, Toast.LENGTH_SHORT).show();
                            }
                            loadingDialog.dismiss();
                        }
                    });
                } else {
                    finish();
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        addressSaved.setText(String.valueOf(DBqueries.addressesModelList.size()) + " Saved addresses");
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            if (Mode == SELECT_ADDRESS) {
                if (DBqueries.selectedAddress != previousAddress) {
                    DBqueries.addressesModelList.get(DBqueries.selectedAddress).setSelected(false);
                    DBqueries.addressesModelList.get(previousAddress).setSelected(true);
                    DBqueries.selectedAddress = previousAddress;
                }
            }
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static void refreshItem(int deselect, int select) {

        addressesAdapter.notifyItemChanged(deselect);
        addressesAdapter.notifyItemChanged(select);


    }

    @Override
    public void onBackPressed() {
        if (Mode == SELECT_ADDRESS) {
            if (DBqueries.selectedAddress != previousAddress) {
                DBqueries.addressesModelList.get(DBqueries.selectedAddress).setSelected(false);
                DBqueries.addressesModelList.get(previousAddress).setSelected(true);
                DBqueries.selectedAddress = previousAddress;
            }
        }
        super.onBackPressed();

    }
}
