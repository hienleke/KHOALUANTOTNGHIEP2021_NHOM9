package com.example.testdoan.view;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testdoan.R;

public class Planning_Holder extends RecyclerView.ViewHolder {


     public TextView title;
     public TextView starttime;
     public TextView endtime;
     public TextView target;
     public TextView current;
     public ImageView edit;
     public ImageView remove;


    public Planning_Holder(@NonNull View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.iteam_planning_title);
        edit =itemView.findViewById(R.id.planning_edit);
        starttime = itemView.findViewById(R.id.iteam_planning_starttime);
        endtime = itemView.findViewById(R.id.iteam_planning_end);
        target = itemView.findViewById(R.id.iteam_planning_target);
        remove = itemView.findViewById(R.id.planning_remove);
        current = itemView.findViewById(R.id.iteam_planning_current);

    }
}
