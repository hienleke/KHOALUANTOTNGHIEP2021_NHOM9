package com.example.testdoan.model;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentId;

public class Planing {
    @DocumentId
    private String id;
    private Timestamp endTime;
    private double amount;
    private Timestamp startTime;

    public Planing() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Planing{" +
                "id='" + id + '\'' +
                ", endTime=" + endTime +
                ", amount=" + amount +
                '}';
    }
}
