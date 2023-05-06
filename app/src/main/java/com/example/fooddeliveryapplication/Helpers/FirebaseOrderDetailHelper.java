package com.example.fooddeliveryapplication.Helpers;

import com.example.fooddeliveryapplication.Model.Bill;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class FirebaseOrderDetailHelper {
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReferenceStatusOrder;

    public interface DataStatus{
        void DataIsLoaded(List<Bill> bills,List<String> img);
        void DataIsInserted();
        void DataIsUpdated();
        void DataIsDeleted();

    }

    public FirebaseOrderDetailHelper() {
        mDatabase = FirebaseDatabase.getInstance();
        mReferenceStatusOrder = mDatabase.getReference();
    }

    public void readAddressById(String addressId,final FirebaseOrderDetailHelper.DataStatus dataStatus )
    {

    }
}
