package com.tamir.offen.OddJob.Messaging;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tamir.offen.OddJob.Add_Job.AddActivity;
import com.tamir.offen.OddJob.ChattingActivity;
import com.tamir.offen.OddJob.Map.map;
import com.tamir.offen.OddJob.R;
import com.tamir.offen.OddJob.User_Registration.User;
import com.tamir.offen.OddJob.User_Registration.UserList;

import java.util.ArrayList;
import java.util.List;


public class ChatSelectionActivity extends AppCompatActivity {
    ListView listViewUsers;

    DatabaseReference databaseUsers;
    List<User> userList;
    private BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_selection);

        listViewUsers = (ListView) findViewById(R.id.listViewUsers);
        databaseUsers = FirebaseDatabase.getInstance().getReference("users");
        userList = new ArrayList<>();

        bottomNavigationView = findViewById(R.id.bottomNavView_Bar);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                switch(item.getItemId()) {
                    case R.id.nav_messages:
                        //intent = new Intent(messages.this, messages.class);
                        //startActivity(intent);
                        break;

                    case R.id.nav_map:
                        intent = new Intent(ChatSelectionActivity.this, map.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        break;

                    case R.id.nav_add_work:
                        intent = new Intent(ChatSelectionActivity.this, AddActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        break;

                }

                return false;
            }
        });


    }
    public void getChatterId(DataSnapshot dataSnapshot){
        String ref = dataSnapshot.getKey().toString();

    }

    @Override
    public void onStart() {
        super.onStart();
        databaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                userList.clear();
                for(DataSnapshot userSnapshot: dataSnapshot.getChildren()){
                    String parentId = userSnapshot.getKey();
                    User user = userSnapshot.getValue(User.class);
                    user.setParentId(parentId);
                    userList.add(user);
                    String chat_id = userSnapshot.getKey();
                }
                final UserList adapter = new UserList(ChatSelectionActivity.this, userList);
                listViewUsers.setAdapter(adapter);
                listViewUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                        User user = userList.get(position);

                        Intent chatIntent = new Intent(ChatSelectionActivity.this, com.tamir.offen.OddJob.Messaging.ChattingActivity.class);
                        chatIntent.putExtra("chat_id",user);
                        startActivity(chatIntent);
                        finish();

                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


}
