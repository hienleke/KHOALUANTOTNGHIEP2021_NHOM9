package com.example.testdoan.view;

import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testdoan.R;

public class Expense_periodicHolder extends RecyclerView.ViewHolder {

    public TextView time;
    public ImageView remove;
     public TextView category;
     public TextView amount;
     public TextView expenseorIncome;
     public Switch switcher;

    public Expense_periodicHolder(@NonNull View itemView) {
        super(itemView);
        time = itemView.findViewById(R.id.iteam_time_periodic);
        category = itemView.findViewById(R.id.iteam_category_name_periodic);
        amount = itemView.findViewById(R.id.iteam_amount_periodic);
        expenseorIncome = itemView.findViewById(R.id.iteam_expenseOrincome_periodic);
        remove =itemView.findViewById(R.id.iteam_delete_button_periodic);
        switcher = itemView.findViewById(R.id.switch1);
    }
}
