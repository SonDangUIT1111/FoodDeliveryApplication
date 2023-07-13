package com.example.fooddeliveryapplication.Activities.Home;

import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fooddeliveryapplication.Adapters.Home.ChatAdapter;
import com.example.fooddeliveryapplication.CustomMessageBox.CustomAlertDialog;
import com.example.fooddeliveryapplication.Model.ItemChatRoom;
import com.example.fooddeliveryapplication.Model.Message;
import com.example.fooddeliveryapplication.Model.User;
import com.example.fooddeliveryapplication.databinding.ActivityChatBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {
    private ActivityChatBinding binding;
    private String userId;
    private DatabaseReference userReference;
    private MutableLiveData<ArrayList<String>> bunchOfReceiverId = new MutableLiveData<>();
    private ArrayList<ItemChatRoom> bunchOfItemChatRoom=new ArrayList<>();
    private MutableLiveData <ArrayList<ItemChatRoom>> bunchOfItemChatRoomLive=new MutableLiveData<>();
    private DatabaseReference chatReference;
    private ValueEventListener chatListener;
    private ChatAdapter chatAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        userId = getIntent().getStringExtra("userId");
        initToolbar();
        initUI();
        initFilterForSearchView();
        createReference();
        createObserver();
        createChatListener();
        loadReceiverId();
    }

    private void initToolbar() {
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle("Messages");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initFilterForSearchView() {
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                chatAdapter.getFilter().filter(s);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                chatAdapter.getFilter().filter(s);
                return true;
            }
        });
    }

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
        chatListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> receiverIds = new ArrayList<>();
                includeHanldeWhenChatChanged(receiverIds, snapshot);
                bunchOfReceiverId.postValue(receiverIds);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
    }

    private void includeHanldeWhenChatChanged(ArrayList<String> receiverIds, DataSnapshot snapshot) {
        for (DataSnapshot item : snapshot.getChildren()) {
            String receiverId = item.getKey();
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

}