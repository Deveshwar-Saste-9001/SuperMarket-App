package com.example.sanjeevani.Fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.sanjeevani.Adapter.ChatAdapter;
import com.example.sanjeevani.Model.ChatModel;
import com.example.sanjeevani.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment {


    public ChatFragment() {
        // Required empty public constructor
    }

    private EditText messase;
    private ImageView sendBtn;
    private RecyclerView chatRecyclerView;
    DatabaseReference reference;

    ChatAdapter chatAdapter;
    List<ChatModel> chatModelList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        messase = view.findViewById(R.id.messageTosend);
        sendBtn = view.findViewById(R.id.send_message);
        chatRecyclerView = view.findViewById(R.id.chat_RecyclerView);

        chatRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setStackFromEnd(true);
        chatRecyclerView.setLayoutManager(linearLayoutManager);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = messase.getText().toString();
                if (!msg.equals("")) {
                    sendMessasge(FirebaseAuth.getInstance().getUid(), "vZvExgTFazQETPQSFpDWyH5A2go1", msg);
                } else {
                    Toast.makeText(getContext(), "You can't send empty message", Toast.LENGTH_SHORT).show();
                }
                messase.setText("");
                messase.requestFocus();
            }
        });
        readMessage(FirebaseAuth.getInstance().getUid(), "vZvExgTFazQETPQSFpDWyH5A2go1");


        return view;
    }

    private void sendMessasge(String sender, String receiver, String message) {
        reference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference rootref = reference.child("Chats").push();

        String MsgId = rootref.getKey();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);
        hashMap.put("messageId", MsgId);
        rootref.setValue(hashMap);

        final DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("ChatList")
                .child(FirebaseAuth.getInstance().getUid())
                .child("vZvExgTFazQETPQSFpDWyH5A2go1");
        chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    chatRef.child("id").setValue("vZvExgTFazQETPQSFpDWyH5A2go1");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void readMessage(final String myid, final String userId) {
        chatModelList = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatModelList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    ChatModel chatModel = dataSnapshot1.getValue(ChatModel.class);
                    if (chatModel.getReceiver().equals(myid) && chatModel.getSender().equals(userId) || chatModel.getReceiver().equals(userId) && chatModel.getSender().equals(myid)) {
                        chatModelList.add(chatModel);
                    }
                    chatAdapter = new ChatAdapter(getContext(), chatModelList);
                    chatRecyclerView.setAdapter(chatAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
