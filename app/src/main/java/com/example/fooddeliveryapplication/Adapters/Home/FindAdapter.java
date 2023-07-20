package com.example.fooddeliveryapplication.Adapters.Home;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fooddeliveryapplication.Activities.ProductInformation.ProductInfoActivity;
import com.example.fooddeliveryapplication.Model.Product;
import com.example.fooddeliveryapplication.R;
import com.example.fooddeliveryapplication.databinding.ItemHomeFindLayoutBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class FindAdapter extends RecyclerView.Adapter implements Filterable {
    private NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
    private ArrayList<Product> ds;
    private ArrayList<Product> dsAll;
    private String userId;
    private String userName;
    private Context mContext;

    public FindAdapter(ArrayList<Product> ds, String id,Context context) {
        this.mContext = context;
        this.dsAll=ds;
        this.ds=ds;
        this.userId = id;
        FirebaseDatabase.getInstance().getReference().child("Users").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userName = snapshot.child("userName").getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemHomeFindLayoutBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Product item = ds.get(position);
        if (item != null) {
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

            Glide.with(viewHolder.binding.getRoot())
                    .load(item.getProductImage1())
                    .placeholder(R.drawable.image_default)
                    .into(viewHolder.binding.imgFood);

            viewHolder.binding.txtFoodName.setText(item.getProductName());
            double ratingStar = (double) Math.round(item.getRatingStar() * 10) / 10;
            viewHolder.binding.txtRating.setText(ratingStar + "/5.0");
            if (item.getRatingStar()>=5) {
                viewHolder.binding.imgRate.setImageResource(R.drawable.rating_star_filled);
            } else if (item.getRatingStar()>=3 && item.getRatingStar()<5) {
                viewHolder.binding.imgRate.setImageResource(R.drawable.rating_star_half);
            } else {
                viewHolder.binding.imgRate.setImageResource(R.drawable.rating_star_empty);
            }
            viewHolder.binding.txtFoodPrice.setText(nf.format(item.getProductPrice()));
            viewHolder.binding.parentOfItemInFindActivity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ProductInfoActivity.class);
                    intent.putExtra("productId", item.getProductId());
                    intent.putExtra("productName", item.getProductName());
                    intent.putExtra("productPrice", item.getProductPrice());
                    intent.putExtra("productImage1", item.getProductImage1());
                    intent.putExtra("productImage2", item.getProductImage2());
                    intent.putExtra("productImage3", item.getProductImage3());
                    intent.putExtra("productImage4", item.getProductImage4());
                    intent.putExtra("ratingStar", item.getRatingStar());
                    intent.putExtra("productDescription", item.getDescription());
                    intent.putExtra("publisherId", item.getPublisherId());
                    intent.putExtra("sold", item.getSold());
                    intent.putExtra("productType", item.getProductType());
                    intent.putExtra("remainAmount", item.getRemainAmount());
                    intent.putExtra("ratingAmount", item.getRatingAmount());
                    intent.putExtra("state", item.getState());
                    intent.putExtra("userId", userId);
                    intent.putExtra("userName", userName);
                    mContext.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return ds == null ? 0 : ds.size();
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
                    ArrayList<Product> tmp=new ArrayList<>();
                    key=key.toLowerCase();
                    for (Product item: dsAll) {
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
                ds = (ArrayList<Product>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemHomeFindLayoutBinding binding;

        public ViewHolder(@NonNull ItemHomeFindLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
