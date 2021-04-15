package com.example.testdoan.view;

import android.content.DialogInterface;
import android.icu.text.DecimalFormat;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testdoan.R;
import com.example.testdoan.externalView.Iteam_expense_adapter_periodic;
import com.example.testdoan.externalView.Iteam_planning_adapter;
import com.example.testdoan.model.ExpensePeriodic;
import com.example.testdoan.model.Planing;
import com.example.testdoan.repository.Budgetmodify;
import com.example.testdoan.viewmodel.PlanningViewModel;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.Date;

@RequiresApi(api = Build.VERSION_CODES.N)
public class Planning extends Fragment {

    private PlanningViewModel mViewModel;
    private Button btnSave;

    private ListView lv;
    private FloatingActionButton addrecurringExpense;
    private FloatingActionButton addrecurringExpense_monthly;
    private FloatingActionButton addPlanning;
    DecimalFormat decimalFormat = new DecimalFormat("0.0");
    private Iteam_expense_adapter_periodic adapter;
    private TextView starttime;
    private double amountqqq;
    private Iteam_expense_adapter_periodic adapter_monthly;
    private Iteam_planning_adapter adapter_planning;

    public static Planning newInstance() {
        return new Planning();
    }
    private RecyclerView recyclerView;
    private RecyclerView recyclerView_monthly;
    private RecyclerView recycleview_planning;
    private ProgressBar progressBar2;
    private TextView endtime;
    private TextView Amount;
    private TextView current;

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
        adapter_monthly.startListening();
        adapter_planning.startListening();
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Query query = MainActivity.db
                .collection("users")
                .document(MainActivity.user.getId())
                .collection("expensePeriodic").whereEqualTo("period", "daily");

        FirestoreRecyclerOptions<ExpensePeriodic> options = new FirestoreRecyclerOptions.Builder<com.example.testdoan.model.ExpensePeriodic>()
                .setQuery(query, com.example.testdoan.model.ExpensePeriodic.class)
                .build();

        adapter = new Iteam_expense_adapter_periodic(options,getContext());


        Query query_monthly = MainActivity.db
                .collection("users")
                .document(MainActivity.user.getId())
                .collection("expensePeriodic").whereEqualTo("period", "monthly");

        FirestoreRecyclerOptions<ExpensePeriodic> optionsMonthly = new FirestoreRecyclerOptions.Builder<com.example.testdoan.model.ExpensePeriodic>()
                .setQuery(query_monthly, com.example.testdoan.model.ExpensePeriodic.class)
                .build();

        adapter_monthly = new Iteam_expense_adapter_periodic(optionsMonthly,getContext());



        Query query_planning = MainActivity.db
                .collection("users")
                .document(MainActivity.user.getId())
                .collection("planning");

        FirestoreRecyclerOptions<Planing> optionsPlanning = new FirestoreRecyclerOptions.Builder<com.example.testdoan.model.Planing>()
                .setQuery(query_planning, com.example.testdoan.model.Planing.class)
                .build();

        adapter_planning = new Iteam_planning_adapter(optionsPlanning,getContext());






    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.planning_fragment, container, false);
        EditText txtBudget = v.findViewById(R.id.txtbudget);
        txtBudget.setText(String.valueOf(decimalFormat.format(MainActivity.budget)));
        btnSave = v.findViewById(R.id.updateBudget);
        addPlanning =v.findViewById(R.id.addPlanning);



        addPlanning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialogFragment fg = form_add_Planning.newInstance(null);
                fg.show(getFragmentManager(),"xxx");
            }
        });


        addrecurringExpense= v.findViewById(R.id.addchitieuthuongxuyen);
        addrecurringExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putString("period", "daily");
                        BottomSheetDialogFragment fg = Form_add_Expense_period.newInstance(b);
                        fg.show(getFragmentManager(),"xxx");
            }
        });
        addrecurringExpense_monthly= v.findViewById(R.id.addchitieuthuongxuyen_monthly);
        addrecurringExpense_monthly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putString("period", "monthly");
                BottomSheetDialogFragment fg = Form_add_Expense_period.newInstance(b);
                fg.show(getFragmentManager(),"xxx");
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Do you make sure update Budget ?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Budgetmodify.init(Double.valueOf(txtBudget.getText().toString().replace(",",".")));

                    }
                })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
            }
        });


        // Create the observer which updates the UI.

        recycleview_planning = v.findViewById(R.id.planning_recycleview);
        recycleview_planning.addItemDecoration(new SpacesItemDecoration(30));
        recycleview_planning.setAdapter(adapter_planning);
        recycleview_planning.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView_monthly= v.findViewById(R.id.periodicIteam_expense_monthly);
        recyclerView_monthly.addItemDecoration(new SpacesItemDecoration(30));
        recyclerView_monthly.setAdapter(adapter_monthly);
        recyclerView_monthly.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView = v.findViewById(R.id.periodicIteam_expense);
        recyclerView.addItemDecoration(new SpacesItemDecoration(30));
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return v ;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(PlanningViewModel.class);
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
        adapter_monthly.stopListening();
        adapter_planning.stopListening();
    }



}