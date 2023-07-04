package com.example.fooddeliveryapplication.Adapters.DeliveryManagement_Seller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fooddeliveryapplication.Helpers.FirebaseOrderDetailHelper;
import com.example.fooddeliveryapplication.Model.BillInfo;
import com.example.fooddeliveryapplication.Model.Product;
import com.example.fooddeliveryapplication.R;
import com.example.fooddeliveryapplication.databinding.ItemOrderDetailListBinding;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class ListOfItemInOrderAdapter extends RecyclerView.Adapter<ListOfItemInOrderAdapter.ViewHolder> {
    private Context mContext;
    private List<BillInfo> billInfos;

    public ListOfItemInOrderAdapter(Context mContext, List<BillInfo> billInfos) {
        this.mContext = mContext;
        this.billInfos = billInfos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ListOfItemInOrderAdapter.ViewHolder(ItemOrderDetailListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ListOfItemInOrderAdapter.ViewHolder holder, int position) {
        BillInfo billInfo = billInfos.get(position);

        new FirebaseOrderDetailHelper().readProductInfo(billInfo.getProductId(), new FirebaseOrderDetailHelper.DataStatus2() {
            @Override
            public void DataIsLoaded(Product product) {
                holder.binding.txtProductNameInDetail.setText(product.getProductName());
                holder.binding.txtPriceOfItemInDetail.setText(convertToVND(product.getProductPrice())+" đ");
                holder.binding.txtCountInDetail.setText("Count: "+String.valueOf(billInfo.getAmount()));
                holder.binding.imgProductImageInDetail.setScaleType(ImageView.ScaleType.CENTER_CROP);

                try
                {
                    Glide.with(mContext)
                            .asBitmap()
                            .load(product.getProductImage1())
                            .placeholder(R.drawable.background_loading_layout)
                            .into(holder.binding.imgProductImageInDetail);
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
        return billInfos == null ? 0 : billInfos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemOrderDetailListBinding binding;

        public ViewHolder(ItemOrderDetailListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
    public String convertToVND(int value) {
        NumberFormat nfi = NumberFormat.getInstance(new Locale("vn","VN"));
        String price = nfi.format(value);
        return price;
    }
}
