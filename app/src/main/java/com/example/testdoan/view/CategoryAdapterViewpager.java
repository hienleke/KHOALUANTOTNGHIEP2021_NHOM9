package com.example.testdoan.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.testdoan.model.Category;

import java.util.List;

public class CategoryAdapterViewpager  extends FragmentStateAdapter {


    public CategoryAdapterViewpager(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return category_manage_fragment.newInstance("asdfas","ASdfasf");
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
