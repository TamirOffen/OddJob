package com.tamir.offen.actionbar;
// Created on 7/25/2018 

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

public class map extends AppCompatActivity implements OnMapReadyCallback {

    //map act

    // Constants
    private static final String TAG = "MapActivity",
                                FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION,
                                COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 17f;
    private static final double NO_VALUE_BUNDLE_NUMBER = -1f;

    // Vars
    private BottomNavigationView bottomNavigationView;
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Boolean locationPermissionsGranted = false;
    private double addJobLat, addJobLng;
    private ArrayList<Marker> markerList = new ArrayList<>();



    private TextView zoomText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // checks if the correct Google Play Services are installed
        if (!isServicesOk()) {
            Intent intent = new Intent(map.this, MainActivity.class);
            startActivity(intent);
        }

        getLocationPermission();


        zoomText = findViewById(R.id.zoomText);


        //Toast.makeText(this, "Lat: " + getBundleDoubleInfo("latitude") + " Lng: " + getBundleDoubleInfo("longitude"), Toast.LENGTH_SHORT).show();

        bottomNavigationView = findViewById(R.id.bottomNavView_Bar);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                switch (item.getItemId()) {
                    case R.id.nav_messages:
                        intent = new Intent(map.this, messages.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                        break;
                    case R.id.nav_map:
                        break;
                    case R.id.nav_add_work:
                        intent = new Intent(map.this, AddJob.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        break;
                }
                return false;
            }
        });
    }

    // returns if correct Google Play Services are installed
    public boolean isServicesOk() {
        Log.d(TAG, "isServicesOk: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(map.this);

        if (available == ConnectionResult.SUCCESS) {
            // everything is fine and the user can make map requests
            Log.d(TAG, "isServicesOk: Google Play Services is working");
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            // an error occured but we can resolve it
            Log.d(TAG, "isServicesOk: an error occured, but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(map.this, available, 0101);
            dialog.show();
        } else {
            Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show();
        }

        return false;
    }

    // checks for location permission from user
    // if not clicked, then an ActivityCompat asks for permission
    private void getLocationPermission(){
        Log.d(TAG, "getLocationPermission: getting location permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                locationPermissionsGranted = true;
                initMap();
            }else{
                ActivityCompat.requestPermissions(this,
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        }else{
            ActivityCompat.requestPermissions(this,
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // goes with getLocationPermission
        Log.d(TAG, "onRequestPermissionsResult: called.");
        locationPermissionsGranted = false;

        switch(requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:{
                if(grantResults.length > 0){
                    for(int i = 0; i < grantResults.length; i++){
                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            locationPermissionsGranted = false;
                            Log.d(TAG, "onRequestPermissionsResult: permission failed");
                            return;
                        }
                    }
                    Log.d(TAG, "onRequestPermissionsResult: permission granted");
                    locationPermissionsGranted = true;
                    //initialize our map
                    initMap();
                }
            }
        }
    }

    // gets the device's current location and moves there
    private void getDeviceLocation() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try {
            if(locationPermissionsGranted) {
                final Task location = fusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {

                            // current location marker
                            Location currentLocation = (Location)task.getResult();
                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), DEFAULT_ZOOM);

                            // add job marker
                            addJobLat = getBundleDoubleInfo("latitude"); addJobLng = getBundleDoubleInfo("longitude");
                            if(addJobLat != NO_VALUE_BUNDLE_NUMBER && addJobLng != NO_VALUE_BUNDLE_NUMBER) {
                                LatLng latLng = new LatLng(addJobLat, addJobLng);
                                addMarker(latLng, "Test");
                                moveCamera(latLng, DEFAULT_ZOOM - 7f);
                            }

                        } else {
                            Toast.makeText(map.this, "Unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.d(TAG, "getDeviceLocation: security exception " + e.getMessage());
        }
    }

    // initializes the Map
    private void initMap() {
        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        // goes with initMap
        // called from getMapAsync()
        mMap = googleMap;

        if (locationPermissionsGranted) {
            getDeviceLocation();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }



            // Google Map options
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            mMap.getUiSettings().setRotateGesturesEnabled(false);
        }


        // adding markers
        /*
        addMarker(new LatLng(38.646122, -121.131029), "Test");
        addMarker(new LatLng(38.646663, -121.131319), "Test 2");

        setMarkerVisibleByTitle(true, "Test");
        setMarkerVisibleByTitle(false, "Test 2");

        final Marker test1 = getMarker(new LatLng(38.646122, -121.131029), "Test 1");
        final Marker test2 = getMarker(new LatLng(38.646663, -121.131319), "Test 2");
        */

        final Marker marker1 = mMap.addMarker(new MarkerOptions().position(new LatLng(38.646122, -121.131029)).title("Marker 1"));
        marker1.setVisible(false);
        final Marker marker2 = mMap.addMarker(new MarkerOptions().position(new LatLng(38.646663, -121.131319)).title("Marker 2"));
        marker2.setVisible(false);

        mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                CameraPosition cameraPosition = mMap.getCameraPosition();
                zoomText.setText(new Float(cameraPosition.zoom).toString());
                if(cameraPosition.zoom > 18) {
                    marker1.setVisible(true);
                    marker2.setVisible(true);
                    /*
                    setMarkerVisibleByTitle(true, "Test");
                    setMarkerVisibleByTitle(true, "Test 2");
                    */
                } else {
                    marker1.setVisible(false);
                    marker2.setVisible(false);
                    /*
                    setMarkerVisibleByTitle(false, "Test");
                    setMarkerVisibleByTitle(false, "Test 2");
                    */
                }

            }
        });


    }

    // moves camera to a new location and zoom
    // adds marker to marker array list
    private void moveCamera(LatLng latLng, float zoom) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    // adds a marker on the Map
    private void addMarker(LatLng latLng, String name) {
        //Drawable marker = getResources().getDrawable(R.mipmap.);
        mMap.addMarker(new MarkerOptions()
                //.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher_round))
                .position(latLng)
                .title(name));

        markerList.add(mMap.addMarker(new MarkerOptions()
                //.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher_round))
                .position(latLng)
                .title(name)));
    }

    // creates a Marker object
    // adds marker to marker array list
    private Marker getMarker(LatLng latLng, String name) {
        markerList.add(mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title(name)));

        return mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title(name));

    }

    // sets a marker visible or not by the marker's title
    private void setMarkerVisibleByTitle(Boolean visible, String title) {
        for (int i = 0; i < markerList.size(); i++) {
            //Toast.makeText(this, markerList.get(i).getTitle(), Toast.LENGTH_SHORT).show();
            if(markerList.get(i).getTitle().equals(title)) {
                //Toast.makeText(this, markerList.get(i).getTitle(), Toast.LENGTH_SHORT).show();
                markerList.get(i).setVisible(visible);
                return;
            }
        }
    }

    // returns the value of the Bundle passed by an Intent
    // returns NO_VALUE_BUNDLE_NUMBER if tag not found
    private double getBundleDoubleInfo(String tag) {
        Intent intentExtras = getIntent();
        Bundle extrasBundle = intentExtras.getExtras();
        if(extrasBundle != null) {
            if(extrasBundle.containsKey(tag)) {
                return extrasBundle.getDouble(tag);
            }
        }
        return NO_VALUE_BUNDLE_NUMBER;
    }

}
