package com.example.fooddeliveryapplication.Adapters.OrderAdapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fooddeliveryapplication.Model.BillInfo;
import com.example.fooddeliveryapplication.Model.CurrencyFormatter;
import com.example.fooddeliveryapplication.Model.Product;
import com.example.fooddeliveryapplication.R;
import com.example.fooddeliveryapplication.databinding.ItemBillinfoBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OrderDetailAdapter extends RecyclerView.Adapter {
    Context context;
    ArrayList<BillInfo> ds;

    public OrderDetailAdapter(Context context, ArrayList<BillInfo> ds) {
        this.context = context;
        this.ds = ds;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemBillinfoBinding.inflate(LayoutInflater.from(context),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        BillInfo billInfo = ds.get(position);
        FirebaseDatabase.getInstance().getReference("Products").child(billInfo.getProductId()).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Product tmp=snapshot.getValue(Product.class);
                        viewHolder.binding.txtName.setText(tmp.getProductName());
                        viewHolder.binding.txtPrice.setText(CurrencyFormatter.getFormatter().format(Double.valueOf(tmp.getProductPrice())* billInfo.getAmount())+"");
                        Glide.with(context)
                                .load(tmp.getProductImage1())
                                .placeholder(R.drawable.default_image)
                                .into(viewHolder.binding.imgFood);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }
        );
        viewHolder.binding.txtCount.setText("Count: "+ billInfo.getAmount()+"");
    }

    @Override
    public int getItemCount() {
        return ds == null ? 0 : ds.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemBillinfoBinding binding;
        public ViewHolder(@NonNull ItemBillinfoBinding tmp) {
            super(tmp.getRoot());
            binding=tmp;
        }
    }
}
