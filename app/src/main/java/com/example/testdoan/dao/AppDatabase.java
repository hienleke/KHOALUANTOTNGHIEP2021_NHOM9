package com.example.testdoan.dao;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.testdoan.model.Category;
import com.example.testdoan.model.Converters;
import com.example.testdoan.model.Expense;
import com.example.testdoan.model.User;

@Database(entities = {User.class, Expense.class, Category.class},
       version = 3)
@TypeConverters({Converters.class})
public abstract  class AppDatabase  extends RoomDatabase {
   public abstract UserDao userDao();
   public abstract CategoryDao categoryDao();
   public abstract ExpenseDao expenseDao();

    private static AppDatabase INSTANCE;

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "app_database")
                            // Wipes and rebuilds instead of migrating
                            // if no Migration object.
                            // Migration is not part of this practical.
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
