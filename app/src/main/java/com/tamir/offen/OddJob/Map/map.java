package com.tamir.offen.OddJob.Map;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tamir.offen.OddJob.Add_Job.AddActivity;
import com.tamir.offen.OddJob.Add_Job.AddJobHandler;
import com.tamir.offen.OddJob.Messaging.ChatSelectionActivity;
import com.tamir.offen.OddJob.User_Registration.LoginActivity;
import com.tamir.offen.OddJob.MainActivity;
import com.tamir.offen.OddJob.Messaging.messages;
import com.tamir.offen.OddJob.R;
import com.tamir.offen.OddJob.Add_Job.WorkBottomSheetDialog;
import com.tamir.offen.OddJob.Navigation_Drawer.*;
import com.tamir.offen.OddJob.User_Registration.User;

import java.util.ArrayList;
import java.util.List;

public class map extends AppCompatActivity implements OnMapReadyCallback,
                                                    GoogleMap.OnMarkerClickListener,
                                                    NavigationView.OnNavigationItemSelectedListener,
                                                    WorkBottomSheetDialog.BottomSheetListener,
                                                    View.OnClickListener{

    // Constants
    private static final String TAG = "MapActivity",
                                FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION,
                                COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION,
                                NO_VALUE_BUNDLE_STRING = "STRING NOT FOUND IN BUNDLE";
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

    // Database:
    DatabaseReference databaseJobs;
    public static List<AddJobHandler> jobs;
    public static List<String> jobIDs;
    private List<User> users;
    public static String currentUserName;
    private TextView textViewUsername;
    public FirebaseDatabase database = FirebaseDatabase.getInstance();
    public DatabaseReference databaseReference = database.getReference("Jobs");
    private DatabaseReference databaseUsers = database.getReference("users");
    private FirebaseAuth firebaseAuth;
    public static AddJobHandler curJob;

    // Navigation Drawer
    private ImageView btnHamburger;
    private DrawerLayout drawerLayout;
    private View navViewHeader;
    private NavigationView navigationView;
    private android.support.v7.widget.Toolbar toolbar;

    // Alert Dialog for internet connection
    AlertDialog.Builder alertDialog;


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
        }


        if (savedInstanceState != null) {
            Toast.makeText(this, "saved instance state", Toast.LENGTH_SHORT).show();
            zoomText.setText(savedInstanceState.getString("Zoom"));
            float zoom = savedInstanceState.getFloat("zoom");
            //Toast.makeText(this, new Float(zoom).toString(), Toast.LENGTH_SHORT).show();
        }

        addJobHandler = new AddJobHandler();

        firebaseAuth = FirebaseAuth.getInstance();

        databaseJobs = FirebaseDatabase.getInstance().getReference("Jobs");
        jobs = new ArrayList<>();
        jobIDs = new ArrayList<>();
        users = new ArrayList<>();

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
                        intent = new Intent(map.this, ChatSelectionActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                        finish();
                        break;
                    case R.id.nav_map:
                        break;
                    case R.id.nav_add_work:
                        intent = new Intent(map.this, AddActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        finish();
                        break;
                }
                return false;
            }
        });

        // Navigation Drawer
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        btnHamburger = findViewById(R.id.imageViewHamburger);
        btnHamburger.setOnClickListener(this);
        btnHamburger.bringToFront();
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navViewHeader = navigationView.getHeaderView(0);
        TextView nav_email = navViewHeader.findViewById(R.id.nav_email);
        nav_email.setText(firebaseAuth.getCurrentUser().getEmail());
        textViewUsername = navViewHeader.findViewById(R.id.nav_username);
        updateUsernameFromDatabase();
    }

    @Override
    protected void onStart() {
        super.onStart();

        Menu navMenu = navigationView.getMenu();
        MenuItem navMenuItem = navMenu.getItem(0);
        navMenuItem.setChecked(true);

        alertDialog = new AlertDialog.Builder(this);
        setUpAlertDialog();
        if (!haveNetworkConnection()) alertDialog.show();

        initJobs();
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

    // Sets up an Internet Connection Alert Dialog
    private void setUpAlertDialog() {
        alertDialog.setTitle("No Internet Connection!");
        alertDialog.setIcon(R.drawable.ic_signal_wifi_off);
        alertDialog.setMessage("Connect your device to the internet and try again");
        alertDialog.setCancelable(false);
        
        alertDialog.setNegativeButton("Try Again", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onStart();
            }
        });
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
                        if (task.isSuccessful() && task.getResult() != null) {

                            // current location marker
                            Location currentLocation = (Location)task.getResult();
                            currPosLatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                            cameraPosition = CameraPosition.builder().target(currPosLatLng).zoom(DEFAULT_ZOOM).tilt(0f).bearing(0f).build();
                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), DEFAULT_ZOOM);
                            
                            if(getBundleStringInfo("Current Job").equals("currJob")) {
                                double newLat = getBundleDoubleInfo("lat");
                                double newLng = getBundleDoubleInfo("lng");
                                moveCamera(new LatLng(newLat, newLng), DEFAULT_ZOOM);
                            }

                            // add job marker
                            addJobTitle = addJobHandler.getTitle(); addJobDesc = addJobHandler.getDesc();
                            jobPrice = addJobHandler.getPrice();
                            /*
                            if (getBundleStringInfo("add marker").equals("add marker")) {
                                LatLng latLng = new LatLng(addJobHandler.getLocation().getLatitude(), addJobHandler.getLocation().getLongitude());
                                addMarker(latLng, addJobTitle, addJobHandler.getTag());
                                moveCamera(latLng, DEFAULT_ZOOM - 0f);
                            }
                            */

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


        /*
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
//                    for(int i = 0; i < markerList.size(); i++) {
//                        setMarkerVisibleByTitle(true, jobs.get(i).getTitle());
//                    }
                    setMarkerVisibleByTitle(true, "Test");
                    setMarkerVisibleByTitle(true, "Test 2");

                } else {
//                    for(int i = 0; i < markerList.size(); i++) {
//                        setMarkerVisibleByTitle(false, jobs.get(i).getTitle());
//                    }
                    setMarkerVisibleByTitle(false, "Test");
                    setMarkerVisibleByTitle(false, "Test 2");

                }

            }
        });
        */


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
        //Toast.makeText(this, markerList.toString(), Toast.LENGTH_SHORT).show();
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
        curJob = getCurJob();
        Toast.makeText(this, curJob.gettime().toString(), Toast.LENGTH_SHORT).show();
        WorkBottomSheetDialog workBottomSheetDialog = new WorkBottomSheetDialog();
        workBottomSheetDialog.show(getSupportFragmentManager(), "workBottomSheetDialog");
        return false;
    }

    // Navigation Drawer
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_current_jobs:
                drawerLayout.closeDrawer(GravityCompat.START);
                Intent intent = new Intent(map.this, currentJobsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                break;
            case R.id.nav_profile:
                drawerLayout.closeDrawer(GravityCompat.START);
                Intent intent1 = new Intent(map.this, profileActivity.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent1);
                break;
            case R.id.nav_settings:
                drawerLayout.closeDrawer(GravityCompat.START);
                Intent intent2 = new Intent(map.this, settingsActivity.class);
                intent2.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent2);
                break;
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        if(view == btnHamburger) {
            drawerLayout.openDrawer(Gravity.LEFT);
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    // Returns the current marker chosen's title
    // Used to get the title in WorkBottomSheetDialog class
    @Override
    public String getJobTitle() {
        if(currentMarker != null) return curJob.getTitle();
        else return "MARKER NOT FOUND";
    }

    @Override
    public String getJobPrice() {
        if(currentMarker != null) return curJob.getPrice();
        else return "MARKER NOT FOUND";
    }

    public LatLng getCurrPosLatLng() {
        return currPosLatLng;
    }


    // initializes a list of jobs from the database and adds them to the map
    private void initJobs() {
        databaseJobs.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                jobs.clear(); jobIDs.clear();
                for(DataSnapshot jobSnapshot : dataSnapshot.getChildren()) {
                    AddJobHandler newJob = jobSnapshot.getValue(AddJobHandler.class);
                    jobIDs.add(jobSnapshot.getKey());
                    jobs.add(newJob);
                }

                for(int i = 0; i < jobs.size(); i++) {
                    double newJobLat = jobs.get(i).getLocation().getLatitude(), newJobLng = jobs.get(i).getLocation().getLongitude();
                    LatLng newJobLocation = new LatLng(newJobLat, newJobLng);
                    String title = jobs.get(i).getTitle();
                    String tag = jobs.get(i).getTag();
                    addMarker(newJobLocation, title, tag);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    // searches the jobs ArrayList by title, should make this more accurate...
    // returns the job linked to the pin clicked
    private AddJobHandler getCurJob() {
        List<AddJobHandler> potentialCurJobs = new ArrayList<>();
        for(int i = 0; i < jobs.size(); i++){
            if(currentMarker.getTitle().equals(jobs.get(i).getTitle())) {
                potentialCurJobs.add(jobs.get(i));
            }
        }
        if(potentialCurJobs.size() > 1) Toast.makeText(this, "More than one marker found(by title). Update getCurJob()!", Toast.LENGTH_LONG).show();
        return potentialCurJobs.get(0);
    }

    // returns the name linked to the email in the Firebase database
    private void updateUsernameFromDatabase() {
        databaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    User user = userSnapshot.getValue(User.class);
                    if(user.getEmail().equals(firebaseAuth.getCurrentUser().getEmail())) {
                        currentUserName = user.getName();
                        textViewUsername.setText(currentUserName);
                        return;
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    
    // returns if there is a wifi connection
    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;
        ConnectivityManager cm = (ConnectivityManager) getSystemService(map.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }



}
