package com.example.testdoan.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.testdoan.model.Category;
import com.example.testdoan.model.Expense;
import com.example.testdoan.model.User;

import java.util.List;

@Dao
public interface ExpenseDao {
    @Insert
    void insert(Expense expense);

    @Update
    public void updateExpense(Expense... expenses);

    @Transaction
    @Query("DELETE FROM Expense")
    void deleteAll();

    @Transaction
    @Query("SELECT * FROM Expense WHERE id = :id")
    public LiveData<Expense> loadExpenseById(int id);

    @Transaction
    @Query("SELECT * from Expense")
    LiveData<List<Expense>> getAllexpenses();
}
