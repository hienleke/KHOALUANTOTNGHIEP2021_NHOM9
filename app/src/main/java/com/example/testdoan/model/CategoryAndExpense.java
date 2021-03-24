package com.example.testdoan.model;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class CategoryAndExpense {
    @Embedded
    public Category category;
    @Relation(
            parentColumn = "id",
            entityColumn = "categoryID"
    )
    public List<Expense> expenses;
}
