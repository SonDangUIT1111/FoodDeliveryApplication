package com.example.fooddeliveryapplication.Adapters.OrderAdapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fooddeliveryapplication.Activities.Order.OrderActivity;
import com.example.fooddeliveryapplication.Activities.Order.OrderDetailActivity;
import com.example.fooddeliveryapplication.CustomMessageBox.CustomAlertDialog;
import com.example.fooddeliveryapplication.CustomMessageBox.SuccessfulToast;
import com.example.fooddeliveryapplication.Helpers.FirebaseStatusOrderHelper;
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
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter {
    private Context context;
    private ArrayList<Bill> dsOrder;
    private int type;
    private String userId;

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
        Bill tmp = dsOrder.get(position);
        ViewHolder viewHolder = (ViewHolder) holder;
        if (type == OrderActivity.CURRENT_ORDER) {
            viewHolder.binding.btnSee.setText("Received");
            viewHolder.binding.btnSee.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new CustomAlertDialog(context,"Do you want to confirm this order?");
                    CustomAlertDialog.binding.btnYes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            new FirebaseStatusOrderHelper().setShippingToCompleted(tmp.getBillId(), new FirebaseStatusOrderHelper.DataStatus() {
                                @Override
                                public void DataIsLoaded(List<Bill> bills, boolean isExistingBill) {

                                }

                                @Override
                                public void DataIsInserted() {

                                }

                                @Override
                                public void DataIsUpdated() {
                                    new SuccessfulToast(context, "Your order has been changed to completed state!").showToast();
                                }

                                @Override
                                public void DataIsDeleted() {

                                }
                            });
                            CustomAlertDialog.alertDialog.dismiss();
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

        }
        else {
            viewHolder.binding.txtStatus.setTextColor(Color.parseColor("#48DC7D"));
            viewHolder.binding.btnSee.setText("Feedback & Rate");
            if (tmp.isCheckAllComment()) {
                viewHolder.binding.btnSee.setEnabled(false);
                viewHolder.binding.btnSee.setBackgroundResource(R.drawable.background_feedback_disnable_button);
            }
            else {
                viewHolder.binding.btnSee.setEnabled(true);
                viewHolder.binding.btnSee.setBackgroundResource(R.drawable.background_feedback_enable_button);
            }
            viewHolder.binding.btnSee.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(context, OrderDetailActivity.class);
                    intent.putExtra("Bill",tmp);
                    intent.putExtra("userId",userId);
                    context.startActivity(intent);
                }
            });
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
        return dsOrder == null ? 0 : dsOrder.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemOrderLayoutBinding binding;

        public ViewHolder(@NonNull ItemOrderLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
