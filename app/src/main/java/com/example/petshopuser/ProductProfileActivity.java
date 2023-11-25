package com.example.petshopuser;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.petshopuser.Helper.NumberFormatHelper;
import com.example.petshopuser.View.CartActivity;
import com.example.petshopuser.adapter.FoodProfileAdapter;
import com.example.petshopuser.adapter.ProductAdapter;
import com.example.petshopuser.callback.ProductsCallBack;
import com.example.petshopuser.callback.StoreCallBack;
import com.example.petshopuser.callback.UserCallBack;
import com.example.petshopuser.common.BeginActivity;
import com.example.petshopuser.common.IntroActivity;
import com.example.petshopuser.dao.DaoProducts;
import com.example.petshopuser.dao.DaoStore;
import com.example.petshopuser.dao.DaoUser;
import com.example.petshopuser.databinding.ActivityProductProfileBinding;
import com.example.petshopuser.local.LocalStorage;
import com.example.petshopuser.model.Order;
import com.example.petshopuser.model.Products;
import com.example.petshopuser.model.Store;
import com.example.petshopuser.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ProductProfileActivity extends AppCompatActivity {
    private FirebaseUser userbase;
    private ActivityProductProfileBinding binding;
    int sluongmua = 0;
    String idProduct = "";
    DaoProducts databaseFood;
    ArrayList<Products> foodArrayList;
    ArrayList<Products> dsfoodall = new ArrayList<>();
    ArrayList<Order> orderArrayList = new ArrayList<>();
    ArrayList<Store> storeArrayList = new ArrayList<>();
    FoodProfileAdapter foodAdapter;
    DatabaseReference databaseReference;
    DaoStore databaseStore;
    ArrayList<User> dsUser = new ArrayList<>();
    DaoUser databaseUser;

    String idstore ="";
    String tokenstore="";
    String idCategory = "";
    Gson gson;
    LocalStorage localStorage;
    double showtien = 0;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userbase = FirebaseAuth.getInstance().getCurrentUser();
        foodArrayList = new ArrayList<>();

        Intent intent = getIntent();

        idProduct = intent.getStringExtra("idfood");
        tokenstore = intent.getStringExtra("tokenstore");
        idCategory = intent.getStringExtra("matl");

        initView();
        initRecyclerView();

        databaseReference = FirebaseDatabase.getInstance().getReference("Order");

        idstore = intent.getStringExtra("idstore");

        String keyhdct = databaseReference.push().getKey();

        localStorage = new LocalStorage(this);
        gson = new Gson();
        orderArrayList.clear();
        binding.plushProductProfileActivity.setOnClickListener(v -> {
            int sluongnhap = Integer.parseInt(binding.tvslProductProfileActivity.getText().toString());
            if (sluongnhap >= 0) {
                int sluongmua_item = Integer.parseInt(binding.tvslProductProfileActivity.getText().toString());
                sluongmua_item++;
                binding.tvslProductProfileActivity.setText(sluongmua_item + "");
                try {
                    Products food = null;
                    Store store = null;
                    User user = null;
                    sluongmua = Integer.parseInt(binding.tvslProductProfileActivity.getText().toString());
                    for (int i = 0; i < dsfoodall.size(); i++) {
                        if (dsfoodall.get(i).getIdP().equals(idProduct)) {
                            food = dsfoodall.get(i);
                            break;
                        }
                    }

                    databaseFood = new DaoProducts(ProductProfileActivity.this);
                    databaseStore = new DaoStore(ProductProfileActivity.this);
                    databaseUser = new DaoUser(ProductProfileActivity.this);
                    for (int i = 0; i < storeArrayList.size(); i++) {
                        if (storeArrayList.get(i).getEmail().equals(idstore)) {
                            store = storeArrayList.get(i);
                            break;
                        }
                    }

                    for (int i = 0; i < dsUser.size(); i++) {
                        if (dsUser.get(i).getEmail().equals(userbase.getEmail())) {
                            user = dsUser.get(i);
                            break;
                        }
                    }

                    String idfoodcheck = idProduct;
                    int check = checkmahdct(orderArrayList, idfoodcheck);
                    Order order = new Order(keyhdct, sluongmua_item, food, store, user);
                    Log.i("Check", String.valueOf(check));
                    if (check >= 0) {
                        int sluongmua = orderArrayList.get(check).getSoLuong();
                        order.setSoLuong(sluongmua+1);
                        orderArrayList.set(check, order);
                        String cartStr = gson.toJson(orderArrayList);
                        localStorage.setCart(cartStr);
                    } else {
                        int postion = -1;
                        for (int i = 0; i < getCartList().size(); i++) {
                            if (getCartList().get(i).getProducts().getIdP().equals(idfoodcheck)) {
                                postion = i;
                                break;
                            }
                        }
                        if (postion < 0) {
                            orderArrayList.add(order);
                            String cartStr = gson.toJson(orderArrayList);
                            localStorage.setCart(cartStr);
                        }
                    }
                } catch (Exception e) {
                    Log.i("fiX",e.getMessage());
                }

                try {
                    for (Order hdct : orderArrayList) {
                        showtien =  hdct.getSoLuong() * hdct.getProducts().getPrice();
                    }
                    binding.btnInsertcartProductProfileActivity.setText("THÊM VÀO GIỎ HÀNG -\t" + NumberFormatHelper.formatNumber(showtien) + "\tVNĐ");
                } catch (Exception e) {
                    Log.i("Error",e.toString());
                }
            }
        });
        binding.minusProductProfileActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int sluongnhap = Integer.parseInt(binding.tvslProductProfileActivity.getText().toString());
                if (sluongnhap != 0) {
                    int sluongmua_item = Integer.parseInt(binding.tvslProductProfileActivity.getText().toString());
                    sluongmua_item--;
                    binding.tvslProductProfileActivity.setText(sluongmua_item + "");
                    String idfoodcheck = idProduct;
                    int check1 = checkmahdct(orderArrayList, idfoodcheck);
                    Products food = null;
                    Store store = null;
                    User user = null;
                    for (int i = 0; i < dsfoodall.size(); i++) {
                        if (dsfoodall.get(i).getIdP().equals(idProduct)) {
                            food = dsfoodall.get(i);
                            break;
                        }
                    }
                    for (int i = 0; i < storeArrayList.size(); i++) {
                        if (storeArrayList.get(i).getEmail() != null && storeArrayList.get(i).getEmail().equals(idstore)) {
                            store = storeArrayList.get(i);
                            break;
                        }
                    }
                    for (int i = 0; i < dsUser.size(); i++) {
                        if (dsUser.get(i).getEmail() != null && dsUser.get(i).getEmail().equals(userbase.getEmail())) {
                            user = dsUser.get(i);
                            break;
                        }
                    }
                    Order orderminus = new Order(keyhdct, sluongmua_item, food, store, user);
                    if (check1 >= 0) {
                        int sluongmua = orderArrayList.get(check1).getSoLuong();
                        orderminus.setSoLuong(sluongmua-1);
                        orderArrayList.set(check1, orderminus);
                        String cartStr = gson.toJson(orderArrayList);
                        localStorage.setCart(cartStr);
                    } else {
                    }

                    try {
                        for (Order hdct1 : orderArrayList) {
                            showtien = hdct1.getSoLuong() * hdct1.getProducts().getPrice();
                        }
                        binding.btnInsertcartProductProfileActivity.setText("THÊM VÀO GIỎ HÀNG -\t" + NumberFormatHelper.formatNumber(showtien) + "\tVNĐ");
                    } catch (Exception e) {
                    }
                }
            }
        });

        binding.btnInsertcartProductProfileActivity.setOnClickListener(v -> startActivity(new Intent(ProductProfileActivity.this, CartActivity.class)));
        binding.toolbarProductProfileActivity.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(ProductProfileActivity.this, MainActivity.class));
                overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out);
                finish();
            }
        });

        databaseFood.getAll(new ProductsCallBack() {
            @Override
            public void onSuccess(ArrayList<Products> lists) {
                dsfoodall.clear();
                dsfoodall.addAll(lists);
            }

            @Override
            public void onError(String message) {

            }
        });
        databaseStore = new DaoStore(ProductProfileActivity.this);
        databaseUser = new DaoUser(ProductProfileActivity.this);

        databaseStore.getAll(new StoreCallBack() {
            @Override
            public void onSuccess(ArrayList<Store> lists) {
                storeArrayList.clear();
                storeArrayList.addAll(lists);
            }

            @Override
            public void onError(String message) {

            }
        });

        databaseUser.getAll(new UserCallBack() {
            @Override
            public void onSuccess(ArrayList<User> lists) {
                dsUser.clear();
                dsUser.addAll(lists);
            }

            @Override
            public void onError(String message) {

            }
        });
    }

    private void initRecyclerView() {
        databaseFood.getAll(new ProductsCallBack() {
            @Override
            public void onSuccess(ArrayList<Products> lists) {
                foodArrayList.clear();
                for (int i = 0; i < lists.size(); i++) {
                    if (lists.get(i).getId() != null && lists.get(i).getId().equalsIgnoreCase(idCategory)) {
                        foodArrayList.add(lists.get(i));
                    }
                }
            }

            @Override
            public void onError(String message) {

            }
        });
        new Handler().postDelayed(() -> {
            foodAdapter = new FoodProfileAdapter(foodArrayList, ProductProfileActivity.this);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ProductProfileActivity.this, LinearLayoutManager.HORIZONTAL, false);
            binding.rvReviewsProductProfileActivity.setLayoutManager(linearLayoutManager);
            binding.rvReviewsProductProfileActivity.setHasFixedSize(true);
            binding.rvReviewsProductProfileActivity.setAdapter(foodAdapter);
        }, 700);
    }

    private void initView() {
        setSupportActionBar(binding.toolbarProductProfileActivity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getWindow().setStatusBarColor(ContextCompat.getColor(ProductProfileActivity.this, R.color.colorPrimary));

        databaseFood = new DaoProducts(ProductProfileActivity.this);
        databaseFood.getAll(new ProductsCallBack() {
            @Override
            public void onSuccess(ArrayList<Products> lists) {
                for (Products products : lists) {
                    if (!products.getIdP().isEmpty() && products.getIdP().equals(idProduct)) {
                        Picasso.get().load(products.getImage()).into(binding.ivDetailPosterProductProfileActivity);
                        Picasso.get().load(products.getImage()).into(binding.ivBackdropProductProfileActivity);
                        binding.collapsingProductProfileActivity.setTitle(products.getNameP());
                        binding.tvDetailRatingProductProfileActivity.setText(NumberFormatHelper.formatNumber(products.getPrice())+" VND");
                        binding.tvDetailReleaseDateProductProfileActivity.setText("đăng bởi@ " + products.getIdStore());
                        binding.txtdiachiProductProfileActivity.setText("Địa Chỉ:\t" + products.getAddress());
                        binding.txtsoluongProductProfileActivity.setText("Số Lượng:\t" + products.getQuantity());
                        binding.txtstatusProductProfileActivity.setText("Loại:\t" + products.getId());
                        binding.txtmotaProductProfileActivity.setText("Mô Tả:\t" + products.getDescription());
                    }
                }
            }

            @Override
            public void onError(String message) {

            }
        });
    }

    public ArrayList<Order> getCartList() {
        if (localStorage.getCart() != null) {
            String jsonCart = localStorage.getCart();
            Log.d("CART : ", jsonCart);
            Type type = new TypeToken<List<Order>>() {
            }.getType();
            orderArrayList = gson.fromJson(jsonCart, type);
            return orderArrayList;
        } else {

        }
        return orderArrayList;
    }

    public int checkmahdct(ArrayList<Order> dsOrder, String mafood) {

        int poss = -1;
        Log.i("Size", String.valueOf(dsOrder.size()));
        for (int i = 0; i < dsOrder.size(); i++) {
            if (dsOrder.get(i).getProducts().getIdP().equals(mafood)) {
                poss = i;
                break;
            }
        }
        return poss;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out);
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_detail, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
//        switch (id) {
//            case R.id.action_detail_favorite:
//                ProductAdapter productAdapter = new ProductAdapter(foodArrayList, this);
//                productAdapter.getUserUidFromSharedPreferences();
//
//        }
        if (id == R.id.action_detail_chat) {
//            Intent intent = new Intent(FoodProfileActivity.this, MessegerActivity.class);
//            intent.putExtra("userId",tokenstore);
//            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}