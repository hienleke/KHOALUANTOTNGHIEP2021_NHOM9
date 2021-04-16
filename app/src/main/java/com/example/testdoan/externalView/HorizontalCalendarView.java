package com.example.testdoan.externalView;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.example.testdoan.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class HorizontalCalendarView extends LinearLayout {
    Context context;
    AttributeSet attributeSet;
    RecyclerView recyclerView;
    int pos =0;



    public void setContext(Context context) {
        this.context = context;
    }

    public AttributeSet getAttributeSet() {
        return attributeSet;
    }

    public void setAttributeSet(AttributeSet attributeSet) {
        this.attributeSet = attributeSet;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    public interface OnCalendarListener{
        void onDateSelected(String date);
    }
    public HorizontalCalendarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.attributeSet = attrs;
        init();
    }
    public HorizontalCalendarView(Context context) {
        super(context);
        this.context = context;
        init();
    }
    public HorizontalCalendarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        this.attributeSet = attrs;
        init();
    }
    private void init() {
        View view = inflate(getContext(),R.layout.horizontal_calendar, null);
        TextView textView = view.findViewById(R.id.text);
        recyclerView = view.findViewById(R.id.re);
        view.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        if(attributeSet!=null) {
            TypedArray attrs = context.obtainStyledAttributes(attributeSet, R.styleable.HorizontalCalendarView);
            textView.setText(attrs.getString(R.styleable.HorizontalCalendarView_text));
            attrs.recycle();
        }else{
            textView.setText("No Text Provided");
        }
        textView.setVisibility(GONE);

        addView(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setUpCalendar(String mode , long start, long end, ArrayList<String> dates, OnCalendarListener onCalendarListener){

        Calendar c1 = Calendar.getInstance();
        c1.setTimeInMillis(start);
      int  modeHandler = 0;
        switch(mode) {
            case "date":
                modeHandler=Calendar.DATE;
                start-=20;
                end+=20;
                break;
            case "week":
                modeHandler=Calendar.WEEK_OF_MONTH;
                start-=20;
                end+=20;
                break;
            case "month":
                modeHandler=Calendar.MONTH;
                start=start-20;
                end=end + 20;
                break;
            case "year":
                modeHandler=Calendar.YEAR;
                start-=10;
                end+=10;
                break;
            case "period":
                modeHandler=Calendar.DATE;
                break;
        }

        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(start);
        ArrayList<HorizontalCalendarModel> list = new ArrayList<>();
        long today = Tools.getTimeInMillis(Tools.getFormattedDateToday());
        long current = start;
        int i=0;
         pos = 0;
        while(current<end){
            c1.add(modeHandler,1);
            switch(mode) {
                case "date":

                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                    if(sdf.format(c1.getTimeInMillis()).equalsIgnoreCase(sdf.format(today))){
                        pos =i;
                        Log.d("Postion",pos+"");
                    }
                    HorizontalCalendarModel model = new HorizontalCalendarModel(c1.getTimeInMillis());
                    if(dates.contains(sdf.format(c1.getTimeInMillis()))){
                        model.setStatus(1);
                    }
                    list.add(model);
                    break;
                case "week":
                    boolean kq =false;
                    c1.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                    long vcl =Calendar.getInstance().getTimeInMillis();
                    String tam = c1.get(Calendar.DATE)+"/"+(c1.get(Calendar.MONTH)+1+"/"+(c1.get(Calendar.YEAR)));
                    c1.add(Calendar.DATE, 6);
                    tam+="-" + c1.get(Calendar.DATE)+"/"+(c1.get(Calendar.MONTH)+1+"/"+(c1.get(Calendar.YEAR)));
                    kq = c1.getTimeInMillis() > vcl  ? true :  false;
                    c1.add(Calendar.DATE, -6);
                    HorizontalCalendarModel model1 = new HorizontalCalendarModel(tam);
                    if(kq && !list.stream().anyMatch(model2-> model2.getStatus()==1))
                    {
                        pos=i;
                        model1.setStatus(1);
                    }
                    list.add(model1);
                    break;
                case "month":
                    HorizontalCalendarModel model2 = new HorizontalCalendarModel((c1.get(Calendar.MONTH) +1) + "/"+c1.get(Calendar.YEAR));
                    if(c1.get(Calendar.MONTH)==Calendar.getInstance().get(Calendar.MONTH) && c1.get(Calendar.YEAR) ==Calendar.getInstance().get(Calendar.YEAR))
                    {
                        pos=i;
                        model2.setStatus(1);
                    }
                    list.add(model2);

                    break;
                case "year":
                    HorizontalCalendarModel model3 = new HorizontalCalendarModel((String.valueOf(c1.get(Calendar.YEAR))));
                    if( c1.get(Calendar.YEAR) ==Calendar.getInstance().get(Calendar.YEAR))
                    {
                        pos=i;
                        model3.setStatus(1);
                    }
                    list.add(model3);
                    break;
                case "period":
                  break;

            }
            current = c1.getTimeInMillis();
            i++;
        }
        if(mode=="period") {
            Calendar c2 = Calendar.getInstance();
            c2.setTimeInMillis(start);

            String tam = c2.get(Calendar.DATE)+"/"+(c2.get(Calendar.MONTH)+1+"/"+(c2.get(Calendar.YEAR)));
            c2.setTimeInMillis(end);

            tam+="-" + c2.get(Calendar.DATE)+"/"+(c2.get(Calendar.MONTH)+1+"/"+(c2.get(Calendar.YEAR)));
            HorizontalCalendarModel model4 = new HorizontalCalendarModel(String.valueOf(tam));
            model4.setStatus(1);
            list.add(model4);
        }


        HorizontalCalendarAdapterDate adapter = new HorizontalCalendarAdapterDate(list,context);
        adapter.setOnCalendarListener(onCalendarListener);

        HorizontalCalendarAdapterMonth adapterMonth = new HorizontalCalendarAdapterMonth(list,context);
        adapterMonth.setOnCalendarListener(onCalendarListener);

        HorizontalCalendarAdapterWeek adapterWeek = new HorizontalCalendarAdapterWeek(list,context);
        adapterWeek.setOnCalendarListener(onCalendarListener);

        HorizontalCalendarAdapterYear adapterYear = new HorizontalCalendarAdapterYear(list,context);
        adapterYear.setOnCalendarListener(onCalendarListener);

        HorizontalCalendarAdapterYear adapterPeriod = new HorizontalCalendarAdapterYear(list,context);
        adapterPeriod.setOnCalendarListener(onCalendarListener);



        switch(mode) {
            case "date":
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                break;
            case "week":
                recyclerView.setAdapter(adapterWeek);
                adapterWeek.notifyDataSetChanged();
                break;
            case "month":
                recyclerView.setAdapter(adapterMonth);
                adapterMonth.notifyDataSetChanged();
                break;
            case "year":
                recyclerView.setAdapter(adapterYear);
                adapterYear.notifyDataSetChanged();
                break;
            case "period":
                recyclerView.setAdapter(adapterPeriod);
                adapterPeriod.notifyDataSetChanged();
                break;

        }
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context,RecyclerView.HORIZONTAL,false);
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);
        recyclerView.setOnFlingListener(null);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.smoothScrollToPosition(pos);

        recyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                View view;
                   view = recyclerView.getLayoutManager().findViewByPosition(pos);
               view.performClick();

                }

        },700);







    }


}
