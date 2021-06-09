package com.example.testdoan.model;

public class Profile {
    private User user;
    private double limit;
    private double budget;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public double getLimit() {
        return limit;
    }

    public void setLimit(double limit) {
        this.limit = limit;
    }

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    public Profile(User user, double limit, double budget) {
        this.user = user;
        this.limit = limit;
        this.budget = budget;
    }
}
