package com.tamir.offen.OddJob;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import java.lang.annotation.Inherited;
import java.sql.Time;
import java.util.Calendar;

public class DateActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener{

    private static final String TAG = "DateActivity";

    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private TextView mDisplayDate1;
    private DatePickerDialog.OnDateSetListener mDateSetListener1;
    private TextView mTimePicker;
    private TextView mTimePicker1;
    private TimePickerDialog.OnTimeSetListener mTimeSetListener1;
    private Button btnBackLoc;
    private TimePickerDialog.OnTimeSetListener mTimeSetListener;
    String callback = "";


    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        if(callback.equalsIgnoreCase("for_start")){
            TextView textView = (TextView) findViewById(R.id.inputstarttime);
            textView.setText(i + ":" + i1);
        }
        if(callback.equalsIgnoreCase("for_end")){
            TextView textView1 = (TextView) findViewById(R.id.inputendtime);
            textView1.setText(i+":"+i1);
        }
        callback="";

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date);



        mTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback="for_start";
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(),"time picker");

            }
        });

        mTimePicker1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback = "for_end";
                DialogFragment timePicker1 = new TimePicker1Fragment();
                timePicker1.show(getSupportFragmentManager(),"time picker1");

            }

        });


        mTimePicker = (TextView) findViewById(R.id.inputstarttime);
        mTimePicker1 = (TextView) findViewById(R.id.inputendtime);
        mDisplayDate = (TextView) findViewById(R.id.StartDateSelect);
        mDisplayDate1 = (TextView) findViewById(R.id.EndDateSelect);
        btnBackLoc = findViewById(R.id.btnBackLoc);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inScaled = false;
        BitmapFactory.decodeResource(getResources(), R.id.progressbar, options);
        int imageHeight = options.outHeight;
        int imageWidth = options.outWidth;
        String imageType = options.outMimeType;
        ImageView timepro = (ImageView) findViewById(R.id.progressbar);
        timepro.setImageBitmap(
                decodeSampledBitmapFromResource(getResources(), R.drawable.rtimepro, 150 , 150));
        Bitmap source = BitmapFactory.decodeResource(getResources(), R.drawable.timepro, options);



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
