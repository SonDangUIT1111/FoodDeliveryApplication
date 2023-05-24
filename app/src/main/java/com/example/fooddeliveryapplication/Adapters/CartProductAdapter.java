package com.example.fooddeliveryapplication.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fooddeliveryapplication.Models.CartInfo;
import com.example.fooddeliveryapplication.Models.Product;
import com.example.fooddeliveryapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class CartProductAdapter extends RecyclerView.Adapter<CartProductAdapter.ViewHolder> {
    private Context mContext;
    private List<CartInfo> mCartInfos;
    private String cartId;

    public CartProductAdapter(Context mContext, List<CartInfo> mCartInfos, String cartId) {
        this.mContext = mContext;
        this.mCartInfos = mCartInfos;
        this.cartId = cartId;
    }

    @NonNull
    @Override
    public CartProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.cart_product_item, parent, false);
        return new CartProductAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartProductAdapter.ViewHolder holder, int position) {
        CartInfo cartInfo = mCartInfos.get(position);

        FirebaseDatabase.getInstance().getReference().child("Products").child(cartInfo.getProductId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Product product = snapshot.getValue(Product.class);
                holder.productName.setText(product.getProductName());
                holder.productPrice.setText("#" + convertToMoney(product.getProductPrice()));
                Glide.with(mContext).load(product.getProductImage1()).placeholder(R.mipmap.ic_launcher).into(holder.productImage);
                holder.productAmount.setText(String.valueOf(cartInfo.getAmount()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        isLiked(holder.like, cartInfo.getProductId());
    }

    private void isLiked(ImageButton like, String productId) {
        FirebaseDatabase.getInstance().getReference().child("Favorites").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(productId).exists()) {
                    like.setImageResource(R.drawable.ic_liked);
                    like.setTag("liked");
                }
                else {
                    like.setImageResource(R.drawable.ic_like);
                    like.setTag("like");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private String convertToMoney(int price) {
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
            return output.substring(1, output.length());

        return output;

    }

    @Override
    public int getItemCount() {
        return mCartInfos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView productImage;
        public TextView productName;
        public TextView productPrice;
        public Button subtract;
        public Button add;
        public Button productAmount;
        public ImageButton like;
        public ImageButton delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            productImage = itemView.findViewById(R.id.product_image);
            productName = itemView.findViewById(R.id.product_name);
            productPrice = itemView.findViewById(R.id.product_price);
            subtract = itemView.findViewById(R.id.subtract);
            add = itemView.findViewById(R.id.add);
            productAmount = itemView.findViewById(R.id.product_amount);
            like = itemView.findViewById(R.id.like);
            delete = itemView.findViewById(R.id.delete);
        }
    }
}
