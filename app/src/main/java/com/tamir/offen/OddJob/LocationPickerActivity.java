package com.tamir.offen.OddJob;

import android.Manifest;
import android.content.ComponentCallbacks2;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
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

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Task;

public class LocationPickerActivity extends AppCompatActivity implements ComponentCallbacks2, OnMapReadyCallback {
    public void onTrimMemory(int level) {

        // Determine which lifecycle or system event was raised.
        switch (level) {

            case ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN:
                break;

            case ComponentCallbacks2.TRIM_MEMORY_RUNNING_MODERATE:
            case ComponentCallbacks2.TRIM_MEMORY_RUNNING_LOW:
            case ComponentCallbacks2.TRIM_MEMORY_RUNNING_CRITICAL:
                break;

            case ComponentCallbacks2.TRIM_MEMORY_BACKGROUND:
            case ComponentCallbacks2.TRIM_MEMORY_MODERATE:
            case ComponentCallbacks2.TRIM_MEMORY_COMPLETE:
                break;

            default:
                break;
        }
    }

    private Button btnToTime, btnBackPrice;
    private RadioGroup locRadioGroup;
    private BottomNavigationView bottomNavigationView;
    private EditText streetAddressEditText, cityEditText, zipEditText, stateEditText;
    private AddJobHandler addJobHandler;
    private FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_picker);

        btnBackPrice = findViewById(R.id.btnBackPrice);
        btnToTime = findViewById(R.id.btnToTime);
        bottomNavigationView = findViewById(R.id.bottomNavView_Bar);
        locRadioGroup = findViewById(R.id.radioGroup);
        streetAddressEditText = findViewById(R.id.customLocationEditText);
        cityEditText = findViewById(R.id.cityEditText);
        zipEditText = findViewById(R.id.zipEditText);
        stateEditText = findViewById(R.id.stateEditText);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(), R.id.progressbar1, options);
        int imageHeight = options.outHeight;
        int imageWidth = options.outWidth;
        String imageType = options.outMimeType;
        ImageView progressbar = (ImageView) findViewById(R.id.progressbar);
        progressbar.setImageBitmap(
                decodeSampledBitmapFromResource(getResources(), R.drawable.rlocpro, 0, 100));
        options.inScaled = false;
        BitmapFactory.decodeResource(getResources(), R.id.progressbar1, options);

        addJobHandler = new AddJobHandler();

        setCustomAddressTextEditable(false);

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

                final LatLng testLatLng = new LatLng(38.645139, -121.164702); //Intel

                final String currRadioBtn = radioButton.getText().toString();
                if (currRadioBtn.equals("Custom Location")) {
                    setCustomAddressTextEditable(true);

                }
                if (currRadioBtn.equals("Current Location")) {
                    setCustomAddressTextEditable(false);
                    map mMap = new map();
                    LatLng curLoc = mMap.getCurrPosLatLng();
                    addJobHandler.setLocation(curLoc);
                }

                btnToTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        addJobHandler.setLocation(testLatLng);
                        Intent intent = new Intent(LocationPickerActivity.this, DateActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

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
                switch (item.getItemId()) {
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

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
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

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {

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

    private void setCustomAddressTextEditable(boolean editable) {
        if (!editable) {
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


    private void getDeviceLocation() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        final Task location = fusedLocationProviderClient.getLastLocation();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }
}
