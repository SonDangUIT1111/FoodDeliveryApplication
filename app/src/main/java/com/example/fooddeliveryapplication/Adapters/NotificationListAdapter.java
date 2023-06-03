package com.example.fooddeliveryapplication.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.example.fooddeliveryapplication.Helpers.FirebaseNotificationHelper;
import com.example.fooddeliveryapplication.Model.Notification;
import com.example.fooddeliveryapplication.R;

import java.util.List;

public class NotificationListAdapter extends RecyclerView.Adapter<NotificationListAdapter.ViewHolder> {
    private Context mContext;
    private List<Notification> notificationList;
    private List<String> mKeys;
    String userId;

    public NotificationListAdapter(Context mContext, List<Notification> notificationList,String id) {
        this.mContext = mContext;
        this.notificationList = notificationList;
        userId = id;
    }

    @NonNull
    @Override
    public NotificationListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.notification_item,parent,false);
        return new NotificationListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationListAdapter.ViewHolder holder, int position) {
        Notification notification = notificationList.get(position);
        holder.txtTitleNotification.setText(notification.getTitle());
        holder.txtContentNotification.setText(notification.getContent());
        holder.txtTimeNotification.setText(notification.getTime());
        if (notification.getImageURL().isEmpty())
        {
            holder.imgNotification.setImageResource(R.drawable.ic_launcher_background);
        }
        else {
            holder.imgNotification.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Glide.with(mContext)
                    .asBitmap()
                    .load(notificationList.get(position).getImageURL())
                    .into(holder.imgNotification);
        }
        if (notification.isRead()==false)
        {
            holder.dotStatusRead.setVisibility(View.VISIBLE);
            holder.background_notification_item.setBackgroundColor(Color.parseColor("#e3e3e3"));
        }
        else {
            holder.dotStatusRead.setVisibility(View.GONE);
            holder.background_notification_item.setBackgroundColor(Color.TRANSPARENT);
        }
        // trigger for navigate to another activity
        holder.background_notification_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo write code to navigate to the activity refer to notification
                if (!notification.isRead())
                {
                    notification.setRead(true);
                    new FirebaseNotificationHelper(mContext).updateNotification(userId, notification, new FirebaseNotificationHelper.DataStatus() {
                        @Override
                        public void DataIsLoaded(List<Notification> notificationList) {

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
        });
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtTitleNotification;
        TextView txtContentNotification;
        TextView txtTimeNotification;
        ImageView imgNotification;
        CardView dotStatusRead;
        ConstraintLayout background_notification_item;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitleNotification = itemView.findViewById(R.id.txtTitleNotification);
            txtContentNotification = itemView.findViewById(R.id.txtContentNotification);
            txtTimeNotification = itemView.findViewById(R.id.txtTimeNotification);
            imgNotification = itemView.findViewById(R.id.imgNotification);
            dotStatusRead = itemView.findViewById(R.id.dotStatusRead);
            background_notification_item = itemView.findViewById(R.id.background_notification_item);
        }
    }
}
