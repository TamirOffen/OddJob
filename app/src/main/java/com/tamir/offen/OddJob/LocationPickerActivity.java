package com.tamir.offen.OddJob;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class LocationPickerActivity extends AppCompatActivity {

    private Button btnToTime, btnBackPrice, btnAddPin;
    private RadioGroup locRadioGroup;
    private BottomNavigationView bottomNavigationView;
    private String title, desc, tag, price, location;
    private EditText streetAddressEditText, cityEditText, zipEditText, stateEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_picker);

        btnBackPrice = findViewById(R.id.btnBackPrice);
        btnToTime = findViewById(R.id.btnToTime);
        btnAddPin = findViewById(R.id.btnAddPin);
        bottomNavigationView = findViewById(R.id.bottomNavView_Bar);
        locRadioGroup = findViewById(R.id.radioGroup);
        streetAddressEditText = findViewById(R.id.streetAddressEditText);
        cityEditText = findViewById(R.id.cityEditText);
        zipEditText = findViewById(R.id.zipEditText);
        stateEditText = findViewById(R.id.stateEditText);

        setCustomAddressTextEditable(false);

        title = getBundleStringInfo("title");
        desc = getBundleStringInfo("desc");
        tag = getBundleStringInfo("tag");
        price = getBundleStringInfo("price");

        //Toast.makeText(this, title+ " " + desc+" "+tag+" "+price, Toast.LENGTH_SHORT).show();

        btnBackPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LocationPickerActivity.this, PriceActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        
        locRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                final RadioButton radioButton = locRadioGroup.findViewById(i);
                int index = radioGroup.indexOfChild(radioButton);

                final String currRadioBtn = radioButton.getText().toString();
                if(currRadioBtn.equals("Custom Location")) {
                    setCustomAddressTextEditable(true);

                }
                if(currRadioBtn.equals("Current Location")) {
                    setCustomAddressTextEditable(false);
                }

                btnToTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(LocationPickerActivity.this, "NOT ACTIVE, CLICK ON ADD PIN", Toast.LENGTH_SHORT).show();
                        /*
                        Intent intent = new Intent(LocationPickerActivity.this, DateActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        */
                    }
                });

                btnAddPin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(LocationPickerActivity.this, map.class);
                        intent.putExtra("location", currRadioBtn);
                        intent.putExtra("price", price);
                        intent.putExtra("tag", tag);
                        intent.putExtra("title", title);
                        intent.putExtra("desc", desc);
                        startActivity(intent);
                    }
                });
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
                        intent = new Intent(LocationPickerActivity.this, messages.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                        break;

                    case R.id.nav_map:
                        intent = new Intent(LocationPickerActivity.this, map.class);
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

    private String getBundleStringInfo(String tag) {
        Intent intentExtras = getIntent();
        Bundle extrasBundle = intentExtras.getExtras();
        if(extrasBundle != null) {
            if(extrasBundle.containsKey(tag)) {
                return extrasBundle.getString(tag);
            }
        }
        return "TAG NOT FOUND";
    }

    private void setCustomAddressTextEditable(boolean editable) {
        if(!editable) {
            streetAddressEditText.setAlpha(0.5f);
            cityEditText.setAlpha(0.5f);
            zipEditText.setAlpha(0.5f);
            stateEditText.setAlpha(0.5f);
            streetAddressEditText.setFocusable(editable);
            cityEditText.setFocusable(editable);
            zipEditText.setFocusable(editable);
            stateEditText.setFocusable(editable);
        } else {
            streetAddressEditText.setAlpha(1f);
            cityEditText.setAlpha(1f);
            zipEditText.setAlpha(1f);
            stateEditText.setAlpha(1f);
            streetAddressEditText.setFocusableInTouchMode(editable);
            cityEditText.setFocusableInTouchMode(editable);
            zipEditText.setFocusableInTouchMode(editable);
            stateEditText.setFocusableInTouchMode(editable);
        }
    }
}
