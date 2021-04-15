package com.example.testdoan.externalView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.icu.text.DecimalFormat;
import android.icu.text.DecimalFormatSymbols;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.testdoan.R;
import com.example.testdoan.model.Expense;
import com.example.testdoan.model.Planing;
import com.example.testdoan.repository.Budgetmodify;
import com.example.testdoan.view.ExpenseHolder;
import com.example.testdoan.view.Form_add_expense;
import com.example.testdoan.view.MainActivity;
import com.example.testdoan.view.Planning_Holder;
import com.example.testdoan.view.form_add_Planning;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.text.SimpleDateFormat;

@RequiresApi(api = Build.VERSION_CODES.N)
public class Iteam_planning_adapter extends FirestoreRecyclerAdapter<Planing, Planning_Holder> {
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    DecimalFormat decimalFormat = new DecimalFormat("#,###.00 ¤");
    private  SimpleDateFormat format;
    private Context context;
    public Iteam_planning_adapter(@NonNull FirestoreRecyclerOptions<Planing> options, Context context) {
        super(options);
        format = new SimpleDateFormat("EEE, dd/MMM/yyyy");
        this.context = context;
        DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();
        symbols.setDecimalSeparator('.');
        decimalFormat.setDecimalFormatSymbols(symbols);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onBindViewHolder(@NonNull Planning_Holder holder, int position, @NonNull Planing model) {

        holder.current.setText(String.valueOf(decimalFormat.format(model.getCurrent())));
        holder.target.setText(String.valueOf(decimalFormat.format(model.getAmount())));
        holder.title.setText(model.getTitle());
        holder.starttime.setText(format.format(model.getTimeStart().toDate()));
        holder.endtime.setText(format.format(model.getTimeEnd().toDate()));


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
                                .collection("planning").document(model.getId())
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
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putString("id", model.getId());
                b.putString("amount", String.valueOf(model.getAmount()));
                b.putString("startTime", String.valueOf(model.getTimeStart().toDate()));
                b.putString("endTime", String.valueOf(model.getTimeEnd().toDate()));
                b.putString("title", model.getTitle());
                BottomSheetDialogFragment fg = form_add_Planning.newInstance(b);
                fg.show(((AppCompatActivity)context).getSupportFragmentManager(),"tagtag");
            }
        });
    }

    @NonNull
    @Override
    public Planning_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view
                = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.savingplanning_iteam, parent, false);

        return new Planning_Holder(view);
    }
}
