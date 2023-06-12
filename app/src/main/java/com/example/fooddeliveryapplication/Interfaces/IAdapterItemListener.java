package com.example.fooddeliveryapplication.Interfaces;

import com.example.fooddeliveryapplication.Model.CartInfo;

import java.util.ArrayList;
import java.util.List;

public interface IAdapterItemListener {
    void onCheckedItemCountChanged(int count, long price, ArrayList<CartInfo> selectedItems);
    void onAddClicked();
    void onSubtractClicked();
    void onDeleteProduct();
}