package com.example.testdoan.view;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.testdoan.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;



public class ExpenseFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private Button openFormAddDebitorRepay;
    private Button openFormAddExpenseIncome;
    private RecyclerView recyclerView;
    private FirestoreRecyclerAdapter adapter ;

    public ExpenseFragment() {

    }

    public static ExpenseFragment newInstance(String param1, String param2) {
        ExpenseFragment fragment = new ExpenseFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        Query query = MainActivity.db
                .collection("iteams")
                .orderBy("timestamp")
                .limit(50);


        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    // Handle error
                    //...
                    return;
                }

                // Convert query snapshot to a list of chats
                List<com.example.testdoan.model.Expense> expenses = snapshot.toObjects(com.example.testdoan.model.Expense.class);

                // Update UI
                // ...
            }
        });


        FirestoreRecyclerOptions<com.example.testdoan.model.Expense> options = new FirestoreRecyclerOptions.Builder<com.example.testdoan.model.Expense>()
                .setQuery(query, com.example.testdoan.model.Expense.class)
                .build();




        adapter = new FirestoreRecyclerAdapter<com.example.testdoan.model.Expense, ExpenseHolder>(options) {
            @Override
            public void onBindViewHolder(ExpenseHolder holder, int position, com.example.testdoan.model.Expense model) {
                holder.amount.setText(String.valueOf(model.getWorth()));
                holder.category.setText(String.valueOf(model.getCategoryID()));
                holder.expenseorIncome.setText(model.isGet() ? "income" : "expense");
                holder.time.setText(model.getTimeCreated().toString());
            }

            @Override
            public ExpenseHolder onCreateViewHolder(ViewGroup group, int i) {
                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.message for each item
                View view = LayoutInflater.from(group.getContext())
                        .inflate(R.layout.iteam_expense_debitrepay, group, false);

                return new ExpenseHolder(view);
            }
        };


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_expense, container, false);
        openFormAddDebitorRepay= v.findViewById(R.id.addIncome);
        openFormAddExpenseIncome =v.findViewById(R.id.addExpense);
        recyclerView=v.findViewById(R.id.iteam_debit_expense_recycleview);


        openFormAddExpenseIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialogFragment fg = Form_add_expense.newInstance("Ádfsa","ÁDfa");
                fg.show(getFragmentManager(),"xxx");
            }
        });

        recyclerView.setAdapter(adapter);
        return v;
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}