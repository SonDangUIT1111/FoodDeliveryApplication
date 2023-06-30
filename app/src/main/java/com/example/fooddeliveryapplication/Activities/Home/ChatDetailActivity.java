package com.example.fooddeliveryapplication.Activities.Home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.fooddeliveryapplication.Adapters.Home.ChatDetailAdapter;
import com.example.fooddeliveryapplication.Dialog.UploadDialog;
import com.example.fooddeliveryapplication.Model.Message;
import com.example.fooddeliveryapplication.Model.User;
import com.example.fooddeliveryapplication.databinding.ActivityChatDetailBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class ChatDetailActivity extends AppCompatActivity {
    ActivityChatDetailBinding binding;
    String publisherId;
    MutableLiveData<User> publisher=new MutableLiveData<>();
    ArrayList<Message> messages=new ArrayList<>();
    String userId;
    UploadDialog uploadDialog;
    ChatDetailAdapter chatDetailAdapter;
    DatabaseReference messageReference=FirebaseDatabase.getInstance().getReference("Message");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityChatDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //---------------------
        launchLoadingDialog();
        registerForObserver();
        initData();
    }

    private void initUI() {
        createAdapter();
        loadDataIntoUI();
        initEventOfComponent();

    }

    private void createAdapter() {
        chatDetailAdapter=new ChatDetailAdapter(ChatDetailActivity.this,messages,userId,publisherId);
    }

    private void loadDataIntoUI() {
        Glide.with(ChatDetailActivity.this)
                .load(publisher.getValue().getAvatarURL())
                .into(binding.imgPublisher);
        binding.txtNamePublisher.setText(publisher.getValue().getNameOfUser());
        binding.recycleViewMessage.setLayoutManager(new LinearLayoutManager(ChatDetailActivity.this, RecyclerView.VERTICAL,false));
        binding.recycleViewMessage.setAdapter(chatDetailAdapter);
    }

    private void initEventOfComponent() {
        setBackEvent();
        setSendMessageEvent();
    }

    private void setSendMessageEvent() {
        binding.btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message=binding.edtMessage.getText().toString().trim();
                sendMessage(message);
            }
        });
    }

    private void sendMessage(String message) {
        if (message.isEmpty()) {
            Toast.makeText(ChatDetailActivity.this,"Chưa nhập tin nhắn",Toast.LENGTH_SHORT).show();
        } else {
            Message newMessage=new Message(message,userId,System.currentTimeMillis(),false);
            loadMessageToFirebase(newMessage);
        }
    }

    private void loadMessageToFirebase(Message newMessage) {
//        DatabaseReference messageReference=FirebaseDatabase.getInstance().getReference("Message").child(userId).child(publisherId);
//        messageReference.push().setValue(newMessage).addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void unused) {
//                newMessage.setIdMessage(messageReference.getKey());
//                messageReference.child(publisherId).child(userId).push().setValue(newMessage);
//                binding.edtMessage.setText("");
//            }
//        });

        DatabaseReference userMessageReference=messageReference.child(userId).child(publisherId).push();
        newMessage.setIdMessage(userMessageReference.getKey());
        userMessageReference.setValue(newMessage).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                messageReference.child(publisherId).child(userId).child(newMessage.getIdMessage())
                        .setValue(newMessage);
                binding.edtMessage.setText("");
            }
        });
    }

    private void setBackEvent() {
        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void launchLoadingDialog() {
        createLoadingDialog();
        uploadDialog.show();
    }

    private void createLoadingDialog() {
        uploadDialog=new UploadDialog(ChatDetailActivity.this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void registerForObserver() {
        publisher.observe(ChatDetailActivity.this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (user!=null) {
                    handleChangedObserver();
                }
            }
        });
    }

    private void handleChangedObserver() {
        loadMessage();
        initUI();
        uploadDialog.dismiss();
    }

    private void initData() {
        userId= FirebaseAuth.getInstance().getCurrentUser().getUid();
        loadIntent();
    }

    private void loadMessage() {
        FirebaseDatabase.getInstance().getReference("Message").child(userId).child(publisherId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Message message=snapshot.getValue(Message.class);
                if (message.getSenderId()!=userId)
                {
                    DatabaseReference messageReferenceOfSender=FirebaseDatabase.getInstance().getReference("Message").child(userId)
                            .child(publisherId).child(message.getIdMessage()).child("seen");
                    DatabaseReference messageReferenceOfReiceiver=FirebaseDatabase.getInstance().getReference("Message").child(publisherId)
                            .child(userId).child(message.getIdMessage()).child("seen");
                    setMessageSeen();
                }
                messages.add(message);
                chatDetailAdapter.notifyItemInserted(messages.size()-1);
                binding.recycleViewMessage.scrollToPosition(chatDetailAdapter.getItemCount()-1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setMessageSeen(DatabaseReference reference, String messageId) {
    }

    private void loadIntent() {
        Intent intent=getIntent();
        if (isFromChatActivity(intent.getAction())) {
            handleIntentFromChatActivity(intent);
        } else {
            handleIntentFromProduntInfoActivity(intent);
        }
    }

    private void handleIntentFromProduntInfoActivity(Intent intent) {
        publisherId=intent.getStringExtra("publisherId");
        initPublisher(publisherId);
    }

    private void handleIntentFromChatActivity(Intent intent) {
        User pulisherTemp= (User) intent.getSerializableExtra("publisher");
        publisherId=pulisherTemp.getUserId();
        notifyToObserver(publisher,pulisherTemp);
    }

    public <T> void notifyToObserver(MutableLiveData<T> mutableLiveData, T object) {
        mutableLiveData.postValue(object);
    }

    private void initPublisher(String publisherId) {
        FirebaseDatabase.getInstance().getReference("Users").child(publisherId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                publisher.postValue(snapshot.getValue(User.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private boolean isFromChatActivity(String action) {
        return action.equalsIgnoreCase("chatActivity");
    }
}