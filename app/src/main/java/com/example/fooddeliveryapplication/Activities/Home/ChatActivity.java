package com.example.fooddeliveryapplication.Activities.Home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.Toast;

import com.example.fooddeliveryapplication.Adapters.Home.ChatAdapter;
import com.example.fooddeliveryapplication.Model.ItemChatRoom;
import com.example.fooddeliveryapplication.Model.Message;
import com.example.fooddeliveryapplication.Model.User;
import com.example.fooddeliveryapplication.databinding.ActivityChatBinding;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class ChatActivity extends AppCompatActivity {
    ActivityChatBinding binding;
    String userId;
    DatabaseReference userReference;
    MutableLiveData<ArrayList<String>> bunchOfReceiverId=new MutableLiveData<>();
    ArrayList<ItemChatRoom> bunchOfItemChatRoom=new ArrayList<>();
    MutableLiveData <ArrayList<ItemChatRoom>> bunchOfItemChatRoomLive=new MutableLiveData<>();
    DatabaseReference chatReference;
    ValueEventListener chatListener;
    ChatAdapter chatAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        userId=getIntent().getStringExtra("userId");
        initUI();
        createReference();
        createObserver();
        createChatListener();
        loadReceiverId();
    }


//    void create() {
//        if (!bunchOfItemChatRoom.isEmpty()){
//            for (int i=0;i<bunchOfItemChatRoom.size();i++) {
//                createItemChatListener(bunchOfItemChatRoom.get(i),i);
//            }
//        } else {
//            Toast.makeText(this,"Bi looi create",Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    private void createItemChatListener(ItemChatRoom itemChatRoom, int position) {
//        chatReference.child(itemChatRoom.getReceiver().getUserId()).addChildEventListener(new ChildEventListener() {
//            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//                itemChatRoom.setLastMessage(snapshot.getValue(Message.class));
//                bunchOfItemChatRoom.set(position, itemChatRoom);
//                chatAdapter.notifyItemChanged(position);
//            }
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//                // Lắng nghe sự kiện thay đổi của tin nhắn
//                itemChatRoom.setLastMessage(snapshot.getValue(Message.class));
//                bunchOfItemChatRoom.set(position, itemChatRoom);
//                chatAdapter.notifyItemChanged(position);
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }

    private void createObserver() {
        createObserverOfReceiverId();
        createObserverOfItemChatRoom();
    }

    private void createObserverOfItemChatRoom() {
        bunchOfItemChatRoomLive.observe(this, new Observer<ArrayList<ItemChatRoom>>() {
            @Override
            public void onChanged(ArrayList<ItemChatRoom> itemChatRooms) {
                chatAdapter.notifyDataSetChanged();
            }
        });
    }

    private void initUI() {
        chatAdapter=new ChatAdapter(ChatActivity.this,bunchOfItemChatRoom);
        binding.recycleViewMessage.setLayoutManager(new LinearLayoutManager(ChatActivity.this, RecyclerView.VERTICAL,false));
        binding.recycleViewMessage.setAdapter(chatAdapter);
    }

    private void createObserverOfReceiverId() {
        bunchOfReceiverId.observe(this, new Observer<ArrayList<String>>() {
            @Override
            public void onChanged(ArrayList<String> strings) {
                loadReceiver(strings);
            }
        });
    }

    private void loadReceiver(ArrayList<String> receiverIds) {
        bunchOfItemChatRoom.clear();
        for (int i=0;i<receiverIds.size();i++) {
                    userReference.child(receiverIds.get(i)).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            includeGetItemChatFromReceiver(snapshot.getValue(User.class),receiverIds);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }

    }

    private void includeGetItemChatFromReceiver(User receiver,ArrayList<String> receiverIds) {
        chatReference.child(receiver.getUserId())
                .orderByChild("timestamp").limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            Message lastMessage=snapshot.getChildren().iterator().next().getValue(Message.class);
                            ItemChatRoom itemChatRoom=new ItemChatRoom(receiver,lastMessage);
                            bunchOfItemChatRoom.add(itemChatRoom);
                            if (bunchOfItemChatRoom.size()==receiverIds.size()) {
                                bunchOfItemChatRoomLive.postValue(bunchOfItemChatRoom);
                            }
                        } else {
                            Toast.makeText(ChatActivity.this, "include",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void createChatListener() {
        chatListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> receiverIds=new ArrayList<>();
                includeHanldeWhenChatChanged(receiverIds,snapshot);
                bunchOfReceiverId.postValue(receiverIds);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
    }

    private void includeHanldeWhenChatChanged(ArrayList<String> receiverIds ,DataSnapshot snapshot) {
        for (DataSnapshot item:snapshot.getChildren()) {
            String receiverId=item.getKey();
            receiverIds.add(receiverId);
        }
    }

    private void createReference() {
        chatReference= FirebaseDatabase.getInstance().getReference("Message").child(userId);
        userReference=FirebaseDatabase.getInstance().getReference("Users");
    }

    private void loadReceiverId() {
            chatReference.addValueEventListener(chatListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding=null;
        chatReference.removeEventListener(chatListener);
    }

}