package com.tamir.offen.OddJob.Messaging;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.tamir.offen.OddJob.AddJob;
import com.tamir.offen.OddJob.Add_Job.AddJobHandler;
import com.tamir.offen.OddJob.User_Registration.*;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.tamir.offen.OddJob.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChattingActivity extends AppCompatActivity {

    private TextView receiver_name, chatStatus;
    private DatabaseReference databaseUsers, databaseJobs;
    private User user;
    private ImageButton sendMessage;
    private EditText inputMessage;
    private FirebaseAuth mAuth;
    private String messageSenderId, curjobId;
    private String messageReceiverId;
    private DatabaseReference rootRef;
    private Button gotoChatSelect;
    private RecyclerView userMessagesList;
    private final List<Message> messageList = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private MessageAdapter messageAdapter;
    private DatabaseReference NotificationReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);
        curjobId = (String) getIntent().getSerializableExtra("oddjob");
        user = (User) getIntent().getSerializableExtra("chat_id");
        receiver_name = findViewById(R.id.receiver_name);
        sendMessage = findViewById(R.id.sendMessage);
        rootRef = FirebaseDatabase.getInstance().getReference();
        inputMessage = findViewById(R.id.inputMessage);
        gotoChatSelect = findViewById(R.id.gotochatselect);
        mAuth = FirebaseAuth.getInstance();

        messageSenderId = mAuth.getCurrentUser().getUid();
        databaseUsers = FirebaseDatabase.getInstance().getReference().child("users");
        NotificationReference = FirebaseDatabase.getInstance().getReference().child("Notification");
        NotificationReference.keepSynced(true);
        String parentId = user.getParentId();
        messageReceiverId = user.getId();
        chatStatus = (TextView) findViewById(R.id.chat_status);
        messageAdapter = new MessageAdapter(messageList);

        userMessagesList = findViewById(R.id.messages_list_of_users);

        linearLayoutManager = new LinearLayoutManager(this);

        userMessagesList.setHasFixedSize(true);

        userMessagesList.setLayoutManager(linearLayoutManager);

        userMessagesList.setAdapter(messageAdapter);

        gotoChatSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        FetchMessages();

        /*if(mAuth.getCurrentUser().getUid().equals(oddjobIDd(curjobId))){
            chatStatus.setText("You have accepted this OddJob!");
        }else{
            chatStatus.setVisibility(View.GONE);
        }*/
        chatStatus.setVisibility(View.GONE);


        databaseUsers.child(user.getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue().toString();
                receiver_name.setText(name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendMessage();
            }
        });

    }

    private void FetchMessages() {
        rootRef.child("Messages").child(messageSenderId).child(messageReceiverId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Message message = dataSnapshot.getValue(Message.class);
                messageList.add(message);
                messageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void SendMessage() {
        String messageText = inputMessage.getText().toString();
        if(TextUtils.isEmpty(messageText)){
            Toast.makeText(ChattingActivity.this, "Please write your message", Toast.LENGTH_SHORT).show();
        }
        else{
            String message_sender_ref = "Messages/"+messageSenderId+"/"+ messageReceiverId;

            String message_receiver_ref = "Messages/"+messageReceiverId+"/"+ messageSenderId;

            DatabaseReference user_message_key = rootRef.child("Messages").child(messageSenderId).child(messageReceiverId).push();

            String message_push_id = user_message_key.getKey();

            Map messageTextBody = new HashMap();

            messageTextBody.put("message", messageText);
            messageTextBody.put("seen", false);
            messageTextBody.put("type", "text");
            messageTextBody.put("time", ServerValue.TIMESTAMP);
            messageTextBody.put("from", messageSenderId);


            Map messageBodyDetails = new HashMap();

            messageBodyDetails.put(message_sender_ref + "/" + message_push_id, messageTextBody);
            messageBodyDetails.put(message_receiver_ref + "/" + message_push_id, messageTextBody);

            rootRef.updateChildren(messageBodyDetails, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                    if(databaseError!=null){
                        Log.d("Chat_log", databaseError.getMessage().toString());
                    }
                    inputMessage.setText("");
                }
            });


        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ChattingActivity.this, ChatSelectionActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }
    public String oddjobIDd(final String string){
        databaseJobs = FirebaseDatabase.getInstance().getReference("Jobs");
        final String[] id = {};
        databaseJobs.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot jobsnapshot: dataSnapshot.getChildren()){
                    if(jobsnapshot.getKey().equals(string)){
                        AddJobHandler oddjob = jobsnapshot.getValue(AddJobHandler.class);
                        id[0] = oddjob.getAccepterID();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return id[0];
    }
}
