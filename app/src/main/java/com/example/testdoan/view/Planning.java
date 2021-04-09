package com.example.testdoan.view;

import android.content.DialogInterface;
import android.icu.text.DecimalFormat;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testdoan.R;
import com.example.testdoan.externalView.Iteam_expense_adapter;
import com.example.testdoan.model.Expense;
import com.example.testdoan.repository.Budgetmodify;
import com.example.testdoan.viewmodel.PlanningViewModel;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.Query;

@RequiresApi(api = Build.VERSION_CODES.N)
public class Planning extends Fragment {

    private PlanningViewModel mViewModel;
    private Button btnSave;
    private ListView lv;
    private FloatingActionButton addrecurringExpense;
    private FloatingActionButton addPlanning;
    DecimalFormat decimalFormat = new DecimalFormat("0.0");
    private Iteam_expense_adapter adapter;
    public static Planning newInstance() {
        return new Planning();
    }
    private RecyclerView recyclerView;


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
                .collection("expense");

        FirestoreRecyclerOptions<Expense> options = new FirestoreRecyclerOptions.Builder<com.example.testdoan.model.Expense>()
                .setQuery(query, com.example.testdoan.model.Expense.class)
                .build();

        adapter = new Iteam_expense_adapter(options,getContext());

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.planning_fragment, container, false);
        EditText txtBudget = v.findViewById(R.id.txtbudget);
        txtBudget.setText(String.valueOf(decimalFormat.format(MainActivity.budget)));
        btnSave = v.findViewById(R.id.updateBudget);
        addrecurringExpense= v.findViewById(R.id.addchitieuthuongxuyen);
        addrecurringExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        BottomSheetDialogFragment fg = Form_add_expense.newInstance(null);
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