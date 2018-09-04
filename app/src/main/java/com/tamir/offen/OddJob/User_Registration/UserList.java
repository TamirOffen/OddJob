package com.tamir.offen.OddJob.User_Registration;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tamir.offen.OddJob.R;

import java.util.List;

/**
 * Created by paen3 on 8/15/2018.
 */

public class  UserList extends ArrayAdapter<User> {
    private Activity context;
    private List<User> userList;

    public UserList(Activity context, List<User> userList){
        super(context, R.layout.chat_selection_layout, userList);
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.chat_selection_layout,null,true);

        TextView textViewName = (TextView) listViewItem.findViewById(R.id.chat_name);
        TextView textViewEmail = (TextView) listViewItem.findViewById(R.id.chat_email);

        User user = userList.get(position);
        String id = user.getId();

        textViewName.setText(user.getName());
        textViewEmail.setText(user.getEmail());


        return listViewItem;
    }
}
