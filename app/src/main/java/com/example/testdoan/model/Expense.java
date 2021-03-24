package com.example.testdoan.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;

@Entity
public class Expense {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private int categoryID;
    private Calendar timeCreated;
    private String note;
    private double worth;
    private boolean applytobudget;
    private boolean isGet;



    public boolean isGet() {
        return isGet;
    }

    public void setGet(boolean get) {
        isGet = get;
    }

    public Expense(int categoryID, Calendar timeCreated, String note, double worth, boolean applytobudget, boolean isGet) {
        this.categoryID = categoryID;
        this.timeCreated = timeCreated;
        this.note = note;
        this.worth = worth;
        this.applytobudget = applytobudget;
        this.isGet = isGet;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public Calendar getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(Calendar timeCreated) {
        this.timeCreated = timeCreated;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public double getWorth() {
        return worth;
    }

    public void setWorth(double worth) {
        this.worth = worth;
    }

    public boolean isApplytobudget() {
        return applytobudget;
    }

    public void setApplytobudget(boolean applytobudget) {
        this.applytobudget = applytobudget;
    }


}
