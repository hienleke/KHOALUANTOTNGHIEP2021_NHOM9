package com.example.testdoan.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.testdoan.model.Category;
import com.example.testdoan.repository.CategoryRepository;
import java.util.List;

public class CategoryViewmodel  extends AndroidViewModel {
    private CategoryRepository categoryRepository;
    private LiveData<List<Category>> allcategory;

    public CategoryViewmodel(@NonNull Application application) {
        super(application);
        categoryRepository = new CategoryRepository(application);
        allcategory =categoryRepository.getAllWords();
    }

    public void insert(Category category)
    {
        categoryRepository.insert(category);
    }
}
