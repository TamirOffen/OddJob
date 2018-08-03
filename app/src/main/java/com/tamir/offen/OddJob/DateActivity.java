package com.tamir.offen.OddJob;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.res.Resources;
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

import java.util.Calendar;
import java.util.Date;

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

    private AddJobHandler addJobHandler;

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

        mTimePicker = findViewById(R.id.inputstarttime);
        mTimePicker1 = findViewById(R.id.inputendtime);
        mDisplayDate = findViewById(R.id.StartDateSelect);
        mDisplayDate1 = findViewById(R.id.EndDateSelect);
        btnBackLoc = findViewById(R.id.btnBackLoc);
        btnAddJob = findViewById(R.id.btnAddJob);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(), R.id.progressbar, options);
        ImageView timepro = findViewById(R.id.progressbar);
        options.inScaled = false;
        timepro.setImageBitmap(
                decodeSampledBitmapFromResource(getResources(), R.drawable.rtimepro, 100, 100));

        bottomNavigationView = findViewById(R.id.bottomNavView_Bar);

        addJobHandler = new AddJobHandler();

        mTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(),"time picker");
            }
        });
        mTimePicker1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

                /*
                if(mTimePicker1.getText().toString().matches("")) {
                    Toast.makeText(DateActivity.this, "Fill out an ending time", Toast.LENGTH_SHORT).show();
                    return;
                } */

                addJobHandler.setDate(0, mDisplayDate.getText().toString());
                addJobHandler.setDate(1, mDisplayDate1.getText().toString());
                addJobHandler.settime(0, mTimePicker.getText().toString());
                addJobHandler.settime(1, mTimePicker1.getText().toString());

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
                        intent = new Intent(DateActivity.this, messages.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                        break;

                    case R.id.nav_map:
                        intent = new Intent(DateActivity.this, map.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                        break;

                    case R.id.nav_add_work:
                        //intent = new Intent(AddJob.this, AddJob.class);
                        //startActivity(intent);
                        break;

                }

                return false;
            }
        });

    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }
}
