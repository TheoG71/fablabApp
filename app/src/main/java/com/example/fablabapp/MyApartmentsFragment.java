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

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class MyApartmentsFragment extends Fragment {

    String TAG = "From MyApartmentsFragment";
    private List<String> myApartDetailsList;
    private RequestQueue mQueue;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_apartments, container, false);

        ListView listView = (ListView) rootView.findViewById(R.id.listView);
        TextView textView = (TextView) rootView.findViewById(R.id.list_err);

        SharedPreferences preferences = getActivity().getSharedPreferences("checkbox",MODE_PRIVATE);

        int user_id = 2; //preferences.getString("id","");

        listView.findViewById(R.id.listView);
        ArrayList<MyApartmentsData> arrayList = new ArrayList<>();

        mQueue = Volley.newRequestQueue(getContext());

        // Get all reservation
        String url ="https://projet-fablab.theo-gustave.fr/api/appartments";

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

                                JSONArray landlord = apartment.getJSONArray("landlord");
                                int landlord_id = landlord.getJSONObject(0).getInt("id");

                                if (landlord_id == user_id){

                                    arrayList.add(new MyApartmentsData(
                                            thumbnail,
                                            address,
                                            free
                                    ));
                                }
                            }

                            if (arrayList.size() > 1){

                                MyApartmentsDataAdapter myApartmentsDataAdapter = new MyApartmentsDataAdapter(getActivity(), R.layout.custom_list_reserve, arrayList);
                                listView.setAdapter(myApartmentsDataAdapter);

                            }else{
                                textView.setText(R.string.my_apartments_list_err);
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

        mQueue.add(request);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //String thumbnail = ((View) view.findViewById(R.id.thumbnail));
                String title_name = ((TextView) view.findViewById(R.id.title_name)).getText().toString();
                String address = ((TextView) view.findViewById(R.id.address)).getText().toString();
                String apartState = ((TextView) view.findViewById(R.id.state)).getText().toString();

                myApartDetailsList = new ArrayList<String>();

                myApartDetailsList.add(title_name);
                myApartDetailsList.add(address);
                //myApartDetailsList.add(thumbnail);

                Log.d(TAG, myApartDetailsList.toString());
                Intent intent = new Intent(getActivity(), MyApartmentsDetailsActivity.class);
                intent.putStringArrayListExtra("myApartDetailsList", (ArrayList<String>) myApartDetailsList);

                startActivity(intent);

            }
        });

        return rootView;

    }
}
