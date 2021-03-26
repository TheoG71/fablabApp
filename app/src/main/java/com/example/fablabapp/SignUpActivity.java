package com.example.fablabapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {

    public static final String ACTIVITY_NAME = "SignUpActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        EditText first_name,  last_name, email, password, confirm_password;
        TextView first_name_err, last_name_err, email_err, password_err,  confirm_password_err;
        Button create_account_btn;

        first_name = findViewById(R.id.first_name);
        first_name_err = findViewById(R.id.first_name_err);

        last_name = findViewById(R.id.last_name);
        last_name_err = findViewById(R.id.last_name_err);

        email = findViewById(R.id.email);
        email_err = findViewById(R.id.email_err);

        password = findViewById(R.id.password);
        password_err = findViewById(R.id.password_err);

        confirm_password = findViewById(R.id.confirm_password);
        confirm_password_err = findViewById(R.id.confirm_password_err);


        create_account_btn = findViewById(R.id.create_account);

        create_account_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Check fields
                // Fill text msg error if needed
                // call API to save info
                // Fill text msg error if needed
                // close Activity

            }
        });


    }
}