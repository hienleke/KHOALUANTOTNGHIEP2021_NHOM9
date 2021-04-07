package com.example.testdoan.externalView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import com.example.testdoan.R;
import com.example.testdoan.model.Category;
import com.example.testdoan.view.CategoryHolder;
import com.example.testdoan.view.Category_Manage;
import com.example.testdoan.view.Form_add_category;
import com.example.testdoan.view.Form_add_expense;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class Iteam_category_adapter extends FirestoreRecyclerAdapter<Category, CategoryHolder> {
    private Context context;
    private ViewPager2 viewPager2;

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public ViewPager2 getViewPager2() {
        return viewPager2;
    }

    public void setViewPager2(ViewPager2 viewPager2) {
        this.viewPager2 = viewPager2;
    }

    public Iteam_category_adapter(@NonNull FirestoreRecyclerOptions<Category> options, Context context) {
        super(options);
        this.context=context;


    }

    @Override
    protected void onBindViewHolder(@NonNull CategoryHolder holder, int position, @NonNull Category model) {
        holder.title.setText(model.getName());
        holder.note.setText(model.getNote());
        holder.edit.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Bundle b = new Bundle();
                b.putString("id", model.getId());
                b.putString("name", model.getName());
                b.putString("note", model.getNote());
                b.putInt("incomeOrexpense", Category_Manage.viewPager2.getCurrentItem());
                BottomSheetDialogFragment fg = Form_add_category.newInstance(b,false);
                fg.show(((AppCompatActivity)context).getSupportFragmentManager(),"tagtag");
                return false;
            }


        });


    }

    @NonNull
    @Override
    public CategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_iteam, parent, false);
        viewPager2 =view.findViewById(R.id.viewpager2_category_manage);
        return new CategoryHolder(view);
    }
}
