package com.example.fablabapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;

public class MyAccountFragment extends Fragment {
    ImageView account_img, logout;
    EditText first_name,last_name,email,password;
    Button save;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_my_account, container, false);

        account_img = rootView.findViewById(R.id.account_img);
        logout = rootView.findViewById(R.id.logout);

        first_name = rootView.findViewById(R.id.first_name);
        last_name = rootView.findViewById(R.id.last_name);
        email = rootView.findViewById(R.id.email);
        password = rootView.findViewById(R.id.password);

        save = rootView.findViewById(R.id.save);

        SharedPreferences preferences = requireActivity().getSharedPreferences("checkbox", MODE_PRIVATE);
        String userId = preferences.getString("id", "");
        getUserData(userId);


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = requireActivity().getSharedPreferences("checkbox",MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("remember", "false");
                editor.apply();
                requireActivity().finish();
            }
        });

        return rootView;
    }
    
    public void getUserData(String userId) {
        String url = "https://projet-fablab.theo-gustave.fr/api/user/" + userId;
        RequestQueue requestQueue = Volley.newRequestQueue(requireActivity());
        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            email.setText(response.getString("email"));
                            first_name.setText(response.getString("firstname"));
                            last_name.setText(response.getString("lastname"));
                            password.setText(response.getString("firstname"));

                            String profilePictureUrl = response.getString("profile_picture");
                            if (profilePictureUrl != null) {
                                Picasso.with(requireActivity()).load(profilePictureUrl).into(account_img);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
}
