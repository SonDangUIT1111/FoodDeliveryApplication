package com.example.fooddeliveryapplication.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fooddeliveryapplication.Model.InfoCurrentProduct;
import com.example.fooddeliveryapplication.R;

import java.util.List;

public class CurrentAdapter extends RecyclerView.Adapter<CurrentAdapter.CurrentViewHolder> {

    private Context mContext;
    private List<InfoCurrentProduct> mListInfoCurrentProducts;

    public CurrentAdapter() {
        this.mContext = mContext;
    }

    public void setData(List<InfoCurrentProduct> list){
        this.mListInfoCurrentProducts = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CurrentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_info_current_product, parent,false);
        return new CurrentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CurrentViewHolder holder, int position) {

        InfoCurrentProduct infoCurrentProduct = mListInfoCurrentProducts.get(position);
        if(infoCurrentProduct==null)
            return;
        holder.imgCurrentProduct.setImageResource(infoCurrentProduct.getResourceId());
        holder.txtNameCurrentProduct.setText(infoCurrentProduct.getNameCurrentProduct());
        holder.txtStateCurrentProduct.setText(infoCurrentProduct.getState());
        holder.txtPriceCurrentProduct.setText(infoCurrentProduct.getPriceCurrentProduct());
    }

    @Override
    public int getItemCount() {
        if(mListInfoCurrentProducts!= null){
            return mListInfoCurrentProducts.size();
        }
        return 0;
    }

    public class CurrentViewHolder extends RecyclerView.ViewHolder{

        private ImageView imgCurrentProduct;
        private TextView txtNameCurrentProduct;
        private TextView txtStateCurrentProduct;
        private TextView txtPriceCurrentProduct;

        public CurrentViewHolder(@NonNull View itemView) {
            super(itemView);

            imgCurrentProduct = itemView.findViewById(R.id.img_Current_product);
            txtNameCurrentProduct = itemView.findViewById(R.id.txtNameCurrentProduct);
            txtStateCurrentProduct = itemView.findViewById(R.id.txtState);
            txtPriceCurrentProduct = itemView.findViewById(R.id.txtPriceCurrentProduct);

        }
    }
}
