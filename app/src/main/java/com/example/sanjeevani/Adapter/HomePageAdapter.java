package com.example.sanjeevani.Adapter;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.sanjeevani.Model.HomePageModel;
import com.example.sanjeevani.Model.HorizontalProductScrollModel;
import com.example.sanjeevani.Model.SliderModel;
import com.example.sanjeevani.Model.WishListModel;
import com.example.sanjeevani.ProductDetailsActivity;
import com.example.sanjeevani.R;
import com.example.sanjeevani.ViewAllActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class HomePageAdapter extends RecyclerView.Adapter {
    private List<HomePageModel> homePageModelList;
    private RecyclerView.RecycledViewPool recycledViewPool;
    private int lastposition = -1;

    public HomePageAdapter(List<HomePageModel> homePageModelList) {
        this.homePageModelList = homePageModelList;
        recycledViewPool = new RecyclerView.RecycledViewPool();
    }

    @Override
    public int getItemViewType(int position) {
        switch (homePageModelList.get(position).getType()) {
            case 0:
                return HomePageModel.BANNER_SLIDER;
            case 1:
                return HomePageModel.STRIP_ADS_BANNER;
            case 2:
                return HomePageModel.HORIZONTAL_SCROLL;
            case 3:
                return HomePageModel.GRID_VIEW_LAYOUT;
            default:
                return -1;

        }

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case HomePageModel.BANNER_SLIDER:
                View bannerSliderview = LayoutInflater.from(parent.getContext()).inflate(R.layout.sliding_add_banner, parent, false);
                return new BannerSliderViewHolder(bannerSliderview);
            case HomePageModel.STRIP_ADS_BANNER:
                View Stripview = LayoutInflater.from(parent.getContext()).inflate(R.layout.strip_ad_layout, parent, false);
                return new StripAdsBannerViewHolder(Stripview);
            case HomePageModel.HORIZONTAL_SCROLL:
                View horizonatalview = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_scroll_layout, parent, false);
                return new HorizontalProductViewHolder(horizonatalview);
            case HomePageModel.GRID_VIEW_LAYOUT:
                View gridview = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_product_view, parent, false);
                return new GridProductViewHolder(gridview);
            default:
                return null;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (homePageModelList.get(position).getType()) {
            case HomePageModel.BANNER_SLIDER:
                List<SliderModel> sliderModelList = homePageModelList.get(position).getSliderModelList();
                ((BannerSliderViewHolder) holder).setBannersliderViewPager(sliderModelList);
                break;

            case HomePageModel.STRIP_ADS_BANNER:
                String Resource = homePageModelList.get(position).getResource();
                String color = homePageModelList.get(position).getBackgroundcolor();
                ((StripAdsBannerViewHolder) holder).setStripads(Resource, color);
                break;
            case HomePageModel.HORIZONTAL_SCROLL:

                String backcolor = homePageModelList.get(position).getBackgroundcolor();
                String title = homePageModelList.get(position).getTitle();
                List<HorizontalProductScrollModel> horizontalProductScrollModelLi = homePageModelList.get(position).getHorizontalProductScrollModelList();
                List<WishListModel> viewAllproductList = homePageModelList.get(position).getViewAllproductList();
                ((HorizontalProductViewHolder) holder).setHorizontalProductLayout(horizontalProductScrollModelLi, title, backcolor, viewAllproductList);
                break;
            case HomePageModel.GRID_VIEW_LAYOUT:
                String gridtitle = homePageModelList.get(position).getTitle();
                String gridcolor = homePageModelList.get(position).getBackgroundcolor();
                List<HorizontalProductScrollModel> gridProductScrollModelLi = homePageModelList.get(position).getHorizontalProductScrollModelList();
                ((GridProductViewHolder) holder).setGridLayout(gridProductScrollModelLi, gridtitle, gridcolor);
                break;
            default:
                return;
        }
        if (lastposition < position) {
            //Animation animation= AnimationUtils.loadAnimation(holder.itemView.getContext(),R.anim.fade_in);
            holder.itemView.setAnimation(AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.fade_in));
            lastposition = position;
        }


    }

    @Override
    public int getItemCount() {
        return homePageModelList.size();
    }

    public class BannerSliderViewHolder extends RecyclerView.ViewHolder {
        private ViewPager bannersliderViewPager;
        private int currentPage;
        private Timer timer;
        final private long DelayTime = 3000;
        final private long preTime = 3000;
        private List<SliderModel> arrengedlist;

        public BannerSliderViewHolder(@NonNull View itemView) {
            super(itemView);
            bannersliderViewPager = itemView.findViewById(R.id.Banner_slide_View_pager1);
        }

        ////////////////////////slider model
        private void setBannersliderViewPager(final List<SliderModel> SliderModelList) {
            currentPage = 2;
            if (timer != null) {
                timer.cancel();
            }
            arrengedlist = new ArrayList<>();
            for (int x = 0; x < SliderModelList.size(); x++) {
                arrengedlist.add(x, SliderModelList.get(x));
            }
            arrengedlist.add(0, SliderModelList.get(SliderModelList.size() - 2));
            arrengedlist.add(1, SliderModelList.get(SliderModelList.size() - 1));

            arrengedlist.add(SliderModelList.get(0));
            arrengedlist.add(SliderModelList.get(1));


            SliderAdapter sliderAdapter = new SliderAdapter(arrengedlist);
            bannersliderViewPager.setClipToPadding(false);
            bannersliderViewPager.setPageMargin(20);
            bannersliderViewPager.setAdapter(sliderAdapter);
            bannersliderViewPager.setAdapter(sliderAdapter);

            bannersliderViewPager.setCurrentItem(currentPage);

            ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    currentPage = position;

                }

                @Override
                public void onPageScrollStateChanged(int state) {
                    if (state == ViewPager.SCROLL_STATE_IDLE) {
                        pageLooper(arrengedlist);
                    }
                }
            };
            bannersliderViewPager.addOnPageChangeListener(onPageChangeListener);
            startBannerSlide(arrengedlist);
            bannersliderViewPager.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    pageLooper(arrengedlist);
                    stopBannerSlide();

                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        startBannerSlide(arrengedlist);

                    }
                    return false;
                }
            });


        }

        private void pageLooper(List<SliderModel> SliderModelList) {
            if (currentPage == SliderModelList.size() - 2) {
                currentPage = 2;
                bannersliderViewPager.setCurrentItem(currentPage, false);
            }
            if (currentPage == 1) {
                currentPage = SliderModelList.size() - 3;
                bannersliderViewPager.setCurrentItem(currentPage, false);
            }


        }

        private void startBannerSlide(final List<SliderModel> SliderModelList) {
            final Handler handler = new Handler();
            final Runnable update = new Runnable() {
                @Override
                public void run() {
                    if (currentPage >= SliderModelList.size()) {
                        currentPage = 1;
                    }
                    bannersliderViewPager.setCurrentItem(currentPage++, true);
                }
            };
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(update);
                }
            }, DelayTime, preTime);
        }

        private void stopBannerSlide() {
            timer.cancel();
        }
    }

    public class StripAdsBannerViewHolder extends RecyclerView.ViewHolder {

        private ImageView stripImage;
        private ConstraintLayout Stripadcontainer;

        public StripAdsBannerViewHolder(@NonNull View itemView) {
            super(itemView);
            stripImage = itemView.findViewById(R.id.strip_ad_image1);
            Stripadcontainer = itemView.findViewById(R.id.Stip_add_container1);


        }

        private void setStripads(String resource, String color) {
            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.drawable.slingbannerplace)).into(stripImage);
            Stripadcontainer.setBackgroundColor(Color.parseColor(color));
        }
    }

    public class HorizontalProductViewHolder extends RecyclerView.ViewHolder {
        private TextView horizontallayouttitle;
        private Button horizontalViewAll;
        private RecyclerView horizontalProductRecycle;
        private ConstraintLayout horizontalcontainer;

        public HorizontalProductViewHolder(@NonNull View itemView) {
            super(itemView);
            horizontallayouttitle = itemView.findViewById(R.id.horizontal_Deal_of_the);
            horizontalViewAll = itemView.findViewById(R.id.view_all_Horizontal_btn);
            horizontalProductRecycle = itemView.findViewById(R.id.horizontal_Scroll_Recicler);
            horizontalcontainer = itemView.findViewById(R.id.horizontal_Product_Container);
            horizontalProductRecycle.setRecycledViewPool(recycledViewPool);

        }

        private void setHorizontalProductLayout(final List<HorizontalProductScrollModel> horizontalProductScrollModelList, final String title, String backcolor, final List<WishListModel> ViewAllHorizontalList) {

            horizontalcontainer.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(backcolor)));
            horizontallayouttitle.setText(title);

            for (final HorizontalProductScrollModel model : horizontalProductScrollModelList) {
                if (!model.getProductID().isEmpty()) {
                    FirebaseFirestore.getInstance().collection("PRODUCTS").document(model.getProductID()).get()
                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {

                                        model.setProductImage(task.getResult().getString("product_image_1"));
                                        model.setProducttitle(task.getResult().getString("product_title"));
                                        model.setDescription(task.getResult().getString("subtitle"));
                                        model.setProductPrice(task.getResult().getString("product_price"));

                                        WishListModel wishListModel = ViewAllHorizontalList.get(horizontalProductScrollModelList.indexOf(model));
                                        wishListModel.setTotalRating((long) task.getResult().get("total_rating"));
                                        wishListModel.setRating(task.getResult().getString("avarage_rating"));
                                        wishListModel.setProductTitleWish(task.getResult().getString("product_title"));
                                        wishListModel.setProductPriceWish(task.getResult().getString("product_price"));
                                        wishListModel.setProductImageWish(task.getResult().getString("product_image_1"));
                                        wishListModel.setCuttedPriceWish(task.getResult().getString("cutted_price"));
                                        wishListModel.setCOD((boolean) task.getResult().get("COD"));
                                        wishListModel.setInStock((long) task.getResult().get("stock_quantity") > 0);
                                        wishListModel.setFreeCoupensNo((long) task.getResult().get("free_coupens"));


                                        if (horizontalProductScrollModelList.indexOf(model) == horizontalProductScrollModelList.size() - 1) {
                                            if (horizontalProductRecycle.getAdapter() != null) {
                                                horizontalProductRecycle.getAdapter().notifyDataSetChanged();
                                            }
                                        }

                                    }
                                }
                            });
                }
            }


            if (horizontalProductScrollModelList.size() > 8) {
                horizontalViewAll.setVisibility(View.VISIBLE);

                horizontalViewAll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ViewAllActivity.ViewAllProductHorizontalModelList = ViewAllHorizontalList;

                        Intent ViewallIntent = new Intent(itemView.getContext(), ViewAllActivity.class);
                        ViewallIntent.putExtra("Layout_code", 0);
                        ViewallIntent.putExtra("title", title);
                        itemView.getContext().startActivity(ViewallIntent);
                    }
                });
            } else {
                horizontalViewAll.setVisibility(View.INVISIBLE);
            }

            HorizonatalProductScrollAdapter horizonatalProductScrollAdapter = new HorizonatalProductScrollAdapter(horizontalProductScrollModelList);
            LinearLayoutManager layoutManager = new LinearLayoutManager(itemView.getContext());
            layoutManager.setOrientation(RecyclerView.HORIZONTAL);
            horizontalProductRecycle.setLayoutManager(layoutManager);
            horizontalProductRecycle.setAdapter(horizonatalProductScrollAdapter);
            horizonatalProductScrollAdapter.notifyDataSetChanged();
        }
    }

    public class GridProductViewHolder extends RecyclerView.ViewHolder {
        private ConstraintLayout GridContainer;
        private TextView gridlayoutTitle;
        private Button GridViewAllBtn;
        private ConstraintLayout gridProductLayout;


        public GridProductViewHolder(@NonNull View itemView) {
            super(itemView);
            gridlayoutTitle = itemView.findViewById(R.id.Grid_Product_title1);
            GridViewAllBtn = itemView.findViewById(R.id.Grid_View_all_btn1);
            gridProductLayout = itemView.findViewById(R.id.Grid_product_layout);
            GridContainer = itemView.findViewById(R.id.Grid_product_container1);

        }

        private void setGridLayout(final List<HorizontalProductScrollModel> horizontalProductScrollModelList, final String title, String Gridcolor) {
            gridlayoutTitle.setText(title);
            GridContainer.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(Gridcolor)));

            for (final HorizontalProductScrollModel model : horizontalProductScrollModelList) {
                if (!model.getProductID().isEmpty()) {
                    FirebaseFirestore.getInstance().collection("PRODUCTS").document(model.getProductID())
                            .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                model.setProductImage(task.getResult().getString("product_image_1"));
                                model.setProducttitle(task.getResult().getString("product_title"));
                                model.setDescription(task.getResult().getString("subtitle"));
                                model.setProductPrice(task.getResult().getString("product_price"));
                                setGridData(title, horizontalProductScrollModelList);
                            }
                        }
                    });
                }
            }

            setGridData(title, horizontalProductScrollModelList);
        }

        private void setGridData(final String title, final List<HorizontalProductScrollModel> horizontalProductScrollModelList) {
            for (int x = 0; x < 4; x++) {
                ImageView productgridImage = gridProductLayout.getChildAt(x).findViewById(R.id.product_Scroll_image);
                TextView producttitle = gridProductLayout.getChildAt(x).findViewById(R.id.product_scroll_title1);
                TextView productDescription = gridProductLayout.getChildAt(x).findViewById(R.id.product_scroll_offer1);
                TextView productprice = gridProductLayout.getChildAt(x).findViewById(R.id.product_scroll_price1);
                Glide.with(itemView.getContext()).load(horizontalProductScrollModelList.get(x).getProductImage()).apply(new RequestOptions().placeholder(R.drawable.proandcatplaceholder)).into(productgridImage);
                //productgridImage.setImageResource(horizontalProductScrollModelList.get(x).getProductImage());
                producttitle.setText(horizontalProductScrollModelList.get(x).getProducttitle());
                productDescription.setText(horizontalProductScrollModelList.get(x).getDescription());
                productprice.setText("Rs. " + horizontalProductScrollModelList.get(x).getProductPrice() + " /-");
                if (!title.equals("")) {
                    final int finalX = x;
                    gridProductLayout.getChildAt(x).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent productDetailIntentNew = new Intent(itemView.getContext(), ProductDetailsActivity.class);
                            productDetailIntentNew.putExtra("productID", horizontalProductScrollModelList.get(finalX).getProductID());
                            itemView.getContext().startActivity(productDetailIntentNew);
                        }
                    });
                }

                if (!title.equals(" ")) {
                    GridViewAllBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ViewAllActivity.ViewAllProductGridModelList = horizontalProductScrollModelList;
                            Intent gridviewIntent = new Intent(itemView.getContext(), ViewAllActivity.class);
                            gridviewIntent.putExtra("Layout_code", 1);
                            gridviewIntent.putExtra("title", title);
                            itemView.getContext().startActivity(gridviewIntent);
                        }
                    });
                }

            }
        }
    }

}
