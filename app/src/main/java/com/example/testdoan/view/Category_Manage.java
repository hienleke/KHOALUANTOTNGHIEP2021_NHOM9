package com.example.testdoan.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.testdoan.R;
import com.example.testdoan.externalView.Iteam_category_adapter;
import com.example.testdoan.model.Category;
import com.example.testdoan.model.Expense;
import com.example.testdoan.model.User;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class Category_Manage extends AppCompatActivity {

    public static ViewPager2 viewPager2;
    public static User user;
    private CategoryAdapterViewpager adapterViewpager;
    private Button Openaddform;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category__manage);
        user = new User();
        Toolbar toolbar = (Toolbar) findViewById(R.id.awesome_toolbar);
        setSupportActionBar(toolbar);
        FirebaseFirestore.setLoggingEnabled(true);
        Openaddform = findViewById(R.id.openAddform);
        Intent t = getIntent();
         String ididid = t.getStringExtra("idcuauser");
        user.setId(ididid);
        Openaddform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putInt("incomeOrexpense", viewPager2.getCurrentItem());
                BottomSheetDialogFragment fg = Form_add_category.newInstance(b,true);
                fg.show(getSupportFragmentManager(), "Xxxx");
            }
        });

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        viewPager2 = findViewById(R.id.viewpager2_category_manage);
        viewPager2.setAdapter(new CategoryAdapterViewpager(getSupportFragmentManager(),getLifecycle()));
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                   if(position==0)
                    tab.setText("INCOME");
                    else
                        tab.setText("EXPENSE");
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