package com.example.testdoan.view;


import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testdoan.R;
import com.example.testdoan.externalView.Iteam_expense_adapter;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.Query;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;

;

@RequiresApi(api = Build.VERSION_CODES.N)
public class ExpenseFragment extends Fragment {

    private static final String ARG_mode = "param1";
    private static final String ARG_time = "param2";
    private String mode;
    private String time;
    private String USERID;

    public String getUSERID() {
        return USERID;
    }

    public void setUSERID(String USERID) {
        this.USERID = USERID;
    }

    private FloatingActionButton openFormAddExpenseIncome;
    private RecyclerView recyclerView;
    private FirestoreRecyclerAdapter adapter ;


    public ExpenseFragment() {
        USERID=MainActivity.user.getId();
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mode = getArguments().getString(ARG_mode);
            time = getArguments().getString(ARG_time);
        }


        Query query = MainActivity.db
                .collection("users")
                .document(USERID)
                .collection("expense");


        switch (mode) {
            case "date":
                int day = Integer.valueOf(time.split("-")[0]);
                int month = Integer.valueOf(time.split("-")[1]);
                int year = Integer.valueOf(time.split("-")[2]);
                LocalDate localDate1 = LocalDate.of(year, month,day);
                ZoneId zoneid = ZoneId.systemDefault();
                Instant instant = Instant.now();
                ZoneOffset currentOffsetForMyZone = zoneid.getRules().getOffset(instant);
                Date begin =Date.from(localDate1.atStartOfDay(zoneid).toInstant());
                Date end =Date.from(localDate1.atTime(23,59,59).toInstant(currentOffsetForMyZone));
                query = query.whereGreaterThanOrEqualTo("timeCreated", begin).whereLessThanOrEqualTo("timeCreated",end).orderBy("timeCreated");
                break;
            case "week":
                String time2begin = time.split("-")[0] ;
                String time2end = time.split("-")[1];
                int day2begin = Integer.valueOf(time2begin.split("/")[0]);
                int month2begin = Integer.valueOf(time2begin.split("/")[1]);
                int year2begin = Integer.valueOf(time2begin.split("/")[2]);
                int day2end = Integer.valueOf(time2end.split("/")[0]);
                int month2end = Integer.valueOf(time2end.split("/")[1]);
                int year2end = Integer.valueOf(time2end.split("/")[2]);
                LocalDate localDate2begin = LocalDate.of(year2begin, month2begin,day2begin);
                LocalDate localDate2end = LocalDate.of(year2end, month2end,day2end);
                ZoneId zoneid2 = ZoneId.systemDefault();
                Instant instant2 = Instant.now();
                ZoneOffset currentOffsetForMyZone2 = zoneid2.getRules().getOffset(instant2);
                Date begin2 =Date.from(localDate2begin.atStartOfDay(zoneid2).toInstant());
                Date end2 =Date.from(localDate2end.atTime(23,59,59).toInstant(currentOffsetForMyZone2));
                query = query.whereGreaterThanOrEqualTo("timeCreated", begin2).whereLessThanOrEqualTo("timeCreated",end2).orderBy("timeCreated");
                break;
            case "month":
                 month2begin = Integer.valueOf(time.split("/")[0]);
                 year2begin = Integer.valueOf(time.split("/")[1]);
                 localDate2begin = LocalDate.of(year2begin, month2begin,1);
                 localDate2end = LocalDate.of(year2begin, month2begin, 1).with(TemporalAdjusters.lastDayOfMonth());
                zoneid2 = ZoneId.systemDefault();
                 instant2 = Instant.now();
                currentOffsetForMyZone2 = zoneid2.getRules().getOffset(instant2);
                 begin2 =Date.from(localDate2begin.atStartOfDay(zoneid2).toInstant());
                 end2 =Date.from(localDate2end.atTime(23,59,59).toInstant(currentOffsetForMyZone2));
                query = query.whereGreaterThanOrEqualTo("timeCreated", begin2).whereLessThanOrEqualTo("timeCreated",end2).orderBy("timeCreated");
                break;
            case "year":
                year2begin = Integer.valueOf(time);
                localDate2begin = LocalDate.of(year2begin, 1,1);
                localDate2end = LocalDate.of(year2begin, 12, 31);
                zoneid2 = ZoneId.systemDefault();
                instant2 = Instant.now();
                currentOffsetForMyZone2 = zoneid2.getRules().getOffset(instant2);
                begin2 =Date.from(localDate2begin.atStartOfDay(zoneid2).toInstant());
                end2 =Date.from(localDate2end.atTime(23,59,59).toInstant(currentOffsetForMyZone2));
                query = query.whereGreaterThanOrEqualTo("timeCreated", begin2).whereLessThanOrEqualTo("timeCreated",end2).orderBy("timeCreated");
                break;
            case "period":
                 time2begin = time.split("-")[0] ;
                 time2end = time.split("-")[1];
                 day2begin = Integer.valueOf(time2begin.split("/")[0]);
                 month2begin = Integer.valueOf(time2begin.split("/")[1]);
                 year2begin = Integer.valueOf(time2begin.split("/")[2]);
                 day2end = Integer.valueOf(time2end.split("/")[0]);
                 month2end = Integer.valueOf(time2end.split("/")[1]);
                year2end = Integer.valueOf(time2end.split("/")[2]);
                 localDate2begin = LocalDate.of(year2begin, month2begin,day2begin);
                 localDate2end = LocalDate.of(year2end, month2end,day2end);
                 zoneid2 = ZoneId.systemDefault();
                instant2 = Instant.now();
                currentOffsetForMyZone2 = zoneid2.getRules().getOffset(instant2);
                 begin2 =Date.from(localDate2begin.atStartOfDay(zoneid2).toInstant());
                 end2 =Date.from(localDate2end.atTime(23,59,59).toInstant(currentOffsetForMyZone2));
                query = query.whereGreaterThanOrEqualTo("timeCreated", begin2).whereLessThanOrEqualTo("timeCreated",end2).orderBy("timeCreated");
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
        View v = inflater.inflate(R.layout.fragment_expense, container, false);
        openFormAddExpenseIncome =v.findViewById(R.id.addIncome);
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