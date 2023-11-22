package com.example.petshopuser.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petshopuser.Helper.NumberFormatHelper;
import com.example.petshopuser.ProductProfileActivity;
import com.example.petshopuser.databinding.RowCategoryBinding;
import com.example.petshopuser.model.Products;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.MyViewHolder> {

    ArrayList<Products> categoryList;
    Context context;

    public FavoriteAdapter(ArrayList<Products> categoryList, Context context) {
        this.categoryList = categoryList;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        RowCategoryBinding binding;

        public MyViewHolder(@NonNull RowCategoryBinding itemBinding) {
            super(itemBinding.getRoot());
            binding = itemBinding;
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        RowCategoryBinding binding = RowCategoryBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        Products categories = categoryList.get(position);
        holder.binding.categoryTitleRowCategory.setText(categories.getNameP());
        Picasso.get()
                .load(categories.getImage())
                .into(holder.binding.categoryImageRowCategory, new Callback() {
                    @Override
                    public void onSuccess() {
                        holder.binding.progressbarRowCategory.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.d("Error : ", e.getMessage());
                    }
                });



        holder.binding.cardViewRowCategory.setOnClickListener(view -> {
            Intent intent = new Intent(context, ProductProfileActivity.class);
            intent.putExtra("idfood",categories.getIdP());
            intent.putExtra("matl",categories.getId());
            intent.putExtra("tokenstore",categories.getTokenStore());
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return categoryList.size();

    }
}
