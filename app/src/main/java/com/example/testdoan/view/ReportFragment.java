package com.example.testdoan.view;

import androidx.lifecycle.ViewModelProvider;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.testdoan.R;
import com.example.testdoan.viewmodel.ReportViewModel;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ReportFragment extends Fragment {

    private ReportViewModel mViewModel;
    private PieChart chart,expenseChart;
    private final int count = 12;

    public static ReportFragment newInstance() {
        return new ReportFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.report_fragment, container, false);
        chart =v.findViewById(R.id.chartIncome);
        chart.setTouchEnabled(true);

        expenseChart =v.findViewById(R.id.chartExpense);
        expenseChart.setTouchEnabled(true);

        handlerIncomeChart();
        handlerExpenseChart();
        return v;
    }

    void handlerIncomeChart()
    {

        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        String label = "type";

        //initializing data
        Map<String, Integer> typeAmountMap = new HashMap<>();
        typeAmountMap.put("Toys",200);
        typeAmountMap.put("Snacks",230);
        typeAmountMap.put("Clothes",100);
        typeAmountMap.put("Stationary",500);
        typeAmountMap.put("Phone",50);

        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor("#304567"));
        colors.add(Color.parseColor("#309967"));
        colors.add(Color.parseColor("#476567"));
        colors.add(Color.parseColor("#890567"));
        colors.add(Color.parseColor("#a35567"));
        colors.add(Color.parseColor("#ff5f67"));
        colors.add(Color.parseColor("#3ca567"));

        //input data and fit data into pie chart entry
        for(String type: typeAmountMap.keySet()){
            pieEntries.add(new PieEntry(typeAmountMap.get(type).floatValue(), type));
        }

        //collecting the entries with label name
        PieDataSet pieDataSet = new PieDataSet(pieEntries,label);
        //setting text size of the value
        pieDataSet.setValueTextSize(12f);
        //providing color list for coloring different entries
        pieDataSet.setColors(colors);
        //grouping the data set from entry to chart
        PieData pieData = new PieData(pieDataSet);
        //showing the value of the entries, default true if not set
        pieData.setValueFormatter(new PercentFormatter());
        pieDataSet.setDrawValues(true);




        chart.setData(pieData);
        chart.animateY(1000);
        chart.invalidate();
        chart.setUsePercentValues(true);
        chart.setHighlightPerTapEnabled(true);
        chart.setUsePercentValues(true);
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

    void handlerExpenseChart()
    {
        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        String label = "type";
        //initializing data
        Map<String, Integer> typeAmountMap = new HashMap<>();
        typeAmountMap.put("Toys",200);
        typeAmountMap.put("Snacks",230);
        typeAmountMap.put("Clothes",100);
        typeAmountMap.put("Stationary",500);
        typeAmountMap.put("Phone",50);
        //initializing colors for the entries
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor("#304567"));
        colors.add(Color.parseColor("#309967"));
        colors.add(Color.parseColor("#476567"));
        colors.add(Color.parseColor("#890567"));
        colors.add(Color.parseColor("#a35567"));
        colors.add(Color.parseColor("#ff5f67"));
        colors.add(Color.parseColor("#3ca567"));

        //input data and fit data into pie chart entry
        for(String type: typeAmountMap.keySet()){
            pieEntries.add(new PieEntry(typeAmountMap.get(type).floatValue(), type));
        }
        //collecting the entries with label name
        PieDataSet pieDataSet = new PieDataSet(pieEntries,label);
        //setting text size of the value
        pieDataSet.setValueTextSize(12f);
        //providing color list for coloring different entries
        pieDataSet.setColors(colors);
        //grouping the data set from entry to chart
        PieData pieData = new PieData(pieDataSet);
        //showing the value of the entries, default true if not set
        pieData.setValueFormatter(new PercentFormatter());
        pieDataSet.setDrawValues(true);

        expenseChart.setData(pieData);
        expenseChart.animateY(1000);
        expenseChart.setUsePercentValues(true);
        expenseChart.setHighlightPerTapEnabled(true);
        expenseChart.setUsePercentValues(true);
        expenseChart.getDescription().setText("");

        Legend l = expenseChart.getLegend(); // get legend of pie
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP); // set vertical alignment for legend
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT); // set horizontal alignment for legend
        l.setOrientation(Legend.LegendOrientation.VERTICAL); // set orientation for legend
        l.setDrawInside(false); // set if legend should be drawn inside or not
        expenseChart.setCenterText("Expense" );
        expenseChart.setCenterTextSize(23f);
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