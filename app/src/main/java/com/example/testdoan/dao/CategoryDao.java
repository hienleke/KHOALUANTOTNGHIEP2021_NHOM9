package com.example.testdoan.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.testdoan.model.Category;
import com.example.testdoan.model.User;

import java.util.List;

@Dao
public interface CategoryDao {
    @Insert
    void insert(Category category);

    @Update
    public void updateCategories(Category... categories);

    @Transaction
    @Query("DELETE FROM category")
    void deleteAll();

    @Transaction
    @Query("SELECT * FROM category WHERE id = :id")
    public LiveData<Category> loadCategoryById(int id);

    @Transaction
    @Query("SELECT * from category")
    LiveData<List<Category>> getAllcategorys();
}
