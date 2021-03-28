package com.example.testdoan.view;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testdoan.R;

public class CategoryHolder extends RecyclerView.ViewHolder {


     public TextView title;
     public TextView note;
     public ImageView edit;


    public CategoryHolder(@NonNull View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.iteam_category_name);
        note = itemView.findViewById(R.id.iteam_category_note);
        edit =itemView.findViewById(R.id.iteam_category_edit);

    }
}
