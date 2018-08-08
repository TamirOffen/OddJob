package com.tamir.offen.OddJob;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.PersistableBundle;
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
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class map extends AppCompatActivity implements OnMapReadyCallback,
                                                    GoogleMap.OnMarkerClickListener,
                                                    WorkBottomSheetDialog.BottomSheetListener {

    // Constants
    private static final String TAG = "MapActivity",
                                FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION,
                                COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION,
                                NO_VALUE_BUNDLE_STRING = "NO STRING FOUND IN BUNDLE";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234,
                             NO_VALUE_BUNDLE_INT = -1;
    private static final float DEFAULT_ZOOM = 17f;
    private static final double NO_VALUE_BUNDLE_NUMBER = -1f;
    private static final boolean NO_VALUE_BUNDLE_BOOLEAN = false;

    // Vars
    private BottomNavigationView bottomNavigationView;
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Boolean locationPermissionsGranted = false;
    private double addJobLat, addJobLng;
    private ArrayList<Marker> markerList = new ArrayList<>();
    private Marker currentMarker;
    private TextView zoomText;
    private String addJobTitle, addJobDesc;
    private CameraPosition cameraPosition;
    private float currZoomValue;
    private static LatLng currPosLatLng;
    private String jobPrice;
    private AddJobHandler addJobHandler;

    // Database - Jobs:
    public List<AddJobHandler> jobs = new ArrayList<>();
    public FirebaseDatabase database = FirebaseDatabase.getInstance();
    public DatabaseReference databaseReference = database.getReference("Jobs");


    public map() {

    }

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
        zoomText.setText(new Float(DEFAULT_ZOOM).toString());
        currZoomValue = DEFAULT_ZOOM;

        if(currPosLatLng != null) {
            cameraPosition = CameraPosition.builder().target(currPosLatLng).zoom(DEFAULT_ZOOM).tilt(0f).bearing(0f).build();
            //Toast.makeText(this, new Float(cameraPosition.zoom).toString(), Toast.LENGTH_SHORT).show();
        } else {
           // Toast.makeText(this, "fail", Toast.LENGTH_SHORT).show();
        }


        if (savedInstanceState != null) {
            Toast.makeText(this, "saved instance state", Toast.LENGTH_SHORT).show();
            zoomText.setText(savedInstanceState.getString("Zoom"));
            float zoom = savedInstanceState.getFloat("zoom");
            //Toast.makeText(this, new Float(zoom).toString(), Toast.LENGTH_SHORT).show();
        }

        addJobHandler = new AddJobHandler();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Jobs");

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
                        intent = new Intent(map.this, AddActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        break;
                }
                return false;
            }
        });
        //Toast.makeText(this, "end of onCreate", Toast.LENGTH_SHORT).show();
    }

    /*
    @Override
    protected void onStart() {
        super.onStart();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                jobs.clear();

                for(DataSnapshot jobsSnapshot : dataSnapshot.getChildren()) {
                    AddJobHandler job = jobsSnapshot.getValue(AddJobHandler.class);

                    jobs.add(job);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    */

    /*
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        CameraPosition lastCurrentCameraLocation = mMap.getCameraPosition();
        LatLng lastCurrentCameraLocationLatLng = lastCurrentCameraLocation.target;
        double lat = lastCurrentCameraLocationLatLng.latitude, lng = lastCurrentCameraLocationLatLng.longitude;
        float lastCurrentCameraLocationZoom = lastCurrentCameraLocation.zoom;

        savedInstanceState.putDouble("lat", lat);
        savedInstanceState.putDouble("lng", lng);
        savedInstanceState.putFloat("zoom", lastCurrentCameraLocationZoom);
        Toast.makeText(this, new Float(lastCurrentCameraLocationZoom).toString(), Toast.LENGTH_SHORT).show();

    }
    */ //onSave...

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
    private void getLocationPermission() {
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
                            currPosLatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                            //addJobHandler.setLocation(currPosLatLng);
                            cameraPosition = CameraPosition.builder().target(currPosLatLng).zoom(DEFAULT_ZOOM).tilt(0f).bearing(0f).build();
                            //Toast.makeText(map.this, new Float(cameraPosition.zoom).toString(), Toast.LENGTH_SHORT).show();
                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), DEFAULT_ZOOM);

                            // add job marker
                            //addJobLat = 38.645139; addJobLng = -121.164702;
                            addJobTitle = addJobHandler.getTitle(); addJobDesc = addJobHandler.getDesc();
                            jobPrice = addJobHandler.getPrice();
                            if (getBundleStringInfo("add marker").equals("add marker")) {
                                LatLng latLng = addJobHandler.getLocation();
                                //Toast.makeText(map.this, "Lat" + latLng.latitude + " lng" + latLng.longitude, Toast.LENGTH_SHORT).show();
                                addMarker(latLng, addJobTitle, addJobHandler.getTag());
                                moveCamera(latLng, DEFAULT_ZOOM - 0f);
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

            // sets map to listen for marker clicks
            mMap.setOnMarkerClickListener(this);

            // Google Map options
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            mMap.getUiSettings().setRotateGesturesEnabled(false);
        }


        // adding markers
        addMarker(new LatLng(38.646122, -121.131029), "Test", "Other");
        addMarker(new LatLng(38.646663, -121.131319), "Test 2", "Transportation");

        setMarkerVisibleByTitle(false, "Test");
        setMarkerVisibleByTitle(false, "Test 2");

        mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                CameraPosition cameraPosition = mMap.getCameraPosition();
                currZoomValue = cameraPosition.zoom;
                zoomText.setText(new Float(currZoomValue).toString());
                if(cameraPosition.zoom > 18) {
                    setMarkerVisibleByTitle(true, "Test");
                    setMarkerVisibleByTitle(true, "Test 2");

                } else {
                    setMarkerVisibleByTitle(false, "Test");
                    setMarkerVisibleByTitle(false, "Test 2");

                }

            }
        });

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    // moves camera to a new location and zoom
    // adds marker to marker array list
    private void moveCamera(LatLng latLng, float zoom) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    // adds a marker on the Map
    private void addMarker(LatLng latLng, String name, String type) {
        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_pin_drop_black_24dp);
        if(type.equals("Technology")) icon = BitmapDescriptorFactory.fromResource(R.drawable.rtech);
        if(type.equals("Transportation")) icon = BitmapDescriptorFactory.fromResource(R.drawable.rtrans);
        if(type.equals("Home / Yard")) icon = BitmapDescriptorFactory.fromResource(R.drawable.rhome);
        if(type.equals("Child / Pet Care")) icon = BitmapDescriptorFactory.fromResource(R.drawable.rcare);
        if(type.equals("Education")) icon = BitmapDescriptorFactory.fromResource(R.drawable.redu);
        if(type.equals("Other")) icon = BitmapDescriptorFactory.fromResource(R.drawable.rother);
        Marker marker = mMap.addMarker(new MarkerOptions().position(latLng).title(name).icon(icon));
        markerList.add(marker);
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

    private Marker returnMarker(String title) {
        for(int i = 0; i < markerList.size(); i++) {
            if(markerList.get(i).getTitle().equals(title)) {
                return markerList.get(i);
            }
        }
        Toast.makeText(this, "Marker not found", Toast.LENGTH_SHORT).show();
        return null;
    }

    // sets a marker visible or not by the marker's title
    private void setMarkerVisibleByTitle(Boolean visible, String title) {
        for (int i = 0; i < markerList.size(); i++) {
            if(markerList.get(i).getTitle().equals(title)) {
                markerList.get(i).setVisible(visible);
                return;
            }
        }
    }

    // returns the value of the Bundle passed by an Intent
    // returns NO_VALUE_BUNDLE_*** if tag not found
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
    private String getBundleStringInfo(String tag) {
        Intent intentExtras = getIntent();
        Bundle extrasBundle = intentExtras.getExtras();
        if(extrasBundle != null) {
            if(extrasBundle.containsKey(tag)) {
                return extrasBundle.getString(tag);
            }
        }
        return NO_VALUE_BUNDLE_STRING;
    }
    private int getBundleIntInfo(String tag) {
        Intent intentExtras = getIntent();
        Bundle extrasBundle = intentExtras.getExtras();
        if(extrasBundle != null) {
            if(extrasBundle.containsKey(tag)) {
                return extrasBundle.getInt(tag);
            }
        }
        return NO_VALUE_BUNDLE_INT;
    }
    private boolean getBundleBooleanInfo(String tag) {
        Intent intentExtras = getIntent();
        Bundle extrasBundle = intentExtras.getExtras();
        if(extrasBundle != null) {
            if(extrasBundle.containsKey(tag)) {
                return extrasBundle.getBoolean(tag);
            }
        }
        return NO_VALUE_BUNDLE_BOOLEAN;
    }

    // OnMarkerClickListener method
    @Override
    public boolean onMarkerClick(Marker marker) {
        currentMarker = marker;
        //Toast.makeText(this, marker.getTitle(), Toast.LENGTH_SHORT).show();
        WorkBottomSheetDialog workBottomSheetDialog = new WorkBottomSheetDialog();
        workBottomSheetDialog.show(getSupportFragmentManager(), "workBottomSheetDialog");
        return false;
    }

    // Returns the current marker chosen's title
    // Used to get the title in WorkBottomSheetDialog class
    @Override
    public String getJobTitle() {
        if(currentMarker != null) return currentMarker.getTitle();
        else return "MARKER NOT FOUND";
    }

    @Override
    public String getJobDesc() {
        if(currentMarker != null) return addJobDesc;
        else return "MARKER NOT FOUND";
    }

    @Override
    public String getJobPrice() {
        if(currentMarker != null) return jobPrice;
        else return "MARKER NOT FOUND";
    }

    public LatLng getCurrPosLatLng() {
        return currPosLatLng;
    }

}
