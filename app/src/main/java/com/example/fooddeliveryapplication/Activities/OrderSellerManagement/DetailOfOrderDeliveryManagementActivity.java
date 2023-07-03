package com.example.fooddeliveryapplication.Activities.OrderSellerManagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.example.fooddeliveryapplication.Adapters.DeliveryManagement_Seller.ListOfItemInOrderAdapter;
import com.example.fooddeliveryapplication.Helpers.FirebaseOrderDetailHelper;
import com.example.fooddeliveryapplication.Model.BillInfo;
import com.example.fooddeliveryapplication.databinding.ActivityDetailOfOrderDeliveryManagementBinding;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class DetailOfOrderDeliveryManagementActivity extends AppCompatActivity {
    private ActivityDetailOfOrderDeliveryManagementBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailOfOrderDeliveryManagementBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getWindow().setStatusBarColor(Color.parseColor("#E8584D"));
        getWindow().setNavigationBarColor(Color.parseColor("#E8584D"));

        Intent intent = getIntent();
        if (null != intent)
        {
            String billId = intent.getStringExtra("billId");
            String addressId = intent.getStringExtra("addressId");
            String recipientId = intent.getStringExtra("recipientId");
            String orderStatus = intent.getStringExtra("orderStatus");
            int price = intent.getIntExtra("totalBill",-1);
            try {
                binding.txtOrderIdDetail.setText("Order Id: "+billId);
                binding.txtBillTotalInDetail.setText(convertToVND(price)+ "đ");
                binding.txtStatusOrderDetail.setText(orderStatus);
                if (orderStatus.equals("Completed"))
                {
                    binding.txtStatusOrderDetail.setTextColor(Color.parseColor("#48DC7D"));
                }
                new FirebaseOrderDetailHelper().readOrderDetail(addressId, recipientId, billId, new FirebaseOrderDetailHelper.DataStatus() {
                    @Override
                    public void DataIsLoaded(String addresss, List<BillInfo> billInfos) {
                        binding.txtAddressDetail.setText(addresss);
                        ListOfItemInOrderAdapter adapter = new ListOfItemInOrderAdapter(DetailOfOrderDeliveryManagementActivity.this,billInfos);
                        binding.recOrderDetail.setLayoutManager(new LinearLayoutManager(DetailOfOrderDeliveryManagementActivity.this));
                        binding.recOrderDetail.setHasFixedSize(true);
                        binding.recOrderDetail.setAdapter(adapter);
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
            catch (Exception ex)
            {

            }
        }
        
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                return;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    public String convertToVND(int value)
    {
        NumberFormat nfi = NumberFormat.getInstance(new Locale("vn","VN"));
        String price = nfi.format(value);
        return price;
    }
}