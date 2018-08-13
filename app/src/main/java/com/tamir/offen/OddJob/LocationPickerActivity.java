package com.tamir.offen.OddJob;

import android.content.ComponentCallbacks2;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.io.IOException;
import java.util.List;

public class LocationPickerActivity extends AppCompatActivity implements ComponentCallbacks2, GoogleApiClient.OnConnectionFailedListener{
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

    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(new LatLng(-40, -168), new LatLng(71, 136));

    private Button btnToTime, btnBackPrice;
    private RadioGroup locRadioGroup;
    private BottomNavigationView bottomNavigationView;
    private EditText streetAddressEditText, cityEditText, zipEditText, stateEditText;

    private AutoCompleteTextView customLocationSearch;
    private PlaceAutocompleteAdapter placeAutocompleteAdapter;
    private GoogleApiClient googleApiClient;

    //private AddJobHandler addJobHandler;
    private FusedLocationProviderClient fusedLocationProviderClient;

    private AddActivity addActivity = new AddActivity();
    private AddJobHandler newJob = addActivity.newJob;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_picker);

        btnBackPrice = findViewById(R.id.btnBackPrice);
        btnToTime = findViewById(R.id.btnToTime);
        bottomNavigationView = findViewById(R.id.bottomNavView_Bar);
        locRadioGroup = findViewById(R.id.radioGroup);


        customLocationSearch = findViewById(R.id.customLocationSearch);
        googleApiClient = new GoogleApiClient.Builder(this).addApi(Places.GEO_DATA_API).addApi(Places.PLACE_DETECTION_API).enableAutoManage(this, this).build();
        placeAutocompleteAdapter = new PlaceAutocompleteAdapter(this, Places.getGeoDataClient(this, null), LAT_LNG_BOUNDS, null);
        customLocationSearch.setAdapter(placeAutocompleteAdapter);
        ImageView progressbar = (ImageView) findViewById(R.id.progressbar4);
        progressbar.setImageBitmap(
                decodeSampledBitmapFromResource(getResources(), R.drawable.rlocpro, 400, 200));
        ImageView numberpro = (ImageView) findViewById(R.id.imageOf5);
        numberpro.setImageBitmap(BitmapOptimizer.decodeSampledBitmapFromResource(getResources(),R.drawable.shadowfour,100,100));


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

                final String currRadioBtn = radioButton.getText().toString();
                if (currRadioBtn.equals("Current Location")) {
                    setCustomAddressTextEditable(false);
                }
                if (currRadioBtn.equals("Custom Location")) {
                    setCustomAddressTextEditable(true);
                }

                btnToTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        com.tamir.offen.OddJob.LatLng addJobLoc = new com.tamir.offen.OddJob.LatLng(38.645139, -121.164702);
                        //LatLng addJobLocation = new LatLng(38.645139, -121.164702); //Folsom Intel
                        if (currRadioBtn.equals("Current Location")) {
                            map mMap = new map();
                            //addJobLocation = mMap.getCurrPosLatLng();
                            double lat = mMap.getCurrPosLatLng().latitude;
                            double lng = mMap.getCurrPosLatLng().longitude;
                            addJobLoc = new com.tamir.offen.OddJob.LatLng(lat, lng);
                        }
                        if (currRadioBtn.equals("Custom Location")) {
                            if(customLocationSearch.getText().toString().matches("")) {
                                Toast.makeText(LocationPickerActivity.this, "Fill out a location", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            String customLocation = customLocationSearch.getText().toString();
                            try {
                                //addJobLocation = getLatLngGeocoder(customLocation);
                                double lat = getLatLngGeocoder(customLocation).latitude;
                                double lng = getLatLngGeocoder(customLocation).longitude;
                                addJobLoc = new com.tamir.offen.OddJob.LatLng(lat, lng);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        //addJobHandler.setLocation(addJobLocation);
                        newJob.setLocation(addJobLoc);
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
            customLocationSearch.setAlpha(0.5f);
            customLocationSearch.setFocusable(editable);
        } else {
            customLocationSearch.setAlpha(1f);
            customLocationSearch.setFocusableInTouchMode(editable);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public LatLng getLatLngGeocoder(String location) throws IOException {
        Geocoder geocoder = new Geocoder(this);
        List<Address> list = geocoder.getFromLocationName(location, 1);
        Address address = list.get(0);
        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
        return latLng;
    }
}
