package com.example.testdoan.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.testdoan.R;
import com.example.testdoan.externalView.Iteam_expense_adapter;
import com.example.testdoan.model.Expense;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.type.DateTime;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;



public class ExpenseFragment extends Fragment {

    private static final String ARG_mode = "param1";
    private static final String ARG_time = "param2";

    private String mode;
    private String time;
    private Button openFormAddDebitorRepay;
    private Button openFormAddExpenseIncome;
    private RecyclerView recyclerView;
    private FirestoreRecyclerAdapter adapter ;

    public ExpenseFragment() {

    }

    public static ExpenseFragment newInstance(String mode, String time) {
        ExpenseFragment fragment = new ExpenseFragment();
        Bundle args = new Bundle();
        args.putString(ARG_mode, mode);
        args.putString(ARG_time, time);
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
            mode = getArguments().getString(ARG_mode);
            time = getArguments().getString(ARG_time);
        }

        Query query = MainActivity.db
                .collection("users")
                .document("YanMbTpDzBW2VKVBwDoC")
                .collection("expense");

        switch (mode) {
            case "date":
                int day = Integer.valueOf(time.split("-")[0]);
                int month = Integer.valueOf(time.split("-")[1]);
                int year = Integer.valueOf(time.split("-")[2]);
                Date begin = new Date(year,month,day);

                SimpleDateFormat sdf = new SimpleDateFormat("yyy-dd-MM-HH.mm.ss");
               sdf.parse(begin.toString());
                long timestamp = begin.getTime();
                query = query.whereGreaterThan("timeCreated", new Timestamp(begin));//.whereLessThan("timeCreated", end);
                break;
            case "week":

                break;
            case "month":

                break;
            case "year":

                break;
        }

        FirestoreRecyclerOptions<com.example.testdoan.model.Expense> options = new FirestoreRecyclerOptions.Builder<com.example.testdoan.model.Expense>()
                .setQuery(query, com.example.testdoan.model.Expense.class)
                .build();

        adapter = new Iteam_expense_adapter(options,getContext());

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
                BottomSheetDialogFragment fg = Form_add_expense.newInstance(null);
                fg.show(getFragmentManager(),"xxx");
            }
        });

        recyclerView.addItemDecoration(new SpacesItemDecoration(50));
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return v;
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }


}