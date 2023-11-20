package com.example.petshopuser;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.petshopuser.View.SearchActivity;
import com.example.petshopuser.View.User.TrangCaNhan;
import com.example.petshopuser.adapter.AdapterViewPayer;
import com.example.petshopuser.adapter.CategoryAdapter;
import com.example.petshopuser.adapter.ProductAdapter;
import com.example.petshopuser.callback.CategoriesCallBack;
import com.example.petshopuser.callback.ProductsCallBack;
import com.example.petshopuser.callback.UserCallBack;
import com.example.petshopuser.dao.DaoCategories;
import com.example.petshopuser.dao.DaoProducts;
import com.example.petshopuser.dao.DaoUser;
import com.example.petshopuser.databinding.ActivityMainBinding;
import com.example.petshopuser.model.Categories;
import com.example.petshopuser.model.ModelItem;
import com.example.petshopuser.model.Products;
import com.example.petshopuser.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private AdapterViewPayer adapterViewPayer;
    private ActivityMainBinding binding;
    private ArrayList<Categories> categoriesArrayList;
    private DaoCategories daoCategories;
    private CategoryAdapter categoryAdapter;
    private ProductAdapter productAdapter;
    private ArrayList<Products> productsArrayList;
    private DaoProducts daoProducts;
    private DaoUser daoUser;
    private FirebaseUser firebaseUser;
    private String anh;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        sharedPreferences = getSharedPreferences("toado", Context.MODE_PRIVATE);
        binding.txtDiachiActivityMain.setText(sharedPreferences.getString("diaChi"," "));

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            // Lưu userUid vào SharedPreferences
            saveUserUidToSharedPreferences(firebaseUser);
        }
        loadImageFromDatabase();
        initializeSlider();
        initializeRecyclerViewCategories();
        initializeRecyclerViewProducts();

        binding.txtDiachiActivityMain.setOnClickListener(v -> {
//            startActivity(new Intent(getApplicationContext(), MapActivity.class));
        });

        binding.anhDaiDienActivityMain.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, TrangCaNhan.class));
        });

        binding.edtSearchActivityMain.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, SearchActivity.class));
        });

        setContentView(view);
    }

    // Hàm để lưu userInfo vào SharedPreferences
    private void saveUserUidToSharedPreferences(FirebaseUser firebaseUser) {
        SharedPreferences sharedPreferences = getSharedPreferences("user_info", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user_uid", firebaseUser.getUid());
        editor.putString("user_email", firebaseUser.getEmail());
        editor.apply();
    }

    // Lấy ảnh từ firebase realtime và hiển thị
    private void loadImageFromDatabase() {
        daoUser = new DaoUser(MainActivity.this);
        daoUser.getAll(new UserCallBack() {
            @Override
            public void onSuccess(ArrayList<User> lists) {
                anh = lists.stream()
                        .filter(user -> user.getToken() != null && user.getToken().equalsIgnoreCase(firebaseUser.getUid()))
                        .findFirst()
                        .map(User::getImage)
                        .orElse(null);

                if (anh != null) {
                    // Load ảnh từ URL sử dụng Picasso
                    Picasso.get().load(anh).into(binding.anhDaiDienActivityMain);
                } else {
                    // Nếu không có đường link ảnh trong database
                    Picasso.get().load("https://vnn-imgs-a1.vgcloud.vn/image1.ictnews.vn/_Files/2020/03/17/trend-avatar-1.jpg").into(binding.anhDaiDienActivityMain);
                }
            }

            @Override
            public void onError(String message) {
                // Xử lý lỗi nếu cần
            }
        });
    }

    // Khởi tạo slider
    private void initializeSlider() {
        adapterViewPayer = new AdapterViewPayer(MainActivity.this);
        binding.imageSliderActivityMain.setSliderAdapter(adapterViewPayer);
        binding.imageSliderActivityMain.setIndicatorAnimation(IndicatorAnimationType.WORM);
        binding.imageSliderActivityMain.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        binding.imageSliderActivityMain.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        binding.imageSliderActivityMain.setIndicatorSelectedColor(Color.WHITE);
        binding.imageSliderActivityMain.setIndicatorUnselectedColor(Color.GRAY);
        binding.imageSliderActivityMain.setScrollTimeInSec(3);
        binding.imageSliderActivityMain.setAutoCycle(true);
        binding.imageSliderActivityMain.startAutoCycle();
        renewItems();
    }

    // Hiển thị categories
    private void initializeRecyclerViewCategories() {
        daoCategories = new DaoCategories(MainActivity.this);
        categoriesArrayList = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(categoriesArrayList, MainActivity.this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
        binding.rcvHomeActivityMain.setLayoutManager(linearLayoutManager);
        binding.rcvHomeActivityMain.setHasFixedSize(true);
        binding.rcvHomeActivityMain.setAdapter(categoryAdapter);

        daoCategories.getAll(new CategoriesCallBack() {
            @Override
            public void onSuccess(ArrayList<Categories> lists) {
                categoriesArrayList.clear();
                categoriesArrayList.addAll(lists);
                categoryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String message) {
                // Xử lý lỗi nếu cần
            }
        });
    }

    // Hiển thị products
    private void initializeRecyclerViewProducts() {
        productsArrayList = new ArrayList<>();
        productAdapter = new ProductAdapter(productsArrayList, MainActivity.this);
        daoProducts = new DaoProducts(MainActivity.this);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this, 2);
        binding.rcvMonanActivityMain.setLayoutManager(gridLayoutManager);
        binding.rcvMonanActivityMain.setHasFixedSize(true);
        binding.rcvMonanActivityMain.setAdapter(productAdapter);

        daoProducts.getAll(new ProductsCallBack() {
            @Override
            public void onSuccess(ArrayList<Products> lists) {
                productsArrayList.clear();
                productsArrayList.addAll(lists);
                productAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String message) {
                // Xử lý lỗi nếu cần
            }
        });
    }

    public void renewItems() {
        List<ModelItem> sliderItemList = new ArrayList<>();

        String[] imageUrls = {
                "https://1.bp.blogspot.com/-1Q2XPYvxMag/YKOI0H3dA9I/AAAAAAAAAB4/ox70QTj9a4831YbPkZgWmk_roKqspC4ogCNcBGAsYHQ/s1747/family.png",
                "https://1.bp.blogspot.com/-cZh16lRm7d4/YKOI0E2gbjI/AAAAAAAAAB0/iSFMyBJrUzsnu9dWpbK1FTWgr9CWcqqtACNcBGAsYHQ/s970/5.png",
                "https://1.bp.blogspot.com/-wN9fUhMoGTo/YKOIzEN-hZI/AAAAAAAAABs/hHOiWywVYuEG5CenGgut4AMUyvuq1uk8ACNcBGAsYHQ/s970/2.png",
                "https://1.bp.blogspot.com/-p-f9fqrBZ0w/YKOIzrCE-rI/AAAAAAAAABw/0CqSJS6QvQw2TWKUhtXpB6Q-2e0-HGwdgCNcBGAsYHQ/s970/4.png",
                "https://1.bp.blogspot.com/-W445RQ2YtbU/YKOI0sIDkzI/AAAAAAAAACA/7r2uGRJFKzkbNjG7i6mZbKaJTLzpNhKwQCNcBGAsYHQ/s970/giamgia.png"
        };
        for (String imageUrl : imageUrls) {
            ModelItem sliderItem = new ModelItem();
            sliderItem.setImageurl(imageUrl);
            sliderItemList.add(sliderItem);
        }
        adapterViewPayer.ViewPagerAdapter(sliderItemList);
    }
    @Override
    public void onResume() {
        super.onResume();
    }
}