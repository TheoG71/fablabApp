package com.example.fablabapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyReservationsDataAdapter extends ArrayAdapter<MyReservationsData> {

    private Context mContext;
    private  int mResource;

    public MyReservationsDataAdapter(@NonNull Context context, int resource, @NonNull ArrayList<MyReservationsData> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResource = resource;
    }



    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        convertView = layoutInflater.inflate(mResource,parent,false);

        ImageView imageView = convertView.findViewById(R.id.thumbnail);
        TextView txtAddress = convertView.findViewById(R.id.address);


        Picasso.with(getContext()).load(getItem(position).getThumbnail()).into(imageView);
        txtAddress.setText(getItem(position).getAddress());
        return convertView;
    }

}
