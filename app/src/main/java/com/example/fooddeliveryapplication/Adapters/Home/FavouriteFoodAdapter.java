package com.example.fooddeliveryapplication.Adapters.Home;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fooddeliveryapplication.Activities.ProductInformation.ProductInfoActivity;
import com.example.fooddeliveryapplication.Model.Product;
import com.example.fooddeliveryapplication.R;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class FavouriteFoodAdapter extends RecyclerView.Adapter<FavouriteFoodAdapter.ViewHolder>{
    private Context mContext;
    private ArrayList<Product> favouriteLists;
    private String userId;
    private String userName;
    NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
    public FavouriteFoodAdapter(Context mContext, ArrayList<Product> lists,String id) {
        this.mContext = mContext;
        this.favouriteLists = lists;
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
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.favourite_product_item,parent,false);
        return new FavouriteFoodAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavouriteFoodAdapter.ViewHolder holder, int position) {
        Product product = favouriteLists.get(position);
        if (product == null) {
            return;
        } else {
            if (position==1) {
                RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) holder.itemView.getLayoutParams();
                int margin = 120;
                layoutParams.setMargins(0, margin, 0, 0);
                holder.itemView.setLayoutParams(layoutParams);
            }
            else if (position== 0) {
                RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) holder.itemView.getLayoutParams();
                int margin = 50;
                layoutParams.setMargins(0, margin, 0, 0);
                holder.itemView.setLayoutParams(layoutParams);
            }

            Glide.with(mContext)
                    .load(product.getProductImage1())
                    .placeholder(R.drawable.image_default)
                    .into(holder.imgFavouriteFood);

            holder.txtFavouriteFoodName.setText(product.getProductName());
            holder.txtFavouriteRating.setText(product.getRatingStar()+"/5.0");
            if (Float.valueOf(product.getRatingStar())>=5) {
                holder.imgFavouriteRate.setImageResource(R.drawable.rating_star_filled);
            } else if (Float.valueOf(product.getRatingStar())>=3 && Float.valueOf(product.getRatingStar())<5) {
                holder.imgFavouriteRate.setImageResource(R.drawable.rating_star_half);
            } else {
                holder.imgFavouriteRate.setImageResource(R.drawable.rating_star_empty);
            }
            holder.txtFavouriteFoodPrice.setText(nf.format(product.getProductPrice()));
            holder.parentOfItemInFavourite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ProductInfoActivity.class);
                    intent.putExtra("productId",product.getProductId());
                    intent.putExtra("productName",product.getProductName());
                    intent.putExtra("productPrice",product.getProductPrice());
                    intent.putExtra("productImage1",product.getProductImage1());
                    intent.putExtra("productImage2",product.getProductImage2());
                    intent.putExtra("productImage3",product.getProductImage3());
                    intent.putExtra("productImage4",product.getProductImage4());
                    intent.putExtra("ratingStar",product.getRatingStar());
                    intent.putExtra("productDescription",product.getDescription());
                    intent.putExtra("publisherId",product.getPublisherId());
                    intent.putExtra("sold",product.getSold());
                    intent.putExtra("userId",userId);
                    intent.putExtra("userName",userName);
                    mContext.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return favouriteLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ShapeableImageView imgFavouriteFood;
        TextView txtFavouriteFoodName;
        TextView txtFavouriteRating;
        ImageView imgFavouriteRate;
        TextView txtFavouriteFoodPrice;
        LinearLayout parentOfItemInFavourite;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgFavouriteFood = itemView.findViewById(R.id.imgFavouriteFood);
            txtFavouriteFoodName = itemView.findViewById(R.id.txtFavouriteFoodName);
            txtFavouriteRating = itemView.findViewById(R.id.txtFavouriteRating);
            imgFavouriteRate = itemView.findViewById(R.id.imgFavouriteRate);
            txtFavouriteFoodPrice = itemView.findViewById(R.id.txtFavouriteFoodPrice);
            parentOfItemInFavourite = itemView.findViewById(R.id.parent_of_item_in_favourite);
        }
    }
}
