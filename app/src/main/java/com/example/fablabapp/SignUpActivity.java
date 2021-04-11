package com.example.fablabapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {

    public static final String ACTIVITY_NAME = "SignUpActivity";

    EditText first_name, last_name, email, password, confirm_password;
    TextView first_name_err, last_name_err, email_err, password_err, confirm_password_err;
    Button create_account_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

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
                String str_email = email.getText().toString();
                String str_firstname = first_name.getText().toString();
                String str_lastname = last_name.getText().toString();
                String str_password = password.getText().toString();
                String str_confirm_password = confirm_password.getText().toString();

                if (checkFields(str_email, str_password, str_firstname, str_lastname)) {
                    if (str_password.equals(str_confirm_password)) {
                        existingUser(str_firstname, str_lastname, str_email, str_password);
                    } else {
                        password_err.setText(R.string.no_match_passwords);
                        confirm_password_err.setText(R.string.no_match_passwords);
                    }
                }
                // Check fields
                // Fill text msg error if needed
                // call API to save info
                // Fill text msg error if needed
                // close Activity

            }
        });
    }

    private boolean checkFields(String email, String password, String firstname, String lastname) {
        boolean isEmailOk;
        boolean isPasswordOk;
        boolean isFirstnameOk;
        boolean isLastnameOk;
        boolean correctFields = false;

        if (!isEmailValid(email)) {
            email_err.setText(R.string.correct_email);
            isEmailOk = false;
        } else {
            email_err.setText("");
            isEmailOk = true;
        }
        if (password.isEmpty()) {
            password_err.setText(R.string.enter_password);
            isPasswordOk = false;
        } else {
            password_err.setText("");
            isPasswordOk = true;
        }
        if (firstname.isEmpty()) {
            first_name_err.setText(R.string.enter_firstname);
            isFirstnameOk = false;
        } else {
            first_name_err.setText("");
            isFirstnameOk = true;
        }
        if (lastname.isEmpty()) {
            last_name_err.setText(R.string.enter_lastname);
            isLastnameOk = false;
        } else {
            last_name_err.setText("");
            isLastnameOk = true;
        }

        if (isEmailOk && isPasswordOk && isFirstnameOk && isLastnameOk) {
            correctFields = true;
        } else correctFields = false;

        return correctFields;
    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public void createUser(String firstname, String lastname, String email, String password) {
        String url = "https://projet-fablab.theo-gustave.fr/api/new/user";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Map<String, String> postParams = new HashMap<String, String>();
        postParams.put("email", email);
        postParams.put("firstname", firstname);
        postParams.put("lastname", lastname);
        postParams.put("password", password);
        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                new JSONObject(postParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        launchLogin();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //TO DO HANDLE ERROR
                    }
                }
        );
        requestQueue.add(objectRequest);
    }

    public void existingUser(String firstname, String lastname, String email, String password) {
        String url = "https://projet-fablab.theo-gustave.fr/api/users";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest objectRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        boolean usedEmail = false;
                        for (int i=0; i < response.length(); i++) {
                            try {
                                JSONObject user = response.getJSONObject(i);
                                if (user.getString("email").equals(email)) {
                                    usedEmail = true;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if (usedEmail) {
                                email_err.setText(R.string.used_email);
                            } else {
                                createUser(firstname, lastname, email, password);
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );
        requestQueue.add(objectRequest);
    }

    public void launchLogin() {
        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
        startActivity(intent);
    }
}