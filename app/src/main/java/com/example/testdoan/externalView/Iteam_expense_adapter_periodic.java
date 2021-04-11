package com.example.testdoan.externalView;

import android.content.Context;
import android.content.DialogInterface;
import android.icu.text.DecimalFormat;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;

import com.example.testdoan.R;

import com.example.testdoan.model.ExpensePeriodic;
import com.example.testdoan.view.Form_add_expense;
import com.example.testdoan.view.MainActivity;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.text.SimpleDateFormat;
import java.util.HashMap;

import com.example.testdoan.view.Expense_periodicHolder;

@RequiresApi(api = Build.VERSION_CODES.N)
public class Iteam_expense_adapter_periodic extends FirestoreRecyclerAdapter<ExpensePeriodic,Expense_periodicHolder> {
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    DecimalFormat decimalFormat = new DecimalFormat("0.0");
    private  SimpleDateFormat format;
    private Context context;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public Iteam_expense_adapter_periodic(@NonNull FirestoreRecyclerOptions<ExpensePeriodic> options,Context context) {
        super(options);
            format = new SimpleDateFormat("EEE, dd/MMM/yyyy");
            this.context = context;
    }


    @Override
    protected void onBindViewHolder(@NonNull Expense_periodicHolder holder, int position, @NonNull ExpensePeriodic model) {
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
                                .collection("expensePeriodic").document(model.getId())
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(context, "success", Toast.LENGTH_SHORT).show();

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
        holder.switcher.setChecked(model.isEnable());
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
                b.putBoolean("enable",model.isEnable());
                BottomSheetDialogFragment fg = Form_add_expense.newInstance(b);
                fg.show(((AppCompatActivity)context).getSupportFragmentManager(),"tagtag");
            }
        });


        holder.switcher.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(buttonView.isChecked())
                {
                    holder.switcher.setEnabled(false);
                    MainActivity.db
                            .collection("users")
                            .document(MainActivity.user.getId())
                            .collection("expensePeriodic").document(model.getId())
                            .update("enable",true)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(context, "success", Toast.LENGTH_SHORT).show();
                                    holder.switcher.setEnabled(true);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(context, "fail", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
                else
                {
                    MainActivity.db
                            .collection("users")
                            .document(MainActivity.user.getId())
                            .collection("expensePeriodic").document(model.getId())
                            .update("enable",false)

                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                            Toast.makeText(context, "success", Toast.LENGTH_SHORT).show();
                                    holder.switcher.setEnabled(true);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(context, "fail", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
            });
            }




    @NonNull
    @Override
    public Expense_periodicHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view
                = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.iteam_expense_debitrepay_periodic, parent, false);

        return new Expense_periodicHolder(view);
    }
}
