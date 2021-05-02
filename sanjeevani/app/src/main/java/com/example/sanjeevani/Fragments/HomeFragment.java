package com.example.sanjeevani.Fragments;


import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.sanjeevani.Adapter.CategoryAdapter;
import com.example.sanjeevani.Adapter.HomePageAdapter;
import com.example.sanjeevani.DBqueries;
import com.example.sanjeevani.Model.CategoryModel;
import com.example.sanjeevani.Model.HomePageModel;
import com.example.sanjeevani.Model.HorizontalProductScrollModel;
import com.example.sanjeevani.Model.SliderModel;
import com.example.sanjeevani.Model.WishListModel;
import com.example.sanjeevani.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import static com.example.sanjeevani.DBqueries.categoryModelList;
import static com.example.sanjeevani.DBqueries.lists;
import static com.example.sanjeevani.DBqueries.loadCategories;
import static com.example.sanjeevani.DBqueries.loadFragmentData;
import static com.example.sanjeevani.DBqueries.loadedCategoriesNames;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {


    public HomeFragment() {
        // Required empty public constructor
    }

    private RecyclerView categoryRecyclerview;
    private CategoryAdapter categoryAdapter;
    private RecyclerView HomepageRecycler;
    private List<CategoryModel> categoryModeFakelList = new ArrayList<>();
    private List<HomePageModel> homePageModelFakeList = new ArrayList<>();
    private FirebaseFirestore firebaseFirestore;
    private HomePageAdapter homePageAdapter;
    private ImageView noInternetConnection;
    public static SwipeRefreshLayout swipeRefreshLayout;
    private ConnectivityManager connectivityManager;
    private NetworkInfo networkInfo;

    @SuppressLint("WrongConstant")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        swipeRefreshLayout = view.findViewById(R.id.Refresh_layout1);
        swipeRefreshLayout.setColorSchemeColors(getContext().getResources().getColor(R.color.colorPrimary), getContext().getResources().getColor(R.color.colorPrimary), getContext().getResources().getColor(R.color.colorPrimary));

        noInternetConnection = view.findViewById(R.id.no_internet_connection1);

        categoryRecyclerview = view.findViewById(R.id.Category_reciclerView1);
        HomepageRecycler = view.findViewById(R.id.HomePage_reciclerView1);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        categoryRecyclerview.setLayoutManager(linearLayoutManager);


        final LinearLayoutManager homepageLayoutManager = new LinearLayoutManager(getContext());
        homepageLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        HomepageRecycler.setLayoutManager(homepageLayoutManager);

/////////////////////categoryFakeList
        categoryModeFakelList.add(new CategoryModel("null", ""));
        categoryModeFakelList.add(new CategoryModel("", ""));
        categoryModeFakelList.add(new CategoryModel("", ""));
        categoryModeFakelList.add(new CategoryModel("", ""));
        categoryModeFakelList.add(new CategoryModel("", ""));
        categoryModeFakelList.add(new CategoryModel("", ""));
        categoryModeFakelList.add(new CategoryModel("", ""));
        categoryModeFakelList.add(new CategoryModel("", ""));
        categoryModeFakelList.add(new CategoryModel("", ""));
        categoryModeFakelList.add(new CategoryModel("", ""));
////////////////////////
        categoryAdapter = new CategoryAdapter(categoryModeFakelList);


        homePageAdapter = new HomePageAdapter(homePageModelFakeList);

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


        connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected() == true) {
            noInternetConnection.setVisibility(View.GONE);

            categoryRecyclerview.setVisibility(View.VISIBLE);
            HomepageRecycler.setVisibility(View.VISIBLE);

            if (categoryModelList.size() == 0) {
                loadCategories(categoryRecyclerview, getContext());
            } else {
                categoryAdapter = new CategoryAdapter(categoryModelList);
                categoryAdapter.notifyDataSetChanged();
            }
            categoryRecyclerview.setAdapter(categoryAdapter);
/////
            if (lists.size() == 0) {
                loadedCategoriesNames.add("Home");
                lists.add(new ArrayList<HomePageModel>());
                homePageAdapter = new HomePageAdapter(lists.get(0));
                loadFragmentData(HomepageRecycler, getContext(), 0, "Home");
            } else {
                homePageAdapter = new HomePageAdapter(lists.get(0));
                homePageAdapter.notifyDataSetChanged();
            }
            HomepageRecycler.setAdapter(homePageAdapter);

        } else {
            categoryRecyclerview.setVisibility(View.GONE);
            HomepageRecycler.setVisibility(View.GONE);
            Glide.with(this).load(R.drawable.giphy).into(noInternetConnection);
            noInternetConnection.setVisibility(View.VISIBLE);
        }

        ////////////refresslayou
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                networkInfo = connectivityManager.getActiveNetworkInfo();
                DBqueries.clearData();
                if (networkInfo != null && networkInfo.isConnected() == true) {
                    noInternetConnection.setVisibility(View.GONE);
                    categoryRecyclerview.setVisibility(View.VISIBLE);
                    HomepageRecycler.setVisibility(View.VISIBLE);


                    categoryAdapter = new CategoryAdapter(categoryModeFakelList);
                    homePageAdapter = new HomePageAdapter(homePageModelFakeList);

                    categoryRecyclerview.setAdapter(categoryAdapter);
                    HomepageRecycler.setAdapter(homePageAdapter);
                    loadCategories(categoryRecyclerview, getContext());

                    loadedCategoriesNames.add("Home");
                    lists.add(new ArrayList<HomePageModel>());
                    loadFragmentData(HomepageRecycler, getContext(), 0, "Home");


                } else {
                    categoryRecyclerview.setVisibility(View.GONE);
                    HomepageRecycler.setVisibility(View.GONE);

                    swipeRefreshLayout.setRefreshing(false);
                    Glide.with(getContext()).load(R.drawable.giphy).into(noInternetConnection);
                    noInternetConnection.setVisibility(View.VISIBLE);

                }

            }
        });
        ////////////
        return view;

    }


}
