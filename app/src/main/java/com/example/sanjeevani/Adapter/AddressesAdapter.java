package com.example.sanjeevani.Adapter;

import android.app.Dialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sanjeevani.AddAdressActivity;
import com.example.sanjeevani.DBqueries;
import com.example.sanjeevani.Model.AddressesModel;
import com.example.sanjeevani.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static com.example.sanjeevani.DeliveryActivity.SELECT_ADDRESS;
import static com.example.sanjeevani.Fragments.MyAccountFragment.MANAGE_ADDRESS;
import static com.example.sanjeevani.MyAddressesActivity.refreshItem;

public class AddressesAdapter extends RecyclerView.Adapter<AddressesAdapter.ViewHolder> {

    private List<AddressesModel> addressesModelList;
    private int MODE;
    private int preSelectedPosition;
    private boolean refress = false;
    private Dialog loadingDialog;

    public AddressesAdapter(List<AddressesModel> addressesModelList, int MODE, Dialog loadingDialog) {
        this.addressesModelList = addressesModelList;
        this.MODE = MODE;
        preSelectedPosition = DBqueries.selectedAddress;
        this.loadingDialog = loadingDialog;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.addresses_item_layout, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String name = addressesModelList.get(position).getName();
        String mobileNo = addressesModelList.get(position).getMobile();
        String city = addressesModelList.get(position).getCity();
        String locality = addressesModelList.get(position).getLocality();
        String flatno = addressesModelList.get(position).getFlatno();
        String pincode = addressesModelList.get(position).getPincode();
        String landMark = addressesModelList.get(position).getLandMark();
        String alternetMobile = addressesModelList.get(position).getAlternetMobile();
        String state = addressesModelList.get(position).getState();
        boolean selected = addressesModelList.get(position).isSelected();

        holder.setData(name, city, pincode, selected, position, mobileNo, alternetMobile, flatno, locality, landMark, state);


    }

    @Override
    public int getItemCount() {
        return addressesModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView Fullname;
        private TextView adress;
        private TextView pincode;
        private ImageView checkIcon;
        private LinearLayout optionContainer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            Fullname = itemView.findViewById(R.id.Name_myad1);
            adress = itemView.findViewById(R.id.address_myad1);
            pincode = itemView.findViewById(R.id.pincode_myad1);
            checkIcon = itemView.findViewById(R.id.icon_view_myad1);
            optionContainer = itemView.findViewById(R.id.option_container_myad1);

        }

        private void setData(String username, String city, String userPincode, boolean selected, final int position, String mobileNo, String alternatemobile, String flatNo, final String locality, String landMark, String state) {
            if (alternatemobile.equals("")) {
                Fullname.setText(username + " - " + mobileNo);
            } else {
                Fullname.setText(username + " - " + mobileNo + " or " + alternatemobile);
            }
            if (landMark.equals("")) {
                adress.setText(flatNo + ", " + locality + ", " + city + ", " + state);
            } else {
                adress.setText(flatNo + ", " + locality + ", " + landMark + ", " + city + ", " + state);
            }
            pincode.setText(userPincode);

            if (MODE == SELECT_ADDRESS) {
                checkIcon.setImageResource(R.drawable.ic_check_black_24dp);
                if (selected) {
                    checkIcon.setVisibility(View.VISIBLE);
                    preSelectedPosition = position;
                } else {
                    checkIcon.setVisibility(View.GONE);
                }
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (preSelectedPosition != position) {
                            addressesModelList.get(position).setSelected(true);
                            addressesModelList.get(preSelectedPosition).setSelected(false);
                            refreshItem(preSelectedPosition, position);
                            preSelectedPosition = position;
                            DBqueries.selectedAddress = position;
                        }
                    }
                });


            } else if (MODE == MANAGE_ADDRESS) {
                optionContainer.setVisibility(View.GONE);
                optionContainer.getChildAt(0).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //////edit address
                        Intent addAddressIntent = new Intent(itemView.getContext(), AddAdressActivity.class);
                        addAddressIntent.putExtra("INTENT", "update_address");
                        addAddressIntent.putExtra("INDEX", position);
                        itemView.getContext().startActivity(addAddressIntent);
                        refress = false;
                    }
                });
                optionContainer.getChildAt(1).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        loadingDialog.show();
                        Map<String, Object> addresses = new HashMap<>();
                        int x = 0;
                        int selected = 0;
                        for (int i = 0; i < addressesModelList.size(); i++) {
                            if (i != position) {
                                x++;
                                addresses.put("city_" + x, addressesModelList.get(i).getCity());
                                addresses.put("locality_" + x, addressesModelList.get(i).getLocality());
                                addresses.put("landmark_" + x, addressesModelList.get(i).getLandMark());
                                addresses.put("flat_no_" + x, addressesModelList.get(i).getFlatno());
                                addresses.put("pincode_" + x, addressesModelList.get(i).getPincode());
                                addresses.put("state_" + x, addressesModelList.get(i).getState());
                                addresses.put("fullname_" + x, addressesModelList.get(i).getName());
                                addresses.put("mobile_no_" + x, addressesModelList.get(i).getMobile());
                                addresses.put("alter_mobile_no_" + x, addressesModelList.get(i).getAlternetMobile());
                                if (addressesModelList.get(position).isSelected()) {
                                    if (position - 1 >= 0) {
                                        if (x == position) {
                                            addresses.put("selected_" + x, true);
                                            selected = x;
                                        } else {
                                            addresses.put("selected_" + x, addressesModelList.get(i).isSelected());

                                        }
                                    } else {
                                        if (x == 1) {
                                            addresses.put("selected_" + x, true);
                                            selected = x;
                                        } else {
                                            addresses.put("selected_" + x, addressesModelList.get(i).isSelected());
                                        }

                                    }
                                } else {
                                    addresses.put("selected_" + x, addressesModelList.get(i).isSelected());
                                    if (addressesModelList.get(i).isSelected()) {
                                        selected = x;
                                    }
                                }
                            }
                        }
                        addresses.put("list_size", x);

                        final int finalSelected = selected;
                        FirebaseFirestore.getInstance().collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_ADDRESSES")
                                .set(addresses).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    DBqueries.addressesModelList.remove(position);
                                    if (finalSelected != -1) {
                                        DBqueries.selectedAddress = finalSelected - 1;
                                        DBqueries.addressesModelList.get(finalSelected - 1).setSelected(true);
                                    } else if (DBqueries.addressesModelList.size() == 0) {
                                        DBqueries.selectedAddress = -1;
                                    }
                                    notifyDataSetChanged();
                                } else {
                                    String error = task.getException().getMessage();
                                    Toast.makeText(itemView.getContext(), error, Toast.LENGTH_SHORT).show();
                                }
                                loadingDialog.dismiss();
                            }
                        });
                        refress = false;
                        //////remove address
                    }
                });

                checkIcon.setImageResource(R.drawable.ic_more_vert_black_24dp);
                checkIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        optionContainer.setVisibility(View.VISIBLE);
                        if (refress) {
                            refreshItem(preSelectedPosition, preSelectedPosition);
                        } else {
                            refress = true;
                        }
                        preSelectedPosition = position;

                    }
                });
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        refreshItem(preSelectedPosition, preSelectedPosition);
                        preSelectedPosition = -1;

                    }
                });


            }

        }
    }
}
