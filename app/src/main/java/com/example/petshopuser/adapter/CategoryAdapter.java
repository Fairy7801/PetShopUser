package com.example.petshopuser.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petshopuser.ListProductActivity;
import com.example.petshopuser.R;
import com.example.petshopuser.databinding.ItemCategorieshomeBinding;
import com.example.petshopuser.model.Categories;
import com.example.petshopuser.model.Products;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder1> {

    ArrayList<Categories> categoryList;
    Context context;

    public CategoryAdapter(ArrayList<Categories> categoryList, Context context) {
        this.categoryList = categoryList;
        this.context = context;
    }

    public class MyViewHolder1 extends RecyclerView.ViewHolder {
        ItemCategorieshomeBinding binding;
        public MyViewHolder1(@NonNull ItemCategorieshomeBinding itemBinding) {
            super(itemBinding.getRoot());
            binding = itemBinding;
        }
    }

    @NonNull
    @Override
    public MyViewHolder1 onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        ItemCategorieshomeBinding binding = ItemCategorieshomeBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder1(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder1 holder, final int position) {
        Categories categories = categoryList.get(position);

        displayCategoriesInfo(holder, categories);

        if (position == 0){
            holder.binding.line1CategoriesHomeAdapter.setBackgroundResource( R.drawable.bg_tl);
            holder.binding.cardView4CategoriesHomeAdapter.setBackgroundResource( R.drawable.bg_t);
            holder.binding.categoryTitleCategoriesHomeAdapter.setTextColor(Color.WHITE);
        }
        holder.binding.cardView1CategoriesHomeAdapter.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                holder.binding.line1CategoriesHomeAdapter.setBackgroundResource( R.drawable.bg_tl);
                holder.binding.cardView4CategoriesHomeAdapter.setBackgroundResource( R.drawable.bg_t);
                holder.binding.categoryTitleCategoriesHomeAdapter.setTextColor(Color.WHITE);
                Intent i = new Intent(context, ListProductActivity.class);
                i.putExtra("matl",categories.getId());
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(i);
            }
        });
    }

    private void displayCategoriesInfo(@NonNull MyViewHolder1 holder, @NonNull Categories categories) {
        holder.binding.categoryTitleCategoriesHomeAdapter.setText(categories.getName());

        Picasso.get()
                .load(categories.getImage())
                .into(holder.binding.categoryImageCategoriesHomeAdapter, new Callback() {
                    @Override
                    public void onSuccess() {
                        holder.binding.progressbar1CategoriesHomeAdapter.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.d("Error : ", e.getMessage());
                    }
                });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }
}

