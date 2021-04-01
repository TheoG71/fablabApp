package com.example.fablabapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;

public class ReservationDetailsActivity extends AppCompatActivity {

    String TAG = "From ReservationDetailsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_details);

        ArrayList<String> infoList = getIntent().getStringArrayListExtra("infoList");
        Log.d(TAG, infoList.toString());

        TextView name = (TextView) findViewById(R.id.name);
        TextView address = (TextView) findViewById(R.id.address);
        TextView start = (TextView) findViewById(R.id.start);
        TextView end = (TextView) findViewById(R.id.end);


        name.setText(infoList.get(0));
        address.setText(infoList.get(1));
        start.setText(infoList.get(2));
        end.setText(infoList.get(3));




    }
}