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
import android.widget.*;

public class TagActivity extends AppCompatActivity {

    private Button btnToPrice, btnBackTitle;

    private RadioGroup tagchooser;
    private ImageView imageViewPhoto;
    private Integer[] photos = {R.drawable.rtech, R.drawable.rtrans, R.drawable.rhome, R.drawable.rcare, R.drawable.redu, R.drawable.rother};

    private BottomNavigationView bottomNavigationView;

    private AddJobHandler addJobHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(), R.id.progressbar4, options);
        BitmapFactory.decodeResource(getResources(),R.id.imageOf3,options);
        ImageView timepro = findViewById(R.id.progressbar4);
        ImageView numberpro = findViewById(R.id.imageOf3);
        options.inScaled = false;
        timepro.setImageBitmap(
                BitmapOptimizer.decodeSampledBitmapFromResource(getResources(), R.drawable.rtagpro, 100, 100));
        numberpro.setImageBitmap(
                BitmapOptimizer.decodeSampledBitmapFromResource(getResources(), R.drawable.shadowtwo, 100, 100));

        btnToPrice = findViewById(R.id.btnToPrice);
        btnBackTitle = findViewById(R.id.btnBackTitle);
        bottomNavigationView = findViewById(R.id.bottomNavView_Bar);

        addJobHandler = new AddJobHandler();

        btnBackTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TagActivity.this, AddActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        this.imageViewPhoto = findViewById(R.id.imageViewPhoto);
        this.tagchooser = findViewById(R.id.tagchooser);
        this.tagchooser.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                final RadioButton radioButton = tagchooser.findViewById(i);
                int index = radioGroup.indexOfChild(radioButton);
                imageViewPhoto.setImageResource(photos[index]);

                btnToPrice.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String tag = radioButton.getText().toString();

                        // add a way to display a toast if next is clicked and there isn't a tag checked
                        if (tag.matches("")) {
                            Toast.makeText(TagActivity.this, "Select a Tag", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        addJobHandler.setTag(tag);

                        Intent intent = new Intent(TagActivity.this, PriceActivity.class);

//                        intent.putExtra("tag", tag);
//                        intent.putExtra("title", title);
//                        intent.putExtra("desc", desc);

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

                switch(item.getItemId()) {
                    case R.id.nav_messages:
                        intent = new Intent(TagActivity.this, messages.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                        break;

                    case R.id.nav_map:
                        intent = new Intent(TagActivity.this, map.class);
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
