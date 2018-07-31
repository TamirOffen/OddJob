package com.tamir.offen.OddJob.UI;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.tamir.offen.OddJob.R;

import static java.lang.Thread.sleep;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Button signupconfbtn = (Button) findViewById(R.id.signupbtn2);

        signupconfbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, com.tamir.offen.OddJob.UI.MainActivity.class);
                startActivity(intent);
                finish();
                return;
            }
        });
    }
}
