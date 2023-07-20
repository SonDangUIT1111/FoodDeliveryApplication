package com.example.fooddeliveryapplication.Adapters.MyShopAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.example.fooddeliveryapplication.Activities.MyShop.AddFoodActivity;
import com.example.fooddeliveryapplication.Activities.ProductInformation.ProductInfoActivity;
import com.example.fooddeliveryapplication.CustomMessageBox.CustomAlertDialog;
import com.example.fooddeliveryapplication.CustomMessageBox.FailToast;
import com.example.fooddeliveryapplication.CustomMessageBox.SuccessfulToast;
import com.example.fooddeliveryapplication.Model.Cart;
import com.example.fooddeliveryapplication.Model.Product;
import com.example.fooddeliveryapplication.R;
import com.example.fooddeliveryapplication.databinding.LayoutFoodItemBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class MyShopAdapter extends RecyclerView.Adapter {
    private ArrayList<Product> ds;
    private ViewBinderHelper viewBinderHelper=new ViewBinderHelper();
    private Context context;
    private String userId;

    public MyShopAdapter(ArrayList<Product> ds, Context context,String id) {
        viewBinderHelper.setOpenOnlyOne(true);
        this.ds = ds;
        this.context = context;
        this.userId = id;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutFoodItemBinding.inflate(LayoutInflater.from(context),parent,false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Product product = ds.get(position);
        ViewHolder viewHolder=(ViewHolder) holder;
        viewBinderHelper.bind(viewHolder.binding.SwipeRevealLayout, product.getProductId());

        viewHolder.binding.txtNameProdiuct.setText(product.getProductName());
        viewHolder.binding.txtPrice.setText(convertToMoney(product.getProductPrice()) + "đ");
        Glide.with(context)
                .load(product.getProductImage1())
                .placeholder(R.drawable.baseline_image_search_24)
                .into(viewHolder.binding.imgFood);
        viewHolder.binding.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CustomAlertDialog(context,"Delete this product?");
                CustomAlertDialog.binding.btnYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CustomAlertDialog.alertDialog.dismiss();

                        FirebaseDatabase.getInstance().getReference("Products").child(product.getProductId()).child("state").setValue("deleted").addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    ds.remove(product);
                                    notifyItemRemoved(position);
                                    new SuccessfulToast(context, "Delete product successfully!").showToast();
                                } else {
                                    new FailToast(context, "Delete product failed!").showToast();
                                    Log.e("My Shop","Error remove");
                                }
                            }
                        });
                    }
                });
                CustomAlertDialog.binding.btnNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CustomAlertDialog.alertDialog.dismiss();
                    }
                });
                CustomAlertDialog.showAlertDialog();
            }
        });
        viewHolder.binding.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    viewHolder.binding.SwipeRevealLayout.resetPivot();
                }
                Intent intent=new Intent(context, AddFoodActivity.class);
                intent.putExtra("Product updating", product);
                context.startActivity(intent);
            }
        });

        viewHolder.binding.productContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProductInfoActivity.class);
                intent.putExtra("productId", product.getProductId());
                intent.putExtra("productName", product.getProductName());
                intent.putExtra("productPrice", product.getProductPrice());
                intent.putExtra("productImage1", product.getProductImage1());
                intent.putExtra("productImage2", product.getProductImage2());
                intent.putExtra("productImage3", product.getProductImage3());
                intent.putExtra("productImage4", product.getProductImage4());
                intent.putExtra("ratingStar", product.getRatingStar());
                intent.putExtra("productDescription", product.getDescription());
                intent.putExtra("publisherId", product.getPublisherId());
                intent.putExtra("sold", product.getSold());
                intent.putExtra("productType", product.getProductType());
                intent.putExtra("remainAmount", product.getRemainAmount());
                intent.putExtra("ratingAmount", product.getRatingAmount());
                intent.putExtra("state", product.getState());
                intent.putExtra("userId", userId);
                intent.putExtra("userName", product);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ds == null ? 0 : ds.size();
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {
        private final LayoutFoodItemBinding binding;

        public ViewHolder(@NonNull LayoutFoodItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
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
