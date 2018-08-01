package com.tamir.offen.OddJob;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import org.w3c.dom.Text;

import java.util.Calendar;

public class DateActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener{

    private static final String TAG = "DateActivity";

    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private TextView mDisplayDate1;
    private DatePickerDialog.OnDateSetListener mDateSetListener1;
    private TextView mTimePicker;
    private TextView mTimePicker1;
    private Button btnBackLoc;

    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
        TextView textView = (TextView)findViewById(R.id.inputstarttime);
        textView.setText(hourOfDay +":" +minute);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date);

        mTimePicker = (TextView) findViewById(R.id.inputstarttime);
        mTimePicker1 = (TextView) findViewById(R.id.inputendtime);
        mDisplayDate = (TextView) findViewById(R.id.StartDateSelect);
        mDisplayDate1 = (TextView) findViewById(R.id.EndDateSelect);
        btnBackLoc = findViewById(R.id.btnBackLoc);

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

    }
}
