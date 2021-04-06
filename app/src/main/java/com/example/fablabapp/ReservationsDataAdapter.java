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

public class ReservationsDataAdapter extends ArrayAdapter<ReservationsData> {

    private Context mContext;
    private  int mResource;

    public ReservationsDataAdapter(@NonNull Context context, int resource, @NonNull ArrayList<ReservationsData> objects) {
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
        TextView txtName = convertView.findViewById(R.id.title_name);
        TextView txtAddress = convertView.findViewById(R.id.address);
        TextView txtStartDate = convertView.findViewById(R.id.start_date);
        TextView txtEndDate = convertView.findViewById(R.id.end_date);
        TextView txtApartState = convertView.findViewById(R.id.state);

        Picasso.with(getContext()).load(getItem(position).getThumbnail()).into(imageView);
        txtName.setText(getItem(position).getName());
        txtAddress.setText(getItem(position).getAddress());
        txtStartDate.setText(getItem(position).getStart_date());
        txtEndDate.setText(getItem(position).getEnd_date());
        txtApartState.setText(getItem(position).getApart_sate());
        return convertView;
    }

}
