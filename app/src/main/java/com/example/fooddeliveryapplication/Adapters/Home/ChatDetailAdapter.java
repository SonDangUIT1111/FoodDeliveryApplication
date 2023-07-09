package com.example.fooddeliveryapplication.Adapters.Home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fooddeliveryapplication.Model.Message;
import com.example.fooddeliveryapplication.databinding.ItemReceiveMessageBinding;
import com.example.fooddeliveryapplication.databinding.ItemSendMessageBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ChatDetailAdapter extends RecyclerView.Adapter{
    Context context;
    ArrayList<Message> ds;
    public final int ITEM_SEND=1001;
    public final int ITEM_RECEIVE=1002;
    public final int NULL_ITEM=1003;
    String publisherId;
    SimpleDateFormat formatHour = new SimpleDateFormat("HH:mm a");
    String userId;

    public ChatDetailAdapter(Context context, ArrayList<Message> ds, String userId, String publisherId) {
        this.context = context;
        this.ds = ds;
        this.userId = userId;
        this.publisherId = publisherId;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType==ITEM_SEND) {
            return new SendViewHolder(ItemSendMessageBinding.inflate(LayoutInflater.from(context),parent,false));
        } else {
            return new ReceiveViewHolder(ItemReceiveMessageBinding.inflate(LayoutInflater.from(context),parent,false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message=ds.get(position);

        if (getItemViewType(position)==ITEM_SEND) {
            SendViewHolder viewHolder=(SendViewHolder) holder;
            viewHolder.binding.txtMessage.setText(message.getContent());
            if (message.isSeen()==true) {
                viewHolder.binding.txtTime.setText(formatHour.format(new Date(message.getTimeStamp()))+" Seen");
                viewHolder.binding.imgCheck.setVisibility(View.GONE);
            } else {
                viewHolder.binding.txtTime.setText(formatHour.format(new Date(message.getTimeStamp()))+" Sent");
                viewHolder.binding.imgDoubleCheck.setVisibility(View.GONE);
            }
        } else {
            ReceiveViewHolder viewHolder=(ReceiveViewHolder) holder;
            viewHolder.binding.txtMessage.setText(message.getContent());
            viewHolder.binding.txtTime.setText(formatHour.format(new Date(message.getTimeStamp())));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (!ds.isEmpty()) {
            Message message=ds.get(position);
            if (message.getSenderId().equals(userId)) {
                return ITEM_SEND;
            } else {
                return ITEM_RECEIVE;
            }
        } else {
            return NULL_ITEM;
        }
    }

    @Override
    public int getItemCount() {
        if (!ds.isEmpty()) {
            return ds.size();
        } else
            return 0;
    }

    public class SendViewHolder extends RecyclerView.ViewHolder {
        ItemSendMessageBinding binding;
        public SendViewHolder(@NonNull ItemSendMessageBinding tmp) {
            super(tmp.getRoot());
            binding=tmp;
        }
    }
    public class ReceiveViewHolder extends RecyclerView.ViewHolder {
        ItemReceiveMessageBinding binding;
        public ReceiveViewHolder(@NonNull ItemReceiveMessageBinding tmp) {
            super(tmp.getRoot());
            binding=tmp;
        }
    }
}
