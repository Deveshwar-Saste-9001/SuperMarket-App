package com.example.sanjeevani.Fragments;


import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sanjeevani.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class UpdatePasswordfragment extends Fragment {


    public UpdatePasswordfragment() {
        // Required empty public constructor
    }

    private EditText oldPassword, newPassword, comfirmNewPassword;
    private Button updatePasswordbtn;
    private Dialog loadingDialog;
    private String email;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_update_passwordfragment, container, false);


        loadingDialog = new Dialog(getContext());
        loadingDialog.setContentView(R.layout.loading_progress_bar);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getContext().getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        email = getArguments().getString("Email");

        oldPassword = view.findViewById(R.id.oldPassword);
        newPassword = view.findViewById(R.id.newPassword);
        comfirmNewPassword = view.findViewById(R.id.new_confirm_Password);
        updatePasswordbtn = view.findViewById(R.id.update_password_btn);
        oldPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkinput();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        newPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkinput();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        comfirmNewPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkinput();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        updatePasswordbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckEmailAndPassword();
            }
        });


        return view;
    }

    private void checkinput() {
        if (!TextUtils.isEmpty(oldPassword.getText()) && oldPassword.length() >= 8) {
            if (!TextUtils.isEmpty(newPassword.getText()) && newPassword.length() >= 8) {
                if (!TextUtils.isEmpty(comfirmNewPassword.getText()) && comfirmNewPassword.length() >= 8) {

                    updatePasswordbtn.setEnabled(true);
                    updatePasswordbtn.setTextColor(Color.rgb(255, 255, 255));

                } else {
                    updatePasswordbtn.setEnabled(false);
                    updatePasswordbtn.setTextColor(Color.argb(50, 255, 255, 255));
                }
            } else {
                updatePasswordbtn.setEnabled(false);
                updatePasswordbtn.setTextColor(Color.argb(50, 255, 255, 255));
            }
        } else {
            updatePasswordbtn.setEnabled(false);
            updatePasswordbtn.setTextColor(Color.argb(50, 255, 255, 255));
        }
    }

    private void CheckEmailAndPassword() {
        if (newPassword.getText().toString().equals(comfirmNewPassword.getText().toString())) {
            loadingDialog.show();
            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            AuthCredential credential = EmailAuthProvider
                    .getCredential(email, oldPassword.getText().toString());

            user.reauthenticate(credential)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                user.updatePassword(newPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            oldPassword.setText(null);
                                            newPassword.setText(null);
                                            comfirmNewPassword.setText(null);
                                            getActivity().finish();
                                            Toast.makeText(getContext(), "Password updated successfully", Toast.LENGTH_SHORT).show();
                                        } else {
                                            String error = task.getException().getMessage();
                                            Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                                        }
                                        loadingDialog.dismiss();
                                    }
                                });

                            } else {
                                loadingDialog.dismiss();
                                String error = task.getException().getMessage();
                                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        } else {
            comfirmNewPassword.setError("Password doesn't matched");
            Toast.makeText(getContext(), "Please Enter Valid Email Address", Toast.LENGTH_SHORT).show();
        }

    }


}
