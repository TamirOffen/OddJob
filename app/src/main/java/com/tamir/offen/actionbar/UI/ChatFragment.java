package com.tamir.offen.actionbar.UI;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tamir.offen.actionbar.R;

/**
 * Created by paen3 on 7/12/2018.
 */

public class ChatFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    return  inflater.inflate(R.layout.fragment_chat, container, false);
    }
}
