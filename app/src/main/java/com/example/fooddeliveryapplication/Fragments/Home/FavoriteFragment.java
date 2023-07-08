package com.example.fooddeliveryapplication.Fragments.Home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.fooddeliveryapplication.Adapters.Home.FavouriteFoodAdapter;
import com.example.fooddeliveryapplication.Helpers.FirebaseFavouriteUserHelper;
import com.example.fooddeliveryapplication.Model.Product;
import com.example.fooddeliveryapplication.databinding.FragmentFavoriteBinding;

import java.util.ArrayList;


public class FavoriteFragment extends Fragment {
    private FragmentFavoriteBinding binding;
    private String userId;

    public FavoriteFragment(String id ) {
        userId = id;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentFavoriteBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        readFavouriteList();

        return view;
    }

    public void readFavouriteList()
    {
        new FirebaseFavouriteUserHelper().readFavouriteList(userId, new FirebaseFavouriteUserHelper.DataStatus() {
            @Override
            public void DataIsLoaded(ArrayList<Product> favouriteProducts,ArrayList<String> keys) {
                StaggeredGridLayoutManager layoutManager=new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
                binding.recFavouriteFood.setLayoutManager(layoutManager);
                binding.recFavouriteFood.setHasFixedSize(true);
                FavouriteFoodAdapter adapter = new FavouriteFoodAdapter(getContext(),favouriteProducts,userId);
                binding.recFavouriteFood.setAdapter(adapter);
                binding.progressBarFavouriteList.setVisibility(View.GONE);
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
    }
}