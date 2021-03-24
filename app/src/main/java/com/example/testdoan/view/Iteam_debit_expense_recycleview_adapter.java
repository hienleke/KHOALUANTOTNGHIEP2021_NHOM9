package com.example.testdoan.view;

import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class Iteam_debit_expense_recycleview_adapter extends FirestoreRecyclerAdapter<Expense,ExpenseHolder> {
    @Override
    protected void onBindViewHolder(@NonNull ExpenseHolder holder, int position, @NonNull Expense model) {

    }

    @NonNull
    @Override
    public ExpenseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }


    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */

    class ExpenseHoler extends FirestoreRecyclerAdapter.hol

}
