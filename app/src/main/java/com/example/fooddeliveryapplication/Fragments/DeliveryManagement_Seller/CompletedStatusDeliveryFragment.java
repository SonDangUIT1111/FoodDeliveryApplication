package com.example.fooddeliveryapplication.Fragments.DeliveryManagement_Seller;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.fooddeliveryapplication.Adapters.DeliveryManagement_Seller.StatusOrderRecyclerViewAdapter;
import com.example.fooddeliveryapplication.Helpers.FirebaseStatusOrderHelper;
import com.example.fooddeliveryapplication.Model.Bill;
import com.example.fooddeliveryapplication.R;

import java.util.List;

public class CompletedStatusDeliveryFragment extends Fragment {

    View view;
    RecyclerView recCompletedDelivery;
    String userId;
    ProgressBar progressBarCompletedDelivery;
    public CompletedStatusDeliveryFragment(String Id) {
        userId = Id;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_completed_status_delivery, container, false);

        // find view by id
        recCompletedDelivery = (RecyclerView) view.findViewById(R.id.recCompletedDelivery);
        progressBarCompletedDelivery = (ProgressBar) view.findViewById(R.id.progressBarCompletedDelivery);

        // pull data and set adapter for recycler view
        new FirebaseStatusOrderHelper(userId).readCompletedBills(userId, new FirebaseStatusOrderHelper.DataStatus() {
            @Override
            public void DataIsLoaded(List<Bill> bills, List<String> img) {
                StatusOrderRecyclerViewAdapter adapter = new StatusOrderRecyclerViewAdapter(getContext(),bills,img);
                recCompletedDelivery.setHasFixedSize(true);
                recCompletedDelivery.setLayoutManager(new LinearLayoutManager(getContext()));
                recCompletedDelivery.setAdapter(adapter);
                progressBarCompletedDelivery.setVisibility(View.GONE);
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