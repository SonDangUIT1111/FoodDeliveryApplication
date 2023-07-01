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
    private ArrayList<User> users;

    private ValueEventListener messsageListener;

    public ChatAdapter(Context context, ArrayList<User> users) {
        this.context = context;
        this.users = users;
        viewBinderHelper.setOpenOnlyOne(true);
    }



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemChatBinding.inflate(LayoutInflater.from(context),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        User currentUser=users.get(position);
        ViewHolder viewHolder=(ViewHolder) holder;
        viewHolder.binding.txtNameUser.setText(currentUser.getNameOfUser());
        Glide.with(context)
                .load(currentUser.getAvatarURL())
                .error(R.drawable.image_default)
                .into(viewHolder.binding.imgUser);
        viewHolder.binding.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, ChatDetailActivity.class);
                intent.setAction("chatActivity");
                intent.putExtra("publisher",currentUser);
                context.startActivity(intent);
            }
        });
//        FirebaseDatabase.getInstance().getReference("Message").
//                child(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                .child(currentUser.getUserId()).orderByChild("timestamp").limitToLast(1).addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        Message message=snapshot.getChildren().iterator().next().getValue(Message.class);
//                        viewHolder.binding.txtLastMessage.setText(message.getContent());
//                        notifyDataSetChanged();
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
    }




    @Override
    public int getItemCount() {
        if (!users.isEmpty())
            return users.size();
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
