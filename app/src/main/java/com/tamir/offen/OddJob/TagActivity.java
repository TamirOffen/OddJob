package com.tamir.offen.OddJob;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.*;
import android.content.*;

public class TagActivity extends AppCompatActivity {

    private RadioGroup tagchooser;
    private ImageView imageViewPhoto;
    private Integer []photos = {R.drawable.rtech, R.drawable.rtrans, R.drawable.rhome, R.drawable.rcare, R.drawable.redu, R.drawable.rother};




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag);
        this.imageViewPhoto = (ImageView) findViewById(R.id.imageViewPhoto);
        this.tagchooser = (RadioGroup) findViewById(R.id.tagchooser);
        this.tagchooser.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton RadioButton = (RadioButton) tagchooser.findViewById(i);
                int index = radioGroup.indexOfChild(RadioButton);
                imageViewPhoto.setImageResource(photos[index]);

            }
        });
    }
}
