package com.tamir.offen.OddJob.UI;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.tamir.offen.OddJob.R;
import com.tamir.offen.OddJob.UI.AdddesFragment;


public class AddFragment extends Fragment implements View.OnClickListener{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    return  inflater.inflate(R.layout.fragment_add, container, false);
    }
    private void initView(View view){
        Button button2des = (Button) view.findViewById(R.id.button2des);
        button2des.setOnClickListener(this);
    }
    private void changeFragment(){
        getFragmentManager().beginTransaction().replace(R.id.main_frame,new AdddesFragment()).addToBackStack(null).commit();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.button2des:
                changeFragment();
                break;
        }
    }
}
