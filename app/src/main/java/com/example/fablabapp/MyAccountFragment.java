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

import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;

public class MyAccountFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_my_account, container, false);

        ImageView account_img, logout;
        EditText first_name,last_name,email,password;
        Button save;

        account_img = rootView.findViewById(R.id.account_img);
        logout = rootView.findViewById(R.id.logout);

        first_name = rootView.findViewById(R.id.first_name);
        last_name = rootView.findViewById(R.id.last_name);
        email = rootView.findViewById(R.id.email);
        password = rootView.findViewById(R.id.password);

        save = rootView.findViewById(R.id.save);


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
}
