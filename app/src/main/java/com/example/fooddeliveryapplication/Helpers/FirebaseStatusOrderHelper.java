package com.example.fooddeliveryapplication.Helpers;

import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.fooddeliveryapplication.Model.Bill;
import com.example.fooddeliveryapplication.Model.BillInfo;
import com.google.android.gms.tasks.OnSuccessListener;
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
    private List<Bill> bills = new ArrayList<>();
    private String userId;
    private List<BillInfo> billInfoList = new ArrayList<>();
    private List<Integer> soldValueList = new ArrayList<>();

    public interface DataStatus{
        void DataIsLoaded(List<Bill> bills, boolean isExistingBill);
        void DataIsInserted();
        void DataIsUpdated();
        void DataIsDeleted();

    }

    public FirebaseStatusOrderHelper(String user) {
        userId = user;
        mDatabase = FirebaseDatabase.getInstance();
        mReferenceStatusOrder = mDatabase.getReference();
    }

    public FirebaseStatusOrderHelper() {
        mDatabase = FirebaseDatabase.getInstance();
        mReferenceStatusOrder = mDatabase.getReference();
    }

    public void readConfirmBills(String userId, final FirebaseStatusOrderHelper.DataStatus dataStatus)
    {
        mReferenceStatusOrder.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bills.clear();
                boolean isExistingBill = false;
                for (DataSnapshot keyNode : snapshot.child("Bills").getChildren()) {
                    if (keyNode.child("senderId").getValue(String.class).equals(userId)
                    &&  keyNode.child("orderStatus").getValue(String.class).equals("Confirm")) {
                        Bill bill = keyNode.getValue(Bill.class);
                        bills.add(bill);
                        isExistingBill = true;
                    }
                }

                if (dataStatus != null) {
                    dataStatus.DataIsLoaded(bills, isExistingBill);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void readShippingBills(String userId, final FirebaseStatusOrderHelper.DataStatus dataStatus)
    {
        mReferenceStatusOrder.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bills.clear();
                boolean isExistingShippintBill = false;
                for (DataSnapshot keyNode : snapshot.child("Bills").getChildren())
                {
                    if (keyNode.child("senderId").getValue(String.class).equals(userId)
                            &&  keyNode.child("orderStatus").getValue(String.class).equals("Shipping"))
                    {
                        Bill bill = keyNode.getValue(Bill.class);
                        bills.add(bill);
                        isExistingShippintBill = true;
                    }
                }

                if (dataStatus != null) {
                    dataStatus.DataIsLoaded(bills, isExistingShippintBill);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void readCompletedBills(String userId,final FirebaseStatusOrderHelper.DataStatus dataStatus) {
        mReferenceStatusOrder.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bills.clear();
                boolean isExistingBill = false;
                for (DataSnapshot keyNode : snapshot.child("Bills").getChildren()) {
                    if (keyNode.child("senderId").getValue(String.class).equals(userId)
                            && keyNode.child("orderStatus").getValue(String.class).equals("Completed")) {
                        Bill bill = keyNode.getValue(Bill.class);
                        bills.add(bill);
                        isExistingBill = true;
                    }
                }

                if (dataStatus != null) {
                    dataStatus.DataIsLoaded(bills, isExistingBill);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void setConfirmToShipping(String billId,final FirebaseStatusOrderHelper.DataStatus dataStatus) {
        mReferenceStatusOrder.child("Bills").child(billId).child("orderStatus").setValue("Shipping")
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        if (dataStatus != null) {
                            dataStatus.DataIsUpdated();
                        }
                    }
                });
    }
    public void setShippingToCompleted(String billId,final FirebaseStatusOrderHelper.DataStatus dataStatus) {
        mReferenceStatusOrder.child("Bills").child(billId).child("orderStatus").setValue("Completed")
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        if (dataStatus != null) {
                            dataStatus.DataIsUpdated();
                        }
                    }
                });

        // set sold and remainAmount value of Product
        billInfoList = new ArrayList<>();
        soldValueList = new ArrayList<>();

        mReferenceStatusOrder.child("BillInfos").child(billId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot keyNode: snapshot.getChildren())
                {
                    BillInfo billInfo = keyNode.getValue(BillInfo.class);
                    billInfoList.add(billInfo);
                }
                readSomeInfoOfBill();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void readSomeInfoOfBill() {
        mReferenceStatusOrder.child("Products").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (BillInfo info : billInfoList) {
                    int sold = snapshot.child(info.getProductId()).child("sold").getValue(int.class) + info.getAmount();
                    soldValueList.add(sold);
                }
                updateSoldValueOfProduct();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void updateSoldValueOfProduct() {
        for (int i = 0; i < billInfoList.size(); i++) {
            mReferenceStatusOrder.child("Products").child(billInfoList.get(i).getProductId()).child("sold").setValue(soldValueList.get(i));
        }
    }
}
