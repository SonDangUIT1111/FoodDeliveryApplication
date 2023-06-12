package com.example.fooddeliveryapplication.Adapters.OrderAdapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fooddeliveryapplication.Activities.Order.OrderActivity;
import com.example.fooddeliveryapplication.Activities.Order.OrderDetailActivity;
import com.example.fooddeliveryapplication.Model.BillInfo;
import com.example.fooddeliveryapplication.Model.CurrencyFormatter;
import com.example.fooddeliveryapplication.Model.Bill;
import com.example.fooddeliveryapplication.R;
import com.example.fooddeliveryapplication.databinding.ItemOrderLayoutBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OrderAdapter extends RecyclerView.Adapter {


    Context context;
    ArrayList<Bill> dsOrder;
    int type;

    String userId;
    public OrderAdapter(Context context, ArrayList<Bill> dsOrder, int type, String id) {
        this.context = context;
        this.dsOrder = dsOrder;
        this.type = type;
        this.userId = id;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemOrderLayoutBinding.inflate(LayoutInflater.from(context),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Bill tmp=dsOrder.get(position);
        ViewHolder viewHolder=(ViewHolder) holder;
        if (type== OrderActivity.CURRENT_ORDER) {
            viewHolder.binding.btnSee.setText("Received");
            viewHolder.binding.btnSee.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder=new AlertDialog.Builder(context);
                    builder.setTitle("Thông báo");
                    builder.setMessage("Bạn có chắc muốn xác nhận đơn hàng?");
                    builder.setIcon(R.drawable.icon_alert);
                    builder.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            FirebaseDatabase.getInstance().getReference("Bills").child(tmp.getBillId()).child("orderStatus").setValue("Completed");
                        }
                    });
                    builder.create().show();
                }
            });

        }
        else {
            viewHolder.binding.txtStatus.setTextColor(Color.parseColor("#00DEC1"));
            viewHolder.binding.btnSee.setText("Feedback & Rate");
            viewHolder.binding.btnSee.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(context, OrderDetailActivity.class);
                    intent.putExtra("Bill",tmp);
                    intent.putExtra("userId",userId);
                    context.startActivity(intent);
                }
            });
            if (tmp.isCheckAllComment()) {
                viewHolder.binding.btnSee.setEnabled(false);
                viewHolder.binding.btnSee.setBackgroundResource(R.drawable.background_feedback_disnable_button);
            }
        }

        viewHolder.binding.txtId.setText(tmp.getBillId()+"");
        viewHolder.binding.txtDate.setText(tmp.getOrderDate()+"");
        viewHolder.binding.txtStatus.setText(tmp.getOrderStatus());
        viewHolder.binding.txtTotal.setText(CurrencyFormatter.getFormatter().format(Double.valueOf(tmp.getTotalPrice()))+"");
        viewHolder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(context, OrderDetailActivity.class);
                    intent.putExtra("Bill",tmp);
                    intent.putExtra("userId",userId);
                    context.startActivity(intent);
                }
            });
        FirebaseDatabase.getInstance().getReference("BillInfos").child(tmp.getBillId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Tạo một BillInfos để lấy ảnh
                BillInfo tmp= new BillInfo();
                for (DataSnapshot item: snapshot.getChildren()) {
                    tmp=item.getValue(BillInfo.class);
                    break;
                }
                FirebaseDatabase.getInstance().getReference("Products").child(tmp.getProductId()).child("productImage1").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Glide.with(context)
                                .load(snapshot.getValue(String.class))
                                .placeholder(R.drawable.default_image)
                                .into(viewHolder.binding.imgFood);
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

    @Override
    public int getItemCount() {
        if (dsOrder.isEmpty()) {
            return 0;
        }
        return dsOrder.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemOrderLayoutBinding binding;
        public ViewHolder(@NonNull ItemOrderLayoutBinding tmp) {
            super(tmp.getRoot());
            binding=tmp;
        }
    }


}
