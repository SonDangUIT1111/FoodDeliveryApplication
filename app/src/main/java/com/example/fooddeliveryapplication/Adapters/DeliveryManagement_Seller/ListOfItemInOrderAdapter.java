package com.example.fooddeliveryapplication.Adapters.DeliveryManagement_Seller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fooddeliveryapplication.Helpers.FirebaseOrderDetailHelper;
import com.example.fooddeliveryapplication.Model.BillInfo;
import com.example.fooddeliveryapplication.Model.Product;
import com.example.fooddeliveryapplication.R;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class ListOfItemInOrderAdapter extends RecyclerView.Adapter<ListOfItemInOrderAdapter.ViewHolder> {
    private Context mContext;
    private List<BillInfo> billInfos;
    private List<String> mKeys;

    public ListOfItemInOrderAdapter(Context mContext, List<BillInfo> billInfos) {
        this.mContext = mContext;
        this.billInfos = billInfos;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.order_detail_list_item,parent,false);
        return new ListOfItemInOrderAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListOfItemInOrderAdapter.ViewHolder holder, int position) {
        BillInfo billInfo = billInfos.get(position);
        new FirebaseOrderDetailHelper().readProductInfo(billInfo.getProductId(), new FirebaseOrderDetailHelper.DataStatus2() {
            @Override
            public void DataIsLoaded(Product product) {
                holder.txtProductNameInDetail.setText(product.getProductName());
                holder.txtPriceOfItemInDetail.setText(convertToVND(product.getProductPrice())+" đ");
                holder.txtCountInDetail.setText("Count: "+String.valueOf(billInfo.getAmount()));
                holder.imgProductImageInDetail.setScaleType(ImageView.ScaleType.CENTER_CROP);

                try
                {
                    Glide.with(mContext)
                            .asBitmap()
                            .load(product.getProductImage1())
                            .placeholder(R.drawable.background_loading_layout)
                            .into(holder.imgProductImageInDetail);
                }catch (Exception ex)
                {

                }
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
    public int getItemCount() {
        return billInfos.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView txtProductNameInDetail;
        TextView txtCountInDetail;
        TextView txtPriceOfItemInDetail;
        ImageView imgProductImageInDetail;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtProductNameInDetail = itemView.findViewById(R.id.txtProductNameInDetail);
            txtCountInDetail = itemView.findViewById(R.id.txtCountInDetail);
            txtPriceOfItemInDetail = itemView.findViewById(R.id.txtPriceOfItemInDetail);
            imgProductImageInDetail = itemView.findViewById(R.id.imgProductImageInDetail);
        }
    }
    public String convertToVND(int value)
    {
        NumberFormat nfi = NumberFormat.getInstance(new Locale("vn","VN"));
        String price = nfi.format(value);
        return price;
    }
}
