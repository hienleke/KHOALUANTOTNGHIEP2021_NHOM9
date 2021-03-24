package com.example.testdoan.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.testdoan.dao.AppDatabase;
import com.example.testdoan.dao.CategoryDao;
import com.example.testdoan.model.Category;

import java.util.List;

public class CategoryRepository {
    private CategoryDao categoryDao;
    private LiveData<List<Category>> allCategory;

    public CategoryRepository(Application application)
    {
        AppDatabase database = AppDatabase.getDatabase(application);
        categoryDao =database.categoryDao();
        allCategory = categoryDao.getAllcategorys();
    }

    public LiveData<List<Category>> getAllWords() {
        return allCategory;
    }

    public void insert (Category category) {
        new insertAsyncTask(categoryDao).execute(category);
    }

    private static class insertAsyncTask extends AsyncTask<Category, Void, Void> {

        private CategoryDao mAsyncTaskDao;

        insertAsyncTask(CategoryDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Category... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }
}
