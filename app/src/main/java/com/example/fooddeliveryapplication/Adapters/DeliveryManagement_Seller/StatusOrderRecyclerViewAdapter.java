package com.example.fooddeliveryapplication.Adapters.DeliveryManagement_Seller;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fooddeliveryapplication.Activities.OrderSellerManagement.DetailOfOrderDeliveryManagementActivity;
import com.example.fooddeliveryapplication.CustomMessageBox.SuccessfulToast;
import com.example.fooddeliveryapplication.Helpers.FirebaseNotificationHelper;
import com.example.fooddeliveryapplication.Helpers.FirebaseStatusOrderHelper;
import com.example.fooddeliveryapplication.Model.Bill;
import com.example.fooddeliveryapplication.Model.Notification;
import com.example.fooddeliveryapplication.databinding.ItemOrderStatusListBinding;

import android.content.Context;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.List;

public class StatusOrderRecyclerViewAdapter extends RecyclerView.Adapter<StatusOrderRecyclerViewAdapter.ViewHolder> {
    Context mContext;
    List<Bill> billList;

    public StatusOrderRecyclerViewAdapter(Context mContext, List<Bill> billList) {
        this.mContext = mContext;
        this.billList = billList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new StatusOrderRecyclerViewAdapter.ViewHolder(ItemOrderStatusListBinding.inflate(LayoutInflater.from(mContext), parent, false));
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Bill bill = billList.get(position);
        holder.binding.txtOrderId.setText(bill.getBillId());
        holder.binding.txtStatus.setText(bill.getOrderStatus());
        holder.binding.txtDateOfOrder.setText(bill.getOrderDate());
        holder.binding.txtOrderTotal.setText(convertToMoney(bill.getTotalPrice()) + "đ");

        holder.binding.imgProductImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Glide.with(mContext)
                .asBitmap()
                .load(bill.getImageUrl())
                .into(holder.binding.imgProductImage);

        if (bill.getOrderStatus().equals("Confirm"))
        {
            holder.binding.btnChangeStatus.setText("Shipping");
            holder.binding.btnChangeStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new FirebaseStatusOrderHelper().setConfirmToShipping(bill.getBillId(), new FirebaseStatusOrderHelper.DataStatus() {
                        @Override
                        public void DataIsLoaded(List<Bill> bills, boolean isExistingBill) {

                        }

                        @Override
                        public void DataIsInserted() {

                        }

                        @Override
                        public void DataIsUpdated() {
                            new SuccessfulToast(mContext, "Order has been changed to shipping state!").showToast();
                            pushNotificationOrderStatusForReceiver(bill.getBillId()," đang giao hàng",bill.getRecipientId(), bill.getImageUrl());
                        }

                        @Override
                        public void DataIsDeleted() {

                        }
                    });
                }
            });
        }
        else if (bill.getOrderStatus().equals("Shipping"))
        {
            holder.binding.btnChangeStatus.setText("Completed");
            holder.binding.btnChangeStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new FirebaseStatusOrderHelper().setShippingToCompleted(bill.getBillId(), new FirebaseStatusOrderHelper.DataStatus() {
                        @Override
                        public void DataIsLoaded(List<Bill> bills, boolean isExistingBill) {

                        }

                        @Override
                        public void DataIsInserted() {

                        }

                        @Override
                        public void DataIsUpdated() {
                            new SuccessfulToast(mContext, "Order has been changed to completed state!").showToast();
                            pushNotificationOrderStatusForReceiver(bill.getBillId()," giao hàng thành công",bill.getRecipientId(), bill.getImageUrl());
                        }

                        @Override
                        public void DataIsDeleted() {

                        }
                    });
                }
            });
        }
        else {
            holder.binding.txtStatus.setTextColor(Color.parseColor("#48DC7D"));
            holder.binding.btnChangeStatus.setVisibility(View.GONE);
        }

        // view detail
        holder.binding.parentOfItemCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DetailOfOrderDeliveryManagementActivity.class);
                intent.putExtra("billId",bill.getBillId());
                intent.putExtra("addressId",bill.getAddressId());
                intent.putExtra("recipientId",bill.getRecipientId());
                intent.putExtra("totalBill",bill.getTotalPrice());
                intent.putExtra("orderStatus",bill.getOrderStatus());
                mContext.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return billList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final ItemOrderStatusListBinding binding;

        public ViewHolder(@NonNull ItemOrderStatusListBinding binding) {
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

    public void pushNotificationOrderStatusForReceiver(String billId,String status,String receiverId,String productImage1) {
        String title = "Order status";
        String content = "Order "+ billId + " has been updated to "+ status+ ", go to My Order to check it.";
        Notification notification = FirebaseNotificationHelper.createNotification(title,content,productImage1,"None",billId,"None", null);
        new FirebaseNotificationHelper(mContext).addNotification(receiverId, notification, new FirebaseNotificationHelper.DataStatus() {
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

}