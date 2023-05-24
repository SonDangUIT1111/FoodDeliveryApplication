package com.example.fooddeliveryapplication.HomeAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fooddeliveryapplication.Model.Food;
import com.example.fooddeliveryapplication.Model.Products;
import com.example.fooddeliveryapplication.R;
import com.example.fooddeliveryapplication.databinding.ActivityFindBinding;
import com.example.fooddeliveryapplication.databinding.ItemHomeFindLayoutBinding;


import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class FindAdapter extends RecyclerView.Adapter implements Filterable {

    NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
    ArrayList<Products>ds;
    ArrayList<Products>dsAll;

    public FindAdapter(ArrayList<Products> ds) {
        this.dsAll=ds;
        this.ds=ds;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemHomeFindLayoutBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Products item=ds.get(position);
            if (item==null) {
                return;
            } else {
                ViewHolder viewHolder=(ViewHolder) holder;

                if (position==1) {
                    RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) viewHolder.itemView.getLayoutParams();
                    int margin = 120;
                    layoutParams.setMargins(0, margin, 0, 0);
                    holder.itemView.setLayoutParams(layoutParams);
                }
                else if (position== 0) {
                    RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) viewHolder.itemView.getLayoutParams();
                    int margin = 50;
                    layoutParams.setMargins(0, margin, 0, 0);
                    holder.itemView.setLayoutParams(layoutParams);
                }
                Glide.with(viewHolder.binding.getRoot()).load(item.getProductImage1()).placeholder(R.drawable.image_default).into(viewHolder.binding.imgFood);
                viewHolder.binding.txtFoodName.setText(item.getProductName());
                viewHolder.binding.txtRating.setText(item.getRatingStar()+"/5.0");
                if (item.getRatingStar()==5) {
                    viewHolder.binding.imgRate.setImageResource(R.drawable.rating_star_filled);
                } else if (item.getRatingStar()>=3 && item.getRatingStar()<5) {
                    viewHolder.binding.imgRate.setImageResource(R.drawable.rating_star_half);
                } else {
                    viewHolder.binding.imgRate.setImageResource(R.drawable.rating_star_empty);
                }
                viewHolder.binding.txtFoodPrice.setText(nf.format(item.getProductPrice()));
            }
    }

    @Override
    public int getItemCount() {
        return ds.size();
    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String key=charSequence.toString();
                if (key.isEmpty())
                    ds=dsAll;
                else {
                    ArrayList<Products> tmp=new ArrayList<>();
                    key=key.toLowerCase();
                    for (Products item: dsAll) {
                        if (item.getProductName().toLowerCase().contains(key)) {
                            tmp.add(item);
                        }
                    }
                    ds=tmp;
                }
                FilterResults results=new FilterResults();
                results.values=ds;
                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                    ds= (ArrayList<Products>) filterResults.values;
                    notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemHomeFindLayoutBinding binding;

        public ViewHolder(@NonNull ItemHomeFindLayoutBinding tmp) {
            super(tmp.getRoot());
            binding=tmp;


        }
    }
}
