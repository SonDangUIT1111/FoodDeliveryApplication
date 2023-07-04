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
import com.example.fooddeliveryapplication.Helpers.FirebaseNotificationHelper;
import com.example.fooddeliveryapplication.Helpers.FirebaseStatusOrderHelper;
import com.example.fooddeliveryapplication.Model.Bill;
import com.example.fooddeliveryapplication.Model.Notification;
import com.example.fooddeliveryapplication.databinding.ItemOrderStatusListBinding;

import android.content.Context;
import android.widget.ImageView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class StatusOrderRecyclerViewAdapter extends RecyclerView.Adapter<StatusOrderRecyclerViewAdapter.ViewHolder> {
    private Context mContext;
    private List<Bill> billList;
    private List<String> listImage;

    public StatusOrderRecyclerViewAdapter(Context mContext, List<Bill> billList,List<String> imgUrl) {
        this.mContext = mContext;
        this.billList = billList;
        this.listImage = imgUrl;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new StatusOrderRecyclerViewAdapter.ViewHolder(ItemOrderStatusListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Bill bill = billList.get(position);
        holder.binding.txtOrderId.setText(bill.getBillId());
        holder.binding.txtStatus.setText(bill.getOrderStatus());
        holder.binding.txtDateOfOrder.setText(bill.getOrderDate());
        holder.binding.txtOrderTotal.setText(convertToVND(bill.getTotalPrice()) + "đ");

        holder.binding.imgProductImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Glide.with(mContext)
                .asBitmap()
                .load(listImage.get(position))
                .into(holder.binding.imgProductImage);

        holder.binding.btnChangeStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bill.getOrderStatus().equals("Confirm")) {
                    holder.binding.btnChangeStatus.setText("Shipping");
                    new FirebaseStatusOrderHelper().setConfirmToShipping(bill.getBillId(), new FirebaseStatusOrderHelper.DataStatus() {
                        @Override
                        public void DataIsLoaded(List<Bill> bills, List<String> img) {

                        }

                        @Override
                        public void DataIsInserted() {

                        }

                        @Override
                        public void DataIsUpdated() {
                            Toast.makeText(mContext, "Đơn hàng đã chuyển sang trạng thái đang giao hàng", Toast.LENGTH_SHORT).show();
                            pushNotificationOrderStatusForReceiver(bill.getBillId()," đang giao hàng",bill.getRecipientId(),listImage.get(position));
                        }

                        @Override
                        public void DataIsDeleted() {

                        }
                    });
                }
                else if (bill.getOrderStatus().equals("Shipping")) {
                    holder.binding.btnChangeStatus.setText("Completed");
                    holder.binding.txtStatus.setTextColor(Color.parseColor("#48DC7D"));
                    holder.binding.btnChangeStatus.setVisibility(View.GONE);
                    new FirebaseStatusOrderHelper().setShippingToCompleted(bill.getBillId(), new FirebaseStatusOrderHelper.DataStatus() {
                        @Override
                        public void DataIsLoaded(List<Bill> bills, List<String> img) {

                        }

                        @Override
                        public void DataIsInserted() {

                        }

                        @Override
                        public void DataIsUpdated() {
                            Toast.makeText(mContext, "Đơn hàng đã chuyển sang trạng thái hoàn thành", Toast.LENGTH_SHORT).show();
                            pushNotificationOrderStatusForReceiver(bill.getBillId()," giao hàng thành công",bill.getRecipientId(),listImage.get(position));
                        }

                        @Override
                        public void DataIsDeleted() {

                        }
                    });
                }
            }
        });
        
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
        return billList == null ? 0 : billList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final ItemOrderStatusListBinding binding;

        public ViewHolder(ItemOrderStatusListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public String convertToVND(int value)
    {
        NumberFormat nfi = NumberFormat.getInstance(new Locale("vn","VN"));
        String price = nfi.format(value);
        return price;
    }

    public void pushNotificationOrderStatusForReceiver(String billId,String status,String receiverId,String productImage1)
    {
        String title = "Tình trạng đơn hàng";
        String content = "Đơn hàng "+ billId+" đã chuyển sang trạng thái "+ status+", vào My Order để xem tình trạng đơn hàng nào";
        Notification notification = FirebaseNotificationHelper.createNotification(title,content,productImage1,"None",billId,"None");
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
