package com.example.testdoan.repository;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.testdoan.view.MainActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.HashMap;
import java.util.Map;

public class Budgetmodify {

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void modify(double amount, boolean expen)
    {
        amount =  expen==true ?  MainActivity.budget -amount :  MainActivity.budget +amount;
        MainActivity.db
                .collection("users")
                .document(MainActivity.user.getId())
                .update("budget",amount)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

    }

    public static void init(double amount)
    {

        Map<String, Double> data = new HashMap<>();
        data.put("budget", amount);
        MainActivity.db
                .collection("users")
                .document(MainActivity.user.getId())
                .set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

    }
}
