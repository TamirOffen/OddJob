package com.tamir.offen.OddJob.Navigation_Drawer;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tamir.offen.OddJob.Add_Job.AddJobHandler;
import com.tamir.offen.OddJob.R;

import java.util.List;

public class JobsList extends ArrayAdapter<AddJobHandler> {

    public Activity context;
    private List<AddJobHandler> jobsList;

    public JobsList(Activity context, List<AddJobHandler> jobsList) {
        super(context, R.layout.jobs_list_layout, jobsList);
        this.context = context;
        this.jobsList = jobsList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.jobs_list_layout, null, true);

        TextView textViewTitle = listViewItem.findViewById(R.id.textViewTitle);
        TextView textViewDesc = listViewItem.findViewById(R.id.textViewDesc);

        AddJobHandler job = jobsList.get(position);

        textViewTitle.setText(job.getTitle());
        textViewDesc.setText(job.getDesc());

        return listViewItem;
    }
}
