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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RequiresApi(api = Build.VERSION_CODES.N)
public class WorkForPeriodTask extends Worker {
    public WorkForPeriodTask(@NonNull Context context, @NonNull WorkerParameters workerParams) {
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
                .collection("expensePeriodic").whereEqualTo("enable", true)
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





        return Result.success();
    }
}
