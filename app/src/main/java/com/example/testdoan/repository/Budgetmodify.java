package com.example.testdoan.repository;

import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.testdoan.view.MainActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

public class Budgetmodify {

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void modify(double amount, boolean expen)
    {
        amount =  expen==true ?  MainActivity.budget -amount :  MainActivity.budget +amount;
        MainActivity.db
                .collection("users")
                .document(MainActivity.user.getId()).collection("profile").document("profile")
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            MainActivity.db
                    .collection("users")
                    .document(MainActivity.user.getId()).collection("profile").document("profile")
                    .update("budget", amount)
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


    public static void savelimit(double amount)
    {

        Map<String, Double> data = new HashMap<>();
        data.put("limit", amount);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            MainActivity.db
                    .collection("users")
                    .document(MainActivity.user.getId()).collection("profile").document("profile")
                    .update("limit", amount)
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
    public static void savenotice(boolean xxx)
    {

        Map<String, Boolean> data = new HashMap<>();
        data.put("notice", xxx);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            MainActivity.db
                    .collection("users")
                    .document(MainActivity.user.getId()).collection("profile").document("profile")
                    .update("notice", xxx)
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


    public static double getlimit()
    {


       return 0;

    }

}
