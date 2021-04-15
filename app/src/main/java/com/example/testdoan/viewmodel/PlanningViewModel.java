package com.example.testdoan.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.testdoan.model.Planing;


public class PlanningViewModel extends ViewModel {
    private MutableLiveData<Planing> users;
    public LiveData<Planing> getUsers() {
        if (users == null) {
            users = new MutableLiveData<Planing>();
            loadUsers();
        }
        return users;
    }

    private void loadUsers() {
        // Do an asynchronous operation to fetch users.
    }
}