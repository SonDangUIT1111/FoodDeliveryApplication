package com.example.fooddeliveryapplication.Adapters.Home;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.example.fooddeliveryapplication.Activities.Home.ChatDetailActivity;
import com.example.fooddeliveryapplication.Model.ItemChatRoom;
import com.example.fooddeliveryapplication.R;
import com.example.fooddeliveryapplication.databinding.ItemChatBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class ChatAdapter extends RecyclerView.Adapter {
    private ViewBinderHelper viewBinderHelper=new ViewBinderHelper();
    private Context context;
    private ArrayList<ItemChatRoom> bunchOfItemChatRooms;
    private ArrayList<ItemChatRoom> currentBunchOfItemChatRooms;

    public ChatAdapter(Context context, ArrayList<ItemChatRoom> itemChatRooms) {
        this.context = context;
        this.bunchOfItemChatRooms = itemChatRooms;
        currentBunchOfItemChatRooms=bunchOfItemChatRooms;
        viewBinderHelper.setOpenOnlyOne(true);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemChatBinding.inflate(LayoutInflater.from(context),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ItemChatRoom itemChatRoom = currentBunchOfItemChatRooms.get(position);
        ViewHolder viewHolder=(ViewHolder) holder;
        viewBinderHelper.bind(viewHolder.binding.SwipeRevealLayout, itemChatRoom.getReceiver().getUserId());
        viewHolder.binding.txtNameUser.setText(itemChatRoom.getReceiver().getUserName());
        viewHolder.binding.txtLastMessage.setTextColor(context.getColor(R.color.app_color2));
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
        Glide.with(context).load(itemChatRoom.getReceiver().getAvatarURL()).placeholder(R.drawable.default_avatar).error(R.drawable.image_default).into(viewHolder.binding.lnItemChat.imgUser);
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
        if (!currentBunchOfItemChatRooms.isEmpty())
            return currentBunchOfItemChatRooms.size();
        else
            return 0;
    }


    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String key=charSequence.toString();
                if (key.trim().isEmpty())
                    currentBunchOfItemChatRooms=bunchOfItemChatRooms;
                else {
                    ArrayList<ItemChatRoom> tmp=new ArrayList<>();
                    key=key.toLowerCase();
                    for (ItemChatRoom item: bunchOfItemChatRooms) {
                        if (item.getReceiver().getUserName().toLowerCase().contains(key)) {
                            tmp.add(item);
                        }
                    }
                    currentBunchOfItemChatRooms=tmp;
                }
                FilterResults results=new FilterResults();
                results.values=currentBunchOfItemChatRooms;
                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                currentBunchOfItemChatRooms = (ArrayList<ItemChatRoom>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemChatBinding binding;
        public ViewHolder(@NonNull ItemChatBinding tmp) {
            super(tmp.getRoot());
            binding=tmp;
        }
    }
}
