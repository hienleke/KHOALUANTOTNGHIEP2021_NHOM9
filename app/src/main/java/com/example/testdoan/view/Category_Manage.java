package com.example.testdoan.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.testdoan.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class Category_Manage extends AppCompatActivity {

    private ViewPager2 viewPager2;
    private CategoryAdapterViewpager adapterViewpager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category__manage);

        ActionBar actionBar = getSupportActionBar();

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);


        viewPager2 = findViewById(R.id.viewpager2_category_manage);
        viewPager2.setAdapter(new CategoryAdapterViewpager(getSupportFragmentManager(),getLifecycle()));
        TabLayout tabLayout = findViewById(R.id.tabLayout);
       // new TabLayoutMediator(tabLayout, viewPager2).attach();
        new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                   if(position==0)
                    tab.setText("INCONME");
                    else
                        tab.setText("ENPENSIVE");
            }
        }).attach();

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent();
        intent.putExtra("category","asdfasfasfasfsd");
        setResult(Form_add_expense.requestcodeForcategory,intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
       switch (item.getItemId()) {
           case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}