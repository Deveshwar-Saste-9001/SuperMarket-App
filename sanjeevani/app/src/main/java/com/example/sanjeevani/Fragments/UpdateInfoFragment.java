package com.example.sanjeevani.Fragments;


import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.sanjeevani.DBqueries;
import com.example.sanjeevani.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class UpdateInfoFragment extends Fragment {


    public UpdateInfoFragment() {
        // Required empty public constructor
    }

    private CircleImageView userProfile;
    private Button changeProfileBtn, removeProfileBtn, updateInfoBtn;
    private EditText username;
    private Dialog loadingDialog, cPassDialog;
    private EditText cpassEdit;
    private Button donePass;
    private String name;
    private String email;
    private String photo;
    private Uri imageuri;
    private boolean updatePhoto = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_update_info, container, false);

        userProfile = view.findViewById(R.id.profile_update_image);
        changeProfileBtn = view.findViewById(R.id.change_photoBtn);
        removeProfileBtn = view.findViewById(R.id.remove_photoBtn);
        updateInfoBtn = view.findViewById(R.id.update_user_btn);
        username = view.findViewById(R.id.name_update);


        loadingDialog = new Dialog(getContext());
        loadingDialog.setContentView(R.layout.loading_progress_bar);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getContext().getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        cPassDialog = new Dialog(getContext());
        cPassDialog.setContentView(R.layout.password_confirmation_dialog);
        cPassDialog.setCancelable(true);
        cPassDialog.getWindow().setBackgroundDrawable(getContext().getDrawable(R.drawable.slider_background));
        cPassDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        cpassEdit = cPassDialog.findViewById(R.id.confirm_password);
        donePass = cPassDialog.findViewById(R.id.done_password);


        name = getArguments().getString("Name");
        email = getArguments().getString("Email");
        photo = getArguments().getString("Photo");
        if (!photo.equals("")) {
            Glide.with(getContext()).load(photo).into(userProfile);
        }
        username.setText(name);


        username.addTextChangedListener(new TextWatcher() {
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

        changeProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (getContext().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
                        galleryIntent.setType("image/+");
                        startActivityForResult(galleryIntent, 1);
                    } else {
                        getActivity().requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
                    }
                } else {
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK);
                    galleryIntent.setType("image/+");
                    startActivityForResult(galleryIntent, 1);
                }

            }
        });

        removeProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageuri = null;
                updatePhoto = true;
                Glide.with(getContext()).load(R.drawable.profile).into(userProfile);
            }
        });

        updateInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                updatePhotoF(user);
            }
        });

        return view;
    }

    private void checkinput() {
        if (!TextUtils.isEmpty(username.getText())) {
            updateInfoBtn.setEnabled(true);
            updateInfoBtn.setTextColor(Color.rgb(255, 255, 255));
        } else {
            updateInfoBtn.setEnabled(false);
            updateInfoBtn.setTextColor(Color.argb(50, 255, 255, 255));
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 2) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK);
                galleryIntent.setType("image/+");
                startActivityForResult(galleryIntent, 1);
            } else {
                Toast.makeText(getContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == getActivity().RESULT_OK) {
                if (data != null) {
                    imageuri = data.getData();
                    updatePhoto = true;
                    Glide.with(getContext()).load(imageuri).into(userProfile);
                } else {
                    Toast.makeText(getContext(), "Image not found!", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }

    private void updatePhotoF(final FirebaseUser user) {
        ////updating photo
        if (updatePhoto) {
            final StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("profiles/" + user.getUid() + ".jpg");
            if (imageuri != null) {
                Glide.with(getContext()).asBitmap().load(imageuri).into(new ImageViewTarget<Bitmap>(userProfile) {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        resource.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] data = baos.toByteArray();

                        UploadTask uploadTask = storageReference.putBytes(data);
                        uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                if (task.isSuccessful()) {
                                    storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Uri> task) {
                                            if (task.isSuccessful()) {
                                                imageuri = task.getResult();
                                                DBqueries.profile = task.getResult().toString();
                                                Glide.with(getContext()).load(DBqueries.profile).into(userProfile);

                                                Map<String, Object> updatedata = new HashMap<>();
                                                updatedata.put("Name", username.getText().toString());
                                                updatedata.put("profile", DBqueries.profile);

                                                updateFields(user, updatedata);

                                            } else {
                                                loadingDialog.dismiss();
                                                DBqueries.profile = "";
                                                String error = task.getException().getMessage();
                                                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                                            }

                                        }
                                    });
                                } else {
                                    loadingDialog.dismiss();
                                    String error = task.getException().getMessage();
                                    Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                        return;
                    }

                    @Override
                    protected void setResource(@Nullable Bitmap resource) {
                        userProfile.setImageResource(R.drawable.profile);
                    }
                });


            } else {
                storageReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            DBqueries.profile = "";
                            Map<String, Object> updatedata = new HashMap<>();
                            updatedata.put("Name", username.getText().toString());
                            updatedata.put("profile", "");

                            updateFields(user, updatedata);
                        } else {
                            String error = task.getException().getMessage();
                            Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        } else {
            Map<String, Object> updatedata = new HashMap<>();
            updatedata.put("Name", username.getText().toString());
            updateFields(user, updatedata);
        }

        //////////////
    }

    private void updateFields(FirebaseUser user, final Map<String, Object> updatedata) {
        FirebaseFirestore.getInstance().collection("USERS").document(user.getUid())
                .update(updatedata).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    DBqueries.fullname = username.getText().toString();
                    Toast.makeText(getContext(), "Updated successfully", Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                } else {
                    String error = task.getException().getMessage();
                    Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                }
                loadingDialog.dismiss();
            }
        });
    }
}
