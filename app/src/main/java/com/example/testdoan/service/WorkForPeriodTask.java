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
import com.example.testdoan.view.MainActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;


@RequiresApi(api = Build.VERSION_CODES.N)
public class WorkForPeriodTask extends Worker {
    public WorkForPeriodTask(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }
    private User u ;

    @NonNull
    @Override
    public Result doWork() {

        u=MainActivity.user;
        FirebaseFirestore db = MainActivity.db;
        Query query = db
                .collection("users")
                .document(u.getId())
                .collection("expensePeriodic").whereEqualTo("enable", true);
        List<ExpensePeriodic> cities = new ArrayList<>();
        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("fail", "Listen failed.", e);
                    return;
                }

                for (QueryDocumentSnapshot doc : value) {
                    if(doc.get("id")!=null)
                    {
                        cities.add(new ExpensePeriodic(doc.getString("id"),doc.getString("category"),doc.getTimestamp("timeCreated"),doc.getString("note") ,doc.getDouble("amount"),doc.getBoolean("expen"),doc.getBoolean("enable") ));
                    }
                }


            }
        });
        DocumentReference vcl = db
                .collection("users")
                .document(u.getId())
                .collection("expensePeriodic").document();
        for (ExpensePeriodic v : cities)
        {
            Map<String, Object> data = new HashMap<>();
            data.put("amount", v.getAmount());
            data.put("category", v.getCategory());
            data.put("expen", v.isExpen());
            data.put("note", v.getNote());
            data.put("timeCreated", Timestamp.now());
            vcl.set(data, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(getApplicationContext(), "success",Toast.LENGTH_LONG).show();
                }

            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "fail",Toast.LENGTH_LONG).show();
                }
            });
        }




        return Result.success();
    }
}
