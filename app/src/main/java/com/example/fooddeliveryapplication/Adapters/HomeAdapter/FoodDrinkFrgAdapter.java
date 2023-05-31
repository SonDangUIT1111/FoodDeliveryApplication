package com.example.fooddeliveryapplication.Adapters.HomeAdapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fooddeliveryapplication.Activities.ProductInformation.ProductInfoActivity;
import com.example.fooddeliveryapplication.Model.Product;
import com.example.fooddeliveryapplication.databinding.ItemHomeBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class FoodDrinkFrgAdapter extends RecyclerView.Adapter {
    NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
    ArrayList<Product> ds;
    String userId;
    String userName;
    Context mContext;

    public FoodDrinkFrgAdapter(ArrayList<Product> ds, String id, Context context)
    {
        mContext = context;
        this.ds = ds;
        userId = id;
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
        newHolder.binding.parentOfItemInHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ProductInfoActivity.class);
                intent.putExtra("productId",item.getProductId());
                intent.putExtra("productName",item.getProductName());
                intent.putExtra("productPrice",item.getProductPrice());
                intent.putExtra("productImage1",item.getProductImage1());
                intent.putExtra("productImage2",item.getProductImage2());
                intent.putExtra("productImage3",item.getProductImage3());
                intent.putExtra("productImage4",item.getProductImage4());
                intent.putExtra("ratingStar",item.getRatingStar());
                intent.putExtra("productDescription",item.getDescription());
                intent.putExtra("publisherId",item.getPublisherId());
                intent.putExtra("sold",item.getSold());
                intent.putExtra("userId",userId);
                intent.putExtra("userName",userName);
                mContext.startActivity(intent);
            }
        });

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
