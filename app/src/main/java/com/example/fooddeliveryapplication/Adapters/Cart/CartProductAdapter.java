package com.example.fooddeliveryapplication.Adapters.Cart;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.example.fooddeliveryapplication.Interfaces.IAdapterItemListener;
import com.example.fooddeliveryapplication.Model.Cart;
import com.example.fooddeliveryapplication.Model.CartInfo;
import com.example.fooddeliveryapplication.Model.Product;
import com.example.fooddeliveryapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CartProductAdapter extends RecyclerView.Adapter<CartProductAdapter.ViewHolder> {
    private Context mContext;
    private List<CartInfo> mCartInfos;
    private String cartId;
    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();
    private AlertDialog.Builder builder;
    private int checkedItemCount = 0;
    private long checkedItemPrice = 0;
    private IAdapterItemListener adapterItemListener;
    private boolean isCheckAll;
    private ArrayList<CartInfo> selectedItems = new ArrayList<>();

    public CartProductAdapter(Context mContext, List<CartInfo> mCartInfos, String cartId, boolean isCheckAll) {
        this.mContext = mContext;
        this.mCartInfos = mCartInfos;
        this.cartId = cartId;
        this.isCheckAll = isCheckAll;
        viewBinderHelper.setOpenOnlyOne(true);
    }

    private void initDialogBuilder(CartInfo cartInfo) {
        builder = new AlertDialog.Builder(mContext);
        builder.setMessage("Delete this product?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                deleteCartInfo(cartInfo);
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
    }

    public void setCheckAll(boolean isCheckAll) {
        this.isCheckAll = isCheckAll;
    }

    private void deleteCartInfo(CartInfo cartInfo) {
        FirebaseDatabase.getInstance().getReference().child("Products").child(cartInfo.getProductId()).child("remainAmount").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int remainAmount = snapshot.getValue(int.class);
                remainAmount += cartInfo.getAmount();
                FirebaseDatabase.getInstance().getReference().child("Products").child(cartInfo.getProductId()).child("remainAmount").setValue(remainAmount);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        FirebaseDatabase.getInstance().getReference().child("Carts").child(cartId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Cart cart = snapshot.getValue(Cart.class);
                FirebaseDatabase.getInstance().getReference().child("Products").child(cartInfo.getProductId()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Product product = snapshot.getValue(Product.class);
                        int totalAmount = cart.getTotalAmount() - cartInfo.getAmount();
                        long totalPrice = cart.getTotalPrice() - (long)(product.getProductPrice() * cartInfo.getAmount());

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

        FirebaseDatabase.getInstance().getReference().child("CartInfos").child(cartId).child(cartInfo.getCartInfoId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(mContext, "Delete product successfully!", Toast.LENGTH_SHORT).show();
                }
            }
        });
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

        initDialogBuilder(cartInfo);

        holder.checkBox.setChecked(isCheckAll);

        FirebaseDatabase.getInstance().getReference().child("Products").child(cartInfo.getProductId()).addListenerForSingleValueEvent(new ValueEventListener() {
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
                holder.checkBox.setChecked(false);
                isCheckAll = false;

                if (adapterItemListener != null) {
                    adapterItemListener.onCheckedItemCountChanged(0, 0, new ArrayList<>());
                    adapterItemListener.onAddClicked();
                }

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
                    isCheckAll = false;

                    if (adapterItemListener != null) {
                        adapterItemListener.onCheckedItemCountChanged(0, 0, new ArrayList<>());
                        adapterItemListener.onSubtractClicked();
                    }

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

        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.like.getTag().equals("like"))
                    FirebaseDatabase.getInstance().getReference().child("Favorites").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(cartInfo.getProductId()).setValue(true);
                else if (holder.like.getTag().equals("liked")) {
                    FirebaseDatabase.getInstance().getReference().child("Favorites").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(cartInfo.getProductId()).removeValue();
                }
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                FirebaseDatabase.getInstance().getReference().child("Products").child(cartInfo.getProductId()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Product product = snapshot.getValue(Product.class);
                        if (isChecked) {
                            checkedItemCount += cartInfo.getAmount();
                            checkedItemPrice += cartInfo.getAmount() * product.getProductPrice();
                            selectedItems.add(cartInfo);
                        }
                        else {
                            checkedItemCount -= cartInfo.getAmount();
                            checkedItemPrice -= cartInfo.getAmount() * product.getProductPrice();
                            selectedItems.removeIf(c -> (c.getCartInfoId().equals(cartInfo.getCartInfoId())));
                        }

                        if (adapterItemListener != null) {
                            adapterItemListener.onCheckedItemCountChanged(checkedItemCount, checkedItemPrice, selectedItems);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }

    private void isLiked(ImageButton imageButton, String productId) {
        FirebaseDatabase.getInstance().getReference().child("Favorites").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(productId).exists()) {
                    imageButton.setImageResource(R.drawable.ic_liked);
                    imageButton.setTag("liked");
                }
                else {
                    imageButton.setImageResource(R.drawable.ic_like);
                    imageButton.setTag("like");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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

    @Override
    public int getItemCount() {
        return mCartInfos.size();
    }

    public void saveStates(Bundle outState) {
        viewBinderHelper.saveStates(outState);
    }

    public void setAdapterItemListener(IAdapterItemListener adapterItemListener) {
        this.adapterItemListener = adapterItemListener;
    }

    public void restoreStates(Bundle instate) {
        viewBinderHelper.restoreStates(instate);
    }

    public List<CartInfo> getSelectedItems() {
        return selectedItems;
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
        public CheckBox checkBox;

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
            checkBox = itemView.findViewById(R.id.check_box);
        }
    }
}
