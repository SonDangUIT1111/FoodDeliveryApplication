package com.example.fooddeliveryapplication.Activities.Home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fooddeliveryapplication.Adapters.Home.ChatDetailAdapter;
import com.example.fooddeliveryapplication.Dialog.LoadingDialog;
import com.example.fooddeliveryapplication.Helpers.FirebaseNotificationHelper;
import com.example.fooddeliveryapplication.Model.Message;
import com.example.fooddeliveryapplication.Model.Notification;
import com.example.fooddeliveryapplication.Model.User;
import com.example.fooddeliveryapplication.R;
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
import java.util.List;
import java.util.Objects;

public class ChatDetailActivity extends AppCompatActivity {
    private ActivityChatDetailBinding binding;
    private String publisherId;
    private MutableLiveData<User> publisher = new MutableLiveData<>();
    private ArrayList<Message> messages = new ArrayList<>();
    private String userId;
    private LoadingDialog uploadDialog;
    private ChatDetailAdapter chatDetailAdapter;
    private DatabaseReference messageReference = FirebaseDatabase.getInstance().getReference("Message");
    private ChildEventListener messageListener;
    private Notification notification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        launchLoadingDialog();
        registerForObserver();
        registerListenerForMessage();
        initData();
    }

    private void registerListenerForMessage() {
        messageListener=new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Message message=snapshot.getValue(Message.class);
                if (!message.getSenderId().equalsIgnoreCase(userId)) {
                    DatabaseReference messageReferenceOfSender=FirebaseDatabase.getInstance().getReference("Message").child(userId)
                            .child(publisherId).child(message.getIdMessage()).child("seen");
                    DatabaseReference messageReferenceOfReiceiver=FirebaseDatabase.getInstance().getReference("Message").child(publisherId)
                            .child(userId).child(message.getIdMessage()).child("seen");
                    setMessageSeen(messageReferenceOfSender);
                    setMessageSeen(messageReferenceOfReiceiver);
                }
                messages.add(message);
                chatDetailAdapter.notifyItemInserted(messages.size()-1);
                binding.recycleViewMessage.scrollToPosition(chatDetailAdapter.getItemCount()-1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Message newMessage = snapshot.getValue(Message.class);
                String messageKey = snapshot.getKey();

                for (int i = 0; i < messages.size(); i++) {
                    Message currentMessage = messages.get(i);
                    if (currentMessage.getIdMessage().equals(messageKey)) {
                        messages.set(i, newMessage);
                        chatDetailAdapter.notifyItemChanged(i);
                        break;
                    }
                }
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
        };
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
        Glide.with(ChatDetailActivity.this).load(publisher.getValue().getAvatarURL()).placeholder(R.drawable.default_avatar).error(R.drawable.image_default).into(binding.imgPublisher);
        binding.txtNamePublisher.setText(publisher.getValue().getUserName());
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
                String message = binding.edtMessage.getText().toString().trim();
                sendMessage(message);
            }
        });
    }

    private void sendMessage(String message) {
        if (!message.isEmpty()) {
            Message newMessage = new Message(message, userId, System.currentTimeMillis(), false);
            loadMessageToFirebase(newMessage);
        }
    }

    private void loadMessageToFirebase(Message newMessage) {
        DatabaseReference userMessageReference=messageReference.child(userId).child(publisherId).push();
        newMessage.setIdMessage(userMessageReference.getKey());
        userMessageReference.setValue(newMessage).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                messageReference.child(publisherId).child(userId).child(newMessage.getIdMessage()).setValue(newMessage);
                binding.edtMessage.setText("");
                pushSendMessageNotification();
            }
        });
    }

    private void pushSendMessageNotification() {
        FirebaseDatabase.getInstance().getReference().child("Users").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User sender = snapshot.getValue(User.class);
                String title = "New message";
                String content = sender.getUserName() + " sent you a new message. Please check it!";
                Notification notification;
                if (!sender.getAvatarURL().equals("")) {
                    notification = FirebaseNotificationHelper.createNotification(title, content, sender.getAvatarURL(), "None", "None", "None", sender);
                }
                else {
                    notification = FirebaseNotificationHelper.createNotification(title, content, "https://t4.ftcdn.net/jpg/01/18/03/35/360_F_118033506_uMrhnrjBWBxVE9sYGTgBht8S5liVnIeY.jpg", "None", "None", "None", sender);
                }
                new FirebaseNotificationHelper(ChatDetailActivity.this).addNotification(publisherId, notification, new FirebaseNotificationHelper.DataStatus() {
                    @Override
                    public void DataIsLoaded(List<Notification> notificationList, List<Notification> notificationListToNotify) {

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

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
        uploadDialog=new LoadingDialog(ChatDetailActivity.this);
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
                if (user != null) {
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
        FirebaseDatabase.getInstance().getReference("Message").child(userId).child(publisherId).addChildEventListener(messageListener);
    }



    private void setMessageSeen(DatabaseReference reference) {
        reference.setValue(true);
    }

    private void loadIntent() {
        Intent intent=getIntent();
        if (isFromChatActivity(intent.getAction())) {
            handleIntentFromChatActivity(intent);
        }
        else if (isFromHomeActivity(intent.getAction())) {
            handleIntentFromHomeActivity(intent);
        }
        else {
            handleIntentFromProductInfoActivity(intent);
        }
    }

    private void handleIntentFromHomeActivity(Intent intent) {
        User user = (User) getIntent().getSerializableExtra("publisher");
        publisherId = user.getUserId();
        notifyToObserver(publisher, user);
    }

    private void handleIntentFromProductInfoActivity(Intent intent) {
        publisherId = intent.getStringExtra("publisherId");
        initPublisher(publisherId);
    }

    private void handleIntentFromChatActivity(Intent intent) {
        User pulisherTemp = (User) intent.getSerializableExtra("publisher");
        publisherId = pulisherTemp.getUserId();
        notifyToObserver(publisher, pulisherTemp);
    }

    public void notifyToObserver(MutableLiveData<User> mutableLiveData, User object) {
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

    private boolean isFromHomeActivity(String action) {
        return action.equalsIgnoreCase("homeActivity");
    }
}