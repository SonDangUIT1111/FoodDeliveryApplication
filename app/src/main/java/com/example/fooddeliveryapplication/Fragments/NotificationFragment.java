package com.example.fooddeliveryapplication.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.fooddeliveryapplication.Adapters.NotificationListAdapter;
import com.example.fooddeliveryapplication.Helpers.FirebaseNotification;
import com.example.fooddeliveryapplication.Model.Notification;
import com.example.fooddeliveryapplication.R;

import java.util.ArrayList;
import java.util.List;


public class NotificationFragment extends Fragment {

    RecyclerView recNotification;
    View view;
    String userId;
    ProgressBar progressBarNotification;
    public NotificationFragment(String Id) {
        userId = Id;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_notification,container,false);

        // find view by id
        recNotification = (RecyclerView) view.findViewById(R.id.recNotification);
        progressBarNotification = (ProgressBar) view.findViewById(R.id.progressBarNotification);

        readNotification();
        return view;
    }

    public void readNotification()
    {
        new FirebaseNotification().readNotification(userId, new FirebaseNotification.DataStatus() {
            @Override
            public void DataIsLoaded(List<Notification> notificationList) {
                NotificationListAdapter adapter = new NotificationListAdapter(getContext(),notificationList,userId);
                recNotification.setHasFixedSize(true);
                recNotification.setLayoutManager(new LinearLayoutManager(getContext()));
                recNotification.setAdapter(adapter);
                progressBarNotification.setVisibility(View.GONE);
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