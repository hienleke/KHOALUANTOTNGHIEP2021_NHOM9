package com.example.testdoan.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.testdoan.model.User;

import java.util.List;

@Dao
public interface UserDao {
    @Insert
    void insert(User user);

    @Update
    public void updateUsers(User... users);

    @Query("DELETE FROM user")
    void deleteAll();

    @Query("SELECT * FROM user WHERE id = :id")
    public LiveData<User> loadUserById(int id);

    @Query("SELECT * from User")
    LiveData<List<User>> getAllusers();
}
