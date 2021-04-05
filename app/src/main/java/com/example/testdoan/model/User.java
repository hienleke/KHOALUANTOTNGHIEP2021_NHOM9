package com.example.testdoan.model;


import com.google.firebase.firestore.DocumentId;

public class User {
    @DocumentId
    private String id;
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

    public User() {
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


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
