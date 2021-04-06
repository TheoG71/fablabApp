package com.example.fablabapp;

import android.net.Uri;

public class ReservationsData {

    String thumbnail;
    String name = "Wonderful place";
    String address = "Rue de Rivoli, 75001 Paris";
    String start_date = "01 Apr 2021";
    String end_date = "15 Apr 2021";
    String apart_sate = "Free";

    public ReservationsData(String thumbnail, String name, String address, String start_date, String end_date, String apart_sate) {
        this.thumbnail = thumbnail;
        this.name = name;
        this.address = address;
        this.start_date = start_date;
        this.end_date = end_date;
        this.apart_sate = apart_sate;
    }


    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getApart_sate() {
        return apart_sate;
    }

    public void setApart_sate(String apart_sate) {
        this.apart_sate = apart_sate;
    }
}
