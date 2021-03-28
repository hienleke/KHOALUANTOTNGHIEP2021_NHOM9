package com.example.testdoan.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.fragment.app.FragmentManager;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.FrameLayout;

import android.widget.Toast;
import com.example.testdoan.R;
import com.example.testdoan.externalView.Tools;
import com.example.testdoan.model.User;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


import  com.example.testdoan.externalView.HorizontalCalendarView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


public class MainActivity extends AppCompatActivity {

    private FrameLayout container;
    private ActionBar actionBar;
    private HorizontalCalendarView calendarView;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener;
    private BottomNavigationView bottomNavigationView;
    private ArrayList datesToBeColored;
    Calendar starttime,endtime;
    public static User user;
    public static FirebaseFirestore db  = FirebaseFirestore.getInstance();
    private String modeCurrent;
    private String timeCurrent;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseFirestore.setLoggingEnabled(true);


        bottomNavigationView = findViewById(R.id.bottom_navigation);
        actionBar = getActionBar();
        container = findViewById(R.id.containerFramelayout);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.containerFramelayout, ExpenseFragment.newInstance("asdfsa","asdfas")).commit();
        handleNavigation();

       calendarView = findViewById(R.id.calendar);

        starttime = Calendar.getInstance();
        starttime.add(Calendar.DATE,-10);


        endtime = Calendar.getInstance();
        endtime.add(Calendar.DATE,10);

        datesToBeColored = new ArrayList();
        datesToBeColored.add(Tools.getFormattedDateToday());

        calendarView.setUpCalendar("date",starttime.getTimeInMillis(),
                endtime.getTimeInMillis(),
                datesToBeColored,
                new HorizontalCalendarView.OnCalendarListener() {
                    @Override
                    public void onDateSelected(String date) {
                        //datesToBeColored.add(date);
                        Toast.makeText(MainActivity.this,date+" clicked!",Toast.LENGTH_SHORT).show();

                    }
                });



    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.optionmenu, menu);
        if(menu instanceof MenuBuilder){
            MenuBuilder m = (MenuBuilder) menu;
            m.setOptionalIconsVisible(true);
        }
        return true;
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        switch (item.getItemId()) {
            case R.id.optionmenu_bydate:
                starttime = Calendar.getInstance();
                starttime.add(Calendar.DATE,-10);
                endtime = Calendar.getInstance();
                endtime.add(Calendar.DATE,10);
                modeCurrent="date";
                calendarView.setUpCalendar("date",starttime.getTimeInMillis(),
                        endtime.getTimeInMillis(),
                        datesToBeColored,
                        new HorizontalCalendarView.OnCalendarListener() {
                            @Override
                            public void onDateSelected(String date) {
                                timeCurrent=date;
                                Toast.makeText(MainActivity.this,date+" clicked!",Toast.LENGTH_SHORT).show();
                                fragmentManager.beginTransaction().replace(R.id.containerFramelayout, ExpenseFragment.newInstance(modeCurrent,timeCurrent),"expenseFragment").commit();
                            }
                        });
                break;
            case R.id.optionmenu_byweek:
                starttime = Calendar.getInstance();
                starttime.add(Calendar.WEEK_OF_MONTH,-10);
                endtime = Calendar.getInstance();
                endtime.add(Calendar.WEEK_OF_MONTH,10);
                modeCurrent="week";
                calendarView.setUpCalendar("week",starttime.getTimeInMillis(),
                        endtime.getTimeInMillis(),
                        datesToBeColored,
                        new HorizontalCalendarView.OnCalendarListener() {
                            @Override
                            public void onDateSelected(String date) {
                                timeCurrent=date;
                                Toast.makeText(MainActivity.this,date+" clicked!",Toast.LENGTH_SHORT).show();

                            }
                        });
                break;
            case R.id.optionmenu_bymonth:
                starttime = Calendar.getInstance();
                starttime.add(Calendar.MONTH,-10);

                modeCurrent="month";
                endtime = Calendar.getInstance();
                endtime.add(Calendar.MONTH,10);
                calendarView.setUpCalendar("month",starttime.getTimeInMillis(),
                        endtime.getTimeInMillis(),
                        datesToBeColored,
                        new HorizontalCalendarView.OnCalendarListener() {
                            @Override
                            public void onDateSelected(String date) {
                                timeCurrent=date;
                                Toast.makeText(MainActivity.this,date+" clicked!",Toast.LENGTH_SHORT).show();
                            }
                        });
                break;
            case R.id.optionmenu_byyear:
                starttime = Calendar.getInstance();
                starttime.add(Calendar.YEAR,-5);
                endtime = Calendar.getInstance();
                endtime.add(Calendar.YEAR,5);
                modeCurrent="year";
                calendarView.setUpCalendar("year",starttime.getTimeInMillis(),
                        endtime.getTimeInMillis(),
                        datesToBeColored,
                        new HorizontalCalendarView.OnCalendarListener() {
                            @Override
                            public void onDateSelected(String date) {
                                timeCurrent=date;
                                Toast.makeText(MainActivity.this,date+" clicked!",Toast.LENGTH_SHORT).show();


                            }
                        });
                break;
        }
        return true;


    }

    void handleNavigation()
    {
        mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentManager fragmentManager = getSupportFragmentManager();

            switch (item.getItemId()) {
                case R.id.menu_expense:
                    fragmentManager.beginTransaction().replace(R.id.containerFramelayout, ExpenseFragment.newInstance(modeCurrent,timeCurrent),"expenseFragment").commit();
                    break;
                case R.id.menu_report:
                    fragmentManager.beginTransaction().replace(R.id.containerFramelayout, Report.newInstance(),"reportFragment").commit();
                    break;
                case R.id.menu_planning:
                    fragmentManager.beginTransaction().replace(R.id.containerFramelayout, Planning.newInstance(),"planningFragment").commit();
                    break;
                case R.id.menu_setting:
                    fragmentManager.beginTransaction().replace(R.id.containerFramelayout, Setting.newInstance(),"settingFragment").commit();
                    break;
            }
            return true;
        }
    };

        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }


    public void onButtonClick(Button button) {
        Toast.makeText(getApplicationContext(), "You have entered: " ,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }
}