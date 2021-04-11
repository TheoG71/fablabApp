package com.example.fablabapp;

import android.content.Intent;
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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ReserveFragment extends Fragment {

    String TAG = "From MyReservationFragment";
    private List<String> apartDetailsList;
    private  RequestQueue mQueue;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_reserve, container, false);

        ListView listView = (ListView) rootView.findViewById(R.id.listView);
        TextView textView = (TextView) rootView.findViewById(R.id.list_err);

        listView.findViewById(R.id.listView);
        ArrayList<ReserveData> arrayList = new ArrayList<>();

        mQueue = Volley.newRequestQueue(getContext());

        // Get all reservation
        String url ="https://projet-fablab.theo-gustave.fr/api/appartments";
        Log.e(TAG,"ALLO");

        JsonArrayRequest request = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        Log.d(TAG, Integer.toString(response.length()));

                        try {

                            for (int i = 0; i < response.length(); i++){
                                JSONObject apartment = response.getJSONObject(i);
                                int id = apartment.getInt("id");
                                String address = apartment.getString("adress");
                                String thumbnail = apartment.getString("picture");
                                String free = "";

                                JSONArray current_tenant = apartment.getJSONArray("current_tenant");

                                if (current_tenant.length() < 1){
                                    free = "Free";
                                    Log.e(TAG, "id : " + Integer.toString(id) + " address : " + address + " Free : " + free);
                                }else {
                                    free = "Not Free";
                                    JSONArray current_tenant_obj = apartment.getJSONArray("current_tenant");
                                    JSONObject current_tenant_index = current_tenant_obj.getJSONObject(0);
                                    int current_tenant_id = current_tenant_index.getInt("id");
                                    Log.e(TAG, "id : " + Integer.toString(id) + " address : " + address + " Free : " + free + " current_tenant : " + current_tenant_id);
                                }

                                arrayList.add(new ReserveData(
                                        thumbnail,
                                        address,
                                        address,
                                        free));
                            }

                            if (arrayList.size() > 1){
                                ReserveDataAdapter reserveDataAdapter = new ReserveDataAdapter(getActivity(), R.layout.custom_list_reserve, arrayList);

                                listView.setAdapter(reserveDataAdapter);
                            }else{
                                textView.setText(R.string.reserve_list_err);
                            }



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                                            }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        mQueue.add(request);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //String thumbnail = ((View) view.findViewById(R.id.thumbnail));
                String title_name = ((TextView) view.findViewById(R.id.title_name)).getText().toString();
                String address = ((TextView) view.findViewById(R.id.address)).getText().toString();
                String apartState = ((TextView) view.findViewById(R.id.state)).getText().toString();

                apartDetailsList = new ArrayList<String>();

                apartDetailsList.add(title_name);
                apartDetailsList.add(address);
                //infoList.add(thumbnail);

                Log.d(TAG, apartDetailsList.toString());
                Intent intent = new Intent(getActivity(), ApartmentDetailsActivity.class);
                intent.putStringArrayListExtra("infoList", (ArrayList<String>) apartDetailsList);

                startActivity(intent);

            }
        });

        return rootView;

    }
}
