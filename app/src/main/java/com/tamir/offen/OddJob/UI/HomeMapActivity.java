package com.tamir.offen.OddJob.UI;

import android.app.DatePickerDialog;
import android.support.v4.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.tamir.offen.OddJob.R;

import java.text.DateFormat;
import java.util.Calendar;

public class HomeMapActivity extends AppCompatActivity {
   // private BottomNavigationView mMainNav;
    //private FrameLayout mMainFrame;
    //private ChatFragment chatFragment;
    //private MapFragment mapFragment;


    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    android.support.v4.app.Fragment selectedFragment = null;

                    switch(item.getItemId()){
                        case R.id.chat:
                            selectedFragment = new com.tamir.offen.OddJob.UI.ChatFragment();
                            break;
                        case R.id.home:
                            selectedFragment = new com.tamir.offen.OddJob.UI.MapFragment();
                            break;
                        case R.id.add_job:
                            selectedFragment = new com.tamir.offen.OddJob.UI.AddFragment();
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, selectedFragment).commit();
                    return true;
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_map);
        BottomNavigationView bottomNav = findViewById(R.id.NavBot);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        Button tvDate = (Button) findViewById(R.id.tvDate);
        /*tvDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(),"date picker");
            }
        });
   */ }
/*
    public void onDateSet(DatePicker view, int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);
        String currentdatestring = DateFormat.getDateInstance().format(c.getTime());
        Button tvDate = (Button) findViewById(R.id.tvDate);
        tvDate.setText(currentdatestring);


    }
*/


}
