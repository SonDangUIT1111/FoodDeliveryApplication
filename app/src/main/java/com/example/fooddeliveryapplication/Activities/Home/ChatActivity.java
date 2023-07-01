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

import com.example.fooddeliveryapplication.Adapters.Home.ChatAdapter;
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
    MutableLiveData <ArrayList<User>> bunchOfReceiver=new MutableLiveData<>();
    MutableLiveData<ArrayList<String>> bunchOfReceiverId=new MutableLiveData<>();
    ArrayList<User> tempBunchOfReceiver=new ArrayList<>();
    DatabaseReference chatReference;
    ValueEventListener chatListener;
    ValueEventListener receiverListener;
    ChatAdapter chatAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        userId=getIntent().getStringExtra("userId");
        initUI();
        createReference();
        createObserverOfReceiverId();
        createObserverOfReceiver();
        createChatListener();
        loadReceiverId();

    }

    private void initUI() {
        chatAdapter=new ChatAdapter(ChatActivity.this,tempBunchOfReceiver);
        binding.recycleViewMessage.setLayoutManager(new LinearLayoutManager(ChatActivity.this, RecyclerView.VERTICAL,false));
        binding.recycleViewMessage.setAdapter(chatAdapter);
    }

    private void createObserverOfReceiver() {
        bunchOfReceiver.observe(this, new Observer<ArrayList<User>>() {
            @Override
            public void onChanged(ArrayList<User> users) {
                chatAdapter.notifyDataSetChanged();
            }
        });
    }

    private void createObserverOfReceiverId() {
        bunchOfReceiverId.observe(this, new Observer<ArrayList<String>>() {
            @Override
            public void onChanged(ArrayList<String> strings) {
                loadReceiver(bunchOfReceiverId.getValue());
            }
        });
    }

    private void loadReceiver(ArrayList<String> receiverIds) {
        tempBunchOfReceiver.clear();
        loadReceiverById(0,receiverIds);
    }

    private void loadReceiverById(int index, ArrayList<String> receiverIds) {
        FirebaseDatabase.getInstance().getReference("Users").child(receiverIds.get(index)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User tmp=snapshot.getValue(User.class);
                tempBunchOfReceiver.add(tmp);
                if (!(index ==receiverIds.size()-1)) {
                    loadReceiverById(index+1,receiverIds);
                } else {
                    bunchOfReceiver.postValue(tempBunchOfReceiver);
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
                for (DataSnapshot item:snapshot.getChildren()) {
                    String receiverId=item.getKey();
                    receiverIds.add(receiverId);
                }
                bunchOfReceiverId.postValue(receiverIds);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
    }

    private void createReference() {
        chatReference= FirebaseDatabase.getInstance().getReference("Message").child(userId);
    }

    private void loadReceiverId() {
            chatReference.addValueEventListener(chatListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding=null;
    }
}