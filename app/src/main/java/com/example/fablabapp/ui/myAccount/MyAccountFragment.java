package com.example.fablabapp.ui.myAccount;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.fablabapp.R;
import com.example.fablabapp.ui.booking.MyAccountViewModel;

public class MyAccountFragment extends Fragment {

    private MyAccountViewModel myAccountViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myAccountViewModel = new ViewModelProvider(this).get(MyAccountViewModel.class);
        View root = inflater.inflate(R.layout.fragment_booking, container, false);
        final TextView textView = root.findViewById(R.id.text_booking);
        myAccountViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

}