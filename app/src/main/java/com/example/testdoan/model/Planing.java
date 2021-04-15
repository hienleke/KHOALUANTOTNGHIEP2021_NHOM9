package com.example.testdoan.model;

import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.testdoan.view.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.Date;

import javax.annotation.Nullable;

public class Planing {
    @DocumentId
    private String id;
    private Timestamp timeStart;
    private double amount;
    private Timestamp timeEnd;
    private double current;
    private String title;

    public Planing() {
    }

    public Timestamp getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(Timestamp timeStart) {
        this.timeStart = timeStart;
    }

    public Timestamp getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(Timestamp timeEnd) {
        this.timeEnd = timeEnd;
    }

    public Planing(String id, Timestamp timeStart, double amount, Timestamp timeEnd, double current, String title) {
        this.id = id;
        this.timeStart = timeStart;
        this.amount = amount;
        this.timeEnd = timeEnd;
        this.current = current;
        this.title = title;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public double getCurrent() {
         result =0;
        MainActivity.
                db.collection("users").document(MainActivity.user.getId()).collection("planning").document(id)
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
                                Timestamp timeEnd = snapshot.getTimestamp("timeEnd");
                                Timestamp timeStart = snapshot.getTimestamp("timeStart");
                                Date endd = timeEnd.toDate();
                                Date startt = timeStart.toDate();



                                Calendar calendar = Calendar.getInstance();
                                calendar.setTime(getTimeStart().toDate());
                                int year = calendar.get(Calendar.YEAR);
//Add one to month {0 - 11}
                                int month = calendar.get(Calendar.MONTH) + 1;
                                int day = calendar.get(Calendar.DAY_OF_MONTH);


                                LocalDate localDate2begin = LocalDate.of(year,month,day);


                                calendar.setTime(getTimeEnd().toDate());
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
                                                result = tong1-tong;
                                            }
                                        });


                            }




                        }
                    }
                });

    }

    public void setCurrent(double current) {
        this.current = current;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }


}
