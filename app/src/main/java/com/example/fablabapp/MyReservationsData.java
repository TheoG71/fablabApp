package com.example.fablabapp;

import android.net.Uri;

public class MyReservationsData {

    String thumbnail;
    String address = "Rue de Rivoli, 75001 Paris";
    int apart_id;
    String public_key;
    String private_key;



    public MyReservationsData(String thumbnail, String address, int apart_id, String public_key, String private_key) {
        this.thumbnail = thumbnail;
        this.address = address;
        this.apart_id = apart_id;
        this.public_key = public_key;
        this.private_key = private_key;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public int getApart_id() {
        return apart_id;
    }

    public void setApart_id(int apart_id) {
        this.apart_id = apart_id;
    }

    public String getPublic_key() {
        return public_key;
    }

    public void setPublic_key(String public_key) {
        this.public_key = public_key;
    }

    public String getPrivate_key() {
        return private_key;
    }

    public void setPrivate_key(String private_key) {
        this.private_key = private_key;
    }


}
