package com.example.petshopuser.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petshopuser.ProductProfileActivity;
import com.example.petshopuser.Helper.NumberFormatHelper;
import com.example.petshopuser.R;
import com.example.petshopuser.dao.DaoRecommendProducts;
import com.example.petshopuser.databinding.ItemfoodBinding;
import com.example.petshopuser.model.Favorite;
import com.example.petshopuser.model.Products;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder1> {

    private ArrayList<Products> categoryList;
    private Context context;
    private DatabaseReference fvrtref, fvrt_listRef;
    private String userUid;
    private Boolean mProcessLike = false;

    public ProductAdapter(ArrayList<Products> categoryList, Context context) {
        this.categoryList = categoryList;
        this.context = context;
        this.userUid = getUserUidFromSharedPreferences();
        this.fvrtref = FirebaseDatabase.getInstance().getReference("favourites");
        this.fvrt_listRef = FirebaseDatabase.getInstance().getReference("favoriteList").child(userUid);
    }

    // Hàm để lấy userUid từ SharedPreferences
    public String getUserUidFromSharedPreferences() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("user_info", Context.MODE_PRIVATE);
        return sharedPreferences.getString("user_uid", "");
    }

    public class MyViewHolder1 extends RecyclerView.ViewHolder {
        ItemfoodBinding binding;
        public MyViewHolder1(@NonNull ItemfoodBinding itemBinding) {
            super(itemBinding.getRoot());
            binding = itemBinding;
        }
    }

    @NonNull
    @Override
    public MyViewHolder1 onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        ItemfoodBinding binding = ItemfoodBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder1(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder1 holder, final int position) {
        Products categories = categoryList.get(position);

        // Hiển thị thông tin sản phẩm
        displayProductInfo(holder, categories);

        // Kiểm tra và xử lý sự kiện khi nhấn nút like
        handleLikeButtonClick(holder, categories);

        // Xử lý sự kiện khi nhấn vào cardview sản phẩm
        holder.binding.cardviewNhanItemProducts.setOnClickListener(v -> {
            // Chuyển đến trang chi tiết sản phẩm
            navigateToProductDetail(categories);
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    // Hiển thị thông tin sản phẩm
    private void displayProductInfo(MyViewHolder1 holder, Products categories) {
        holder.binding.txtNameFoodItemProducts.setText(categories.getNameP());
        holder.binding.txtDiachiItemProducts.setText(categories.getAddress());
        holder.binding.txtGiaItemProducts.setText(NumberFormatHelper.formatNumber(categories.getPrice()) + " VNĐ");

        Picasso.get()
                .load(categories.getImage())
                .into(holder.binding.imgFoodItemProducts, new Callback() {
                    @Override
                    public void onSuccess() {
                        holder.binding.progressbarItemProducts.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.d("Error : ", e.getMessage());
                    }
                });

        // Kiểm tra và hiển thị trạng thái yêu thích
        favouriteChecker(categories.getIdP(), holder);
    }

    // Xử lý sự kiện khi nhấn nút like
    private void handleLikeButtonClick(MyViewHolder1 holder, Products categories) {
        holder.binding.likeBtnItemProducts.setOnClickListener(v -> {
            mProcessLike = true;
            fvrtref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (mProcessLike.equals(true)) {
                        // Xử lý thêm hoặc xoá sản phẩm khỏi danh sách yêu thích
                        processLikeButtonAction(snapshot, categories);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        });
    }

    // Xử lý sự kiện khi nhấn vào cardview sản phẩm
    private void navigateToProductDetail(Products categories) {
        recommendCategories(categories.getId());
        Intent i = new Intent(context, ProductProfileActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Truyền thông tin sản phẩm qua intent
        i.putExtra("idfood", categories.getIdP());
        i.putExtra("matl", categories.getId());
        i.putExtra("tokenstore", categories.getTokenStore());

        context.startActivity(i);
    }

    public void recommendCategories(String idCategory) {
        DaoRecommendProducts daoRecommendProducts = new DaoRecommendProducts(context);
        daoRecommendProducts.saveSearchHistory(userUid,idCategory);
    }

    // Kiểm tra và hiển thị trạng thái yêu thích
    private void favouriteChecker(final String postkey, MyViewHolder1 holder) {
        fvrtref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Hiển thị icon yêu thích tùy thuộc vào trạng thái
                holder.binding.likeBtnItemProducts.setImageResource(
                        snapshot.child(postkey).hasChild(userUid)
                                ? R.drawable.ic_heart_liked
                                : R.drawable.ic_heart
                );
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    // Xử lý thêm hoặc xoá sản phẩm khỏi danh sách yêu thích
    private void processLikeButtonAction(DataSnapshot snapshot, Products categories) {
        if (snapshot.child(categories.getIdP()).hasChild(userUid)) {
            fvrtref.child(categories.getIdP()).child(userUid).removeValue();
            fvrt_listRef.child(categories.getIdP()).removeValue();
            Toast.makeText(context, "Xoá Khỏi Danh Sách Yêu Thích", Toast.LENGTH_SHORT).show();
            mProcessLike = false;
        } else {
            fvrtref.child(categories.getIdP()).child(userUid).setValue(true);
            Favorite favorite = new Favorite(fvrt_listRef.push().getKey(), userUid, categories, true);

            fvrt_listRef.child(categories.getIdP()).setValue(favorite);
            mProcessLike = false;

            Toast.makeText(context, "Thêm Vào Danh Sách Yêu Thích", Toast.LENGTH_SHORT).show();
        }
    }
}