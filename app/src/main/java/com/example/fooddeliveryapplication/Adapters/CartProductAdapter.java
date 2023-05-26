package com.example.fooddeliveryapplication.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.example.fooddeliveryapplication.Models.Cart;
import com.example.fooddeliveryapplication.Models.CartInfo;
import com.example.fooddeliveryapplication.Models.Product;
import com.example.fooddeliveryapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;

public class CartProductAdapter extends RecyclerView.Adapter<CartProductAdapter.ViewHolder> {
    private Context mContext;
    private List<CartInfo> mCartInfos;
    private String cartId;
    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();

    public CartProductAdapter(Context mContext, List<CartInfo> mCartInfos, String cartId) {
        this.mContext = mContext;
        this.mCartInfos = mCartInfos;
        this.cartId = cartId;
        viewBinderHelper.setOpenOnlyOne(true);
    }

    @NonNull
    @Override
    public CartProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_cart_product, parent, false);
        return new CartProductAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartProductAdapter.ViewHolder holder, int position) {
        CartInfo cartInfo = mCartInfos.get(position);

        viewBinderHelper.bind(holder.swipeRevealLayout, cartInfo.getCartInfoId());

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

        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Change display value
                int amount = Integer.parseInt(holder.productAmount.getText().toString());
                amount++;
                holder.productAmount.setText(String.valueOf(amount));

                // Save to firebase
                FirebaseDatabase.getInstance().getReference().child("CartInfos").child(cartId).child(cartInfo.getCartInfoId()).child("amount").setValue(amount);

                FirebaseDatabase.getInstance().getReference().child("Carts").child(cartId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Cart cart = snapshot.getValue(Cart.class);
                        FirebaseDatabase.getInstance().getReference().child("Products").child(cartInfo.getProductId()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot1) {
                                Product product = snapshot1.getValue(Product.class);
                                int totalAmount = cart.getTotalAmount() + 1;
                                long totalPrice = cart.getTotalPrice() + product.getProductPrice();

                                HashMap<String, Object> map = new HashMap<>();
                                map.put("totalAmount", totalAmount);
                                map.put("totalPrice", totalPrice);
                                FirebaseDatabase.getInstance().getReference().child("Carts").child(cartId).updateChildren(map);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        holder.subtract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!holder.productAmount.getText().toString().equals("1")) {
                    // Change display value
                    int amount = Integer.parseInt(holder.productAmount.getText().toString());
                    amount--;
                    holder.productAmount.setText(String.valueOf(amount));

                    Toast.makeText(mContext, cartId, Toast.LENGTH_SHORT).show();

                    // Save to firebase
                    FirebaseDatabase.getInstance().getReference().child("CartInfos").child(cartId).child(cartInfo.getCartInfoId()).child("amount").setValue(amount);

                    FirebaseDatabase.getInstance().getReference().child("Carts").child(cartId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Cart cart = snapshot.getValue(Cart.class);
                            FirebaseDatabase.getInstance().getReference().child("Products").child(cartInfo.getProductId()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot1) {
                                    Product product = snapshot1.getValue(Product.class);
                                    int totalAmount = cart.getTotalAmount() - 1;
                                    long totalPrice = cart.getTotalPrice() - product.getProductPrice();

                                    Toast.makeText(mContext, String.valueOf(totalPrice), Toast.LENGTH_SHORT).show();

                                    HashMap<String, Object> map = new HashMap<>();
                                    map.put("totalAmount", totalAmount);
                                    map.put("totalPrice", totalPrice);
                                    FirebaseDatabase.getInstance().getReference().child("Carts").child(cartId).updateChildren(map);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });
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

    public void saveStates(Bundle outState) {
        viewBinderHelper.saveStates(outState);
    }

    public void restoreStates(Bundle instate) {
        viewBinderHelper.restoreStates(instate);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public SwipeRevealLayout swipeRevealLayout;
        public ImageView productImage;
        public TextView productName;
        public TextView productPrice;
        public Button subtract;
        public Button add;
        public Button productAmount;
        public ImageButton like;
        public ImageButton delete;
        public View buttonsContainer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            swipeRevealLayout = itemView.findViewById(R.id.swipe_reveal_layout);
            productImage = itemView.findViewById(R.id.product_image);
            productName = itemView.findViewById(R.id.product_name);
            productPrice = itemView.findViewById(R.id.product_price);
            subtract = itemView.findViewById(R.id.subtract);
            add = itemView.findViewById(R.id.add);
            productAmount = itemView.findViewById(R.id.product_amount);
            like = itemView.findViewById(R.id.like);
            delete = itemView.findViewById(R.id.delete);
            buttonsContainer = itemView.findViewById(R.id.buttons_container);
        }
    }
}
