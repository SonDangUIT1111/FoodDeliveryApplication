package com.example.fooddeliveryapplication.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fooddeliveryapplication.Adapters.ListOfItemInOrderAdapter;
import com.example.fooddeliveryapplication.Helpers.FirebaseOrderDetailHelper;
import com.example.fooddeliveryapplication.Model.BillInfo;
import com.example.fooddeliveryapplication.R;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class DetailOfOrderDeliveryManagementActivity extends AppCompatActivity {

    TextView txtOrderIdDetail;
    TextView txtAddressDetail;
    TextView txtBillTotalInDetail;
    TextView txtStatusOrderDetail;
    RecyclerView recOrderDetail;
    ImageButton btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_of_order_delivery_management);
        //find view by id
        txtOrderIdDetail = (TextView) findViewById(R.id.txtOrderIdDetail);
        txtAddressDetail = (TextView) findViewById(R.id.txtAddressDetail);
        txtBillTotalInDetail = (TextView) findViewById(R.id.txtBillTotalInDetail);
        txtStatusOrderDetail = (TextView) findViewById(R.id.txtStatusOrderDetail);
        recOrderDetail = (RecyclerView) findViewById(R.id.recOrderDetail);
        btnBack = (ImageButton) findViewById(R.id.btnBack);

        Intent intent = getIntent();
        if (null != intent)
        {
            String billId = intent.getStringExtra("billId");
            String addressId = intent.getStringExtra("addressId");
            String recipientId = intent.getStringExtra("recipientId");
            String orderStatus = intent.getStringExtra("orderStatus");
            int price = intent.getIntExtra("totalBill",-1);
            try {
                txtOrderIdDetail.setText("Order Id: "+billId);
                txtBillTotalInDetail.setText(convertToVND(price)+ "VND");
                txtStatusOrderDetail.setText(orderStatus);
                if (orderStatus.equals("Completed"))
                {
                    txtStatusOrderDetail.setTextColor(Color.parseColor("#48DC7D"));
                }
                new FirebaseOrderDetailHelper().readOrderDetail(addressId, recipientId, billId, new FirebaseOrderDetailHelper.DataStatus() {
                    @Override
                    public void DataIsLoaded(String addresss, List<BillInfo> billInfos) {
                        txtAddressDetail.setText(addresss);
                        ListOfItemInOrderAdapter adapter = new ListOfItemInOrderAdapter(DetailOfOrderDeliveryManagementActivity.this,billInfos);
                        recOrderDetail.setLayoutManager(new LinearLayoutManager(DetailOfOrderDeliveryManagementActivity.this));
                        recOrderDetail.setHasFixedSize(true);
                        recOrderDetail.setAdapter(adapter);
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
        
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                return;
            }
        });

    }
    public String convertToVND(int value)
    {
        NumberFormat nfi = NumberFormat.getInstance(new Locale("vn","VN"));
        String price = nfi.format(value);
        return price;
    }
}