package com.example.testdoan.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.icu.text.DecimalFormat;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.fragment.app.FragmentManager;

import com.example.testdoan.R;
import com.example.testdoan.externalView.HorizontalCalendarView;
import com.example.testdoan.externalView.Tools;
import com.example.testdoan.model.User;
import com.example.testdoan.repository.Budgetmodify;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


@RequiresApi(api = Build.VERSION_CODES.N)
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
    private String modeCurrent = "date";
    private String timeCurrent;
    private boolean CurrentTabisExpense =true;
    private boolean CurrentTabisReport =false;
    public static Double budget= 0.0;
    DecimalFormat decimalFormat = new DecimalFormat("0.0");





    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseFirestore.setLoggingEnabled(true);
        Date date1 = new Date();



        Intent userdata = getIntent();
        user = new User();
        user.setId(userdata.getStringExtra("userId"));
        user.setEmail(userdata.getStringExtra("userEmail"));
        user.setName(userdata.getStringExtra("userName"));
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        String strDate = formatter.format(date1);
        timeCurrent=strDate;


        db.collection("users").document(user.getId())
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w("xxx", "Listen failed.", e);
                            return;
                        }

                        if (snapshot != null ) {
                            Log.d("xxxbudget", "Current data: " + snapshot.getData());
                            budget=snapshot.getDouble("budget");
                            actionBar = getSupportActionBar();
                            actionBar.setTitle("Budget: "+ (budget==null ? "0" : decimalFormat.format(budget)));

                            if(budget==null)
                            {
                                Budgetmodify.init(0);

                            }

                        } else {
                            Log.d("xxxbudet", "Current data: null");

                        }
                    }
                });


        bottomNavigationView = findViewById(R.id.bottom_navigation);
        container = findViewById(R.id.containerFramelayout);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.containerFramelayout, ExpenseFragment.newInstance(modeCurrent,timeCurrent)).commit();
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
                        modeCurrent="date";
                        timeCurrent=date;
                        Toast.makeText(MainActivity.this,date+" clicked!",Toast.LENGTH_SHORT).show();
                       if(CurrentTabisExpense)
                        fragmentManager.beginTransaction().replace(R.id.containerFramelayout, ExpenseFragment.newInstance(modeCurrent,timeCurrent),"expenseFragment").commit();
                       if(CurrentTabisReport)
                           fragmentManager.beginTransaction().replace(R.id.containerFramelayout, ReportFragment.newInstance(modeCurrent,timeCurrent),"expenseFragment").commit();
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
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                String strDate = formatter.format(new Date());
                timeCurrent=strDate;
                if(CurrentTabisExpense)
                fragmentManager.beginTransaction().replace(R.id.containerFramelayout, ExpenseFragment.newInstance(modeCurrent,timeCurrent),"expenseFragment").commit();
                if(CurrentTabisReport)
                    fragmentManager.beginTransaction().replace(R.id.containerFramelayout, ReportFragment.newInstance(modeCurrent,timeCurrent),"ReportFragment").commit();
                calendarView.setUpCalendar("date",starttime.getTimeInMillis(),
                        endtime.getTimeInMillis(),
                        datesToBeColored,
                        new HorizontalCalendarView.OnCalendarListener() {
                            @Override
                            public void onDateSelected(String date) {
                                timeCurrent=date;
                                Toast.makeText(MainActivity.this,date+" clicked!",Toast.LENGTH_SHORT).show();
                                if(CurrentTabisExpense)
                                    fragmentManager.beginTransaction().replace(R.id.containerFramelayout, ExpenseFragment.newInstance(modeCurrent,timeCurrent),"expenseFragment").commit();
                                if(CurrentTabisReport)
                                    fragmentManager.beginTransaction().replace(R.id.containerFramelayout, ReportFragment.newInstance(modeCurrent,timeCurrent),"ReportFragment").commit();
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
                                if(CurrentTabisExpense)
                                    fragmentManager.beginTransaction().replace(R.id.containerFramelayout, ExpenseFragment.newInstance(modeCurrent,timeCurrent),"expenseFragment").commit();
                                if(CurrentTabisReport)
                                    fragmentManager.beginTransaction().replace(R.id.containerFramelayout, ReportFragment.newInstance(modeCurrent,timeCurrent),"ReportFragment").commit();
                            }
                        });
                break;
            case R.id.optionmenu_bymonth:
                starttime = Calendar.getInstance();
                starttime.add(Calendar.MONTH,-10);

                modeCurrent="month";
                endtime = Calendar.getInstance();
                endtime.add(Calendar.MONTH,10);
               // fragmentManager.beginTransaction().replace(R.id.containerFramelayout, ExpenseFragment.newInstance(modeCurrent,timeCurrent),"expenseFragment").commit();
                calendarView.setUpCalendar("month",starttime.getTimeInMillis(),
                        endtime.getTimeInMillis(),
                        datesToBeColored,
                        new HorizontalCalendarView.OnCalendarListener() {
                            @Override
                            public void onDateSelected(String date) {
                                timeCurrent=date;
                                Toast.makeText(MainActivity.this,date+" clicked!",Toast.LENGTH_SHORT).show();
                                if(CurrentTabisExpense)
                                    fragmentManager.beginTransaction().replace(R.id.containerFramelayout, ExpenseFragment.newInstance(modeCurrent,timeCurrent),"expenseFragment").commit();
                                if(CurrentTabisReport)
                                    fragmentManager.beginTransaction().replace(R.id.containerFramelayout, ReportFragment.newInstance(modeCurrent,timeCurrent),"ReportFragment").commit();
                            }
                        });
                break;
            case R.id.optionmenu_byyear:
                starttime = Calendar.getInstance();
                starttime.add(Calendar.YEAR,-5);
                endtime = Calendar.getInstance();
                endtime.add(Calendar.YEAR,5);
                modeCurrent="year";
              //  fragmentManager.beginTransaction().replace(R.id.containerFramelayout, ExpenseFragment.newInstance(modeCurrent,timeCurrent),"expenseFragment").commit();

                calendarView.setUpCalendar("year",starttime.getTimeInMillis(),
                        endtime.getTimeInMillis(),
                        datesToBeColored,
                        new HorizontalCalendarView.OnCalendarListener() {
                            @Override
                            public void onDateSelected(String date) {
                                timeCurrent=date;
                                Toast.makeText(MainActivity.this,date+" clicked!",Toast.LENGTH_SHORT).show();
                                if(CurrentTabisExpense)
                                    fragmentManager.beginTransaction().replace(R.id.containerFramelayout, ExpenseFragment.newInstance(modeCurrent,timeCurrent),"expenseFragment").commit();
                                if(CurrentTabisReport)
                                    fragmentManager.beginTransaction().replace(R.id.containerFramelayout, ReportFragment.newInstance(modeCurrent,timeCurrent),"ReportFragment").commit();

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
                    CurrentTabisExpense=true;
                    CurrentTabisReport=false;
                    fragmentManager.beginTransaction().replace(R.id.containerFramelayout, ExpenseFragment.newInstance(modeCurrent,timeCurrent),"expenseFragment").commit();
                    break;
                case R.id.menu_report:
                    CurrentTabisExpense=false;
                    CurrentTabisReport=true;
                    fragmentManager.beginTransaction().replace(R.id.containerFramelayout, ReportFragment.newInstance(modeCurrent,timeCurrent),"reportFragment").commit();
                    break;
                case R.id.menu_planning:
                    CurrentTabisExpense=false;
                    CurrentTabisReport=false;
                    fragmentManager.beginTransaction().replace(R.id.containerFramelayout, Planning.newInstance(),"planningFragment").commit();
                    break;
                case R.id.menu_setting:
                    CurrentTabisExpense=false;
                    CurrentTabisReport=false;
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}