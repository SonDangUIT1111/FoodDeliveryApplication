package com.example.fooddeliveryapplication.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fooddeliveryapplication.Model.Bill;
import com.example.fooddeliveryapplication.R;
import android.content.Context;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class StatusOrderRecyclerViewAdapter extends RecyclerView.Adapter<StatusOrderRecyclerViewAdapter.ViewHolder> {
    Context mContext;
    List<Bill> billList;
    List<String> listImage;

    public StatusOrderRecyclerViewAdapter(Context mContext, List<Bill> billList,List<String> imgUrl) {
        this.mContext = mContext;
        this.billList = billList;
        this.listImage = imgUrl;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.order_status_list_item,parent,false);
        return new StatusOrderRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Bill bill = billList.get(position);
        holder.txtOrderId.setText(bill.getBillId());
        holder.txtStatus.setText(bill.getOrderStatus());
        holder.txtDateOfOrder.setText(bill.getOrderDate());
        holder.txtOrderTotal.setText(convertToVND(bill.getTotalPrice()) + " VND");

        holder.imgProductImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Glide.with(mContext)
                .asBitmap()
                .load(listImage.get(position))
                .into(holder.imgProductImage);

        if (bill.getOrderStatus().equals("Confirm"))
        {
            holder.btnChangeStatus.setText("Shipping");
            //todo logic for btn
        }
        else if (bill.getOrderStatus().equals("Shipping"))
        {
            holder.btnChangeStatus.setText("Completed");
            //todo logic for btn
        }
        else {
            holder.btnChangeStatus.setVisibility(View.GONE);
        }
    }


    @Override
    public int getItemCount() {
        return billList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView imgProductImage;
        public TextView txtOrderId;
        public TextView txtStatus;
        public TextView txtDateOfOrder;
        public TextView txtOrderTotal;
        public Button btnChangeStatus;
        public String key;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProductImage = itemView.findViewById(R.id.imgProductImage);
            txtOrderId = itemView.findViewById(R.id.txtOrderId);
            txtStatus = itemView.findViewById(R.id.txtStatus);
            txtDateOfOrder = itemView.findViewById(R.id.txtDateOfOrder);
            txtOrderTotal = itemView.findViewById(R.id.txtOrderTotal);
            btnChangeStatus = itemView.findViewById(R.id.btnChangeStatus);
        }
    }

    public String convertToVND(int value)
    {
        NumberFormat nfi = NumberFormat.getInstance(new Locale("vn","VN"));
        String price = nfi.format(value);
        return price;
    }

}
