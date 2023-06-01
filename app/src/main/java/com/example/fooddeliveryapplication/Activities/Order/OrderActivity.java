package com.example.fooddeliveryapplication.Activities.Order;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fooddeliveryapplication.Adapters.OrderAdapter.OrderViewPaperAdapter;
import com.example.fooddeliveryapplication.Dialog.LoadingDialog;
import com.example.fooddeliveryapplication.Model.Bill;
import com.example.fooddeliveryapplication.databinding.ActivityOrderBinding;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OrderActivity extends AppCompatActivity {
    String idCurrentUser;
    ActivityOrderBinding binding;
    public static final int CURRENT_ORDER=10001;
    public static final int HISTORY_ORDER=10002;
    ArrayList <Bill> dsCurrentOrder=new ArrayList<>();
    ArrayList <Bill> dsHistoryOrder=new ArrayList<>();
    private LoadingDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        dialog=new LoadingDialog(this);
        dialog.show();
        initData();
    }

    private void initUI() {
        OrderViewPaperAdapter viewPaperAdapter=new OrderViewPaperAdapter(OrderActivity.this,dsCurrentOrder,dsHistoryOrder);
        binding.viewPaper2.setAdapter(viewPaperAdapter);
        binding.viewPaper2.setUserInputEnabled(false);
        new TabLayoutMediator(binding.tabLayout,binding.viewPaper2, ((tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Current Order");
                    break;
                case 1:
                    tab.setText("History Order");
                    break;
            }
        })).attach();
        dialog.dismiss();
    }

    private void initData() {
        FirebaseDatabase.getInstance().getReference("Bills").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dsCurrentOrder.clear();
                dsHistoryOrder.clear();
                for (DataSnapshot item:snapshot.getChildren()) {
                    Bill tmp=item.getValue(Bill.class);
//                    if (tmp.getRecipientId().equalsIgnoreCase(LoginActivity.getCurrentUser().getUserId()))
//                    Dòng dưới là test sản phẩm
                        if (!tmp.getOrderStatus().equalsIgnoreCase("Completed")) {
                            dsCurrentOrder.add(tmp);
                        } else
                            dsHistoryOrder.add(tmp);

                }
                initUI();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding=null;
    }
}