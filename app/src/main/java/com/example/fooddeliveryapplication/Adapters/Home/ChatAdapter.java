package com.example.fooddeliveryapplication.Adapters.Home;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.example.fooddeliveryapplication.Activities.Home.ChatDetailActivity;
import com.example.fooddeliveryapplication.Model.ItemChatRoom;
import com.example.fooddeliveryapplication.Model.Message;
import com.example.fooddeliveryapplication.Model.User;
import com.example.fooddeliveryapplication.R;
import com.example.fooddeliveryapplication.databinding.ItemChatBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter {
    private ViewBinderHelper viewBinderHelper=new ViewBinderHelper();
    private Context context;
    private ArrayList<ItemChatRoom> bunchOfItemChatRooms;

    private ValueEventListener messsageListener;

    public ChatAdapter(Context context, ArrayList<ItemChatRoom> itemChatRooms) {
        this.context = context;
        this.bunchOfItemChatRooms = itemChatRooms;
        viewBinderHelper.setOpenOnlyOne(true);
    }



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemChatBinding.inflate(LayoutInflater.from(context),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ItemChatRoom itemChatRoom=bunchOfItemChatRooms.get(position);
        ViewHolder viewHolder=(ViewHolder) holder;
        viewBinderHelper.bind(viewHolder.binding.SwipeRevealLayout, itemChatRoom.getReceiver().getUserId());
        viewHolder.binding.txtNameUser.setText(itemChatRoom.getReceiver().getNameOfUser());
        viewHolder.binding.txtLastMessage.setTextColor(context.getColor(R.color.blue_chat));
        if (itemChatRoom.getLastMessage().getSenderId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            viewHolder.binding.imgNewMessage.setVisibility(View.INVISIBLE);
            viewHolder.binding.txtLastMessage.setTextColor(context.getColor(R.color.gray));
        } else {
            if (itemChatRoom.getLastMessage().isSeen()) {
                viewHolder.binding.imgNewMessage.setVisibility(View.INVISIBLE);
                viewHolder.binding.txtLastMessage.setTextColor(context.getColor(R.color.gray));
            } else {
                viewHolder.binding.imgNewMessage.setVisibility(View.VISIBLE);
            }
        }
        Glide.with(context)
                .load(itemChatRoom.getReceiver().getAvatarURL())
                .error(R.drawable.image_default)
                .into(viewHolder.binding.imgUser);
        viewHolder.binding.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, ChatDetailActivity.class);
                intent.setAction("chatActivity");
                intent.putExtra("publisher",itemChatRoom.getReceiver());
                context.startActivity(intent);
            }
        });
        viewHolder.binding.txtLastMessage.setText(itemChatRoom.getLastMessage().getContent());
    }




    @Override
    public int getItemCount() {
        if (!bunchOfItemChatRooms.isEmpty())
            return bunchOfItemChatRooms.size();
        else
            return 0;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemChatBinding binding;
        public ViewHolder(@NonNull ItemChatBinding tmp) {
            super(tmp.getRoot());
            binding=tmp;
        }
    }
}
