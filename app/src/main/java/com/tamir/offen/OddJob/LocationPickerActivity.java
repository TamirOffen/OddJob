package com.tamir.offen.OddJob;

import android.content.ComponentCallbacks2;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class LocationPickerActivity extends AppCompatActivity implements ComponentCallbacks2 {
    public void onTrimMemory(int level) {

        // Determine which lifecycle or system event was raised.
        switch (level) {

            case ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN:

                /*
                   Release any UI objects that currently hold memory.

                   The user interface has moved to the background.
                */

                break;

            case ComponentCallbacks2.TRIM_MEMORY_RUNNING_MODERATE:
            case ComponentCallbacks2.TRIM_MEMORY_RUNNING_LOW:
            case ComponentCallbacks2.TRIM_MEMORY_RUNNING_CRITICAL:

                /*
                   Release any memory that your app doesn't need to run.

                   The device is running low on memory while the app is running.
                   The event raised indicates the severity of the memory-related event.
                   If the event is TRIM_MEMORY_RUNNING_CRITICAL, then the system will
                   begin killing background processes.
                */

                break;

            case ComponentCallbacks2.TRIM_MEMORY_BACKGROUND:
            case ComponentCallbacks2.TRIM_MEMORY_MODERATE:
            case ComponentCallbacks2.TRIM_MEMORY_COMPLETE:

                /*
                   Release as much memory as the process can.

                   The app is on the LRU list and the system is running low on memory.
                   The event raised indicates where the app sits within the LRU list.
                   If the event is TRIM_MEMORY_COMPLETE, the process will be one of
                   the first to be terminated.
                */

                break;

            default:
                /*
                  Release any non-critical data structures.

                  The app received an unrecognized memory level value
                  from the system. Treat this as a generic low-memory message.
                */
                break;
        }
    }

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
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(), R.id.progressbar, options);
        int imageHeight = options.outHeight;
        int imageWidth = options.outWidth;
        String imageType = options.outMimeType;
        ImageView progressbar =(ImageView) findViewById(R.id.progressbar);
        progressbar.setImageBitmap(
                decodeSampledBitmapFromResource(getResources(), R.drawable.rlocpro, 0, 100));
        options.inScaled = false;
        BitmapFactory.decodeResource(getResources(), R.id.progressbar, options);



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
                        //Toast.makeText(LocationPickerActivity.this, "NOT ACTIVE, CLICK ON ADD PIN", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(LocationPickerActivity.this, DateActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

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
