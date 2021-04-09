package com.example.testdoan.model;


import com.google.firebase.firestore.DocumentId;

public class Category {
    @DocumentId
    private String id;
    private String name;
    private String note;
    private boolean expen;

    public Category() {
    }


    public Category(String name, String note) {
        this.name = name;
        this.note = note;
    }

    public Category(String name, String note, boolean expen) {
        this.name = name;
        this.note = note;
        this.expen = expen;
    }

    public boolean isExpen() {
        return expen;
    }

    public void setExpen(boolean expen) {
        this.expen = expen;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Category(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
