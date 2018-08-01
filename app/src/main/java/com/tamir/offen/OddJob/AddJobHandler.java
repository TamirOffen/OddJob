package com.tamir.offen.OddJob;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public class AddJobHandler{

    private String title, desc;

    public AddJobHandler() {
        AddActivity ac = new AddActivity();
        title = ac.test;
    }

    public String getTitle() {
        return title;
    }
}
