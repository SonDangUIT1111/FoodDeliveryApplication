package com.example.fooddeliveryapplication.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.fooddeliveryapplication.R;

import java.text.NumberFormat;
import java.util.Locale;

public class DetailOfOrderDeliveryManagementActivity extends AppCompatActivity {

    TextView txtOrderIdDetail;
    TextView txtAddressDetail;
    TextView txtBillTotalInDetail;
    RecyclerView recOrderDetail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_of_order_delivery_management);


    }
    public String convertToVND(int value)
    {
        NumberFormat nfi = NumberFormat.getInstance(new Locale("vn","VN"));
        String price = nfi.format(value);
        return price;
    }
}