package com.tamir.offen.OddJob.Messaging;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.tamir.offen.OddJob.R;

import java.util.List;

import static android.view.Gravity.RIGHT;

/**
 * Created by paen3 on 8/18/2018.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    private List<Message> userMessagesList;
    private FirebaseAuth mAuth;


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
    public void onBindViewHolder(MessageViewHolder holder, int position) {

        String message_sender_id  = mAuth.getCurrentUser().getUid();

        Message message = userMessagesList.get(position);

        String fromUserId = message.getFrom();

        if(fromUserId.equalsIgnoreCase(message_sender_id)){
            holder.messageText.setTextColor(Color.WHITE);
            holder.messageText.setGravity(Gravity.END);
        }
        else{
            holder.messageText.setBackgroundResource(R.drawable.message_text_background_two);
            holder.messageText.setTextColor(Color.WHITE);
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
        public MessageViewHolder(View view){
            super(view);
            messageText = (TextView) view.findViewById(R.id.message_text);

        }

    }
}
