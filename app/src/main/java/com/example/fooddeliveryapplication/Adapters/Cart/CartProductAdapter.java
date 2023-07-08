package com.example.fooddeliveryapplication.Adapters.Cart;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.example.fooddeliveryapplication.Activities.Home.EditProfileActivity;
import com.example.fooddeliveryapplication.Activities.ProductInformation.ProductInfoActivity;
import com.example.fooddeliveryapplication.CustomMessageBox.CustomAlertDialog;
import com.example.fooddeliveryapplication.CustomMessageBox.FailToast;
import com.example.fooddeliveryapplication.CustomMessageBox.SuccessfulToast;
import com.example.fooddeliveryapplication.Helpers.FirebaseNotificationHelper;
import com.example.fooddeliveryapplication.Helpers.FirebaseProductInfoHelper;
import com.example.fooddeliveryapplication.Helpers.FirebaseUserInfoHelper;
import com.example.fooddeliveryapplication.Interfaces.IAdapterItemListener;
import com.example.fooddeliveryapplication.Model.Cart;
import com.example.fooddeliveryapplication.Model.CartInfo;
import com.example.fooddeliveryapplication.Model.Notification;
import com.example.fooddeliveryapplication.Model.Product;
import com.example.fooddeliveryapplication.Model.User;
import com.example.fooddeliveryapplication.R;
import com.example.fooddeliveryapplication.databinding.ItemCartProductBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
    private int checkedItemCount = 0;
    private long checkedItemPrice = 0;
    private IAdapterItemListener adapterItemListener;
    private boolean isCheckAll;
    private String userId;
    private String userName;
    private ArrayList<CartInfo> selectedItems = new ArrayList<>();

    public CartProductAdapter(Context mContext, List<CartInfo> mCartInfos, String cartId, boolean isCheckAll, String id) {
        this.mContext = mContext;
        this.mCartInfos = mCartInfos;
        this.cartId = cartId;
        this.isCheckAll = isCheckAll;
        this.userId = id;
        viewBinderHelper.setOpenOnlyOne(true);

        new FirebaseUserInfoHelper(mContext).readUserInfo(userId, new FirebaseUserInfoHelper.DataStatus() {
            @Override
            public void DataIsLoaded(User user) {
                userName = user.getUserName();
            }

            @Override
            public void DataIsInserted() {

            }

            @Override
            public void DataIsUpdated() {

            }

            @Override
            public void DataIsDeleted() {

            }
        });
    }

    @NonNull
    @Override
    public CartProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemCartProductBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CartProductAdapter.ViewHolder holder, int position) {
        CartInfo cartInfo = mCartInfos.get(position);

        viewBinderHelper.bind(holder.binding.swipeRevealLayout, cartInfo.getCartInfoId());

        holder.binding.checkBox.setChecked(isCheckAll);

        FirebaseDatabase.getInstance().getReference().child("Products").child(cartInfo.getProductId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Product product = snapshot.getValue(Product.class);
                holder.binding.productName.setText(product.getProductName());
                holder.binding.productPrice.setText(convertToMoney(product.getProductPrice())+"đ");
                Glide.with(mContext).load(product.getProductImage1()).placeholder(R.mipmap.ic_launcher).into(holder.binding.productImage);
                holder.binding.productAmount.setText(String.valueOf(cartInfo.getAmount()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        isLiked(holder.binding.like, cartInfo.getProductId());

        holder.binding.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference().child("Products").child(cartInfo.getProductId()).child("remainAmount").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        int amount = Integer.parseInt(holder.binding.productAmount.getText().toString());
                        int remainAmount = snapshot.getValue(int.class);
                        if (amount >= remainAmount) {
                            new FailToast().showToast(mContext, "Can't add anymore!");
                        }
                        else {
                            // Change display value
                            amount++;
                            holder.binding.productAmount.setText(String.valueOf(amount));
                            holder.binding.checkBox.setChecked(false);
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

                            FirebaseDatabase.getInstance().getReference().child("Products").child(cartInfo.getProductId()).child("remainAmount").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    int remainAmount = snapshot.getValue(int.class) - 1;
                                    FirebaseDatabase.getInstance().getReference().child("Products").child(cartInfo.getProductId()).child("remainAmount").setValue(remainAmount);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        holder.binding.subtract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!holder.binding.productAmount.getText().toString().equals("1")) {
                    // Change display value
                    int amount = Integer.parseInt(holder.binding.productAmount.getText().toString());
                    amount--;
                    holder.binding.productAmount.setText(String.valueOf(amount));
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

                    FirebaseDatabase.getInstance().getReference().child("Products").child(cartInfo.getProductId()).child("remainAmount").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            int remainAmount = snapshot.getValue(int.class) + 1;
                            FirebaseDatabase.getInstance().getReference().child("Products").child(cartInfo.getProductId()).child("remainAmount").setValue(remainAmount);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                else {
                    new FailToast().showToast(mContext, "Can't reduce anymore!");
                }
            }
        });

        holder.binding.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.binding.like.getTag().equals("like")) {
                    FirebaseDatabase.getInstance().getReference().child("Favorites").child(userId).child(cartInfo.getProductId()).setValue(true);
                    pushNotificationFavourite(cartInfo);
                    new SuccessfulToast().showToast(mContext,"Added to your favourite list");
                }
                else if (holder.binding.like.getTag().equals("liked")) {
                    FirebaseDatabase.getInstance().getReference().child("Favorites").child(userId).child(cartInfo.getProductId()).removeValue();
                }
            }
        });

        holder.binding.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CustomAlertDialog(mContext,"Delete this product?");
                CustomAlertDialog.btnYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CustomAlertDialog.alertDialog.dismiss();

                        FirebaseDatabase.getInstance().getReference().child("CartInfos").child(cartId).child(cartInfo.getCartInfoId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    new SuccessfulToast().showToast(mContext, "Delete product successfully!");
                                    if (adapterItemListener != null) {
                                        adapterItemListener.onDeleteProduct();
                                    }
                                }
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

                        FirebaseDatabase.getInstance().getReference().child("Products").child(cartInfo.getProductId()).child("remainAmount").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                int remainAmount = snapshot.getValue(int.class) + cartInfo.getAmount();
                                FirebaseDatabase.getInstance().getReference().child("Products").child(cartInfo.getProductId()).child("remainAmount").setValue(remainAmount);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                });
                CustomAlertDialog.btnNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CustomAlertDialog.alertDialog.dismiss();
                    }
                });
                CustomAlertDialog.showAlertDialog();
            }
        });

        holder.binding.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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
        FirebaseDatabase.getInstance().getReference().child("Favorites").child(userId).addValueEventListener(new ValueEventListener() {
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
        return mCartInfos == null ? 0 : mCartInfos.size();
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

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemCartProductBinding binding;

        public ViewHolder(ItemCartProductBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
    public void pushNotificationFavourite(CartInfo cartInfo)
    {
        new FirebaseProductInfoHelper(cartInfo.getProductId()).readInformationById(new FirebaseProductInfoHelper.DataStatusInformationOfProduct() {
            @Override
            public void DataIsLoaded(Product product) {
                String title = "Sản phẩm yêu thích";
                String content = userName + " đã thích sản phẩm "+ product.getProductName() + " của bạn. Nhấn vào để xem lượt yêu thích nào.";
                Notification notification = FirebaseNotificationHelper.createNotification(title,content,product.getProductImage1(),product.getProductId(),"None","None");
                new FirebaseNotificationHelper(mContext).addNotification(product.getPublisherId(), notification, new FirebaseNotificationHelper.DataStatus() {
                    @Override
                    public void DataIsLoaded(List<Notification> notificationList,List<Notification> notificationListToNotify) {

                    }

                    @Override
                    public void DataIsInserted() {

                    }

                    @Override
                    public void DataIsUpdated() {

                    }

                    @Override
                    public void DataIsDeleted() {

                    }
                });
            }

            @Override
            public void DataIsInserted() {

            }

            @Override
            public void DataIsUpdated() {

            }

            @Override
            public void DataIsDeleted() {

            }
        });
    }
}
