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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
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
import com.tamir.offen.OddJob.Messaging.Message;
import com.tamir.offen.OddJob.User_Registration.UserList;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ChatSelectionActivity extends AppCompatActivity {

    /*Initialize List of Recent Chats Listview, DatabaseReferences,
    a list for those recent chat users, and Bottom Navigation Element a*/
    private ListView listViewUsers;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseUsers;
    private DatabaseReference databaseMessaging;
    public static List<User> userList;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_selection);

        listViewUsers = findViewById(R.id.listViewUsers);

        //Call the Firebase Databsase References
        databaseUsers = FirebaseDatabase.getInstance().getReference().child("users");
        databaseMessaging = FirebaseDatabase.getInstance().getReference().child("Messages");
        firebaseAuth = FirebaseAuth.getInstance();

        /*Creating an ArrayList to hold the names and emails of users
        I should show to the current user*/
        userList = new ArrayList<>();

        /*Creating a list to hold all the keys of the users
        the current User has chatted with*/
        final List<String> messagingUsers = new ArrayList<>();


        /*The way I setup the database of messages is that the user's UID
        key is the parent of all the UID keys that it has chat history with
        This way, it is easy to make a list of recent chats*/
        databaseMessaging.child(firebaseAuth.getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot usersnapshot: dataSnapshot.getChildren()){

                    /*Iterate through the childs of the current Users UID key
                    and add those user keys to a list*/
                    messagingUsers.add(usersnapshot.getKey());
                 }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //Refer to this very important function at the bottom of this code
        fetchUserList(messagingUsers);


        //This code is just for the
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


    //!!IMPORTANT FUNCTION!!

    public void fetchUserList(final List<String> list){

        //This function is very important in that it takes in a list of user UID keys
        //that the current user has talked to and, from this, inflates the ListView setup
        //on the activity with their corresponding names and email addresses

        databaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                userList.clear();


                //This loop iterates through all the users in the User database and if
                //the their key is the messagingUsers list that I passed in,
                //it will add that UID to a final list of user

                for(DataSnapshot userSnapshot: dataSnapshot.getChildren()){

                    String parentId = userSnapshot.getKey();
                    User user = userSnapshot.getValue(User.class);


                    if(list.contains(user.getId())){

                        user.setParentId(parentId);
                        userList.add(user);
                        String chat_id = userSnapshot.getKey();

                    }
                }

                //this other adapter class I created allows me to inflate the listview with the write information
                final UserList adapter = new UserList(ChatSelectionActivity.this, userList);

                //inflates the listview
                listViewUsers.setAdapter(adapter);

                //this part makes sure when the user clicks on it, they are transferred to an
                //activity that brings up the chat history of that user
                listViewUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                        User user = userList.get(position);

                        Intent chatIntent = new Intent(ChatSelectionActivity.this, com.tamir.offen.OddJob.Messaging.ChattingActivity.class);
                        chatIntent.putExtra("chat_id", user);
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
