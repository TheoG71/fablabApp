package com.example.fablabapp;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
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
import android.view.View;
import android.view.contentcapture.ContentCaptureCondition;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

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

public class MyReservationDetailsActivity extends AppCompatActivity {

    private final String TAG = "From MyReservationDetailsActivity";
    BluetoothAdapter mBluetoothAdapter;
    BluetoothSocket socket;
    private RequestQueue mQueue;
    String public_key;
    String private_key;
    private OutputStream outputStream;
    private InputStream inStream;
    boolean door = false;

    TextView name, address, start, end;
    ImageView img;
    @SuppressLint("UseSwitchCompatOrMaterialCode") Switch new_switch;
    Button connectButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_details);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if(!mBluetoothAdapter.isEnabled()){
            enableDisableBT();
        }

        ArrayList<String> infoList = getIntent().getStringArrayListExtra("infoList");

        name = (TextView) findViewById(R.id.name);
        address = (TextView) findViewById(R.id.address);
        start = (TextView) findViewById(R.id.start);
        end = (TextView) findViewById(R.id.end);
        img = (ImageView) findViewById(R.id.apart_img);
//        SeekBar seekBar = (SeekBar)findViewById(R.id.seekbar);
        new_switch = (Switch) findViewById(R.id.Switch);
        new_switch.setVisibility(View.INVISIBLE);
        connectButton = (Button) findViewById(R.id.ButtonConnect);

        final int[] progressChangedValue = {0};

        Picasso.with(this).load(infoList.get(0)).into(img);
        address.setText(infoList.get(1));
        int user_id = Integer.parseInt(infoList.get(2));
        private_key = infoList.get(3);
        public_key = infoList.get(4);

        getRentalData(Integer.toString(user_id), infoList);

        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "btnDiscover: Looking for unpaired devices.");

                if(mBluetoothAdapter.isDiscovering()){
                    mBluetoothAdapter.cancelDiscovery();
                    Log.d(TAG, "btnDiscover: Canceling discovery.");

                    //check BT permissions in manifest
                    checkBTPermissions();

                    mBluetoothAdapter.startDiscovery();
                    IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                    registerReceiver(mBroadcastReceiverConnect, discoverDevicesIntent);
                }
                if(!mBluetoothAdapter.isDiscovering()){
                    //check BT permissions in manifest
                    checkBTPermissions();
                    mBluetoothAdapter.startDiscovery();
                    IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                    registerReceiver(mBroadcastReceiverConnect, discoverDevicesIntent);
                }
            }
        });

        new_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    door = true;
                    openDoor();
                } else {
                    door = false;
                    closeDoor();
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        resetConnection();
    }

    private void getRentalData (String userId, ArrayList<String> infoList) {
        mQueue = Volley.newRequestQueue(this);

        // Get all reservation
        String url ="https://projet-fablab.theo-gustave.fr/api/rental/" + userId;
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
                                    start.setText(apart.getString("start_date").substring(0,9));
                                    end.setText(apart.getString("end_date").substring(0,9));
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
                        error.printStackTrace();
                    }
                }
        );
        mQueue.add(jsonObjectRequest);
    }

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
            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);

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

    private final BroadcastReceiver mBroadcastReceiverConnect = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            BluetoothSocket socket = null;
            BluetoothDevice device = null;

            Log.d(TAG, "onReceive: ACTION FOUND.");

            if (action.equals(BluetoothDevice.ACTION_FOUND)){
                device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Log.d(TAG, "New device found : " + device.getName() + ": " + device.getAddress());
                // Compare
                if (public_key.equals(device.getName())){

                    ParcelUuid[] uuid = device.getUuids();
                    try {
                        socket = device.createRfcommSocketToServiceRecord(uuid[0].getUuid());
                        socket.connect();
                        mBluetoothAdapter.cancelDiscovery();
                        Log.e(TAG, "Connected to " + device.getName());

                        outputStream = null;
                        inStream = null;

                        try {
                            outputStream = socket.getOutputStream();
                            inStream = socket.getInputStream();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        getDoorState();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    };

    private void getDoorState() {
        byte[] buffer = new byte[1024];
        int bytes;
        String incommingMessage;
        String msg_send = "STATE=" + private_key + "\n";

        try {
            outputStream.write(msg_send.getBytes());
            bytes = inStream.read(buffer);
            incommingMessage = new String(buffer, 0, bytes);
            Thread.sleep(500);
            Log.e(TAG, "Incomming message : " + incommingMessage);

            toggleSwitch(incommingMessage);
            connectButton.setVisibility(View.INVISIBLE);
            new_switch.setVisibility(View.VISIBLE);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void openDoor() {
        try {
            String msg_send = "OPEN=" + private_key + "\n";
            Thread.sleep(500);
            outputStream.write(msg_send.getBytes());
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }

    private void closeDoor() {
        try {
            String msg_send = "CLOSE=" + private_key + "\n";
            Thread.sleep(500);
            outputStream.write(msg_send.getBytes());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void resetConnection() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (inStream != null) {
            try {inStream.close();} catch (Exception e) {
                e.printStackTrace();
            }
            inStream = null;
        }

        if (outputStream != null) {
            try {outputStream.close();} catch (Exception e) {
                e.printStackTrace();
            }
            outputStream = null;
        }

        if (socket != null) {
            try {socket.close();} catch (Exception e) {
                e.printStackTrace();
            }
            socket = null;
        }
    }


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

    private void toggleSwitch (String doorState) {
        if (doorState.equals("C") && new_switch.isSelected()) {
            new_switch.toggle();
        } else if (doorState.equals("O") && !new_switch.isSelected()) {
            new_switch.toggle();
        }
    }
}