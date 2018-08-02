package com.tamir.offen.OddJob;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.model.LatLng;

public class AddJobHandler{

    private static String title, desc, tag, price, lat, lng;
    private static String[] dates = new String[2], times = new String[2];
    private static LatLng location;

    public AddJobHandler() {

    }

    public void setTitle(String newTitle) {
        title = newTitle;
    }

    public String getTitle() {
        return title;
    }

    public void setDesc(String newDesc) {
        desc = newDesc;
    }

    public String getDesc() {
        return desc;
    }

    public void setTag(String newTag) {
        tag = newTag;
    }

    public String getTag() {
        return tag;
    }

    public void setPrice(String newPrice) {
        price = newPrice;
    }

    public String getPrice() {
        return price;
    }

    public void setLocation(LatLng newLoc) {
        location = newLoc;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setDate(int index, String newDate) {
        dates[index] = newDate;
    }

    public String getDate(int index) {
        return dates[index];
    }

    public void settime(int index, String newTime) {
        times[index] = newTime;
    }

    public String gettime(int index) {
        return times[index];
    }
}
