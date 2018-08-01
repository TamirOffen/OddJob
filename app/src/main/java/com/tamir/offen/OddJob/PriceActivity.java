package com.tamir.offen.OddJob;

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

public class PriceActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private Button btnToLoc, btnBackTag;
    private EditText editTextPrice;
    private String title, desc, tag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price);

        bottomNavigationView = findViewById(R.id.bottomNavView_Bar);
        btnBackTag = findViewById(R.id.btnBackTag);
        btnToLoc = findViewById(R.id.btnToLoc);
        editTextPrice = findViewById(R.id.editTextPrice);

        title = getBundleStringInfo("title");
        desc = getBundleStringInfo("desc");
        tag = getBundleStringInfo("tag");

        btnToLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String price = editTextPrice.getText().toString();

                if (price.matches("")) {
                    Toast.makeText(PriceActivity.this, "Enter a Price", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent(PriceActivity.this, LocationPickerActivity.class);
                intent.putExtra("price", price);
                intent.putExtra("tag", tag);
                intent.putExtra("title", title);
                intent.putExtra("desc", desc);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        btnBackTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PriceActivity.this, TagActivity.class);
                startActivity(intent);
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
                        intent = new Intent(PriceActivity.this, messages.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                        break;

                    case R.id.nav_map:
                        intent = new Intent(PriceActivity.this, map.class);
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


    private String getBundleStringInfo(String tag) {
        Intent intentExtras = getIntent();
        Bundle extrasBundle = intentExtras.getExtras();
        if(extrasBundle != null) {
            if(extrasBundle.containsKey(tag)) {
                return extrasBundle.getString(tag);
            }
        }
        return "TAG NOT FOUND";
    }
}
