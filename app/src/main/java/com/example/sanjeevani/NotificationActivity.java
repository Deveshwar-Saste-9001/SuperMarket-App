package com.example.sanjeevani;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.sanjeevani.Adapter.NotificationAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class NotificationActivity extends AppCompatActivity {

    private RecyclerView notificationRecyclerView;
    public static NotificationAdapter notificationAdapter;
    private boolean runQury = false;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        Toolbar toolbar = (Toolbar) findViewById(R.id.notificationToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Notifications");

        notificationRecyclerView = findViewById(R.id.notificationRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        notificationRecyclerView.setLayoutManager(layoutManager);

        notificationAdapter = new NotificationAdapter(DBqueries.notificationModelList);
        notificationRecyclerView.setAdapter(notificationAdapter);

        Map<String, Object> readMap = new HashMap<>();
        for (int x = 0; x < DBqueries.notificationModelList.size(); x++) {
            if (DBqueries.notificationModelList.get(x).isReaded()) {
                runQury = true;
            }
            readMap.put("Readed_" + x, true);
        }
        if (runQury) {
            FirebaseFirestore.getInstance().collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_NOTIFICATIONS")
                    .update(readMap);
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        for (int x = 0; x < DBqueries.notificationModelList.size(); x++) {
            DBqueries.notificationModelList.get(x).setReaded(true);
        }
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
