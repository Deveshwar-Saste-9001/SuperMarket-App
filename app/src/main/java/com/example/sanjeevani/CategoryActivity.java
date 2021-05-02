package com.example.sanjeevani;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sanjeevani.Adapter.HomePageAdapter;
import com.example.sanjeevani.Model.HomePageModel;
import com.example.sanjeevani.Model.HorizontalProductScrollModel;
import com.example.sanjeevani.Model.SliderModel;
import com.example.sanjeevani.Model.WishListModel;

import java.util.ArrayList;
import java.util.List;

import static com.example.sanjeevani.DBqueries.lists;
import static com.example.sanjeevani.DBqueries.loadFragmentData;
import static com.example.sanjeevani.DBqueries.loadedCategoriesNames;

public class CategoryActivity extends AppCompatActivity {
    //private RecyclerView CategoryRecyclerView;
    private RecyclerView CategoryRecyclerView;
    private HomePageAdapter adapter;
    private List<HomePageModel> homePageModelFakeList = new ArrayList<>();

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        String Title = getIntent().getStringExtra("CategoryName");
        getSupportActionBar().setTitle(Title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CategoryRecyclerView = findViewById(R.id.Category_Recycler_View_page1);

        /////////HomePage Fake List;
        List<SliderModel> sliderModelFakeList = new ArrayList<>();
        sliderModelFakeList.add(new SliderModel("null", "#b6b6b6"));
        sliderModelFakeList.add(new SliderModel("null", "#b6b6b6"));
        sliderModelFakeList.add(new SliderModel("null", "#b6b6b6"));
        sliderModelFakeList.add(new SliderModel("null", "#b6b6b6"));
        List<HorizontalProductScrollModel> horizontalProductScrollModelFakeList = new ArrayList<>();
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("", "", "", "", ""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("", "", "", "", ""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("", "", "", "", ""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("", "", "", "", ""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("", "", "", "", ""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("", "", "", "", ""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("", "", "", "", ""));

        homePageModelFakeList.add(new HomePageModel(0, sliderModelFakeList));
        homePageModelFakeList.add(new HomePageModel(1, "", "#b6b6b6"));
        homePageModelFakeList.add(new HomePageModel(2, "", "#b6b6b6", horizontalProductScrollModelFakeList, new ArrayList<WishListModel>()));
        homePageModelFakeList.add(new HomePageModel(3, "", "#b6b6b6", horizontalProductScrollModelFakeList));

        ////////////

        LinearLayoutManager LayoutManager = new LinearLayoutManager(this);
        LayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        CategoryRecyclerView.setLayoutManager(LayoutManager);

        adapter = new HomePageAdapter(homePageModelFakeList);


        int listPosition = 0;
        for (int x = 0; x < loadedCategoriesNames.size(); x++) {
            if (loadedCategoriesNames.get(x).equals(Title.toUpperCase())) {
                listPosition = x;
            }
        }
        if (listPosition == 0) {
            loadedCategoriesNames.add(Title.toUpperCase());
            lists.add(new ArrayList<HomePageModel>());
            //adapter = new HomePageAdapter(lists.get(loadedCategoriesNames.size()-1));
            loadFragmentData(CategoryRecyclerView, this, loadedCategoriesNames.size() - 1, Title.toUpperCase());

        } else {
            adapter = new HomePageAdapter(lists.get(listPosition));
        }


        CategoryRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_icon, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_Search3) {
            Intent searchIntent = new Intent(this, SearchActivity.class);
            startActivity(searchIntent);
            return true;
        } else if (id == android.R.id.home) {
            finish();
            return true;

        }

        return super.onOptionsItemSelected(item);
    }
}
