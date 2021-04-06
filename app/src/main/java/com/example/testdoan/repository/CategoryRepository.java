package com.example.testdoan.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.testdoan.model.Category;

import java.util.List;

public class CategoryRepository  {

    private LiveData<List<Category>> allCategory;

    public CategoryRepository(Application application)
    {

    }

    public LiveData<List<Category>> getAllWords() {
        return allCategory;
    }



}
