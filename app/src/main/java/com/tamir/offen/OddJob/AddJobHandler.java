package com.tamir.offen.OddJob;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class AddJobHandler{

    private static String title, desc, tag, price;
    private static List<String> dates = new ArrayList<>(), times = new ArrayList<>();
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

    public void setDate(String startDate, String endDate) {
        dates.clear();
        dates.add(startDate); dates.add(endDate);
    }

    public List<String> getDates() {
        return dates;
    }

    public void setTime(String startTime, String endTime) {
        times.clear();
        times.add(startTime); times.add(endTime);
    }

    public List<String> gettime() {
        return times;
    }
}
