package com.example.testdoan.service;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.testdoan.model.ExpensePeriodic;
import com.example.testdoan.model.User;
import com.example.testdoan.repository.Budgetmodify;
import com.example.testdoan.view.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RequiresApi(api = Build.VERSION_CODES.N)
public class WorkForPeriodTask_daily_monthly extends Worker {
    public WorkForPeriodTask_daily_monthly(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }
    private User u ;

    @NonNull
    @Override
    public Result doWork() {
        FirebaseFirestore db = MainActivity.db;
        List<ExpensePeriodic> cities = new ArrayList<>();
        u= MainActivity.user;
        db.collection("users")
                .document(u.getId())
                .collection("expensePeriodic").whereEqualTo("enable", true).whereEqualTo("period","daily")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                cities.add(new ExpensePeriodic(doc.getString("category"),doc.getTimestamp("timeCreated"),doc.getString("note") ,doc.getDouble("amount"),doc.getBoolean("expen"),doc.getBoolean("enable") ));

                            }
                            CollectionReference vcl = db
                                    .collection("users")
                                    .document(u.getId())
                                    .collection("expense");
                            for (ExpensePeriodic v : cities)
                            {
                                Map<String, Object> data = new HashMap<>();
                                data.put("amount", v.getAmount());
                                data.put("category", v.getCategory());
                                data.put("expen", v.isExpen());
                                data.put("note", v.getNote());
                                data.put("timeCreated", Timestamp.now());

                                vcl.add(data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Toast.makeText(getApplicationContext(), "success",Toast.LENGTH_LONG).show();
                                        Budgetmodify.modify(v.getAmount(),v.isExpen());
                                    }
                                })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(getApplicationContext(), "fail",Toast.LENGTH_LONG).show();
                                            }
                                        });
                            }
                        } else {
                            Log.d("Xxx", "Error getting documents: ", task.getException());
                        }
                    }
                });


        db.collection("users")
                .document(u.getId())
                .collection("expensePeriodic").whereEqualTo("enable", true).whereEqualTo("period","monthly")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot doc : task.getResult()) {

                                cities.add(new ExpensePeriodic(doc.getString("category"),doc.getTimestamp("timeCreated"),doc.getString("note") ,doc.getDouble("amount"),doc.getBoolean("expen"),doc.getBoolean("enable") ));

                            }
                            CollectionReference vcl = db
                                    .collection("users")
                                    .document(u.getId())
                                    .collection("expense");


                            for (ExpensePeriodic v : cities)
                            {
                                Date from  = v.getTimeCreated().toDate();
                                if(!CompareDatetoadd(from))
                                    continue;
                                Map<String, Object> data = new HashMap<>();
                                data.put("amount", v.getAmount());
                                data.put("category", v.getCategory());
                                data.put("expen", v.isExpen());
                                data.put("note", v.getNote());
                                data.put("timeCreated", Timestamp.now());

                                vcl.add(data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Toast.makeText(getApplicationContext(), "success",Toast.LENGTH_LONG).show();
                                        Budgetmodify.modify(v.getAmount(),v.isExpen());
                                    }
                                })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(getApplicationContext(), "fail",Toast.LENGTH_LONG).show();
                                            }
                                        });
                            }
                        } else {
                            Log.d("Xxx", "Error getting documents: ", task.getException());
                        }
                    }
                });





        return Result.success();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private boolean CompareDatetoadd(Date iteam)
    {

        Calendar calendar =Calendar.getInstance();
        calendar.setTime(iteam);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        LocalDate localDatefrom = LocalDate.of(year,month,day);
        LocalDate lastDateofrom = LocalDate.of(year, month, day).with(TemporalAdjusters.lastDayOfMonth());

        LocalDate lastDateofthismonth = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());
        Period periodToNextJavaRelease = Period.between(localDatefrom, LocalDate.now());
        if(periodToNextJavaRelease.getMonths()>=1)
        {
            if(localDatefrom.isEqual(lastDateofrom))
            {
                if(LocalDate.now().isEqual(lastDateofthismonth))
                    return true;
            }
            else if (localDatefrom.getDayOfMonth()== LocalDate.now().getDayOfMonth())
            {
                return true;
            }
        }


        return false;



    }
}

