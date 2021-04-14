package com.example.testdoan.view;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testdoan.R;

public class ExpenseHolder extends RecyclerView.ViewHolder {

    public TextView time;
    public ImageView remove;
     public TextView category;
     public TextView amount;
     public TextView expenseorIncome;
     public ConstraintLayout iteam_background;

    public ExpenseHolder(@NonNull View itemView) {
        super(itemView);
        time = itemView.findViewById(R.id.iteam_time);
        category = itemView.findViewById(R.id.iteam_category_name);
        amount = itemView.findViewById(R.id.iteam_amount);
        expenseorIncome = itemView.findViewById(R.id.iteam_expenseOrincome);
        remove =itemView.findViewById(R.id.iteam_delete_button);
        iteam_background=itemView.findViewById(R.id.iteam_background_depit);
    }
}
