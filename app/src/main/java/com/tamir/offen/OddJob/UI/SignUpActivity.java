package com.tamir.offen.OddJob.UI;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.util.CrashUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.tamir.offen.OddJob.LocationPickerActivity;
import com.tamir.offen.OddJob.PriceActivity;
import com.tamir.offen.OddJob.R;

import static java.lang.Thread.sleep;

public class SignUpActivity extends AppCompatActivity{

    EditText nameenter, passenter, confenter,emailenter;
    public Button tologin, signupconfbtn;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        signupconfbtn = (Button) findViewById(R.id.signupbtn2);
        tologin = findViewById(R.id.button2login);
        emailenter = (EditText) findViewById(R.id.emailenter);
        passenter = (EditText) findViewById(R.id.passenter);
        nameenter = (EditText) findViewById(R.id.nameenter);
        confenter = (EditText) findViewById(R.id.confenter);
        mAuth = FirebaseAuth.getInstance();


        signupconfbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

    }
    public void registerUser(){
        String email = emailenter.getText().toString().trim();
        String password = passenter.getText().toString().trim();
        String name = nameenter.getText().toString().trim();
        String confirm = confenter.getText().toString().trim();

        if(email.isEmpty()){
            emailenter.setError("Email is required");
            emailenter.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailenter.setError("Please enter a valid email");
            emailenter.requestFocus();
            return;
        }
        if(password.isEmpty()){
            passenter.setError("Password is required");
            passenter.requestFocus();
            return;
        }
        if(password.length()<6){
            passenter.setError("Password must be at least 6 characters");
            passenter.requestFocus();
            return;
        }
        if(name.isEmpty()){
            nameenter.setError("Please enter your full name");
            nameenter.requestFocus();
            return;
        }
        if(confirm.isEmpty()){
            confenter.setError("Please confirm your password");
            confenter.requestFocus();
            return;
        }
        if(confirm != password){
            confenter.setError("Passwords must match");
            confenter.requestFocus();
            return;
        }
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),"Signup Successful", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }
}
