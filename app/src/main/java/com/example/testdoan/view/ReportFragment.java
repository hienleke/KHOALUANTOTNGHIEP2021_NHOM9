package com.example.testdoan.view;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModelProvider;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.LocaleList;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.testdoan.R;
import com.example.testdoan.model.Expense;
import com.example.testdoan.viewmodel.ReportViewModel;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class ReportFragment extends Fragment {

    private ReportViewModel mViewModel;
    private PieChart chart, expenseChart;
    private BarChart barChart;
    List<Expense> expenses = new ArrayList<>();
    List<Expense> incomes = new ArrayList<>();
    private static final String ARG_mode = "param1";
    private static final String ARG_time = "param2";
    private String mode;
    private String time;
    private String USERID;

    public static ReportFragment newInstance(String mode, String time) {
        ReportFragment fragment = new ReportFragment();
        Bundle args = new Bundle();

        args.putString(ARG_mode, mode);
        args.putString(ARG_time, time);

        fragment.setArguments(args);
        return fragment;
    }

    public ReportFragment() {

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mode = getArguments().getString(ARG_mode);
            time = getArguments().getString(ARG_time);
        }
        USERID=MainActivity.user.getId();
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.report_fragment, container, false);
        chart = v.findViewById(R.id.chartIncome);
        chart.setTouchEnabled(true);
        barChart = v.findViewById(R.id.chartBar);
        expenseChart = v.findViewById(R.id.chartExpense);
        expenseChart.setTouchEnabled(true);

        Query query = MainActivity.db
                .collection("users")
                .document(USERID)
                .collection("expense");

        switch (mode) {
            case "date":
                int day = Integer.valueOf(time.split("-")[0]);
                int month = Integer.valueOf(time.split("-")[1]);
                int year = Integer.valueOf(time.split("-")[2]);
                LocalDate localDate1 = LocalDate.of(year, month, day);
                ZoneId zoneid = ZoneId.systemDefault();
                Instant instant = Instant.now();
                ZoneOffset currentOffsetForMyZone = zoneid.getRules().getOffset(instant);
                Date begin = Date.from(localDate1.atStartOfDay(zoneid).toInstant());
                Date end = Date.from(localDate1.atTime(23, 59, 59).toInstant(currentOffsetForMyZone));
                query = query.whereGreaterThanOrEqualTo("timeCreated", begin).whereLessThanOrEqualTo("timeCreated", end);
                getdataforChart(query);
                createLinechart("date", begin, year, month, day);
                break;
            case "week":
                String time2begin = time.split("-")[0];
                String time2end = time.split("-")[1];
                int day2begin = Integer.valueOf(time2begin.split("/")[0]);
                int month2begin = Integer.valueOf(time2begin.split("/")[1]);
                int year2begin = Integer.valueOf(time2begin.split("/")[2]);
                int day2end = Integer.valueOf(time2end.split("/")[0]);
                int month2end = Integer.valueOf(time2end.split("/")[1]);
                int year2end = Integer.valueOf(time2end.split("/")[2]);
                LocalDate localDate2begin = LocalDate.of(year2begin, month2begin, day2begin);
                LocalDate localDate2end = LocalDate.of(year2end, month2end, day2end);
                ZoneId zoneid2 = ZoneId.systemDefault();
                Instant instant2 = Instant.now();
                ZoneOffset currentOffsetForMyZone2 = zoneid2.getRules().getOffset(instant2);
                Date begin2 = Date.from(localDate2begin.atStartOfDay(zoneid2).toInstant());
                Date end2 = Date.from(localDate2end.atTime(23, 59, 59).toInstant(currentOffsetForMyZone2));
                query = query.whereGreaterThanOrEqualTo("timeCreated", begin2).whereLessThanOrEqualTo("timeCreated", end2);
                getdataforChart(query);

                break;
            case "month":
                month2begin = Integer.valueOf(time.split("/")[0]);
                year2begin = Integer.valueOf(time.split("/")[1]);
                localDate2begin = LocalDate.of(year2begin, month2begin, 1);
                localDate2end = LocalDate.of(year2begin, month2begin, 1).with(TemporalAdjusters.lastDayOfMonth());
                zoneid2 = ZoneId.systemDefault();
                instant2 = Instant.now();
                currentOffsetForMyZone2 = zoneid2.getRules().getOffset(instant2);
                begin2 = Date.from(localDate2begin.atStartOfDay(zoneid2).toInstant());
                end2 = Date.from(localDate2end.atTime(23, 59, 59).toInstant(currentOffsetForMyZone2));
                query = query.whereGreaterThanOrEqualTo("timeCreated", begin2).whereLessThanOrEqualTo("timeCreated", end2);
                getdataforChart(query);
                createLinechart("month",end2,year2begin,month2begin,1);
                break;
            case "year":
                year2begin = Integer.valueOf(time);
                localDate2begin = LocalDate.of(year2begin, 1, 1);
                localDate2end = LocalDate.of(year2begin, 12, 31);
                zoneid2 = ZoneId.systemDefault();
                instant2 = Instant.now();
                currentOffsetForMyZone2 = zoneid2.getRules().getOffset(instant2);
                begin2 = Date.from(localDate2begin.atStartOfDay(zoneid2).toInstant());
                end2 = Date.from(localDate2end.atTime(23, 59, 59).toInstant(currentOffsetForMyZone2));
                query = query.whereGreaterThanOrEqualTo("timeCreated", begin2).whereLessThanOrEqualTo("timeCreated", end2);
                getdataforChart(query);
                createLinechart("year", begin2, year2begin, 1, 1);
                break;
        }
        return v;
    }

    void handlerIncomeChart(List<Expense> data) {
        if (data.size() == 0)
            return;
        Map<String, Double> typeAmountMap = new HashMap<>();
        for (Expense e : data) {
            if (typeAmountMap.containsKey(e.getCategory()))
                typeAmountMap.put(e.getCategory(), typeAmountMap.get(e.getCategory()) + e.getAmount());
            else
                typeAmountMap.put(e.getCategory(), e.getAmount());
        }
        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        String label = "Category";

        int[] colors;
        colors = getContext().getResources().getIntArray(R.array.mdcolor_random);

        //input data and fit data into pie chart entry
        Set<String> local = typeAmountMap.keySet();
        for (String type : local) {
            pieEntries.add(new PieEntry(typeAmountMap.get(type).floatValue(), type));
        }

        //collecting the entries with label name
        PieDataSet pieDataSet = new PieDataSet(pieEntries, label);
        //setting text size of the value
        pieDataSet.setValueTextSize(12f);
        pieDataSet.setValueTextColor(Color.BLACK);
        //providing color list for coloring different entries
        pieDataSet.setColors(colors);
        //grouping the data set from entry to chart
        PieData pieData = new PieData(pieDataSet);
        //showing the value of the entries, default true if not set
        pieData.setValueFormatter(new PercentFormatter());
        pieDataSet.setDrawValues(true);

        chart.setData(pieData);
        chart.animateY(600);
        chart.invalidate();
        chart.setUsePercentValues(true);
        chart.setHighlightPerTapEnabled(true);
        chart.getDescription().setText("");


        Legend l = chart.getLegend(); // get legend of pie
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP); // set vertical alignment for legend
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT); // set horizontal alignment for legend
        l.setOrientation(Legend.LegendOrientation.VERTICAL); // set orientation for legend
        //   l.setDrawInside(false); // set if legend should be drawn inside or not
        chart.setCenterText("Income");
        chart.setCenterTextSize(23f);
        chart.setCenterTextColor(Color.parseColor("#1F8B24"));
        chart.invalidate();

    }

    void handlerExpenseChart(List<Expense> data) {
        if (data.size() == 0)
            return;
        Map<String, Double> typeAmountMap = new HashMap<>();
        for (Expense e : data) {
            if (typeAmountMap.containsKey(e.getCategory()))
                typeAmountMap.put(e.getCategory(), typeAmountMap.get(e.getCategory()) + e.getAmount());
            else
                typeAmountMap.put(e.getCategory(), e.getAmount());
        }
        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        String label = "Category";

        int[] colors;
        colors = getContext().getResources().getIntArray(R.array.mdcolor_random);

        //input data and fit data into pie chart entry
        for (String type : typeAmountMap.keySet()) {
            pieEntries.add(new PieEntry(typeAmountMap.get(type).floatValue(), type));
        }

        PieDataSet pieDataSet = new PieDataSet(pieEntries, label);
        pieDataSet.setValueTextSize(12f);
        pieDataSet.setColors(colors);
        PieData pieData = new PieData(pieDataSet);
        pieData.setValueFormatter(new PercentFormatter());
        pieDataSet.setDrawValues(true);

        expenseChart.setData(pieData);
        expenseChart.animateY(600);
        expenseChart.setUsePercentValues(true);
        expenseChart.setHighlightPerTapEnabled(true);
        expenseChart.getDescription().setText("");
        expenseChart.setCenterTextSize(23f);

        Legend l = expenseChart.getLegend(); // get legend of pie
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP); // set vertical alignment for legend
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT); // set horizontal alignment for legend
        l.setOrientation(Legend.LegendOrientation.VERTICAL); // set orientation for legend
        expenseChart.setCenterText("Expense");
        expenseChart.setCenterTextColor(Color.RED);
        expenseChart.invalidate();

    }

    void getdataforChart(Query query) {
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        Expense iteam = new Expense(doc.getId(), doc.getString("category"), doc.getTimestamp("timeCreated"), doc.getString("note"), doc.getDouble("amount"), doc.getBoolean("expen"));
                        if (iteam.isExpen()) {
                            expenses.add(iteam);
                        } else {
                            incomes.add(iteam);
                        }
                    }
                } else {
                    Log.d("sai r", "Error getting documents: ", task.getException());
                }
                handlerIncomeChart(incomes);
                handlerExpenseChart(expenses);
            }
        });
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ReportViewModel.class);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createLinechart(String mode, Date time, int year, int month, int day) {
        Query query = MainActivity.db
                .collection("users")
                .document(USERID)
                .collection("expense");

        ArrayList<BarEntry> barEntriesIncome = new ArrayList<>();
        ArrayList<BarEntry> barEntriesExpense = new ArrayList<>();
        switch (mode) {
            case "date":
                barChart.getDescription().setText("Day in week");
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(time);
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                calendar.setFirstDayOfWeek(Calendar.MONDAY);
                ZoneId zoneid = ZoneId.systemDefault();
                Instant instant = Instant.now();
                ZoneOffset currentOffsetForMyZone = zoneid.getRules().getOffset(instant);
                LocalDate localDate1 = LocalDate.of(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DATE));
                Date begin = Date.from(localDate1.atStartOfDay(zoneid).toInstant());
                calendar.add(Calendar.DAY_OF_MONTH, 6);
                localDate1 = LocalDate.of(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DATE));
                Date end = Date.from(localDate1.atTime(23, 59, 59).toInstant(currentOffsetForMyZone));
                query = query.whereGreaterThanOrEqualTo("timeCreated", begin).whereLessThanOrEqualTo("timeCreated", end);

                List<Expense> expensesTemp = new ArrayList<>();
                List<Expense> incomesTemp = new ArrayList<>();
                query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                Expense iteam = new Expense(doc.getId(), doc.getString("category"), doc.getTimestamp("timeCreated"), doc.getString("note"), doc.getDouble("amount"), doc.getBoolean("expen"));
                                if (iteam.isExpen()) {
                                    expensesTemp.add(iteam);
                                } else {
                                    incomesTemp.add(iteam);
                                    Log.d("xxxx", iteam.getTimeCreated() + "//" + iteam.getTimeCreated().toDate() + "//");
                                }
                            }
                        } else {
                            Log.d("sai r", "Error getting documents: ", task.getException());
                        }

                        BarEntry monday = new BarEntry(1, 0);
                        BarEntry tueday = new BarEntry(2, 0);
                        BarEntry wed = new BarEntry(3, 0);
                        BarEntry thu = new BarEntry(4, 0);
                        BarEntry fri = new BarEntry(5, 0);
                        BarEntry sat = new BarEntry(6, 0);
                        BarEntry sun = new BarEntry(7, 0);
                        for (Expense e : expensesTemp) {
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(e.getTimeCreated().toDate());

                            switch (cal.get(Calendar.DAY_OF_WEEK)) {
                                case Calendar.MONDAY:
                                    float tamp = monday.getY();
                                    monday.setY(tamp += e.getAmount());
                                    break;
                                case Calendar.TUESDAY:
                                    tamp = tueday.getY();
                                    tueday.setY(tamp += e.getAmount());
                                    break;
                                case Calendar.WEDNESDAY:
                                    tamp = wed.getY();
                                    wed.setY(tamp += e.getAmount());
                                    break;
                                case Calendar.THURSDAY:
                                    tamp = thu.getY();
                                    thu.setY(tamp += e.getAmount());
                                    break;
                                case Calendar.FRIDAY:
                                    tamp = fri.getY();
                                    fri.setY(tamp += e.getAmount());
                                    break;
                                case Calendar.SATURDAY:
                                    tamp = sat.getY();
                                    sat.setY(tamp += e.getAmount());
                                    break;
                                case Calendar.SUNDAY:
                                    tamp = sun.getY();
                                    sun.setY(tamp += e.getAmount());
                                    break;
                            }
                        }

                        BarEntry mondayi = new BarEntry(1, 0);
                        BarEntry tuedayi = new BarEntry(2, 0);
                        BarEntry wedi = new BarEntry(3, 0);
                        BarEntry thui = new BarEntry(4, 0);
                        BarEntry frii = new BarEntry(5, 0);
                        BarEntry sati = new BarEntry(6, 0);
                        BarEntry suni = new BarEntry(7, 0);
                        for (Expense e : incomesTemp) {
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(e.getTimeCreated().toDate());

                            switch (cal.get(Calendar.DAY_OF_WEEK)) {
                                case Calendar.MONDAY:
                                    float tamp = mondayi.getY();
                                    mondayi.setY(tamp += e.getAmount());
                                    break;
                                case Calendar.TUESDAY:
                                    tamp = tuedayi.getY();
                                    tuedayi.setY(tamp += e.getAmount());
                                    break;
                                case Calendar.WEDNESDAY:
                                    tamp = wedi.getY();
                                    wedi.setY(tamp += e.getAmount());
                                    break;
                                case Calendar.THURSDAY:
                                    tamp = thui.getY();
                                    thui.setY(tamp += e.getAmount());
                                    break;
                                case Calendar.FRIDAY:
                                    tamp = frii.getY();
                                    frii.setY(tamp += e.getAmount());
                                    break;
                                case Calendar.SATURDAY:
                                    tamp = sati.getY();
                                    sati.setY(tamp += e.getAmount());
                                    break;
                                case Calendar.SUNDAY:
                                    tamp = suni.getY();
                                    suni.setY(tamp += e.getAmount());
                                    break;
                            }
                        }

                        String[] xValues = new String[]{"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
                        barEntriesIncome.add(mondayi);
                        barEntriesIncome.add(tuedayi);
                        barEntriesIncome.add(wedi);
                        barEntriesIncome.add(thui);
                        barEntriesIncome.add(frii);
                        barEntriesIncome.add(sati);
                        barEntriesIncome.add(suni);

                        barEntriesExpense.add(monday);
                        barEntriesExpense.add(tueday);
                        barEntriesExpense.add(wed);
                        barEntriesExpense.add(thu);
                        barEntriesExpense.add(fri);
                        barEntriesExpense.add(sat);
                        barEntriesExpense.add(sun);
                        createBarchart(xValues, barEntriesIncome, barEntriesExpense,7);

                    }
                });


                break;
            case "week":

                return;

            case "month":
                barChart.getDescription().setText("Day in month");
                calendar = Calendar.getInstance();
                calendar.setTime(time);
                instant = Instant.now();
                zoneid = ZoneId.systemDefault();
                currentOffsetForMyZone = zoneid.getRules().getOffset(instant);
                localDate1 = LocalDate.of(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, 1);
                 begin = Date.from(localDate1.atStartOfDay(zoneid).toInstant());
                localDate1 = LocalDate.of(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH)+1 , calendar.get(Calendar.DATE)).with(TemporalAdjusters.lastDayOfMonth());
                end = Date.from(localDate1.atTime(23, 59, 59).toInstant(currentOffsetForMyZone));
                query = query.whereGreaterThanOrEqualTo("timeCreated", begin).whereLessThanOrEqualTo("timeCreated", end);
                 expensesTemp = new ArrayList<>();
                incomesTemp = new ArrayList<>();
                query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                Expense iteam = new Expense(doc.getId(), doc.getString("category"), doc.getTimestamp("timeCreated"), doc.getString("note"), doc.getDouble("amount"), doc.getBoolean("expen"));
                                if (iteam.isExpen()) {
                                    expensesTemp.add(iteam);
                                } else {
                                    incomesTemp.add(iteam);
                                    Log.d("xxxx", iteam.getTimeCreated() + "//" + iteam.getTimeCreated().toDate() + "//");
                                }
                            }
                        } else {
                            Log.d("sai r", "Error getting documents: ", task.getException());
                        }

                        HashMap<Integer,Double> mapExpense = new HashMap<>();
                        HashMap<Integer,Double> mapIncome = new HashMap<>();
                        for (Expense e : expensesTemp) {
                            int day = e.getTimeCreated().toDate().getDate();
                            if (mapExpense.containsKey(day)) {
                                mapExpense.put(day, (mapExpense.get(day) + e.getAmount()));
                            } else
                                mapExpense.put(day, e.getAmount());
                        }

                        for (Expense e : incomesTemp) {
                            int day = e.getTimeCreated().toDate().getDate();
                            if (mapIncome.containsKey(day)) {
                                mapIncome.put(day, (mapIncome.get(day) + e.getAmount()));
                            } else
                                mapIncome.put(day, e.getAmount());
                        }

                        Iterator it = mapIncome.entrySet().iterator();
                        while (it.hasNext()) {
                            Map.Entry pair = (Map.Entry)it.next();
                            Double d = (Double) pair.getValue();
                            barEntriesIncome.add(new BarEntry((Integer)pair.getKey(),d.floatValue()));

                            it.remove();
                        }

                        Iterator it1 = mapExpense.entrySet().iterator();
                        while (it1.hasNext()) {
                            Map.Entry pair = (Map.Entry)it1.next();
                            Double d = (Double) pair.getValue();
                            barEntriesExpense.add(new BarEntry((Integer)pair.getKey(),d.floatValue()));
                            it1.remove();
                        }

                        List<String> temptemp = new ArrayList<String>();
                       int limit = end.getDate();
                        for(int i=1;i<=limit;i++)
                        {
                            temptemp.add(String.valueOf(i));
                        }

                        String[] xValues = new String[temptemp.size()];
                        xValues = temptemp.toArray(xValues);


                        createBarchart(xValues,   chuanhoa(barEntriesIncome,limit),   chuanhoa(barEntriesExpense,limit), limit);

                    }
                });



                break;
            case "year":
                barChart.getDescription().setText("Month in year");
                int year2begin = Integer.valueOf(year);
                LocalDate localDate2begin = LocalDate.of(year2begin, 1, 1);
                LocalDate localDate2end = LocalDate.of(year2begin, 12, 31);
                ZoneId zoneid2 = ZoneId.systemDefault();
                Instant instant2 = Instant.now();
                ZoneOffset currentOffsetForMyZone2 = zoneid2.getRules().getOffset(instant2);
                Date begin2 = Date.from(localDate2begin.atStartOfDay(zoneid2).toInstant());
                Date end2 = Date.from(localDate2end.atTime(23, 59, 59).toInstant(currentOffsetForMyZone2));
                query = query.whereGreaterThanOrEqualTo("timeCreated", begin2).whereLessThanOrEqualTo("timeCreated", end2);
        
             
                expensesTemp = new ArrayList<>();
                incomesTemp = new ArrayList<>();
                query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                Expense iteam = new Expense(doc.getId(), doc.getString("category"), doc.getTimestamp("timeCreated"), doc.getString("note"), doc.getDouble("amount"), doc.getBoolean("expen"));
                                if (iteam.isExpen()) {
                                    expensesTemp.add(iteam);
                                } else {
                                    incomesTemp.add(iteam);
                                    Log.d("xxxx", iteam.getTimeCreated() + "//" + iteam.getTimeCreated().toDate() + "//");
                                }
                            }
                        } else {
                            Log.d("sai r", "Error getting documents: ", task.getException());
                        }

                        HashMap<Integer,Double> mapExpense = new HashMap<>();
                        HashMap<Integer,Double> mapIncome = new HashMap<>();
                        for (Expense e : expensesTemp) {
                            int day = e.getTimeCreated().toDate().getMonth()+1;
                            if (mapExpense.containsKey(day)) {
                                mapExpense.put(day, (mapExpense.get(day) + e.getAmount()));
                            } else
                                mapExpense.put(day, e.getAmount());
                        }

                        for (Expense e : incomesTemp) {
                            int day = e.getTimeCreated().toDate().getMonth()+1;
                            if (mapIncome.containsKey(day)) {
                                mapIncome.put(day, (mapIncome.get(day) + e.getAmount()));
                            } else
                                mapIncome.put(day, e.getAmount());
                        }

                        Iterator it = mapIncome.entrySet().iterator();
                        while (it.hasNext()) {
                            Map.Entry pair = (Map.Entry)it.next();
                            Double d = (Double) pair.getValue();
                            barEntriesIncome.add(new BarEntry((Integer)pair.getKey(),d.floatValue()));

                            it.remove();
                        }

                        Iterator it1 = mapExpense.entrySet().iterator();
                        while (it1.hasNext()) {
                            Map.Entry pair = (Map.Entry)it1.next();
                            Double d = (Double) pair.getValue();
                            barEntriesExpense.add(new BarEntry((Integer)pair.getKey(),d.floatValue()));
                            it1.remove();
                        }

                        List<String> temptemp = new ArrayList<String>();
                        int limit = 12;
                        for(int i=1;i<=limit;i++)
                        {
                            temptemp.add(String.valueOf(i));
                        }
                        String[] xValues = new String[temptemp.size()];
                        xValues = temptemp.toArray(xValues);
                        createBarchart(xValues,   chuanhoa(barEntriesIncome,limit),   chuanhoa(barEntriesExpense,limit), limit);

                    }
                });
                break;
            default:
                return;

        }
    }

    private void createBarchart(String[] xValues, List<BarEntry> barEntriesIncome, List<BarEntry> barEntriesExpense, int limit) {
        BarDataSet barDataSet = new BarDataSet(barEntriesIncome, "Income");
        barDataSet.setColor(Color.GREEN);
        BarDataSet barDataSet1 = new BarDataSet(barEntriesExpense, "Expense");
        barDataSet1.setColors(Color.RED);
        BarData data = new BarData(barDataSet, barDataSet1);
        barChart.setData(data);
        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xValues));
        barChart.animateY(500);
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.getXAxis().setDrawGridLines(false);
        XAxis xAxis = barChart.getXAxis();
        xAxis.setCenterAxisLabels(true);
        xAxis.setGranularityEnabled(true);

        float barSpace = 0.04f;
        float groupSpace = 0.32f;
        int groupCount = limit;
        data.setBarWidth(0.3f);

        barChart.getXAxis().setAxisMinimum(0);
        barChart.getXAxis().setAxisMaximum(0 + barChart.getBarData().getGroupWidth(groupSpace, barSpace) * groupCount);
        barChart.groupBars(0, groupSpace, barSpace);
        barChart.invalidate();
    }

    private ArrayList<BarEntry> chuanhoa(ArrayList<BarEntry> data, int limit)
    {
        ArrayList<BarEntry> afterChuanhoa = new ArrayList<>();
        for(int i=0;i<limit+1;i++)
        {
            afterChuanhoa.add(new BarEntry(0,0));
        }
        for (BarEntry b: data) {
            afterChuanhoa.set((int) b.getX(), new BarEntry(b.getX(),b.getY()));
        }
        afterChuanhoa.remove(0);
        return afterChuanhoa;
    }

}