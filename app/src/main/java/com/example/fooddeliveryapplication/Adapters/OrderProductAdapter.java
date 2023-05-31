package com.example.fooddeliveryapplication.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fooddeliveryapplication.Models.CartInfo;
import com.example.fooddeliveryapplication.Models.Product;
import com.example.fooddeliveryapplication.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class OrderProductAdapter extends RecyclerView.Adapter<OrderProductAdapter.ViewHolder>{
    private Context mContext;
    private List<CartInfo> mCartInfos;

    public OrderProductAdapter(Context mContext, List<CartInfo> mCartInfos) {
        this.mContext = mContext;
        this.mCartInfos = mCartInfos;
    }

    @NonNull
    @Override
    public OrderProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_order_product, parent, false);
        return new OrderProductAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderProductAdapter.ViewHolder holder, int position) {
        CartInfo cartInfo = mCartInfos.get(position);

        FirebaseDatabase.getInstance().getReference().child("Products").child(cartInfo.getProductId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Product product = snapshot.getValue(Product.class);
                holder.productName.setText(product.getProductName());
                holder.productPrice.setText("#" + convertToMoney(product.getProductPrice()));
                Glide.with(mContext).load(product.getProductImage1()).placeholder(R.mipmap.ic_launcher).into(holder.productImage);
                holder.amount.setText(String.valueOf(cartInfo.getAmount()) + " x ");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mCartInfos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView amount;
        public TextView productName;
        public TextView productPrice;
        public ImageView productImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            amount = itemView.findViewById(R.id.amount);
            productName = itemView.findViewById(R.id.order_product_name);
            productPrice = itemView.findViewById(R.id.order_product_price);
            productImage = itemView.findViewById(R.id.order_product_image);
        }
    }

    private String convertToMoney(long price) {
        String temp = String.valueOf(price);
        String output = "";
        int count = 3;
        for (int i = temp.length() - 1; i >= 0; i--) {
            count--;
            if (count == 0) {
                count = 3;
                output = "," + temp.charAt(i) + output;
            }
            else {
                output = temp.charAt(i) + output;
            }
        }

        if (output.charAt(0) == ',')
            return output.substring(1);

        return output;

    }
}
