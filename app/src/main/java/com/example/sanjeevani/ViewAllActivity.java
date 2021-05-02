package com.example.sanjeevani;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;

import com.example.sanjeevani.Adapter.GridProductLayoutAdapter;
import com.example.sanjeevani.Adapter.WishListAdapter;
import com.example.sanjeevani.Model.HorizontalProductScrollModel;
import com.example.sanjeevani.Model.WishListModel;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ViewAllActivity extends AppCompatActivity {
    private RecyclerView viewallRecyclerview;
    private GridView viewallGridView;

    public static List<HorizontalProductScrollModel> ViewAllProductGridModelList;
    public static List<WishListModel> ViewAllProductHorizontalModelList;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all);

        Toolbar toolbar = (Toolbar) findViewById(R.id.ViewAlltoolbar4);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(getIntent().getStringExtra("title"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewallRecyclerview = findViewById(R.id.recicler_view_viewall1);
        viewallGridView = findViewById(R.id.product_gridview_viewall1);

        int layout_code = getIntent().getIntExtra("Layout_code", -1);

        if (layout_code == 0) {
            viewallRecyclerview.setVisibility(View.VISIBLE);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            viewallRecyclerview.setLayoutManager(layoutManager);



            WishListAdapter Adapter = new WishListAdapter(ViewAllProductHorizontalModelList, false);
            viewallRecyclerview.setAdapter(Adapter);
            Adapter.notifyDataSetChanged();
        } else if (layout_code == 1) {

            viewallGridView.setVisibility(View.VISIBLE);

            GridProductLayoutAdapter gridProductLayoutAdapter = new GridProductLayoutAdapter(ViewAllProductGridModelList);
            viewallGridView.setAdapter(gridProductLayoutAdapter);
            gridProductLayoutAdapter.notifyDataSetChanged();

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
