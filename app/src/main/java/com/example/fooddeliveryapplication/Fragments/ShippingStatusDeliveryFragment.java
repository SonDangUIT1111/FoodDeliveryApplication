package com.example.fooddeliveryapplication.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.fooddeliveryapplication.R;


public class ShippingStatusDeliveryFragment extends Fragment {

    View view;
    String userId;
    public ShippingStatusDeliveryFragment(String Id) {
        userId = Id;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_shipping_status_delivery, container, false);
        ImageView imageView = (ImageView) view.findViewById(R.id.img);

        Glide.with(getContext()).asBitmap().load("https://expertphotography.b-cdn.net/wp-content/uploads/2019/05/beautiful-photography-man-sitting-in-front-of-lake.jpg").into(imageView);

        return view;
    }
}