package com.example.fablabapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import static android.content.Context.MODE_PRIVATE;

public class MyReservationsFragment extends Fragment {

    String TAG = "From MyReservationFragment";
    private RequestQueue mQueue;
    ArrayList<String> infoList = new ArrayList<String>();



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_my_reservations, container, false);

        SharedPreferences preferences = getActivity().getSharedPreferences("checkbox",MODE_PRIVATE);

        String user_id = "2"; //preferences.getString("id","");

        ListView listView = (ListView) rootView.findViewById(R.id.listView);

        listView.findViewById(R.id.listView);
        ArrayList<ReservationsData> arrayList = new ArrayList<>();

        mQueue = Volley.newRequestQueue(getContext());

        // Get all reservation
        String url ="https://projet-fablab.theo-gustave.fr/api/user/" + user_id;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray occupied_apartments = response.getJSONArray("occupied_appartments");
                            for (int i = 0; i < occupied_apartments.length(); i++){
                                JSONObject index = occupied_apartments.getJSONObject(i);
                                String address = index.getString("adress");
                                String latitude = index.getString("latitude");
                                String longitude = index.getString("longitude");
                                JSONObject lock = index.getJSONObject("lock_id");
                                int apart_id = lock.getInt("id");
                                String public_key = lock.getString("public_key");
                                String private_key = lock.getString("private_key");


                                arrayList.add(new ReservationsData(
                                        "https://cdn.pixabay.com/photo/2013/04/11/19/46/building-102840_150.jpg",
                                        address,
                                        apart_id,
                                        public_key,
                                        private_key
                                ));

                                ReservationsDataAdapter reservationsDataAdapter = new ReservationsDataAdapter(getActivity(), R.layout.custom_list_reserve, arrayList);

                                listView.setAdapter(reservationsDataAdapter);


                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error

                    }
                });

        mQueue.add(jsonObjectRequest);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                infoList.add(arrayList.get(position).thumbnail);
                infoList.add(arrayList.get(position).address);
                infoList.add(Integer.toString(arrayList.get(position).apart_id));
                infoList.add(arrayList.get(position).private_key);
                infoList.add(arrayList.get(position).public_key);

                Intent intent = new Intent(getActivity(), ReservationDetailsActivity.class);
                intent.putStringArrayListExtra("infoList", (ArrayList<String>) infoList);

                startActivity(intent);

            }
        });

        return rootView;

    }

}
