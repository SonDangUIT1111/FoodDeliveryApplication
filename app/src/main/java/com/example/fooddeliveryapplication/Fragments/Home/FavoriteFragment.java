package com.example.fooddeliveryapplication.Fragments.Home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.fooddeliveryapplication.Adapters.Home.FavouriteFoodAdapter;
import com.example.fooddeliveryapplication.Helpers.FirebaseFavouriteUserHelper;
import com.example.fooddeliveryapplication.Model.Product;
import com.example.fooddeliveryapplication.R;

import java.util.ArrayList;


public class FavoriteFragment extends Fragment {
    RecyclerView recFavouriteFood;
    View view;
    String userId;
    ProgressBar progressBarFavouriteList;
    public FavoriteFragment(String id ) {
        userId = id;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_favorite,container,false);

        // find view by id
        recFavouriteFood = (RecyclerView) view.findViewById(R.id.recFavouriteFood);
        progressBarFavouriteList = (ProgressBar) view.findViewById(R.id.progressBarFavouriteList);

        readFavouriteList();
        return view;
    }

    public void readFavouriteList()
    {
        new FirebaseFavouriteUserHelper().readFavouriteList(userId, new FirebaseFavouriteUserHelper.DataStatus() {
            @Override
            public void DataIsLoaded(ArrayList<Product> favouriteProducts,ArrayList<String> keys) {
                StaggeredGridLayoutManager layoutManager=new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
                recFavouriteFood.setLayoutManager(layoutManager);
                recFavouriteFood.setHasFixedSize(true);
                FavouriteFoodAdapter adapter = new FavouriteFoodAdapter(getContext(),favouriteProducts,userId);
                recFavouriteFood.setAdapter(adapter);
                progressBarFavouriteList.setVisibility(View.GONE);
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