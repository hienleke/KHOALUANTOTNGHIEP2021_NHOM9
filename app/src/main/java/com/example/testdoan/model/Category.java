package com.example.testdoan.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Category {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String note;

    public Category(String note, String name) {
        this.note = note;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
