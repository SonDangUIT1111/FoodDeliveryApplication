package com.example.fooddeliveryapplication.Interfaces;

import com.example.fooddeliveryapplication.Model.Address;

public interface IAddressAdapterListener {
    void onCheckedChanged(Address selectedAddress);
    void onDeleteAddress();
}
