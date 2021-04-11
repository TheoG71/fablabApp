package com.example.fablabapp;

public class ReserveData {

    String thumbnail;
    String name = "Wonderful place";
    String address = "Rue de Rivoli, 75001 Paris";
    String apart_sate = "Free";

    public ReserveData(String thumbnail, String name, String address, String apart_sate) {
        this.thumbnail = thumbnail;
        this.name = name;
        this.address = address;

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

    public String getApart_sate() {
        return apart_sate;
    }

    public void setApart_sate(String apart_sate) {
        this.apart_sate = apart_sate;
    }
}
