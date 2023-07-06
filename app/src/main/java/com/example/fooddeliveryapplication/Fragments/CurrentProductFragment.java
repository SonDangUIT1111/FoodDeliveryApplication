package com.example.fooddeliveryapplication.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fooddeliveryapplication.Adapters.CurrentAdapter;
import com.example.fooddeliveryapplication.Model.InfoCurrentProduct;
import com.example.fooddeliveryapplication.R;
import com.example.fooddeliveryapplication.databinding.FragmentCurrentProductBinding;

import java.util.ArrayList;
import java.util.List;


public class CurrentProductFragment extends Fragment {
    private FragmentCurrentProductBinding binding;
    private RecyclerView rcvCurrentProduct;
    private CurrentAdapter currentAdapter;
    private ArrayList<InfoCurrentProduct> mListInfoCurrentProducts;
    private FragmentManager fragmentManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCurrentProductBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        rcvCurrentProduct = view.findViewById(R.id.rcv_Current_product);
        currentAdapter = new CurrentAdapter(getContext(), mListInfoCurrentProducts);
        fragmentManager = getActivity().getSupportFragmentManager();

        currentAdapter.setData(getListInfoCurrentProduct());

        rcvCurrentProduct.setLayoutManager(new GridLayoutManager(getContext(),1));
        rcvCurrentProduct.setAdapter(currentAdapter);

        return view;
    }

    private List<InfoCurrentProduct> getListInfoCurrentProduct() {
        List<InfoCurrentProduct> list = new ArrayList<>();

        list.add(new InfoCurrentProduct(R.drawable.pop_1, "Product1", "State", "2000"));
        list.add(new InfoCurrentProduct(R.drawable.pop_1, "Product2", "State", "2000"));
        list.add(new InfoCurrentProduct(R.drawable.pop_1, "Product3", "State", "2000"));

        return list;
    }
}