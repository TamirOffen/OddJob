package com.tamir.offen.OddJob;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class BottomSheetActivity extends AppCompatActivity {

    private JobScreenListener jobScreenListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_sheet);
    }

    public interface JobScreenListener {
        String title();
        String price();
        String tag();
        String[] dates();
        String[] times();
        String desc();
    }

}
