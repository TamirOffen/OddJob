package com.tamir.offen.OddJob;

public class LatLng {

    private double latitude;
    private double longitude;

    public LatLng() {

    }

    public LatLng(double lat, double lng) {
        latitude = lat;
        longitude = lng;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

}
