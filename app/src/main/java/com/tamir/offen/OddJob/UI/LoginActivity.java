package com.tamir.offen.OddJob.UI;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.tamir.offen.OddJob.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button login2btn = (Button) findViewById(R.id.signupbtn2);
        Button login2signup = (Button) findViewById(R.id.login2signup);

        login2btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent  ie = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(ie);
            }
        });
        login2signup.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent ia = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(ia);
            }
        });
    }
}
