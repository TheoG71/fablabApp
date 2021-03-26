package com.example.fablabapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    public static final String ACTIVITY_NAME = "SplashActivity";

    TextView app_name;
    RelativeLayout content_waves;
    ImageView waves, bg_waves;
    CharSequence charSequence;
    int index;
    long delay = 200;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        app_name = findViewById(R.id.app_name);
        waves = findViewById(R.id.waves);
        bg_waves = findViewById(R.id.bg_waves);
        content_waves = findViewById(R.id.content_waves);

        content_waves.animate().setDuration(1000).scaleY(3).setStartDelay(2000);

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
            }
        },4000);

    }
}