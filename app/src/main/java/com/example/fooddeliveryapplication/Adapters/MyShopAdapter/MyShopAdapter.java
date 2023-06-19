package com.example.fooddeliveryapplication.Adapters.MyShopAdapter;

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
import com.example.fooddeliveryapplication.Model.Product;
import com.example.fooddeliveryapplication.R;
import com.example.fooddeliveryapplication.databinding.LayoutFoodItemBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MyShopAdapter extends RecyclerView.Adapter {

    private ArrayList<Product> ds;
    private ViewBinderHelper viewBinderHelper=new ViewBinderHelper();
    private Context context;
    String userId;

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

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Product product =ds.get(position);
       ViewHolder viewHolder=(ViewHolder) holder;
        viewBinderHelper.bind(viewHolder.binding.SwipeRevealLayout, product.getProductId());

       viewHolder.binding.txtNameProdiuct.setText(product.getProductName());
       viewHolder.binding.txtPrice.setText(product.getProductPrice()+"");
        Glide.with(context)
                .load(product.getProductImage1())
                .placeholder(R.drawable.baseline_image_search_24)
                .into(viewHolder.binding.imgFood);
        viewHolder.binding.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference("Products").child(product.getProductId()+"").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            ds.remove(product);
                            notifyItemRemoved(position);
                            Toast.makeText(context, "Đã xóa thành công", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Xóa không thành công", Toast.LENGTH_SHORT).show();
                            Log.e("My Shop","Error remove");
                        }
                    }
                });
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
    }

    @Override
    public int getItemCount() {
        return ds.size();
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        LayoutFoodItemBinding binding;
        public ViewHolder(@NonNull LayoutFoodItemBinding tmp) {
            super(tmp.getRoot());
            binding=tmp;
        }
    }

}
