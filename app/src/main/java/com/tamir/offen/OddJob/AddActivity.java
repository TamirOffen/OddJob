package com.tamir.offen.OddJob;

import android.content.Intent;
import android.graphics.BitmapFactory;
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

public class AddActivity extends AppCompatActivity {

    // vars
    private EditText editTextTitle;
    private EditText editTextDesc;
    private Button btnToTag;
    private BottomNavigationView bottomNavigationView;

    private AddJobHandler addJobHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(), R.id.progressbar1, options);
        BitmapFactory.decodeResource(getResources(),R.id.imageOf1,options);
        ImageView timepro = findViewById(R.id.progressbar1);
        ImageView numberpro = findViewById(R.id.imageOf1);
        options.inScaled = false;
        timepro.setImageBitmap(
                BitmapOptimizer.decodeSampledBitmapFromResource(getResources(), R.drawable.rdespro, 100, 100));
        numberpro.setImageBitmap(
                BitmapOptimizer.decodeSampledBitmapFromResource(getResources(), R.drawable.shadowone, 100, 100));

        editTextTitle = findViewById(R.id.editTextTitle);
        editTextDesc = findViewById(R.id.editTextDesc);
        btnToTag = findViewById(R.id.btnDesc);
        bottomNavigationView = findViewById(R.id.bottomNavView_Bar);

        addJobHandler= new AddJobHandler();

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

                addJobHandler.setTitle(editTextTitle.getText().toString());
                addJobHandler.setDesc(editTextDesc.getText().toString());

                //Intent goToMapIntent = new Intent(AddActivity.this, map.class);
                Intent goToTagIntent = new Intent(AddActivity.this, TagActivity.class);

                //goToTagIntent.putExtra("title", title);
                //goToTagIntent.putExtra("desc", desc);

                startActivity(goToTagIntent);
                //startActivity(addJobHandlerIntent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
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
