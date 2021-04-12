package com.example.fablabapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ApartmentDetailsActivity extends AppCompatActivity {

    String TAG = "From ApartmentDetailsActivity";
    private DatePickerDialog datePickerDialog;
    private RequestQueue mQueue;

    TextView start;
    TextView end;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apartment_details);

        initDatePicker();

        mQueue = Volley.newRequestQueue(this);

        SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
        String user_id = preferences.getString("id", "");

        TextView address = (TextView) findViewById(R.id.address);
        start = (TextView) findViewById(R.id.start);
        end = (TextView) findViewById(R.id.end);
        ImageView img = (ImageView) findViewById(R.id.apart_img);
        Button reserve = (Button) findViewById(R.id.reserve);


        ArrayList<String> apartDetailsList = getIntent().getStringArrayListExtra("apartDetailsList");
        Log.d(TAG, apartDetailsList.toString());

        String apart_id = apartDetailsList.get(0);
        address.setText(apartDetailsList.get(1));
        Picasso.with(this).load(apartDetailsList.get(3)).into(img);

        start.setText(getTodaysDate());
        end.setText(getTodaysDate());


        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePicker(v);
            }
        });

        reserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String end_date = end.getText().toString();

                String url = "https://projet-fablab.theo-gustave.fr/api/new/rental/+" + apart_id + "/" + user_id;
                Log.e(TAG, "ALLO");

                RequestQueue requestQueue = Volley.newRequestQueue(ApartmentDetailsActivity.this);
                Map<String, String> postParams = new HashMap<String, String>();
                postParams.put("rental_reference", "Un nom");
                postParams.put("end_date", end_date);
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                        Request.Method.POST,
                        url,
                        new JSONObject(postParams),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(ApartmentDetailsActivity.this);
                                alertBuilder.setTitle(R.string.alert_title);
                                alertBuilder.setMessage(R.string.alert_msg);
                                alertBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Log.d(TAG,response.toString());
                                        finish();
                                    }
                                });
                                alertBuilder.show();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                error.printStackTrace();
                            }
                        }
                );
                requestQueue.add(jsonObjectRequest);
            }
        });


    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                month = month + 1;
                String date = makeDateString(day, month, year);
                start.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(this, dateSetListener, year, month, day);


    }

    private String makeDateString(int day, int month, int year) {

        String m = "";

        if (month < 10) {
            m = "0" + String.valueOf(month);

        }else{
            m = String.valueOf(month);
        }

        return String.valueOf(year) + "-" +  m + "-" +  String.valueOf(day);
    }

    public void openDatePicker(View view) {
        datePickerDialog.show();
    }

    private String getTodaysDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }
}