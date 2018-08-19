package com.tamir.offen.OddJob;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChattingActivity extends AppCompatActivity {

    private TextView receiver_name;
    private DatabaseReference databaseUsers;
    private User user;
    private ImageButton sendMessage;
    private EditText inputMessage;
    private FirebaseAuth mAuth;
    private String messageSenderId;
    private String messageReceiverId;
    private DatabaseReference rootRef;
    private RecyclerView userMessagesList;
    private final List<Message> messageList = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private MessageAdapter messageAdapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);
        user = (User) getIntent().getSerializableExtra("chat_id");
        receiver_name = (TextView) findViewById(R.id.receiver_name);
        sendMessage = (ImageButton) findViewById(R.id.sendMessage);
        rootRef = FirebaseDatabase.getInstance().getReference();
        inputMessage = (EditText) findViewById(R.id.inputMessage);
        mAuth = FirebaseAuth.getInstance();
        messageSenderId = mAuth.getCurrentUser().getUid();
        databaseUsers = FirebaseDatabase.getInstance().getReference().child("users");
        String parentId = user.getParentId();
        messageReceiverId = user.getId();
        messageAdapter = new MessageAdapter(messageList);

        userMessagesList = (RecyclerView) findViewById(R.id.messages_list_of_users);

        linearLayoutManager = new LinearLayoutManager(this);

        userMessagesList.setHasFixedSize(true);

        userMessagesList.setLayoutManager(linearLayoutManager);

        userMessagesList.setAdapter(messageAdapter);

        FetchMessages();

        databaseUsers.child(parentId).addValueEventListener(new ValueEventListener() {
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


        //receiver_name.setText(chat_id);
        //Toast.makeText(ChattingActivity.this, chat_id, Toast.LENGTH_SHORT).show();
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
}
