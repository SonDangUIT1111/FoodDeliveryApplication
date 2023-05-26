package com.example.fooddeliveryapplication.Adapters.HomeAdapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fooddeliveryapplication.Model.Product;
import com.example.fooddeliveryapplication.databinding.ItemHomeBinding;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class FoodDrinkFrgAdapter extends RecyclerView.Adapter {
    NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
    ArrayList<Product> ds;

    public FoodDrinkFrgAdapter(ArrayList<Product> ds) {
        this.ds = ds;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemHomeBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder newHolder=(ViewHolder)holder;
        Product item = ds.get(position);
        Glide.with(newHolder.binding.getRoot())
                .load(item.getProductImage1())
                .into(newHolder.binding.imgFood);
        newHolder.binding.txtFoodName.setText(item.getProductName());
        newHolder.binding.txtFoodPrice.setText(nf.format(item.getProductPrice()));

    }
    @Override
    public int getItemCount() {
        return ds.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemHomeBinding binding;
        public ViewHolder(@NonNull ItemHomeBinding tmp) {
            super(tmp.getRoot());
            this.binding=tmp;
        }
    }
}
