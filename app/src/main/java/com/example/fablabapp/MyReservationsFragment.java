package com.example.fablabapp;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class MyReservationsFragment extends Fragment {


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
                Uri.parse("https://cdn.pixabay.com/photo/2013/04/11/19/46/building-102840_960_720.jpg"),
                "Paris",
                "Rue de Rivoli, 75001 Paris",
                "01/04/2021",
                "15/04/2021",
                "Free"));

        arrayList.add(new ReservationsData(
                Uri.parse("https://cdn.pixabay.com/photo/2013/04/11/19/46/building-102840_960_720.jpg"),
                "Paris",
                "Rue de Rivoli, 75001 Paris",
                "01/04/2021",
                "15/04/2021",
                "Free"));

        arrayList.add(new ReservationsData(
                Uri.parse("https://cdn.pixabay.com/photo/2013/04/11/19/46/building-102840_960_720.jpg"),
                "Paris",
                "Rue de Rivoli, 75001 Paris",
                "01/04/2021",
                "15/04/2021",
                "Free"));

        ReservationsDataAdapter reservationsDataAdapter = new ReservationsDataAdapter(getActivity(), R.layout.custom_list_reservations, arrayList);

        listView.setAdapter(reservationsDataAdapter);

        return rootView;

    }
}
