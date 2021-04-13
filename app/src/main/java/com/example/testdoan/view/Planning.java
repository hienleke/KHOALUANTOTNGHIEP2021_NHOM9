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
import com.example.testdoan.model.ExpensePeriodic;
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
    private FloatingActionButton addPlanning;
    DecimalFormat decimalFormat = new DecimalFormat("0.0");
    private Iteam_expense_adapter_periodic adapter;
    private TextView starttime;
    private double amountqqq;

    public static Planning newInstance() {
        return new Planning();
    }
    private RecyclerView recyclerView;
    private ProgressBar progressBar2;
    private TextView endtime;
    private TextView Amount;

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Query query = MainActivity.db
                .collection("users")
                .document(MainActivity.user.getId())
                .collection("expensePeriodic");

        FirestoreRecyclerOptions<ExpensePeriodic> options = new FirestoreRecyclerOptions.Builder<com.example.testdoan.model.ExpensePeriodic>()
                .setQuery(query, com.example.testdoan.model.ExpensePeriodic.class)
                .build();

        adapter = new Iteam_expense_adapter_periodic(options,getContext());

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.planning_fragment, container, false);
        endtime = v.findViewById(R.id.textView17);
        starttime = v.findViewById(R.id.textView12);
        Amount =v.findViewById(R.id.amountExpected);
        progressBar2 = v.findViewById(R.id.progressBar4);
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
                        BottomSheetDialogFragment fg = Form_add_Expense_period.newInstance(null);
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

        amountqqq =0;
        MainActivity.
        db.collection("users").document(MainActivity.user.getId()).collection("planning").document("planning")
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w("xxx", "Listen failed.", e);
                            return;
                        }

                        if (snapshot != null ) {
                            Log.d("xxxbudget", "Current data: " + snapshot.getData());
                            try {
                                SimpleDateFormat formatter =   new SimpleDateFormat("EEE, dd/MMM/yyyy");
                                double amount = snapshot.getDouble("amount");
                                amountqqq=amount;
                                Timestamp timeEnd = snapshot.getTimestamp("timeEnd");
                                Timestamp timeStart = snapshot.getTimestamp("timeStart");
                                starttime.setText(formatter.format(timeStart.toDate()));
                                endtime.setText(formatter.format(timeEnd.toDate()));
                                Amount.setText(String.valueOf(amount));
                                Date endd = timeEnd.toDate();
                                Date startt = timeStart.toDate();



                                Calendar calendar =Calendar.getInstance();
                                calendar.setTime(startt);
                                int year = calendar.get(Calendar.YEAR);
//Add one to month {0 - 11}
                                int month = calendar.get(Calendar.MONTH) + 1;
                                int day = calendar.get(Calendar.DAY_OF_MONTH);


                                LocalDate localDate2begin = LocalDate.of(year,month,day);


                                calendar.setTime(endd);
                                 year = calendar.get(Calendar.YEAR);
//Add one to month {0 - 11}
                                 month = calendar.get(Calendar.MONTH) + 1;
                                day = calendar.get(Calendar.DAY_OF_MONTH);
                                LocalDate localDate2end = LocalDate.of(year,month,day);

                                ZoneId zoneid2 = ZoneId.systemDefault();
                                Instant instant2 = Instant.now();
                                ZoneOffset currentOffsetForMyZone2 = zoneid2.getRules().getOffset(instant2);
                                Date begin2 =Date.from(localDate2begin.atStartOfDay(zoneid2).toInstant());
                                Date end2 =Date.from(localDate2end.atTime(23,59,59).toInstant(currentOffsetForMyZone2));


                                MainActivity.db
                                        .collection("users")
                                        .document(MainActivity.user.getId())
                                        .collection("expense")
                                        .whereGreaterThanOrEqualTo("timeCreated", begin2)
                                        .whereLessThanOrEqualTo("timeCreated",end2).get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                double tong=0;
                                                double tong1=0;
                                                for (QueryDocumentSnapshot doc : task.getResult()) {
                                                    if (doc.getBoolean("expen")) {
                                                        tong+=doc.getDouble("amount");
                                                    }
                                                    if (!doc.getBoolean("expen")) {
                                                        tong1+=doc.getDouble("amount");
                                                    }
                                                }

                                                progressBar2.setMax((int) amountqqq);
                                                progressBar2.setProgress((int)(tong1-tong));
                                            }
                                        });


                            }
                            catch (Exception ex)
                            {
                                ex.printStackTrace();
                                starttime.setText("No planning");
                                endtime.setText("No planning");
                                Amount.setText("No planning");
                            }



                        } else {
                            starttime.setText("No planning");
                            endtime.setText("No planning");
                            Amount.setText("No planning");
                        }
                    }
                });



        v.findViewById(R.id.planning_remove).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Do you want to delete it ?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity.db
                                .collection("users")
                                .document(MainActivity.user.getId())
                                .collection("planning").document("planning")
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(getContext(), "success", Toast.LENGTH_SHORT).show();

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getContext(), "fail", Toast.LENGTH_SHORT).show();
                                    }
                                });

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



        recyclerView = v.findViewById(R.id.periodicIteam_expense);
        recyclerView.addItemDecoration(new SpacesItemDecoration(20));
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
    }



}