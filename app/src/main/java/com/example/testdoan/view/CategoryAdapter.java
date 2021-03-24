package com.example.testdoan.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testdoan.model.Category;

import java.util.List;

public class CategoryAdapter  extends RecyclerView.Adapter<CategoryAdapter.categoryViewHolder> {

    private final LayoutInflater mInflater;
    private List<Category> mWords; // Ca

    public CategoryAdapter(Context context) {
        this.mInflater =LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public CategoryAdapter.categoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.categoryViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }


    public class categoryViewHolder extends RecyclerView.ViewHolder {
        public categoryViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
