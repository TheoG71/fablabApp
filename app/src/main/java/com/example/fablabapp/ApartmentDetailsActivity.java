package com.example.fablabapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

public class ApartmentDetailsActivity extends AppCompatActivity {

    String TAG = "From ApartmentDetailsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apartment_details);

        ArrayList<String> apartDetailsList = getIntent().getStringArrayListExtra("apartDetailsList");
        Log.d(TAG, apartDetailsList.toString());
    }
}