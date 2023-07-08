package com.example.fooddeliveryapplication.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fooddeliveryapplication.Model.HistoryProduct;
import com.example.fooddeliveryapplication.databinding.ItemHistoryProductBinding;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {
    private Context mContext;
    private List<HistoryProduct> mListHistoryProducts;

    public HistoryAdapter(Context mContext, List<HistoryProduct> mListHistoryProducts) {
        this.mContext = mContext;
        this.mListHistoryProducts = mListHistoryProducts;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HistoryViewHolder(ItemHistoryProductBinding.inflate(LayoutInflater.from(mContext), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        HistoryProduct historyProduct = mListHistoryProducts.get(position);

        if (historyProduct == null)
            return;

        holder.binding.imgHistoryProduct.setImageResource(historyProduct.getResourceId());
        holder.binding.txtNameHistoryProduct.setText(historyProduct.getNameHistoryProduct());
        holder.binding.txtStateHistory.setText(historyProduct.getState());
        holder.binding.txtPriceHistoryProduct.setText(historyProduct.getPriceHistoryProduct());
    }

    @Override
    public int getItemCount() {
        return mListHistoryProducts == null ? 0 : mListHistoryProducts.size();
    }

    public class HistoryViewHolder extends RecyclerView.ViewHolder{
        private final ItemHistoryProductBinding binding;

        public HistoryViewHolder(@NonNull ItemHistoryProductBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
