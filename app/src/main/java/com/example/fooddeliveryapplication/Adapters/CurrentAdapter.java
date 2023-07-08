package com.example.fooddeliveryapplication.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fooddeliveryapplication.Model.InfoCurrentProduct;
import com.example.fooddeliveryapplication.databinding.ItemInfoCurrentProductBinding;

import java.util.List;

public class CurrentAdapter extends RecyclerView.Adapter<CurrentAdapter.CurrentViewHolder> {
    private Context mContext;
    private List<InfoCurrentProduct> mListInfoCurrentProducts;

    public CurrentAdapter(Context mContext, List<InfoCurrentProduct> mListInfoCurrentProducts) {
        this.mContext = mContext;
        this.mListInfoCurrentProducts = mListInfoCurrentProducts;
    }

    public void setData(List<InfoCurrentProduct> list){
        this.mListInfoCurrentProducts = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CurrentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CurrentViewHolder(ItemInfoCurrentProductBinding.inflate(LayoutInflater.from(mContext), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CurrentViewHolder holder, int position) {
        InfoCurrentProduct infoCurrentProduct = mListInfoCurrentProducts.get(position);

        if (infoCurrentProduct == null)
            return;

        holder.binding.imgCurrentProduct.setImageResource(infoCurrentProduct.getResourceId());
        holder.binding.txtNameCurrentProduct.setText(infoCurrentProduct.getNameCurrentProduct());
        holder.binding.txtState.setText(infoCurrentProduct.getState());
        holder.binding.txtPriceCurrentProduct.setText(infoCurrentProduct.getPriceCurrentProduct());
    }

    @Override
    public int getItemCount() {
        return mListInfoCurrentProducts == null ? 0 : mListInfoCurrentProducts.size();
    }

    public static class CurrentViewHolder extends RecyclerView.ViewHolder{
        private final ItemInfoCurrentProductBinding binding;

        public CurrentViewHolder(@NonNull ItemInfoCurrentProductBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
