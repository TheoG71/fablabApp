package com.example.fablabapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.health.ServiceHealthStats;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button login, sign_up;
        EditText email, password;
        TextView login_err;
        CheckBox remember_me;

        login = findViewById(R.id.login);
        sign_up = findViewById(R.id.sign_up);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login_err = findViewById(R.id.login_err);
        remember_me = findViewById(R.id.rememberMe);

        // Check if already log
        //wasUserConnected(login_err);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check email field
                String str_email = email.getText().toString();
                String str_password = password.getText().toString();

//                String msg_err = checkFields(str_email, str_password);
//                if (!msg_err.isEmpty()) {
//                    login_err.setText(msg_err);
//                } else {
                    // Call API to check info
                    getUserId(str_email, str_password);
                    SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);

                    if (preferences.getString("id", "") != null) {
                        if (remember_me.isChecked()) {
                            pushToSharedPreferences("remember", "true");
                            pushToSharedPreferences("email", str_email);
                            pushToSharedPreferences("password", str_password);
                        }
                    } else {
                        login_err.setText(R.string.invalid_email);
                    }
//                }
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

    private void wasUserConnected(TextView login_err) {
        SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
        String remember = preferences.getString("remember", "");
        Log.d(TAG, "remember : " + remember);

        if (remember == "true") {
            // Check email field
            String str_email = preferences.getString("email", "");
            String str_password = preferences.getString("password", "");
            String str_id = preferences.getString("id", "");

            String msg_err = checkFields(str_email, str_password);

            if (!msg_err.isEmpty()) {
                login_err.setText(msg_err);
            } else {
                // Call API to check user infos
                getUserId(str_email, str_password);

                if (preferences.getString("id", "") != null) {
                    launchApp();
                }

            }
        }
    }

    private String checkFields(String email, String password) {
        String msg_err = "";

        if (!isEmailValid(email)) {
            msg_err = "Please enter a correct email address";
        }
        if (password.isEmpty()) {
            msg_err = "Please enter your password";
        }
        return msg_err;
    }

    private void getUserId(String email, String password) {
        String url = "https://projet-fablab.theo-gustave.fr/api/usermail/" + email;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response != null) {
                            try {
                                String userId = response.getString("id");
                                logUser(userId, password);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Log.d(TAG, "Response null");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, error.toString());
                    }
                }
        );
        requestQueue.add(objectRequest);
    }

    public void logUser(String userId, String password) {
        String url = "https://projet-fablab.theo-gustave.fr/api/usercredentials/" + userId + "/" + password;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("result")) {
                                pushToSharedPreferences("id", userId);
                                launchApp();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, error.toString());
                    }
                }
        );
        requestQueue.add(objectRequest);
    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public void launchApp() {
        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
        startActivity(intent);
    }

    public void pushToSharedPreferences(String key, String value) {
        SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    @Override
    public void onBackPressed() {

    }
}