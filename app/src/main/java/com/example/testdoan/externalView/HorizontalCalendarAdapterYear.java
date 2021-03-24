package com.example.testdoan.externalView;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testdoan.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;


class HorizontalCalendarAdapterYear extends RecyclerView.Adapter<HorizontalCalendarAdapterYear.MyViewHolder> {
    private ArrayList<HorizontalCalendarModel> list;
    private Context mCtx;
    private HorizontalCalendarView.OnCalendarListener onCalendarListener;

    public void setOnCalendarListener(HorizontalCalendarView.OnCalendarListener onCalendarListener) {
        this.onCalendarListener = onCalendarListener;
    }

    public HorizontalCalendarAdapterYear(ArrayList<HorizontalCalendarModel> list, Context mCtx) {
        this.list = list;
        this.mCtx = mCtx;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView year;
        LinearLayout parent;

        public MyViewHolder(View view) {
            super(view);


            parent = view.findViewById(R.id.parent_year);
            year = view.findViewById(R.id.year_year);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.horizontal_calendar_list_item_year, parent, false);

        return new MyViewHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final HorizontalCalendarModel model = list.get(position);

        Display display = ((Activity)mCtx).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display. getSize(size);
        int width = size. x;
        holder.parent.setMinimumWidth(Math.round(width/7));
        holder.year.setText(model.getData());

        if(model.getStatus()==0){
            holder.year.setTextColor(mCtx.getColor(R.color.grey_600));
            holder.parent.setBackgroundColor(Color.TRANSPARENT);
        }else{
            holder.year.setTextColor(mCtx.getColor(R.color.textColorLight));
            holder.parent.setBackgroundResource(R.drawable.color_status_1);
        }

        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCalendarListener.onDateSelected(model.getData().toString());
                for (HorizontalCalendarModel m :list
                ) {
                    m.setStatus(0);
                }
                model.setStatus(1);
                notifyDataSetChanged();

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}