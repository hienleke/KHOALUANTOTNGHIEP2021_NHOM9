package com.example.testdoan.externalView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.icu.text.DecimalFormat;
import android.icu.text.DecimalFormatSymbols;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.testdoan.R;
import com.example.testdoan.model.Expense;
import com.example.testdoan.model.ExpensePeriodic;
import com.example.testdoan.model.Planing;
import com.example.testdoan.view.MainActivity;
import com.example.testdoan.view.Planning_Holder;
import com.example.testdoan.view.form_add_Planning;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Nullable;

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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onBindViewHolder(@NonNull Planning_Holder holder, int position, @NonNull Planing model) {

        holder.target.setText(String.valueOf(decimalFormat.format(model.getAmount())));
        holder.title.setText(model.getTitle());
        holder.starttime.setText(format.format(model.getTimeStart().toDate()));
        holder.endtime.setText(format.format(model.getTimeEnd().toDate()));
        holder.current.setText(String.valueOf(decimalFormat.format(model.getCurrent())));
        holder.progressBar2.setMax((int) model.getAmount());
        if(holder!=null)
        setprocess(holder.progressBar2,holder.current,model.getTimeStart(),model.getTimeEnd(),model.getId(),holder.Estimated_time,model.getAmount());
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
    @RequiresApi(api = Build.VERSION_CODES.O)
    void setprocess(ProgressBar v, TextView current, Timestamp start, Timestamp end, String id, TextView estime, double target)
    {
        MainActivity.
                db.collection("users").document(MainActivity.user.getId()).collection("planning").document(id)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w("xxx", "Listen failed.", e);
                            return;
                        }

                        if (snapshot != null && snapshot.exists()) {
                            Log.d("xxxbudget", "Current data: " + snapshot.getData());

                            SimpleDateFormat formatter = new SimpleDateFormat("EEE, dd/MMM/yyyy");
                            double amount = snapshot.getDouble("amount");
                            Timestamp timeEnd = snapshot.getTimestamp("timeEnd");
                            Timestamp timeStart = snapshot.getTimestamp("timeStart");
                            Date endd = timeEnd.toDate();
                            Date startt = timeStart.toDate();
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(start.toDate());
                            int year = calendar.get(Calendar.YEAR);
//Add one to month {0 - 11}
                            int month = calendar.get(Calendar.MONTH) + 1;
                            int day = calendar.get(Calendar.DAY_OF_MONTH);

                            LocalDate localDate2begin = LocalDate.of(year, month, day);

                            calendar.setTime(end.toDate());
                            year = calendar.get(Calendar.YEAR);
//Add one to month {0 - 11}
                            month = calendar.get(Calendar.MONTH) + 1;
                            day = calendar.get(Calendar.DAY_OF_MONTH);
                            LocalDate localDate2end = LocalDate.of(year, month, day);

                            ZoneId zoneid2 = ZoneId.systemDefault();
                            Instant instant2 = Instant.now();
                            ZoneOffset currentOffsetForMyZone2 = zoneid2.getRules().getOffset(instant2);
                            Date begin2 = Date.from(localDate2begin.atStartOfDay(zoneid2).toInstant());
                            Date end2 = Date.from(localDate2end.atTime(23, 59, 59).toInstant(currentOffsetForMyZone2));


                            MainActivity.db
                                    .collection("users")
                                    .document(MainActivity.user.getId())
                                    .collection("expense")
                                    .whereGreaterThanOrEqualTo("timeCreated", begin2)
                                    .whereLessThanOrEqualTo("timeCreated", end2).get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            double tong = 0;
                                            double tong1 = 0;
                                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                                if (doc.getBoolean("expen")) {
                                                    tong += doc.getDouble("amount");
                                                }
                                                else{
                                                    tong1 += doc.getDouble("amount");
                                                }
                                            }
                                            int x = (int) tong1 - (int) tong;
                                            v.setProgress(x,true);


                                            current.setText(decimalFormat.format(x));
                                            if( v.getMax()-x <= 0)
                                           {
                                                estime.setText("The plan has been completed !!!");
                                                estime.setTextColor(ContextCompat.getColor(context, R.color.backgroundselect));
                                                return;
                                          }
                                            if(v.getMax()-x > 0 && new Date().after(end.toDate()))
                                            {
                                                estime.setText("It's too late to complete the plan. The planning can not be successful !!!");
                                                estime.setTextColor(ContextCompat.getColor(context, R.color.red_900));
                                                return;
                                            }
                                            MainActivity.db
                                                    .collection("users")
                                                    .document(MainActivity.user.getId())
                                                    .collection("expensePeriodic").whereEqualTo("enable", true)
                                                    .get()
                                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                            double tong = 0;
                                                            double tong1 = 0;

                                                            List<ExpensePeriodic> thutheo_thang = new ArrayList<>();
                                                            List<ExpensePeriodic> chitheo_thang = new ArrayList<>();
                                                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                                                if (doc.getBoolean("expen") && doc.getString("period").equalsIgnoreCase("daily")) {
                                                                    tong += doc.getDouble("amount");
                                                                }
                                                                else if (!doc.getBoolean("expen") && doc.getString("period").equalsIgnoreCase("daily")){
                                                                    tong1 += doc.getDouble("amount");
                                                                }
                                                                else  if (doc.getBoolean("expen") && doc.getString("period").equalsIgnoreCase("monthly")) {
                                                                   chitheo_thang.add(new ExpensePeriodic(doc.getString("category"),doc.getTimestamp("timeCreated"),doc.getString("note") ,doc.getDouble("amount"),doc.getBoolean("expen"),doc.getBoolean("enable") ));
                                                                }
                                                                else  if (!doc.getBoolean("expen") && doc.getString("period").equalsIgnoreCase("monthly")) {
                                                                    thutheo_thang.add(new ExpensePeriodic(doc.getString("category"), doc.getTimestamp("timeCreated"), doc.getString("note"), doc.getDouble("amount"), doc.getBoolean("expen"), doc.getBoolean("enable")));
                                                                }
                                                            }
                                                            double y = (double) tong1 - (double) tong;
                                                            double tamppp =0.0;
                                                            double conlai = target-x;
                                                            for (Date date = new Date(); date.before(end.toDate());)
                                                            {
                                                                tamppp+=y;
                                                                    for (ExpensePeriodic ep : chitheo_thang)
                                                                    {
                                                                        if(ep.getTimeCreated().toDate().getDate()==date.getDate())
                                                                        {
                                                                            tamppp-=ep.getAmount();
                                                                        }
                                                                    }
                                                                for (ExpensePeriodic ep : thutheo_thang)
                                                                {
                                                                    if(ep.getTimeCreated().toDate().getDate()==date.getDate())
                                                                    {
                                                                        tamppp+=ep.getAmount();
                                                                    }
                                                                }
                                                                    if(tamppp>=conlai)
                                                                    {
                                                                        estime.setText(format.format(date));
                                                                        estime.setTextColor(ContextCompat.getColor(context, R.color.orange_500));
                                                                        break;
                                                                    }


                                                                Calendar c = Calendar.getInstance();
                                                                c.setTime(date);
                                                                c.add(Calendar.DATE, 1);
                                                                date=c.getTime();
                                                                if(date.after(end.toDate()) && tamppp<=0)
                                                                {
                                                                    estime.setTextColor(ContextCompat.getColor(context, R.color.red_700));
                                                                    estime.setText(" Can't not be done right now because you don't have enough income !!!");
                                                                    break;
                                                                }

                                                            }





                                                        }
                                                    });








                                        }
                                    });




                        }
                    }
                });



    }






}
