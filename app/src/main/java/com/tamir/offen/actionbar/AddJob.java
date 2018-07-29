package com.tamir.offen.actionbar;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddJob extends AppCompatActivity{

    // vars
    private BottomNavigationView bottomNavigationView;
    private EditText editTextLat, editTextLng, editTextTitle;
    private Button addMarkerBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_job);

        TextView textView = findViewById(R.id.activityTitleAddJob);
        textView.setText("Add a Job");

        editTextLat = findViewById(R.id.editTextLat);
        editTextLng = findViewById(R.id.editTextLng);
        addMarkerBtn = findViewById(R.id.addMarkerBtn);
        editTextTitle = findViewById(R.id.editTextTitle);

        addMarkerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String latStr = editTextLat.getText().toString(), lngStr = editTextLng.getText().toString(), title = editTextTitle.getText().toString();
                final double lat, lng;

                if(title.matches("")) {
                    Toast.makeText(AddJob.this, "Enter a title", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!latStr.matches("")) lat = Double.parseDouble(editTextLat.getText().toString());
                else lat = 10f;

                if(!lngStr.matches("")) lng = Double.parseDouble(editTextLng.getText().toString());
                else lng = 10f;

                Intent goToMapIntent = new Intent(AddJob.this, map.class);
                goToMapIntent.putExtra("latitude", lat);
                goToMapIntent.putExtra("longitude", lng);
                goToMapIntent.putExtra("title", title);

                startActivity(goToMapIntent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });



        bottomNavigationView = findViewById(R.id.bottomNavView_Bar);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Intent intent;

                switch(item.getItemId()) {
                    case R.id.nav_messages:
                        intent = new Intent(AddJob.this, messages.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                        break;

                    case R.id.nav_map:
                        intent = new Intent(AddJob.this, map.class);
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

}
