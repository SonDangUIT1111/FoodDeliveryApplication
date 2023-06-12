package com.example.fooddeliveryapplication.Adapters.ProductInfomation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fooddeliveryapplication.databinding.ItemProductInfoBinding;

import java.util.ArrayList;

public class ProductInfoImageAdapter extends RecyclerView.Adapter {

    Context mContext;
    ArrayList<String> dsImage;

    public ProductInfoImageAdapter(Context mContext, ArrayList<String> dsImage) {
        this.mContext = mContext;
        this.dsImage = dsImage;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemProductInfoBinding.inflate(LayoutInflater.from(mContext),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder=(ViewHolder) holder;
        Glide.with(mContext)
                .load(dsImage.get(position))
                .into(viewHolder.binding.imgFood);
    }

    @Override
    public int getItemCount() {
        if (!dsImage.isEmpty()) {
            return dsImage.size();
        }
        return 0;
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        ItemProductInfoBinding binding;
        public ViewHolder(@NonNull ItemProductInfoBinding tmp) {
            super(tmp.getRoot());
            binding=tmp;
        }
    }
}
