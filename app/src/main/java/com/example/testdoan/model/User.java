package com.example.testdoan.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;


public class User {
    private int id;
    private String name;
    private String sdt;
    private String email;
    private String pass;

    public User(String name, String sdt, String email, String pass) {
        this.name = name;
        this.sdt = sdt;
        this.email = email;
        this.pass = pass;
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

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
