package com.example.fooddeliveryapplication.Helpers;

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
    List<Bill> bills = new ArrayList<>();
    List<String> images = new ArrayList<>();
    String userId;
    List<BillInfo> temp = new ArrayList<>();
    List<Integer> tempInt = new ArrayList<>();

    public interface DataStatus{
        void DataIsLoaded(List<Bill> bills,List<String> img);
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
                images.clear();
                for (DataSnapshot keyNode : snapshot.child("Bills").getChildren())
                {
                    if (keyNode.child("senderId").getValue(String.class).equals(userId)
                    &&  keyNode.child("orderStatus").getValue(String.class).equals("Confirm"))
                    {
                        Bill bill = keyNode.getValue(Bill.class);
                        bills.add(bill);

                        // get first product id
                        DataSnapshot snapChild = snapshot.child("BillInfos").child(bill.getBillId());
                        String firstProductId = "";
                        for (DataSnapshot keyChild : snapChild.getChildren())
                        {
                            firstProductId = keyChild.child("productId").getValue(String.class);
                        }
                        // get image for bill
                        images.add(snapshot.child("Products").child(firstProductId).child("productImage1").getValue(String.class));
                    }
                }

                if (dataStatus != null) {
                    dataStatus.DataIsLoaded(bills,images);
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
                images.clear();
                for (DataSnapshot keyNode : snapshot.child("Bills").getChildren())
                {
                    if (keyNode.child("senderId").getValue(String.class).equals(userId)
                            &&  keyNode.child("orderStatus").getValue(String.class).equals("Shipping"))
                    {
                        Bill bill = keyNode.getValue(Bill.class);
                        bills.add(bill);

                        // get first product id
                        DataSnapshot snapChild = snapshot.child("BillInfos").child(bill.getBillId());
                        String firstProductId = "";
                        for (DataSnapshot keyChild : snapChild.getChildren())
                        {
                            firstProductId = keyChild.child("productId").getValue(String.class);
                        }
                        // get image for bill
                        images.add(snapshot.child("Products").child(firstProductId).child("productImage1").getValue(String.class));
                    }
                }

                if (dataStatus != null) {
                    dataStatus.DataIsLoaded(bills,images);
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
                images.clear();
                for (DataSnapshot keyNode : snapshot.child("Bills").getChildren()) {
                    if (keyNode.child("senderId").getValue(String.class).equals(userId)
                            && keyNode.child("orderStatus").getValue(String.class).equals("Completed")) {
                        Bill bill = keyNode.getValue(Bill.class);
                        bills.add(bill);

                        // get first product id
                        DataSnapshot snapChild = snapshot.child("BillInfos").child(bill.getBillId());
                        String firstProductId = "";
                        for (DataSnapshot keyChild : snapChild.getChildren()) {
                            firstProductId = keyChild.child("productId").getValue(String.class);
                        }
                        // get image for bill
                        images.add(snapshot.child("Products").child(firstProductId).child("productImage1").getValue(String.class));
                    }
                }

                if (dataStatus != null) {
                    dataStatus.DataIsLoaded(bills, images);
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


        // set sold value of Product
        temp = new ArrayList<>();
        tempInt = new ArrayList<>();
        mReferenceStatusOrder.child("BillInfos").child(billId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot keyNode: snapshot.getChildren())
                {
                    BillInfo billInfo = keyNode.getValue(BillInfo.class);
                    temp.add(billInfo);
                }
                readSomeInfoOfBill();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
    public void readSomeInfoOfBill()
    {
        mReferenceStatusOrder.child("Products").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (BillInfo info:temp)
                {
                    int count = snapshot.child(info.getProductId()).child("sold").getValue(int.class) + info.getAmount();
                    tempInt.add(count);
                }
                updateSoldOfProduct();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void updateSoldOfProduct()
    {
        for (int i = 0;i < temp.size(); i++)
        {
            mReferenceStatusOrder.child("Products").child(temp.get(i).getProductId()).child("sold").setValue(tempInt.get(i)).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {

                }
            });
        }
    }
}
