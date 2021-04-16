package com.example.testdoan.model;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentId;

public class Planing {
    @DocumentId
    private String id;
    private Timestamp timeStart;
    private double amount;
    private Timestamp timeEnd;
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
        this.title = title;
    }


    public  double getCurrent() {
          return 0;
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
