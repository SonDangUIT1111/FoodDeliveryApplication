package com.example.fooddeliveryapplication.Helpers;

import androidx.annotation.NonNull;

import com.example.fooddeliveryapplication.Model.Bill;
import com.example.fooddeliveryapplication.Model.BillInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FirebaseStatusOrderHelper {
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReferenceStatusOrder;
    List<Bill> bills = new ArrayList<>();

    public interface DataStatus{
        void DataIsLoaded(List<Bill> bills, List<String> keys);
        void DataIsInserted();
        void DataIsUpdated();
        void DataIsDeleted();

    }

    public FirebaseStatusOrderHelper() {
        mDatabase = FirebaseDatabase.getInstance();
        mReferenceStatusOrder = mDatabase.getReference("/Bills");
    }

    public void readConfirmBills(String userId, final FirebaseStatusOrderHelper.DataStatus dataStatus)
    {
        mReferenceStatusOrder.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bills.clear();
                List<String> keys = new ArrayList<>();
                for (DataSnapshot keyNode : snapshot.getChildren())
                {
                    if (keyNode.child("senderId").getValue(String.class).equals(userId)
                    &&  keyNode.child("orderStatus").getValue(String.class).equals("Confirm"))
                    {
                        DataSnapshot snapshotList = keyNode.child("billInfos");
                        List<BillInfo> billInfos = new ArrayList<>();
                        for (DataSnapshot snapChild : snapshotList.getChildren())
                        {
                            billInfos.add(snapChild.getValue(BillInfo.class));
                        }
                        Bill bill = new Bill();
//                        bill.orderStatus = "Confirm";
//                        bill.billInfos = billInfos;
//                        bill.billId = keyNode.child("billId").getValue(String.class);
//                        bill.orderDate = keyNode.child("orderDate").getValue(String.class);
//                        bill.totalPrice = keyNode.child("totalPrice").getValue(int.class);
                        bills.add(bill);
                        keys.add(keyNode.getKey());
                    }
                }
                dataStatus.DataIsLoaded(bills,keys);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
