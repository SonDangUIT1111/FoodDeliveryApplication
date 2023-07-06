package com.example.fooddeliveryapplication.Fragments.DeliveryManagement_Seller;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fooddeliveryapplication.Adapters.DeliveryManagement_Seller.StatusOrderRecyclerViewAdapter;
import com.example.fooddeliveryapplication.Helpers.FirebaseStatusOrderHelper;
import com.example.fooddeliveryapplication.Model.Bill;
import com.example.fooddeliveryapplication.databinding.FragmentCompletedStatusDeliveryBinding;

import java.util.List;

public class CompletedStatusDeliveryFragment extends Fragment {
    private FragmentCompletedStatusDeliveryBinding binding;
    private String userId;

    public CompletedStatusDeliveryFragment(String Id) {
        userId = Id;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCompletedStatusDeliveryBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // pull data and set adapter for recycler view
        new FirebaseStatusOrderHelper(userId).readCompletedBills(userId, new FirebaseStatusOrderHelper.DataStatus() {
            @Override
            public void DataIsLoaded(List<Bill> bills, boolean isExistingBill) {
                StatusOrderRecyclerViewAdapter adapter = new StatusOrderRecyclerViewAdapter(getContext(),bills);
                binding.recCompletedDelivery.setHasFixedSize(true);
                binding.recCompletedDelivery.setLayoutManager(new LinearLayoutManager(getContext()));
                binding.recCompletedDelivery.setAdapter(adapter);
                binding.progressBarCompletedDelivery.setVisibility(View.GONE);
                if (isExistingBill) {
                    binding.txtNoneItem.setVisibility(View.GONE);
                }
                else {
                    binding.txtNoneItem.setVisibility(View.VISIBLE);
                }
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