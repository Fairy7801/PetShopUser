package com.example.petshopuser.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petshopuser.Helper.NumberFormatHelper;
import com.example.petshopuser.R;
import com.example.petshopuser.databinding.RowCartBinding;
import com.example.petshopuser.local.LocalStorage;
import com.example.petshopuser.model.Order;
import com.google.gson.Gson;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {

    ArrayList<Order> cartList;
    Context context;
    String _subtotal;
    LocalStorage localStorage;
    Gson gson;
    private boolean showDeleteButton;

    public CartAdapter(ArrayList<Order> cartList, Context context, boolean showDeleteButton) {
        this.cartList = cartList;
        this.context = context;
        this.showDeleteButton = showDeleteButton;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        RowCartBinding binding = RowCartBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final Order cart = cartList.get(position);
        localStorage = new LocalStorage(context);
        gson = new Gson();
        holder.binding.productTitleRowCard.setText(cart.getProducts().getNameP());
        holder.binding.productAttributeRowCard.setText(cart.getProducts().getId());
        holder.binding.quantityRowCard.setText(NumberFormatHelper.formatNumber(cart.getSoLuong()));
        holder.binding.productPriceRowCard.setText(NumberFormatHelper.formatNumber(cart.getProducts().getPrice()));

        _subtotal = NumberFormatHelper.formatNumber(cart.getSoLuong() * cart.getProducts().getPrice());

        holder.binding.subTotalRowCard.setText(_subtotal);
        Picasso.get()
                .load(cart.getProducts().getImage())
                .into(holder.binding.productImageRowCard, new Callback() {
                    @Override
                    public void onSuccess() {
                        holder.binding.progressbarRowCard.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.d("Error : ", e.getMessage());
                    }
                });

        if (showDeleteButton) {
            holder.binding.cartDeleteRowCard.setVisibility(View.VISIBLE);
        } else {
            holder.binding.cartDeleteRowCard.setVisibility(View.GONE);
        }

        holder.binding.cartDeleteRowCard.setOnClickListener(v -> {
            cartList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, cartList.size());
            notifyDataSetChanged();
            Gson gson = new Gson();
            String cartStr = gson.toJson(cartList);
            localStorage.setCart(cartStr);
        });
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final RowCartBinding binding;

        public MyViewHolder(@NonNull RowCartBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
