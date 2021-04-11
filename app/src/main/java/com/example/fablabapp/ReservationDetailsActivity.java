package com.example.fablabapp;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.util.Log;
import android.view.contentcapture.ContentCaptureCondition;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.UUID;

public class ReservationDetailsActivity extends AppCompatActivity {

    String TAG = "From ReservationDetailsActivity";
    BluetoothAdapter mBluetoothAdapter;
    Context context;
    private RequestQueue mQueue;
    String public_key;
    String private_key;
    private OutputStream outputStream;
    private InputStream inStream;
    boolean door;


    public void enableDisableBT(){
        if(mBluetoothAdapter == null){
            Log.d(TAG, "enableDisableBT: Does not have BT capabilities.");
        }
        if(!mBluetoothAdapter.isEnabled()){
            Log.d(TAG, "enableDisableBT: enabling BT.");
            Intent enableBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enableBTIntent);

            IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(mBroadcastReceiver1, BTIntent);
        }
        if(mBluetoothAdapter.isEnabled()){
            Log.d(TAG, "enableDisableBT: disabling BT.");
            mBluetoothAdapter.disable();

            IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(mBroadcastReceiver1, BTIntent);
        }



    }

    private final BroadcastReceiver mBroadcastReceiver1 = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (action.equals(mBluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, mBluetoothAdapter.ERROR);

                switch(state){
                    case BluetoothAdapter.STATE_OFF:
                        Log.d(TAG, "onReceive: STATE OFF");
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        Log.d(TAG, "mBroadcastReceiver1: STATE TURNING OFF");
                        break;
                    case BluetoothAdapter.STATE_ON:
                        Log.d(TAG, "mBroadcastReceiver1: STATE ON");
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        Log.d(TAG, "mBroadcastReceiver1: STATE TURNING ON");
                        break;
                }
            }
        }
    };

    private BroadcastReceiver mBroadcastReceiver3 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            Log.d(TAG, "onReceive: ACTION FOUND.");

            if (action.equals(BluetoothDevice.ACTION_FOUND)){
                BluetoothDevice device = intent.getParcelableExtra (BluetoothDevice.EXTRA_DEVICE);
                // Compare
                if (public_key.equals(device.getName())){

                    ParcelUuid[] uuid = device.getUuids();
                    BluetoothSocket socket = null;
                    try {
                        socket = device.createRfcommSocketToServiceRecord(uuid[0].getUuid());
                        socket.connect();
                        outputStream = socket.getOutputStream();
                        inStream = socket.getInputStream();

                        String msg_send = "OPEN=" + private_key;

                        outputStream.write(msg_send.getBytes());

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                Log.d(TAG, "New device found : " + device.getName() + ": " + device.getAddress());
            }
        }
    };

    public void read() {
        final int BUFFER_SIZE = 1024;
        byte[] buffer = new byte[BUFFER_SIZE];
        int bytes = 0;
        int b = BUFFER_SIZE;

        while (true) {
            try {
                bytes = inStream.read(buffer, bytes, BUFFER_SIZE - bytes);
                Log.d(TAG, "Y'a un truc mais je sais pas quoi ??");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void checkBTPermissions() {
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP){
            int permissionCheck = this.checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION");
            permissionCheck += this.checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION");
            if (permissionCheck != 0) {

                this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001); //Any number
            }
        }else{
            Log.d(TAG, "checkBTPermissions: No need to check permissions. SDK version < LOLLIPOP.");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_details);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if(!mBluetoothAdapter.isEnabled()){
            enableDisableBT();
        }

        ArrayList<String> infoList = getIntent().getStringArrayListExtra("infoList");

        TextView name = (TextView) findViewById(R.id.name);
        TextView address = (TextView) findViewById(R.id.address);
        TextView start = (TextView) findViewById(R.id.start);
        TextView end = (TextView) findViewById(R.id.end);
        ImageView img = (ImageView) findViewById(R.id.apart_img);
        SeekBar seekBar = (SeekBar)findViewById(R.id.seekbar);

        final int[] progressChangedValue = {0};

        Picasso.with(this).load(infoList.get(0)).into(img);
        address.setText(infoList.get(1));
        int user_id = Integer.parseInt(infoList.get(2));
        private_key = infoList.get(3);
        public_key = infoList.get(4);

        mQueue = Volley.newRequestQueue(this);

        // Get all reservation
        String url ="https://projet-fablab.theo-gustave.fr/api/rental/" + Integer.toString(user_id);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONArray tenant_rentals = response.getJSONArray("tenant_rentals");

                            for (int i = 0 ; i < tenant_rentals.length(); i++){

                                JSONObject apart = tenant_rentals.getJSONObject(i);
                                String address = apart.getJSONObject("appartement_id").getString("adress");

                                if (address.equals(infoList.get(1))){

                                    start.setText(apart.getString("start_date"));
                                    end.setText(apart.getString("end_date"));

                                }
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

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                if (progress < 10){
                    seekBar.setProgress(10);

                }
                if (progress > 80){
                    seekBar.setProgress(90);
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {


            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                //Toast.makeText(ReservationDetailsActivity.this, "Seek bar progress is :" + progressChangedValue[0], Toast.LENGTH_SHORT).show();
                if (seekBar.getProgress() < 80){
                    door = false;
                    seekBar.setProgress(10);
                }
                if (seekBar.getProgress() == 90){
                    door = true;
                    Log.d(TAG, "btnDiscover: Looking for unpaired devices.");

                    if(mBluetoothAdapter.isDiscovering()){
                        mBluetoothAdapter.cancelDiscovery();
                        Log.d(TAG, "btnDiscover: Canceling discovery.");

                        //check BT permissions in manifest
                        checkBTPermissions();

                        mBluetoothAdapter.startDiscovery();
                        IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                        registerReceiver(mBroadcastReceiver3, discoverDevicesIntent);
                    }
                    if(!mBluetoothAdapter.isDiscovering()){

                        //check BT permissions in manifest
                        checkBTPermissions();

                        mBluetoothAdapter.startDiscovery();
                        IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                        registerReceiver(mBroadcastReceiver3, discoverDevicesIntent);
                    }
                }

            }
        });
    }
}