package com.example.fablabapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    public static final String ACTIVITY_NAME = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button login, sign_up;
        EditText email, password;

        login = findViewById(R.id.login);
        sign_up = findViewById(R.id.sign_up);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        // Check if already log

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check email field
                // Call API to check info

                // If ok
                Intent intent = new Intent( MainActivity.this, HomeActivity.class);
                startActivity(intent);

            }
        });

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed() {

    }
}