package com.example.petshopuser.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petshopuser.Helper.NumberFormatHelper;
import com.example.petshopuser.ProductProfileActivity;
import com.example.petshopuser.R;
import com.example.petshopuser.dao.DaoRecommendProducts;
import com.example.petshopuser.model.Products;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class FoodProfileAdapter extends RecyclerView.Adapter<FoodProfileAdapter.MyViewHolder> {

    ArrayList<Products> categoryList;
    Context context;
    FirebaseUser firebaseUser;

    public FoodProfileAdapter(ArrayList<Products> categoryList, Context context) {
        this.categoryList = categoryList;
        this.context = context;
        this.firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.itemfoodprofile, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        Products categories = categoryList.get(position);
        holder.title.setText(categories.getNameP());
        holder.txtdiachi.setText(categories.getAddress());
        holder.txtgia.setText(String.valueOf(NumberFormatHelper.formatNumber(categories.getPrice())+" VNĐ"));
        Picasso.get()
                .load(categories.getImage())
                .into(holder.imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        holder.progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.d("Error : ", e.getMessage());
                    }
                });


        holder.cardView1.setOnClickListener(v -> {
            recommendCategories(categories.getId());
            Intent intent = new Intent(context, ProductProfileActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("idfood",categories.getIdP());
            intent.putExtra("matl",categories.getId());
            intent.putExtra("tokenstore",categories.getTokenStore());
            context.startActivity(intent);

        });

    }

    public void recommendCategories(String idCategory) {
        DaoRecommendProducts daoRecommendProducts = new DaoRecommendProducts(context);
        daoRecommendProducts.saveSearchHistory(firebaseUser.getUid(),idCategory);
    }

    @Override
    public int getItemCount() {
        return categoryList.size();

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView title,txtdiachi,txtgia;
        ProgressBar progressBar;
        CardView cardView1;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imgfood);
            title = itemView.findViewById(R.id.txtnamefood);
            progressBar = itemView.findViewById(R.id.progressbar);
            txtdiachi = itemView.findViewById(R.id.txtdiachi);
            txtgia = itemView.findViewById(R.id.txtgia);
            cardView1 = itemView.findViewById(R.id.cardview1);

        }
    }
}

