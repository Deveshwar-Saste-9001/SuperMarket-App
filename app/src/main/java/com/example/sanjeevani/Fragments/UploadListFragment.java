package com.example.sanjeevani.Fragments;


import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.sanjeevani.Adapter.ListPhotoAdapter;
import com.example.sanjeevani.Model.ListPhotoModel;
import com.example.sanjeevani.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * A simple {@link Fragment} subclass.
 */
public class UploadListFragment extends Fragment {


    public UploadListFragment() {
        // Required empty public constructor
    }

    private Button uploadListBtn;
    private RecyclerView uplodlistRecyclerView;
    private Uri fileUri;
    private String myUrl;
    private StorageTask uploadTask;
    List<ListPhotoModel> listPhotoModelList;
    DatabaseReference reference;

    private Dialog loadingDialog;
    ListPhotoAdapter chatAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_upload_list, container, false);

        loadingDialog = new Dialog(getContext());
        loadingDialog.setContentView(R.layout.loading_progress_bar);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getContext().getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        uploadListBtn = view.findViewById(R.id.list_uploderBtn);
        uplodlistRecyclerView = view.findViewById(R.id.uplodLisyRecycler);

        uplodlistRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setStackFromEnd(true);
        uplodlistRecyclerView.setLayoutManager(linearLayoutManager);


        uploadListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (getContext().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
                        galleryIntent.setType("image/+");
                        startActivityForResult(galleryIntent, 438);
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
        readMessage(FirebaseAuth.getInstance().getUid(), "vZvExgTFazQETPQSFpDWyH5A2go1");

        return view;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 438) {
            if (resultCode == getActivity().RESULT_OK) {
                if (data != null) {
                    loadingDialog.show();
                    fileUri = data.getData();

                    StorageReference storageReference = FirebaseStorage.getInstance().getReference();


                    reference = FirebaseDatabase.getInstance().getReference();
                    final DatabaseReference newkey = reference.child("Lists").push();
                    final String MsgId = newkey.getKey();
                    final StorageReference filePath = storageReference.child("list/" + MsgId + ".jpg");

                    uploadTask = filePath.putFile(fileUri);
                    uploadTask.continueWithTask(new Continuation() {
                        @Override
                        public Object then(@NonNull Task task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }
                            return filePath.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {

                                Uri downloadUrl = task.getResult();
                                myUrl = downloadUrl.toString();

                                String sender = FirebaseAuth.getInstance().getUid();
                                String receiver = "vZvExgTFazQETPQSFpDWyH5A2go1";

                                HashMap<String, Object> hashMap = new HashMap<>();
                                hashMap.put("sender", sender);
                                hashMap.put("receiver", receiver);
                                hashMap.put("message", myUrl);
                                hashMap.put("messageId", MsgId);
                                newkey.setValue(hashMap);

                                final DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference("ListUploadedUser")
                                        .child(receiver)
                                        .child(FirebaseAuth.getInstance().getUid());
                                dataRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (!dataSnapshot.exists()) {
                                            dataRef.child("id").setValue(FirebaseAuth.getInstance().getUid());
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                                loadingDialog.dismiss();
                            } else {
                                loadingDialog.dismiss();
                                Toast.makeText(getContext(), "Image not found!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            }
        }
    }

    private void readMessage(final String myid, final String userId) {
        listPhotoModelList = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Lists");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listPhotoModelList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    ListPhotoModel ModelList = dataSnapshot1.getValue(ListPhotoModel.class);
                    if (ModelList.getReceiver().equals(myid) && ModelList.getSender().equals(userId) || ModelList.getReceiver().equals(userId) && ModelList.getSender().equals(myid)) {
                        listPhotoModelList.add(ModelList);
                    }
                    chatAdapter = new ListPhotoAdapter(getContext(), listPhotoModelList);
                    uplodlistRecyclerView.setAdapter(chatAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
