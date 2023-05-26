package com.example.fooddeliveryapplication.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fooddeliveryapplication.Model.HistoryProduct;
import com.example.fooddeliveryapplication.Model.InfoCurrentProduct;
import com.example.fooddeliveryapplication.R;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private Context mContext;
    private List<HistoryProduct> mListHistoryProducts;

    public HistoryAdapter() {
        this.mContext = mContext;
    }

    public void setData(List<HistoryProduct> list){
        this.mListHistoryProducts = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history_product, parent,false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {

        HistoryProduct historyProduct = mListHistoryProducts.get(position);
        if(historyProduct==null)
            return;
        holder.imgHistoryProduct.setImageResource(historyProduct.getResourceId());
        holder.txtNameHistoryProduct.setText(historyProduct.getNameHistoryProduct());
        holder.txtStateHistoryProduct.setText(historyProduct.getState());
        holder.txtPriceHistoryProduct.setText(historyProduct.getPriceHistoryProduct());
    }

    @Override
    public int getItemCount() {
        if(mListHistoryProducts!= null){
            return mListHistoryProducts.size();
        }
        return 0;
    }

    public class HistoryViewHolder extends RecyclerView.ViewHolder{

        private ImageView imgHistoryProduct;
        private TextView txtNameHistoryProduct;
        private TextView txtStateHistoryProduct;
        private TextView txtPriceHistoryProduct;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);

            imgHistoryProduct = itemView.findViewById(R.id.img_History_product);
            txtNameHistoryProduct = itemView.findViewById(R.id.txtNameHistoryProduct);
            txtStateHistoryProduct = itemView.findViewById(R.id.txtState_History);
            txtPriceHistoryProduct = itemView.findViewById(R.id.txtPriceHistoryProduct);
        }
    }
}
