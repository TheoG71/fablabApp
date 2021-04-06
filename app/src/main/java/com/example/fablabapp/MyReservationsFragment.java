package com.example.fablabapp;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class MyReservationsFragment extends Fragment {

    String TAG = "From MyReservationFragment";
    private List<String> infoList;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_my_reservations, container, false);

        ListView listView = (ListView) rootView.findViewById(R.id.listView);

        listView.findViewById(R.id.listView);
        ArrayList<ReservationsData> arrayList = new ArrayList<>();

        // Get all reservation
        // Make loop

        arrayList.add(new ReservationsData(
                "https://cdn.pixabay.com/photo/2013/04/11/19/46/building-102840_960_720.jpg",
                "Paris",
                "Rue de Rivoli, 75001 Paris",
                "01/04/2021",
                "15/04/2021",
                "Free"));

        arrayList.add(new ReservationsData(
                "https://pixabay.com/get/ge29a0ea2ec9705b3a7127c2b6b7a134ece23512e9d939c7838456cbaa60d17d5440c7706b43273c41a4c221f54d6e3db85071e0707acb179be32641e4d552752_640.jpg",
                "Lyon",
                "Rue de Rivoli, 75001 Paris",
                "01/04/2021",
                "15/04/2021",
                "Free"));

        arrayList.add(new ReservationsData(
                "https://pixabay.com/get/g048ef316442f9a842ae0cffc459ef68710d90d259d9eee85d195b33b04d4a32421d14774e597e30b54db7087f1ebe9b5cbddaa002bae26b57e4a7225c7ec45bb_640.jpg",
                "Bordeaux",
                "Rue de Rivoli, 75001 Paris",
                "01/04/2021",
                "15/04/2021",
                "Free"));

        ReservationsDataAdapter reservationsDataAdapter = new ReservationsDataAdapter(getActivity(), R.layout.custom_list_reservations, arrayList);

        listView.setAdapter(reservationsDataAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {



                //String thumbnail = ((View) view.findViewById(R.id.thumbnail));
                String title_name = ((TextView) view.findViewById(R.id.title_name)).getText().toString();
                String address = ((TextView) view.findViewById(R.id.address)).getText().toString();
                String startDate = ((TextView) view.findViewById(R.id.start_date)).getText().toString();
                String endDate = ((TextView) view.findViewById(R.id.end_date)).getText().toString();
                String apartState = ((TextView) view.findViewById(R.id.state)).getText().toString();

                infoList = new ArrayList<String>();

                infoList.add(title_name);
                infoList.add(address);
                infoList.add(startDate);
                infoList.add(endDate);
                //infoList.add(thumbnail);

                Log.d(TAG, infoList.toString());
                Intent intent = new Intent(getActivity(), ReservationDetailsActivity.class);
                intent.putStringArrayListExtra("infoList", (ArrayList<String>) infoList);

                startActivity(intent);

            }
        });

        return rootView;

    }
}
