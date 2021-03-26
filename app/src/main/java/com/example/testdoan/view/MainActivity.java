package com.example.testdoan.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.fragment.app.FragmentManager;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.testdoan.R;
import com.example.testdoan.externalView.Tools;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

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
    public static FirebaseFirestore db ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseFirestore.setLoggingEnabled(true);
        db = FirebaseFirestore.getInstance();

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


        Map<String, Object> user = new HashMap<>();
        user.put("first", "Ada");
        user.put("last", "Lovelace");
        user.put("born", 1815);

// Add a new document with a generated ID
        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("asdfasdf", "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("asdfas", "Error adding document", e);
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
                    fragmentManager.beginTransaction().replace(R.id.containerFramelayout, ExpenseFragment.newInstance("asdf","asdfa")).commit();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }
}