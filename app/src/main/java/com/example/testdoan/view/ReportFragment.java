package com.example.testdoan.view;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModelProvider;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.testdoan.R;
import com.example.testdoan.model.Expense;
import com.example.testdoan.viewmodel.ReportViewModel;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class ReportFragment extends Fragment {

    private ReportViewModel mViewModel;
    private PieChart chart,expenseChart;
    private LineChart lineChart;


    private static final String ARG_mode = "param1";
    private static final String ARG_time = "param2";
    private String mode;
    private String time;

    public static ReportFragment newInstance(String mode, String time) {
        ReportFragment fragment = new ReportFragment();
        Bundle args = new Bundle();

        args.putString(ARG_mode, mode);
        args.putString(ARG_time, time);

        fragment.setArguments(args);
        return fragment;
    }

    public  ReportFragment()
    {

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mode = getArguments().getString(ARG_mode);
            time = getArguments().getString(ARG_time);
        }

        List<Expense> expenses = new ArrayList<>();
        List<Expense> incomes = new ArrayList<>();
        Query query = MainActivity.db
                .collection("users")
                .document("YanMbTpDzBW2VKVBwDoC")
                .collection("expense");
        switch (mode) {
            case "date":
                int day = Integer.valueOf(time.split("-")[0]);
                int month = Integer.valueOf(time.split("-")[1]);
                int year = Integer.valueOf(time.split("-")[2]);

                LocalDate localDate1 = LocalDate.of(year, month,day);
                ZoneId zoneid = ZoneId.systemDefault();
                Instant instant = Instant.now();
                ZoneOffset currentOffsetForMyZone = zoneid.getRules().getOffset(instant);
                Date begin =Date.from(localDate1.atStartOfDay(zoneid).toInstant());
                Date end =Date.from(localDate1.atTime(23,59,59).toInstant(currentOffsetForMyZone));
                query = query.whereGreaterThanOrEqualTo("timeCreated", begin).whereLessThanOrEqualTo("timeCreated",end);
                query.addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w("error listen", "Listen failed.", e);
                            return;
                        }
                        for (QueryDocumentSnapshot doc : value) {
                            if (doc.get("name") != null) {
                                Expense iteam = new Expense(doc.getId(), doc.getString("category"), doc.getTimestamp("timeCreated"), doc.getString("note"), doc.getDouble("amount"), doc.getBoolean("expen"));
                                if (iteam.isExpen()) {
                                    expenses.add(iteam);
                                } else {
                                    incomes.add(iteam);
                                }
                            }
                        }

                    }
                });




                break;
            case "week":
                String time2begin = time.split("-")[0] ;
                String time2end = time.split("-")[1];
                int day2begin = Integer.valueOf(time2begin.split("/")[0]);
                int month2begin = Integer.valueOf(time2begin.split("/")[1]);
                int year2begin = Integer.valueOf(time2begin.split("/")[2]);
                int day2end = Integer.valueOf(time2end.split("/")[0]);
                int month2end = Integer.valueOf(time2end.split("/")[1]);
                int year2end = Integer.valueOf(time2end.split("/")[2]);
                LocalDate localDate2begin = LocalDate.of(year2begin, month2begin,day2begin);
                LocalDate localDate2end = LocalDate.of(year2end, month2end,day2end);
                ZoneId zoneid2 = ZoneId.systemDefault();
                Instant instant2 = Instant.now();
                ZoneOffset currentOffsetForMyZone2 = zoneid2.getRules().getOffset(instant2);
                Date begin2 =Date.from(localDate2begin.atStartOfDay(zoneid2).toInstant());
                Date end2 =Date.from(localDate2end.atTime(23,59,59).toInstant(currentOffsetForMyZone2));
                query = query.whereGreaterThanOrEqualTo("timeCreated", begin2).whereLessThanOrEqualTo("timeCreated",end2);
                break;
            case "month":
                month2begin = Integer.valueOf(time.split("/")[0]);
                year2begin = Integer.valueOf(time.split("/")[1]);
                localDate2begin = LocalDate.of(year2begin, month2begin,1);
                localDate2end = LocalDate.of(year2begin, month2begin, 1).with(TemporalAdjusters.lastDayOfMonth());
                zoneid2 = ZoneId.systemDefault();
                instant2 = Instant.now();
                currentOffsetForMyZone2 = zoneid2.getRules().getOffset(instant2);
                begin2 =Date.from(localDate2begin.atStartOfDay(zoneid2).toInstant());
                end2 =Date.from(localDate2end.atTime(23,59,59).toInstant(currentOffsetForMyZone2));
                query = query.whereGreaterThanOrEqualTo("timeCreated", begin2).whereLessThanOrEqualTo("timeCreated",end2);
                break;
            case "year":
                year2begin = Integer.valueOf(time);
                localDate2begin = LocalDate.of(year2begin, 1,1);
                localDate2end = LocalDate.of(year2begin, 12, 31);
                zoneid2 = ZoneId.systemDefault();
                instant2 = Instant.now();
                currentOffsetForMyZone2 = zoneid2.getRules().getOffset(instant2);
                begin2 =Date.from(localDate2begin.atStartOfDay(zoneid2).toInstant());
                end2 =Date.from(localDate2end.atTime(23,59,59).toInstant(currentOffsetForMyZone2));
                query = query.whereGreaterThanOrEqualTo("timeCreated", begin2).whereLessThanOrEqualTo("timeCreated",end2);
                break;
        }

        handlerIncomeChart(incomes);
        handlerExpenseChart(expenses);

    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.report_fragment, container, false);
        chart =v.findViewById(R.id.chartIncome);
        chart.setTouchEnabled(true);

        expenseChart =v.findViewById(R.id.chartExpense);
        expenseChart.setTouchEnabled(true);
        return v;
    }

    void handlerIncomeChart(List<Expense> data)
    {
        Map<String, Double> typeAmountMap = new HashMap<>();
        for (Expense e:data) {
            if(typeAmountMap.containsKey(e.getCategory()))
                typeAmountMap.put(e.getCategory(),Double.valueOf(typeAmountMap.get(e.getCategory())) + e.getAmount());
            else
                typeAmountMap.put(e.getCategory(), e.getAmount());
        }
        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        String label = "type";
        Query query = MainActivity.db
                .collection("users")
                .document("YanMbTpDzBW2VKVBwDoC")
                .collection("expense");
        //initializing data

        int[] colors ;
        colors= getContext().getResources().getIntArray(R.array.mdcolor_random);

        //input data and fit data into pie chart entry
        Set<String> local = typeAmountMap.keySet();
        for(String type: local ){
            pieEntries.add(new PieEntry(typeAmountMap.get(type).floatValue(), type));
        }

        //collecting the entries with label name
        PieDataSet pieDataSet = new PieDataSet(pieEntries,label);
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
        l.setDrawInside(false); // set if legend should be drawn inside or not
        chart.setCenterText("Income" );
        chart.setCenterTextSize(23f);
        chart.setCenterTextColor(Color.parseColor("#1F8B24"));
        chart.invalidate();

    }

    void handlerExpenseChart(List<Expense> data)
    {
        Map<String, Double> typeAmountMap = new HashMap<>();
        for (Expense e:data) {
            if(typeAmountMap.containsKey(e.getCategory()))
                typeAmountMap.put(e.getCategory(),Double.valueOf(typeAmountMap.get(e.getCategory())) + e.getAmount());
            else
                typeAmountMap.put(e.getCategory(), e.getAmount());
        }
        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        String label = "Category";



        int[] colors ;
        colors= getContext().getResources().getIntArray(R.array.mdcolor_random);

        //input data and fit data into pie chart entry
        for(String type: typeAmountMap.keySet()){
            pieEntries.add(new PieEntry(typeAmountMap.get(type).floatValue(), type));
        }

        PieDataSet pieDataSet = new PieDataSet(pieEntries,label);
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
        l.setDrawInside(false); // set if legend should be drawn inside or not
        expenseChart.setCenterText("Expense" );
        expenseChart.setCenterTextColor(Color.parseColor("#1F8B24"));
        expenseChart.invalidate();

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ReportViewModel.class);
        // TODO: Use the ViewModel
    }

}