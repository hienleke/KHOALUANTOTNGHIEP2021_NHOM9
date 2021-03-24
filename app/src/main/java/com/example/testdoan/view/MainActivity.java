package com.example.testdoan.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.fragment.app.FragmentManager;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;
import com.example.testdoan.R;
import com.example.testdoan.externalView.Tools;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.ArrayList;
import java.util.Calendar;
import  com.example.testdoan.externalView.HorizontalCalendarView;




public class MainActivity extends AppCompatActivity {

    private FrameLayout container;
    private ActionBar actionBar;
    private HorizontalCalendarView calendarView;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener;
    private BottomNavigationView bottomNavigationView;
    private ArrayList datesToBeColored;
    Calendar starttime,endtime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        actionBar = getActionBar();
        container = findViewById(R.id.containerFramelayout);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.containerFramelayout, Expense.newInstance("asdfsa","asdfas")).commit();
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


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.optionmenu_bydate:
                starttime = Calendar.getInstance();
                starttime.add(Calendar.DATE,-10);
                endtime = Calendar.getInstance();
                endtime.add(Calendar.DATE,10);
                calendarView.setUpCalendar("date",starttime.getTimeInMillis(),
                        endtime.getTimeInMillis(),
                        datesToBeColored,
                        new HorizontalCalendarView.OnCalendarListener() {
                            @Override
                            public void onDateSelected(String date) {
                                Toast.makeText(MainActivity.this,date+" clicked!",Toast.LENGTH_SHORT).show();
                            }
                        });
                break;
            case R.id.optionmenu_byweek:
                starttime = Calendar.getInstance();
                starttime.add(Calendar.WEEK_OF_MONTH,-10);
                endtime = Calendar.getInstance();
                endtime.add(Calendar.WEEK_OF_MONTH,10);
                calendarView.setUpCalendar("week",starttime.getTimeInMillis(),
                        endtime.getTimeInMillis(),
                        datesToBeColored,
                        new HorizontalCalendarView.OnCalendarListener() {
                            @Override
                            public void onDateSelected(String date) {
                                Toast.makeText(MainActivity.this,date+" clicked!",Toast.LENGTH_SHORT).show();

                            }
                        });
                break;
            case R.id.optionmenu_bymonth:
                starttime = Calendar.getInstance();
                starttime.add(Calendar.MONTH,-10);


                endtime = Calendar.getInstance();
                endtime.add(Calendar.MONTH,10);
                calendarView.setUpCalendar("month",starttime.getTimeInMillis(),
                        endtime.getTimeInMillis(),
                        datesToBeColored,
                        new HorizontalCalendarView.OnCalendarListener() {
                            @Override
                            public void onDateSelected(String date) {
                                Toast.makeText(MainActivity.this,date+" clicked!",Toast.LENGTH_SHORT).show();
                            }
                        });
                break;
            case R.id.optionmenu_byyear:
                starttime = Calendar.getInstance();
                starttime.add(Calendar.YEAR,-5);
                endtime = Calendar.getInstance();
                endtime.add(Calendar.YEAR,5);
                calendarView.setUpCalendar("year",starttime.getTimeInMillis(),
                        endtime.getTimeInMillis(),
                        datesToBeColored,
                        new HorizontalCalendarView.OnCalendarListener() {
                            @Override
                            public void onDateSelected(String date) {
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
                    fragmentManager.beginTransaction().replace(R.id.containerFramelayout,Expense.newInstance("asdf","asdfa")).commit();
                    break;
                case R.id.menu_report:
                    fragmentManager.beginTransaction().replace(R.id.containerFramelayout, Report.newInstance()).commit();
                    break;
                case R.id.menu_planning:
                    fragmentManager.beginTransaction().replace(R.id.containerFramelayout, Planning.newInstance()).commit();
                    break;
                case R.id.menu_setting:
                    fragmentManager.beginTransaction().replace(R.id.containerFramelayout, Setting.newInstance()).commit();
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

}