package com.tamir.offen.OddJob.Add_Job;

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
import android.widget.ImageView;
import android.widget.Toast;

import com.tamir.offen.OddJob.BitmapOptimizer;
import com.tamir.offen.OddJob.Map.map;
import com.tamir.offen.OddJob.Messaging.*;
import com.tamir.offen.OddJob.R;

public class AddActivity extends AppCompatActivity {

    // vars
    private EditText editTextTitle;
    private EditText editTextDesc;
    private Button btnToTag;
    private BottomNavigationView bottomNavigationView;

    public static AddJobHandler newJob = new AddJobHandler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        ImageView timepro = findViewById(R.id.progressbar4);
        ImageView numberpro = findViewById(R.id.imageOf1);
        timepro.setImageBitmap(
                BitmapOptimizer.decodeSampledBitmapFromResource(getResources(), R.drawable.rdespro, 400, 200));
        numberpro.setImageBitmap(
                BitmapOptimizer.decodeSampledBitmapFromResource(getResources(), R.drawable.shadowone, 100, 100));

        editTextTitle = findViewById(R.id.editTextTitle);
        editTextDesc = findViewById(R.id.editTextDesc);
        btnToTag = findViewById(R.id.btnDesc);
        bottomNavigationView = findViewById(R.id.bottomNavView_Bar);

        btnToTag.setOnClickListener(new View.OnClickListener() {
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

                newJob.setTitle(editTextTitle.getText().toString());
                newJob.setDesc(editTextDesc.getText().toString());

                //Intent goToMapIntent = new Intent(AddActivity.this, map.class);
                Intent goToTagIntent = new Intent(AddActivity.this, TagActivity.class);

                startActivity(goToTagIntent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
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
                        intent = new Intent(AddActivity.this, ChatSelectionActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                        finish();
                        break;

                    case R.id.nav_map:
                        intent = new Intent(AddActivity.this, map.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                        finish();
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

    public AddActivity() { }

}
