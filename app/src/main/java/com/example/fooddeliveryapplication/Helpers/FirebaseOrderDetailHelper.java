package com.example.fooddeliveryapplication.Helpers;

import androidx.annotation.NonNull;

import com.example.fooddeliveryapplication.Model.Bill;
import com.example.fooddeliveryapplication.Model.BillInfo;
import com.example.fooddeliveryapplication.Model.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FirebaseOrderDetailHelper {
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReferenceStatusOrder;

    List<BillInfo> billInfos = new ArrayList<>();
    public interface DataStatus{
        void DataIsLoaded(String addresss, List<BillInfo> billInfos);
        void DataIsInserted();
        void DataIsUpdated();
        void DataIsDeleted();

    }
    public interface DataStatus2{
        void DataIsLoaded(Product product);
        void DataIsInserted();
        void DataIsUpdated();
        void DataIsDeleted();

    }

    public FirebaseOrderDetailHelper() {
        mDatabase = FirebaseDatabase.getInstance();
        mReferenceStatusOrder = mDatabase.getReference();
    }

    public void readOrderDetail(String addressId,String userId,String billId,final FirebaseOrderDetailHelper.DataStatus dataStatus )
    {
        mReferenceStatusOrder.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String addressDetail = snapshot.child("Address").child(userId).child(addressId).child("detailAddress").getValue(String.class);
                billInfos.clear();
                for (DataSnapshot keyNode: snapshot.child("BillInfos").child(billId).getChildren())
                {
                    billInfos.add(keyNode.getValue(BillInfo.class));
                }
                if (dataStatus != null) {
                    dataStatus.DataIsLoaded(addressDetail,billInfos);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void readProductInfo(String productId, final FirebaseOrderDetailHelper.DataStatus2 dataStatus)
    {
        mReferenceStatusOrder.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Product product = snapshot.child("Products").child(productId).getValue(Product.class);
                if (dataStatus != null) {
                    dataStatus.DataIsLoaded(product);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
