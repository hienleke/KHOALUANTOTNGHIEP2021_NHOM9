package com.example.testdoan.view;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testdoan.R;

public class ExpenseHolder extends RecyclerView.ViewHolder {
     TextView time;
     TextView category;
     TextView amount;
     TextView expenseorIncome;

    public ExpenseHolder(@NonNull View itemView) {
        super(itemView);
        time = itemView.findViewById(R.id.iteam_time);
        category = itemView.findViewById(R.id.iteam_category);
        amount = itemView.findViewById(R.id.iteam_amount);
        expenseorIncome = itemView.findViewById(R.id.iteam_expenseOrincome);
    }
}
