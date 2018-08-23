package com.tamir.offen.OddJob.Navigation_Drawer;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.tamir.offen.OddJob.Add_Job.AddActivity;
import com.tamir.offen.OddJob.Map.map;
import com.tamir.offen.OddJob.Messaging.messages;
import com.tamir.offen.OddJob.R;

public class settingsActivity extends AppCompatActivity implements View.OnClickListener,
        BottomNavigationView.OnNavigationItemSelectedListener,
        NavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView bottomNavigationView;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private ImageView btnHamburger;
    private View navViewHeader;
    private FirebaseAuth firebaseAuth;
    private map mMap = new map();
    private String username = mMap.currentUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        btnHamburger = findViewById(R.id.imageViewHamburger);
        bottomNavigationView = findViewById(R.id.bottomNavView_Bar);
        navigationView = findViewById(R.id.nav_view);
        drawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);

        firebaseAuth = FirebaseAuth.getInstance();

        Menu menuBottomNavBar = bottomNavigationView.getMenu();
        MenuItem menuItemBottomNavBar = menuBottomNavBar.getItem(1);
        menuItemBottomNavBar.setChecked(true);

        Menu menuNavDrawer = navigationView.getMenu();
        MenuItem menuItemNavDrawer = menuNavDrawer.getItem(3);
        menuItemNavDrawer.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.bringToFront();
        setSupportActionBar(toolbar);

        btnHamburger.setOnClickListener(this);

        navViewHeader = navigationView.getHeaderView(0);
        TextView nav_email = navViewHeader.findViewById(R.id.nav_email);
        TextView nav_username = navViewHeader.findViewById(R.id.nav_username);
        nav_email.setText(firebaseAuth.getCurrentUser().getEmail());
        nav_username.setText(username);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_messages:
                Intent intent01 = new Intent(settingsActivity.this, messages.class);
                startActivity(intent01);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;
            case R.id.nav_add_work:
                Intent intent02 = new Intent(settingsActivity.this, AddActivity.class);
                startActivity(intent02);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.nav_map:
                drawerLayout.closeDrawer(GravityCompat.START);
                Intent intent0 = new Intent(settingsActivity.this, map.class);
                intent0.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent0);
                break;
            case R.id.nav_current_jobs:
                drawerLayout.closeDrawer(GravityCompat.START);
                Intent intent1 = new Intent(settingsActivity.this, currentJobsActivity.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent1);
                break;
            case R.id.nav_profile:
                drawerLayout.closeDrawer(GravityCompat.START);
                Intent intent2 = new Intent(settingsActivity.this, profileActivity.class);
                intent2.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent2);
                break;
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        if (view == btnHamburger) {
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
}
