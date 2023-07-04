package com.example.fooddeliveryapplication.Adapters.Home;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fooddeliveryapplication.databinding.ItemIntroBinding;

import java.util.ArrayList;

public class IntroAdapter  extends  RecyclerView.Adapter{
    private ArrayList<Integer> ds;
    private Activity context;

    public IntroAdapter(ArrayList<Integer> ds, Activity context) {
        this.ds = ds;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new IntroAdapter.ViewHolder(ItemIntroBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder=(ViewHolder) holder;
        viewHolder.binding.img.setImageResource(ds.get(position));
    }

    @Override
    public int getItemCount() {
        return ds == null ? 0 : ds.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemIntroBinding binding;

        public ViewHolder(@NonNull ItemIntroBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
