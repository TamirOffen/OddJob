package com.tamir.offen.OddJob.Add_Job;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tamir.offen.OddJob.Add_Job.AddJobHandler;
import com.tamir.offen.OddJob.Map.map;
import com.tamir.offen.OddJob.R;

public class BottomSheetActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView textViewTitle, textViewPrice, textViewStartDate, textViewEndDate, textViewStartTime, textViewEndTime, textViewDesc;
    private ImageView imageViewIcon;
    private Button btnAcceptJob, btnMessage, btnBackToMap;
    map mMap = new map();
    AddJobHandler curJob = mMap.curJob;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_sheet);

        textViewTitle = findViewById(R.id.textViewTitle);
        textViewPrice = findViewById(R.id.textViewPrice);
        textViewStartDate = findViewById(R.id.textViewStartDate);
        textViewEndDate = findViewById(R.id.textViewEndDate);
        textViewStartTime = findViewById(R.id.textViewStartTime);
        textViewEndTime = findViewById(R.id.textViewEndTime);
        textViewDesc = findViewById(R.id.textViewDesc);
        btnAcceptJob = findViewById(R.id.btnAcceptJob);
        btnMessage = findViewById(R.id.btnMessage);
        btnBackToMap = findViewById(R.id.btnBackToMap);
        imageViewIcon = findViewById(R.id.imageViewIcon);

        textViewTitle.setText(curJob.getTitle());
        textViewPrice.setText(curJob.getPrice());
        textViewStartDate.setText(curJob.getDates().get(0));
        textViewEndDate.setText(curJob.getDates().get(1));
        //textViewStartTime.setText(curJob.gettime().get(0));
        //textViewEndTime.setText(curJob.gettime().get(1));
        textViewDesc.setText(curJob.getDesc());

        updateTagIcon();

        btnBackToMap.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if(view == btnBackToMap) onBackPressed();
    }

    private void updateTagIcon() {
        String tag = curJob.getTag();
        if(tag.equals("Technology")) imageViewIcon.setImageResource(R.drawable.rtech);
        if(tag.equals("Transportation")) imageViewIcon.setImageResource(R.drawable.rtrans);
        if(tag.equals("Home / Yard")) imageViewIcon.setImageResource(R.drawable.rhome);
        if(tag.equals("Child / Pet Care")) imageViewIcon.setImageResource(R.drawable.rcare);
        if(tag.equals("Education")) imageViewIcon.setImageResource(R.drawable.redu);
        if(tag.equals("Other")) imageViewIcon.setImageResource(R.drawable.rother);
    }
}
