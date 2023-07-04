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
    private Context mContext;
    private ArrayList<String> dsImage;

    public ProductInfoImageAdapter(Context mContext, ArrayList<String> dsImage) {
        this.mContext = mContext;
        ArrayList<String> copyCat = new ArrayList<String>();

        if (dsImage.get(0) != null && !dsImage.get(0).equals(""))
            copyCat.add(dsImage.get(0));
        if (dsImage.get(1) != null && !dsImage.get(1).equals(""))
            copyCat.add(dsImage.get(1));
        if (dsImage.get(2) != null && !dsImage.get(2).equals(""))
            copyCat.add(dsImage.get(2));
        if (dsImage.get(3) != null && !dsImage.get(3).equals(""))
            copyCat.add(dsImage.get(3));

        this.dsImage = copyCat;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemProductInfoBinding.inflate(LayoutInflater.from(mContext), parent, false));
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
        return dsImage == null ? 0 : dsImage.size();
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemProductInfoBinding binding;

        public ViewHolder(@NonNull ItemProductInfoBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
