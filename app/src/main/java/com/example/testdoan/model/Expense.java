package com.example.testdoan.model;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentId;


public class Expense {
    @DocumentId
    private String id;
    private String category;
    private Timestamp timeCreated;
    private String note;
    private double amount;
    private boolean expen;

    public Expense() {
    }

    public Expense(String id, String category, Timestamp timeCreated, String note, double amount, boolean expen) {
        this.id = id;
        this.category = category;
        this.timeCreated = timeCreated;
        this.note = note;
        this.amount = amount;
        this.expen = expen;
    }

    public Expense(String category, Timestamp timeCreated, String note, double amount, boolean expen) {
        this.category = category;
        this.timeCreated = timeCreated;
        this.note = note;
        this.amount = amount;
        this.expen = expen;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Timestamp getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(Timestamp timeCreated) {
        this.timeCreated = timeCreated;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public boolean isExpen() {
        return expen;
    }

    public void setExpen(boolean expen) {
        this.expen = expen;
    }
}
