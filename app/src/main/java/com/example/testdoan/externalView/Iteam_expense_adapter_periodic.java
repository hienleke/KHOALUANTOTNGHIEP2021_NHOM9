package com.example.testdoan.externalView;

import android.content.Context;
import android.content.DialogInterface;
import android.icu.text.DecimalFormat;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.testdoan.model.Expense;
import com.example.testdoan.model.ExpensePeriodic;
import com.example.testdoan.repository.Budgetmodify;
import com.example.testdoan.view.ExpenseHolder;
import com.example.testdoan.view.Form_add_expense;
import com.example.testdoan.view.MainActivity;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.text.SimpleDateFormat;

@RequiresApi(api = Build.VERSION_CODES.N)
public class Iteam_expense_adapter_periodic extends FirestoreRecyclerAdapter<ExpensePeriodic, ExpenseHolderPeriodic> {
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    DecimalFormat decimalFormat = new DecimalFormat("0.0");
    private  SimpleDateFormat format;
    private Context context;
    public Iteam_expense_adapter_periodic(@NonNull FirestoreRecyclerOptions<Expense> options, Context context) {
        super(options);
        format = new SimpleDateFormat("EEE, dd/MMM/yyyy");
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ExpenseHolder holder, int position, @NonNull Expense model) {
        boolean expenTemp = model.isExpen();
        double amountTemp= model.getAmount();
        holder.amount.setText(String.valueOf(decimalFormat.format(model.getAmount())));
        holder.category.setText(String.valueOf(model.getCategory()));
        holder.expenseorIncome.setText(model.isExpen()==false ? "Income" : "Expense");
        String strDate = format.format(model.getTimeCreated().toDate());
        holder.time.setText(strDate);
        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Do you want to delete it ?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity.db
                                .collection("users")
                                .document(MainActivity.user.getId())
                                .collection("expense_periodic").document(model.getId())
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(context, "success", Toast.LENGTH_SHORT).show();
                                        Budgetmodify.modify(amountTemp, expenTemp ? false :true);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(context, "fail", Toast.LENGTH_SHORT).show();
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
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putString("id", model.getId());
                b.putString("amount", String.valueOf(model.getAmount()));
                b.putString("category", model.getCategory());
                b.putString("time", String.valueOf(model.getTimeCreated().toDate()));
                b.putString("type", model.isExpen()==true ? "Income" : "Expense");
                b.putString("note", model.getNote());
                BottomSheetDialogFragment fg = Form_add_expense.newInstance(b);
                fg.show(((AppCompatActivity)context).getSupportFragmentManager(),"tagtag");
            }
        });
    }

    @Override
    protected void onBindViewHolder(@NonNull ExpenseHolderPeriodic holder, int position, @NonNull ExpensePeriodic model) {

    }

    @NonNull
    @Override
    public ExpenseHolderPeriodic onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }
}