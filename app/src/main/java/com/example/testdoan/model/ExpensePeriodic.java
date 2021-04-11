package com.example.testdoan.model;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentId;

public class ExpensePeriodic {

    @DocumentId
    private String id;
    private String category;
    private Timestamp timeCreated;
    private String note;
    private double amount;
    private boolean expen;
    private boolean enable;

    public ExpensePeriodic(String id, String category, Timestamp timeCreated, String note, double amount, boolean expen, boolean enable) {
        this.id = id;
        this.category = category;
        this.timeCreated = timeCreated;
        this.note = note;
        this.amount = amount;
        this.expen = expen;

        this.enable = enable;
    }



    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public ExpensePeriodic() {
    }

    public ExpensePeriodic(String id, String category, Timestamp timeCreated, String note, double amount, boolean expen) {
        this.id = id;
        this.category = category;
        this.timeCreated = timeCreated;
        this.note = note;
        this.amount = amount;
        this.expen = expen;
    }

    public ExpensePeriodic(String category, Timestamp timeCreated, String note, double amount, boolean expen) {
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
