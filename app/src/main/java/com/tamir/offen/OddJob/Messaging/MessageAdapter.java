package com.tamir.offen.OddJob.Messaging;

import android.graphics.Color;
import android.graphics.Path;
import android.icu.text.RelativeDateTimeFormatter;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tamir.offen.OddJob.R;
import com.tamir.offen.OddJob.User_Registration.User;

import java.util.List;

import static android.view.Gravity.END;
import static android.view.Gravity.RIGHT;
import static android.view.Gravity.START;

/**
 * Created by paen3 on 8/18/2018.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    private List<Message> userMessagesList;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseUsers;
    private TextView fullName;


    public MessageAdapter(List<Message> userMessagesList){
        this.userMessagesList = userMessagesList;
    }




    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.messages_layout_of_user,parent,false);

        mAuth = FirebaseAuth.getInstance();

        return new MessageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final MessageViewHolder holder, int position) {

        String message_sender_id  = mAuth.getCurrentUser().getUid();

        Message message = userMessagesList.get(position);

        String fromUserId = message.getFrom();

        databaseUsers = FirebaseDatabase.getInstance().getReference().child("users");


        if(fromUserId.equalsIgnoreCase(message_sender_id)){
            databaseUsers.child(fromUserId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String from = dataSnapshot.child("name").getValue().toString();
                    String fromFirstName[] = from.split(" ",2);
                    holder.messageSender.setText(fromFirstName[0]);
                    holder.messageSender.setGravity(Gravity.END);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            holder.messageText.setTextColor(Color.WHITE);
            holder.messageText.setGravity(Gravity.END);
        }
        else{
            databaseUsers.child(fromUserId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String to = dataSnapshot.child("name").getValue().toString();
                    String toFirstName[] = to.split(" ",2);
                    holder.messageSender.setText(toFirstName[0]);
                    holder.messageSender.setGravity(Gravity.START);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            holder.messageText.setBackgroundResource(R.drawable.message_text_background_two);

            holder.messageText.setTextColor(Color.BLACK);
            holder.messageText.setGravity(Gravity.START);

        }

        holder.messageText.setText(message.getMessage());


    }

    @Override
    public int getItemCount() {
        return userMessagesList.size();
    }


    public class MessageViewHolder extends RecyclerView.ViewHolder{
        public TextView messageText;
        public TextView messageSender;
        public MessageViewHolder(View view){
            super(view);
            messageText = (TextView) view.findViewById(R.id.message_text);
            messageSender = view.findViewById(R.id.textViewMessageSender);

        }

    }
}
