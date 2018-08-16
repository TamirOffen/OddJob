package com.tamir.offen.OddJob;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.tamir.offen.OddJob.Add_Job.DateActivity;
import com.tamir.offen.OddJob.Map.map;
import com.tamir.offen.OddJob.Messaging.messages;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Main Activity";

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        bottomNavigationView = findViewById(R.id.bottomNavView_Bar);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Intent intent;

                switch(item.getItemId()) {
                    case R.id.nav_messages:
                        intent = new Intent(MainActivity.this, messages.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                        break;

                    case R.id.nav_map:
                        intent = new Intent(MainActivity.this, map.class);
                        startActivity(intent);
                        break;

                    case R.id.nav_add_work:
                        intent = new Intent(MainActivity.this, DateActivity.class);
                        startActivity(intent);
                        break;

                }

                return false;
            }
        });
    }
}
