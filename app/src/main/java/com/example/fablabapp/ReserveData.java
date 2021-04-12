package com.example.fablabapp;

public class ReserveData {



    String id;
    String thumbnail = "";
    String address = "Rue de Rivoli, 75001 Paris";
    String apart_sate = "Free";

    public ReserveData(String id, String thumbnail, String address, String apart_sate) {
        this.id = id;
        this.thumbnail = thumbnail;
        this.address = address;
        this.apart_sate = apart_sate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getApart_sate() {
        return apart_sate;
    }

    public void setApart_sate(String apart_sate) {
        this.apart_sate = apart_sate;
    }




}
