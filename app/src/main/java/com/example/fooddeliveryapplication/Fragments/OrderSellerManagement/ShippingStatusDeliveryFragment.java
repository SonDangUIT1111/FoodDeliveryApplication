package com.example.fooddeliveryapplication.Fragments.OrderSellerManagement;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.fooddeliveryapplication.Adapters.OrderSellerManagement.StatusOrderRecyclerViewAdapter;
import com.example.fooddeliveryapplication.Helpers.FirebaseStatusOrderHelper;
import com.example.fooddeliveryapplication.Model.Bill;
import com.example.fooddeliveryapplication.R;

import java.util.List;


public class ShippingStatusDeliveryFragment extends Fragment {

    View view;
    String userId;
    RecyclerView recShippingDelivery;
    ProgressBar progressBarShippingDelivery;
    public ShippingStatusDeliveryFragment(String Id) {
        userId = Id;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_shipping_status_delivery, container, false);


        // find view by id
        recShippingDelivery = (RecyclerView) view.findViewById(R.id.recShippingDelivery);
        progressBarShippingDelivery = (ProgressBar) view.findViewById(R.id.progressBarShippingDelivery);

        //set adapter and pull data for recycler view
        new FirebaseStatusOrderHelper(userId).readShippingBills(userId, new FirebaseStatusOrderHelper.DataStatus() {
            @Override
            public void DataIsLoaded(List<Bill> bills, List<String> img) {
                StatusOrderRecyclerViewAdapter adapter = new StatusOrderRecyclerViewAdapter(getContext(),bills,img);
                recShippingDelivery.setLayoutManager(new LinearLayoutManager(getContext()));
                recShippingDelivery.setHasFixedSize(true);
                recShippingDelivery.setAdapter(adapter);
                progressBarShippingDelivery.setVisibility(View.GONE);
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
        return view;
    }
}