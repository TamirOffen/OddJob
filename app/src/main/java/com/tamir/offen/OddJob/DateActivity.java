package com.tamir.offen.OddJob;

import android.app.ActivityManager;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import java.util.Random;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseReference;

import java.util.Calendar;

public class DateActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener{

    private static final String TAG = "DateActivity";

    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private TextView mDisplayDate1;
    private DatePickerDialog.OnDateSetListener mDateSetListener1;
    private TextView mTimePicker;
    private TextView mTimePicker1;
    private Button btnBackLoc, btnAddJob;
    private BottomNavigationView bottomNavigationView;

    //private AddJobHandler addJobHandler;
    private map mMap = new map();
    private AddActivity addActivity = new AddActivity();
    private AddJobHandler newJob = addActivity.newJob;
    //private DatabaseReference databaseReference = mMap.databaseReference;

    String callback = "";

    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {

        if(callback.equalsIgnoreCase("start_time")){
            TextView textView = (TextView)findViewById(R.id.inputstarttime);
            textView.setText(hourOfDay + ":" + minute);
        }
        else if (callback.equalsIgnoreCase("end_time")){
            TextView textView1 = (TextView)findViewById(R.id.inputendtime);
            textView1.setText(hourOfDay + ":" + minute);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date);

        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(mi);
        double availableMegs = mi.availMem / 0x100000L;
        double percentAvail = mi.availMem / (double)mi.totalMem * 100.0;

        mTimePicker = findViewById(R.id.inputstarttime);
        mTimePicker1 = findViewById(R.id.inputendtime);
        mDisplayDate = findViewById(R.id.StartDateSelect);
        mDisplayDate1 = findViewById(R.id.EndDateSelect);
        btnBackLoc = findViewById(R.id.btnBackLoc);
        btnAddJob = findViewById(R.id.btnAddJob);
        ImageView timepro = findViewById(R.id.progressbar4);
        timepro.setImageBitmap(
                BitmapOptimizer.decodeSampledBitmapFromResource(getResources(), R.drawable.rtimepro, 400, 200));
        ImageView numberpro = findViewById(R.id.imageOf5);
        numberpro.setImageBitmap(
                BitmapOptimizer.decodeSampledBitmapFromResource(getResources(), R.drawable.shadowfive,100, 100));

        bottomNavigationView = findViewById(R.id.bottomNavView_Bar);

        mTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback = "start_time";
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(),"time picker");
            }
        });
        mTimePicker1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback = "end_time";
                DialogFragment timePicker1 = new TimePicker1Fragment();
                timePicker1.show(getSupportFragmentManager(),"time picker");
            }
        });

        btnBackLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DateActivity.this, LocationPickerActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });


        mDisplayDate1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Calendar cal1 = Calendar.getInstance();
                int year1 = cal1.get(Calendar.YEAR);
                int month1 = cal1.get(Calendar.MONTH);
                int day1 = cal1.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        DateActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener1,
                        year1,month1,day1
                );
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        mDateSetListener1 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year1, int month1, int day1) {
                month1 = month1 + 1;
                Log.d(TAG, "onDateSet: " + month1 +"/" + day1 +"/" + year1);
                String date1 = month1 +"/" + day1 + "/" + year1;
                mDisplayDate1.setText(date1);
            }
        };



        mDisplayDate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        DateActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day
                );
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: " + month +"/" + day +"/" + year);
                String date = month +"/" + day + "/" + year;
                mDisplayDate.setText(date);
            }
        };

        btnAddJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(mDisplayDate.getText().toString().matches("")) {
                    Toast.makeText(DateActivity.this, "Fill out a starting date", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(mDisplayDate1.getText().toString().matches("")) {
                    Toast.makeText(DateActivity.this, "Fill out an ending date", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(mTimePicker.getText().toString().matches("")) {
                    Toast.makeText(DateActivity.this, "Fill out a starting time", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(mTimePicker1.getText().toString().matches("")) {
                    Toast.makeText(DateActivity.this, "Fill out an ending time", Toast.LENGTH_SHORT).show();
                    return;
                }


                newJob.setDate(mDisplayDate.getText().toString(), mDisplayDate1.getText().toString());
                newJob.setTime(mTimePicker.getText().toString(), mTimePicker1.getText().toString());
                newJob.setID(generateID(3,2));

                addJob();

                Intent intent = new Intent(DateActivity.this, map.class);
                intent.putExtra("add marker", "add marker");
                startActivity(intent);

            }
        });

        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Intent intent;

                switch(item.getItemId()) {
                    case R.id.nav_messages:
                        intent = new Intent(DateActivity.this, ChatSelectionActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                        break;

                    case R.id.nav_map:
                        intent = new Intent(DateActivity.this, map.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                        break;

                    case R.id.nav_add_work:
                        break;

                }

                return false;
            }
        });

    }

    public void addJob() {
        DatabaseReference newJobReference = mMap.databaseReference.push();
        newJobReference.setValue(newJob);

        /*
        AddJobHandler job = new AddJobHandler();

        String title = job.getTitle();
        String desc = job.getDesc();
        String tag = job.getTag();
        LatLng loc = job.getLocation();
        String price = job.getPrice();
        String startDate = job.getDate(0), endDate = job.getDate(1);
        String startTime = job.gettime(0), endTime = job.gettime(1);

        //Toast.makeText(this, "Title: " + title + " Desc: " + desc + " Tag: " + tag + " Price: " + price + " Date: " + startDate + endDate + " Time: " + startTime + endTime, Toast.LENGTH_LONG).show();

        String id = mMap.databaseReference.push().getKey();
        
        mMap.databaseReference.child(id).setValue(job);

        Toast.makeText(DateActivity.this, "Job Added to Database", Toast.LENGTH_SHORT).show();
        */
    }

    private String generateID(int intBound, int letterBound) {
        String[] letters = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
        String id = "";
        Random rand = new Random();
        for(int i = 0; i < intBound; i++) {
            int randNum = rand.nextInt(10);
            id += randNum;
        }
        for(int i = 0; i < letterBound; i++) {
            int num = rand.nextInt(26);
            String randLetter = letters[num];
            id += randLetter;
        }
        return id;
    }

}
