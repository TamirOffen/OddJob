package com.tamir.offen.OddJob.Add_Job;

import java.util.ArrayList;
import java.util.List;


public class AddJobHandler{


    private String title, desc, tag, price, ID, sender, ojID;
    private List<String> dates = new ArrayList<>(), times = new ArrayList<>();
    private com.tamir.offen.OddJob.Add_Job.LatLng location;


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

    public void setLocation(com.tamir.offen.OddJob.Add_Job.LatLng newLoc) {
        location = newLoc;
    }

    public com.tamir.offen.OddJob.Add_Job.LatLng getLocation() {
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

    public void setID(String newID) {
        ID = newID;
    }

    public String getID(){
        return ID;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String newSender) {
        sender = newSender;
    }

    @Override
    public String toString() {
        return "Title: " + title;
    }

    public String getOjID() {
        return ojID;
    }

    public void setOjID(String ojID) {
        this.ojID = ojID;
    }
}


