package com.tamir.offen.OddJob;

import android.app.AlertDialog;
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
import android.widget.Toast;

public class AddActivity extends AppCompatActivity {

    // vars
    private EditText editTextTitle;
    private EditText editTextDesc;
    private Button btnDesc;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        editTextTitle = findViewById(R.id.editTextTitle);
        editTextDesc = findViewById(R.id.editTextDesc);
        btnDesc = findViewById(R.id.btnDesc);
        bottomNavigationView = findViewById(R.id.bottomNavView_Bar);

        btnDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String title = editTextTitle.getText().toString(), desc = editTextDesc.getText().toString();

                if(title.matches("")) {
                    Toast.makeText(AddActivity.this, "Enter a Title", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(desc.matches("")) {
                    Toast.makeText(AddActivity.this, "Enter a Description", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent goToMapIntent = new Intent(AddActivity.this, map.class);
                goToMapIntent.putExtra("latitude", 10);
                goToMapIntent.putExtra("longitude", 10);
                goToMapIntent.putExtra("title", title);
                goToMapIntent.putExtra("desc", desc);

                startActivity(goToMapIntent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
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
                        intent = new Intent(AddActivity.this, messages.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                        break;

                    case R.id.nav_map:
                        intent = new Intent(AddActivity.this, map.class);
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
