package com.example.fooddeliveryapplication.Activities.Order;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fooddeliveryapplication.Activities.Feedback.FeedBackActivity;
import com.example.fooddeliveryapplication.Activities.Home.ChatDetailActivity;
import com.example.fooddeliveryapplication.Adapters.OrderAdapter.OrderDetailAdapter;
import com.example.fooddeliveryapplication.Dialog.LoadingDialog;
import com.example.fooddeliveryapplication.Helpers.FirebaseNotificationHelper;
import com.example.fooddeliveryapplication.Model.Bill;
import com.example.fooddeliveryapplication.Model.BillInfo;
import com.example.fooddeliveryapplication.Model.Notification;
import com.example.fooddeliveryapplication.R;
import com.example.fooddeliveryapplication.databinding.ActivityOrderDetailBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class OrderDetailActivity extends AppCompatActivity {
    private ActivityOrderDetailBinding binding;

    private ArrayList<BillInfo> dsBillInfo=new ArrayList<>();
    private Bill currentBill;
    private LoadingDialog loadingDialog;
    private String userId;
    private Notification notification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityOrderDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getWindow().setStatusBarColor(Color.parseColor("#E8584D"));
        getWindow().setNavigationBarColor(Color.parseColor("#E8584D"));

        //Lấy Intent
        Intent intent=getIntent();
        //Khởi tạo dữ liệu
        currentBill= (Bill) intent.getSerializableExtra("Bill");
        userId = intent.getStringExtra("userId");
        loadingDialog=new LoadingDialog(this);
        loadingDialog.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        dsBillInfo.clear();
       FirebaseDatabase.getInstance().getReference("Bills").child(currentBill.getBillId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    currentBill=snapshot.getValue(Bill.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        initData();

    }

    private void initData() {
        FirebaseDatabase.getInstance().getReference("BillInfos").child(currentBill.getBillId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot item:snapshot.getChildren()) {
                    BillInfo tmp=item.getValue(BillInfo.class);
                    dsBillInfo.add(tmp);
                }
                //Cập nhật giao diện sau khi đã có dữ liệu
                initUI();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void initUI() {
        String status=currentBill.getOrderStatus();
        if (status.equalsIgnoreCase("Completed")) {
            binding.lnOderDetail.btn.setVisibility(View.VISIBLE);
            binding.imgStatus.setImageResource(R.drawable.line_status_completed);
        } else if (status.equalsIgnoreCase("Shipping")) {
            binding.imgStatus.setImageResource(R.drawable.line_status_shipping);
        } else {
            binding.imgStatus.setImageResource(R.drawable.line_status_confirmed);
        }
        OrderDetailAdapter adapter=new OrderDetailAdapter(this,dsBillInfo);
        binding.lnOderDetail.ryc.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL,false));
        binding.lnOderDetail.ryc.setAdapter(adapter);
        binding.lnOderDetail.ryc.setHasFixedSize(true);
        binding.lnOderDetail.txtTotalPrice.setText(convertToMoney(currentBill.getTotalPrice())+ "đ");
        binding.txtId.setText(currentBill.getBillId());
        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        loadingDialog.dismiss();
       //Nếu tất cả billInfo đã được feedback thì sẽ không cho người dùng feedback nữa
       if (currentBill.isCheckAllComment()) {
           binding.lnOderDetail.btn.setEnabled(false);
           binding.lnOderDetail.btn.setBackgroundResource(R.drawable.background_feedback_disnable_button);
       }
       else {
           binding.lnOderDetail.btn.setEnabled(true);
           binding.lnOderDetail.btn.setBackgroundResource(R.drawable.background_feedback_enable_button);
       }
       //Set sự kiện nút chuyển qua feedback cho product
       binding.lnOderDetail.btn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               filterItemChecked();
               Intent intent=new Intent(OrderDetailActivity.this,FeedBackActivity.class);
               intent.putExtra("Current Bill",currentBill);
               intent.putExtra("List of billInfo",dsBillInfo);
               intent.putExtra("userId",userId);
               startActivity(intent);
           }
       });
    }
    private void filterItemChecked() {
        Iterator<BillInfo> iterator=dsBillInfo.iterator();
        while (iterator.hasNext()) {
            BillInfo billInfo =iterator.next();
            if (billInfo.isCheck())
                iterator.remove();
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
}