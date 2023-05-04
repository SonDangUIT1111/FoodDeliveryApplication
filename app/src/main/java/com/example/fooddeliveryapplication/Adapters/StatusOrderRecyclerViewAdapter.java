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

import java.util.List;

public class StatusOrderRecyclerViewAdapter extends RecyclerView.Adapter<StatusOrderRecyclerViewAdapter.ViewHolder> {
    Context mContext;
    List<Bill> billList;
    private List<String> mKeys;

    public StatusOrderRecyclerViewAdapter(Context mContext, List<Bill> billList, List<String> mKeys) {
        this.mContext = mContext;
        this.billList = billList;
        this.mKeys = mKeys;
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
        holder.txtOrderTotal.setText(String.valueOf(bill.getTotalPrice()));


        holder.imgProductImage = new ImageView(mContext);
        holder.imgProductImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        if (bill.getBillInfos().size() > 0)
        {
            Glide.with(holder.imgProductImage.getContext())
                    .asBitmap()
                    .load(bill.billInfos.get(0).productImage)
                    .into(holder.imgProductImage);
        }

        if (bill.orderStatus.equals("Confirm"))
        {
            holder.btnChangeStatus.setText("Shipping");
            //todo logic for btn
        }
        else if (bill.orderStatus.equals("Shipping"))
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

}
