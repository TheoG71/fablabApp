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

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

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

        String user_id = preferences.getString("id","");;

        ListView listView = (ListView) rootView.findViewById(R.id.listView);
        TextView textView = (TextView) rootView.findViewById(R.id.list_err);


        listView.findViewById(R.id.listView);
        ArrayList<MyReservationsData> arrayList = new ArrayList<>();

        mQueue = Volley.newRequestQueue(getContext());

        // Get all reservation
        String url ="https://projet-fablab.theo-gustave.fr/api/rental/" + user_id;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray tenant_rentals = response.getJSONArray("tenant_rentals");

                            for (int i = 0; i < tenant_rentals.length(); i++){
                                JSONObject index = tenant_rentals.getJSONObject(i);
                                JSONObject apart = index.getJSONObject("appartement_id");

                                String address = apart.getString("adress");
                                int apart_id = apart.getInt("id");
                                String thumbnail = apart.getString("picture");

                                JSONObject lock = apart.getJSONObject("lock_id");

                                String public_key = lock.getString("public_key");
                                String private_key = lock.getString("private_key");


                                arrayList.add(new MyReservationsData(
                                        thumbnail,
                                        address,
                                        apart_id,
                                        public_key,
                                        private_key
                                ));

                            }

                            if (arrayList.size() >= 1){

                                MyReservationsDataAdapter myReservationsDataAdapter = new MyReservationsDataAdapter(getActivity(), R.layout.custom_list_reserve, arrayList);
                                listView.setAdapter(myReservationsDataAdapter);

                            }else{
                                textView.setText(R.string.my_reservations_list_err);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
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

                Intent intent = new Intent(getActivity(), MyReservationDetailsActivity.class);
                intent.putStringArrayListExtra("infoList", (ArrayList<String>) infoList);

                startActivity(intent);

            }
        });

        return rootView;

    }

}
