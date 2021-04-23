package com.example.testdoan.view;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.icu.text.DecimalFormat;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.FragmentManager;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.example.testdoan.R;
import com.example.testdoan.externalView.HorizontalCalendarView;
import com.example.testdoan.externalView.Tools;
import com.example.testdoan.model.User;
import com.example.testdoan.repository.Budgetmodify;
import com.example.testdoan.service.WorkForPeriodTask_daily_monthly;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


@RequiresApi(api = Build.VERSION_CODES.N)
public class MainActivity extends AppCompatActivity implements getdataFromFragment {

    private static final String CHANNEL_ID ="budgetnotice" ;
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
    public static double limit =0;
    public static boolean isNoticeenable;
   DecimalFormat decimalFormat = new DecimalFormat("#,###.00 ¤");


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



        Constraints constraints = new Constraints.Builder()
                // The Worker needs Network connectivity
                .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
                .setRequiresCharging(false)
                .setRequiresBatteryNotLow(false)
                .setRequiresStorageNotLow(false)
                .setRequiresDeviceIdle(false)
                .build();
        PeriodicWorkRequest Dailywork =
                new PeriodicWorkRequest.Builder(WorkForPeriodTask_daily_monthly.class, 1, TimeUnit.DAYS).setConstraints(constraints).build();

        WorkManager
                .getInstance(getApplicationContext())
                .enqueueUniquePeriodicWork("xxx", ExistingPeriodicWorkPolicy.KEEP,Dailywork);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            MainActivity.db
                    .collection("users")
                    .document(MainActivity.user.getId()).collection("profile").document("profile")
                    .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                            if (error != null) {
                                Log.w("xxx", "Listen failed.", error);
                                return;
                            }

                            if (value.getDouble("limit")!=null && value.getBoolean("notice") !=null) {
                                limit = value.getDouble("limit");
                                isNoticeenable=value.getBoolean("notice");
                            }
                            else
                            {
                                Map<String,Object> data = new HashMap<>();
                                data.put("budget",0.0);
                                data.put("notice",true);
                                data.put("limit",0.0);

                                MainActivity.db.collection("users").document( user.getId())
                                        .collection("profile").document("profile")
                                        .set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(getApplicationContext(), "succes", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }
                    });



        }


        db.collection("users").document(user.getId()).collection("profile").document("profile")
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
                            {

                                try {
                                    if(budget<limit && isNoticeenable)
                                    {
                                        createNotificationChannel();
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0,
                                                intent, PendingIntent.FLAG_ONE_SHOT);

                                        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                                                .setSmallIcon(android.R.drawable.stat_sys_warning)
                                                .setContentTitle("Manage Money Warning")
                                                .setContentText("Your budget too low")
                                                .setDefaults(NotificationCompat.DEFAULT_ALL)
                                                .setPriority(NotificationCompat.PRIORITY_MAX);

                                        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
                                        notificationManager.notify(2, builder.build());
                                    }

                                }
                                catch (Exception ex)
                                {
                                    ex.printStackTrace();
                                }

                            }
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
            case R.id.optionmenu_period:
              BottomSheetDialogFragment fg = form_period.newInstance(null);
                fg.show(getSupportFragmentManager(),"Xsdf");

                break;
        }
        return true;


    }
    @Override
    public void onlicknext(Date from, Date end) {
        modeCurrent="period";
        calendarView.setUpCalendar("period",from.getTime(),
                end.getTime(),
                datesToBeColored,
                new HorizontalCalendarView.OnCalendarListener() {
                    @Override
                    public void onDateSelected(String date) {
                        timeCurrent=date;
                        Toast.makeText(MainActivity.this,date+" clicked!",Toast.LENGTH_SHORT).show();
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        if(CurrentTabisExpense)
                            fragmentManager.beginTransaction().replace(R.id.containerFramelayout, ExpenseFragment.newInstance(modeCurrent,timeCurrent),"expenseFragment").commit();
                        if(CurrentTabisReport)
                            fragmentManager.beginTransaction().replace(R.id.containerFramelayout, ReportFragment.newInstance(modeCurrent,timeCurrent),"ReportFragment").commit();
                    }
                });
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
                    ViewGroup.LayoutParams params = calendarView.getRecyclerView().getLayoutParams();
                    params.height= ViewGroup.LayoutParams.WRAP_CONTENT;
                    calendarView.getRecyclerView().setLayoutParams(params);
                    calendarView.requestLayout();
                    fragmentManager.beginTransaction().replace(R.id.containerFramelayout, ExpenseFragment.newInstance(modeCurrent,timeCurrent),"expenseFragment").commit();
                    break;
                case R.id.menu_report:
                    CurrentTabisExpense=false;
                    CurrentTabisReport=true;
                  params = calendarView.getRecyclerView().getLayoutParams();
                  params.height= ViewGroup.LayoutParams.WRAP_CONTENT;
                  params.width= ViewGroup.LayoutParams.MATCH_PARENT;
                  calendarView.invalidate();
                    fragmentManager.beginTransaction().replace(R.id.containerFramelayout, ReportFragment.newInstance(modeCurrent,timeCurrent),"reportFragment").commit();

                    break;
                case R.id.menu_planning:
                    CurrentTabisExpense=false;
                    CurrentTabisReport=false;
                    params = calendarView.getRecyclerView().getLayoutParams();
                    params.height=0;
                    calendarView.getRecyclerView().setLayoutParams(params);
                    fragmentManager.beginTransaction().replace(R.id.containerFramelayout, Planning.newInstance(),"planningFragment").commit();
                    break;
                case R.id.menu_setting:
                    CurrentTabisExpense=false;
                    CurrentTabisReport=false;
                    params = calendarView.getRecyclerView().getLayoutParams();
                    params.height= 0;
                    calendarView.getRecyclerView().setLayoutParams(params);
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

    public FrameLayout getContainer() {
        return container;
    }

    public void setContainer(FrameLayout container) {
        this.container = container;
    }


    public void setActionBar(ActionBar actionBar) {
        this.actionBar = actionBar;
    }

    public HorizontalCalendarView getCalendarView() {
        return calendarView;
    }

    public void setCalendarView(HorizontalCalendarView calendarView) {
        this.calendarView = calendarView;
    }

    public BottomNavigationView.OnNavigationItemSelectedListener getmOnNavigationItemSelectedListener() {
        return mOnNavigationItemSelectedListener;
    }

    public void setmOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener) {
        this.mOnNavigationItemSelectedListener = mOnNavigationItemSelectedListener;
    }

    public BottomNavigationView getBottomNavigationView() {
        return bottomNavigationView;
    }

    public void setBottomNavigationView(BottomNavigationView bottomNavigationView) {
        this.bottomNavigationView = bottomNavigationView;
    }

    public ArrayList getDatesToBeColored() {
        return datesToBeColored;
    }

    public void setDatesToBeColored(ArrayList datesToBeColored) {
        this.datesToBeColored = datesToBeColored;
    }

    public Calendar getStarttime() {
        return starttime;
    }

    public void setStarttime(Calendar starttime) {
        this.starttime = starttime;
    }

    public Calendar getEndtime() {
        return endtime;
    }

    public void setEndtime(Calendar endtime) {
        this.endtime = endtime;
    }

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        MainActivity.user = user;
    }

    public static FirebaseFirestore getDb() {
        return db;
    }

    public static void setDb(FirebaseFirestore db) {
        MainActivity.db = db;
    }

    public String getModeCurrent() {
        return modeCurrent;
    }

    public void setModeCurrent(String modeCurrent) {
        this.modeCurrent = modeCurrent;
    }

    public String getTimeCurrent() {
        return timeCurrent;
    }

    public void setTimeCurrent(String timeCurrent) {
        this.timeCurrent = timeCurrent;
    }

    public boolean isCurrentTabisExpense() {
        return CurrentTabisExpense;
    }

    public void setCurrentTabisExpense(boolean currentTabisExpense) {
        CurrentTabisExpense = currentTabisExpense;
    }

    public boolean isCurrentTabisReport() {
        return CurrentTabisReport;
    }

    public void setCurrentTabisReport(boolean currentTabisReport) {
        CurrentTabisReport = currentTabisReport;
    }

    public static Double getBudget() {
        return budget;
    }

    public static void setBudget(Double budget) {
        MainActivity.budget = budget;
    }

    public DecimalFormat getDecimalFormat() {
        return decimalFormat;
    }

    public void setDecimalFormat(DecimalFormat decimalFormat) {
        this.decimalFormat = decimalFormat;
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Notice";
            String description = "Your budget too low";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.enableVibration(true);
            channel.setDescription(description);
            channel.setShowBadge(true);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}