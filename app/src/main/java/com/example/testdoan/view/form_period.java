package com.example.testdoan.view;

import android.app.DatePickerDialog;
import android.icu.text.DecimalFormat;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.testdoan.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


@RequiresApi(api = Build.VERSION_CODES.N)
public class form_period extends BottomSheetDialogFragment{
    private TextView editText_time;
    private TextView timefinish;
    private boolean isclick=false;
    DecimalFormat decimalFormat = new DecimalFormat("0.0");
    private Date date;
    private Date date2;
    private getdataFromFragment listener;

    public form_period() {
        // Required empty public constructor
    }

    public static form_period newInstance(Bundle bundle) {
        form_period fragment = new form_period();
        Bundle args = new Bundle();
        args.putBundle("expense",bundle);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_form_period, container, false);
        timefinish = v.findViewById(R.id.form_period_finish);
        editText_time = v.findViewById(R.id.form_period_start);
        editText_time.setText( new SimpleDateFormat("EEE, dd/MMM/yyyy").format(Calendar.getInstance().getTime())   );
        editText_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(year, monthOfYear, dayOfMonth);
                                SimpleDateFormat format = new SimpleDateFormat("EEE, dd/MMM/yyyy");
                                String strDate = format.format(calendar.getTime());
                                editText_time.setText(strDate);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        timefinish.setText( new SimpleDateFormat("EEE, dd/MMM/yyyy").format(Calendar.getInstance().getTime())   );
        timefinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(year, monthOfYear, dayOfMonth);
                                SimpleDateFormat format = new SimpleDateFormat("EEE, dd/MMM/yyyy");
                                String strDate = format.format(calendar.getTime());
                                timefinish.setText(strDate);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });


        TextView timeTextview =  (TextView)v.findViewById(R.id.form_period_start);
        TextView timeTextview2 =  (TextView)v.findViewById(R.id.form_period_finish);

        Button save = v.findViewById(R.id.form_period_next);


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isclick == true)
                    return;
                isclick = true;

                String time = timeTextview.getText().toString();
                String timefinish = timeTextview2.getText().toString();
                SimpleDateFormat formatter = new SimpleDateFormat("EEE, dd/MMM/yyyy");
                 date = new Date();
                 date2 = new Date();
                try {
                    date = formatter.parse(time);
                    date2 = formatter.parse(timefinish);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("loi ngay", "loi ngay");
                    isclick = false;
                    return;
                }
                if (date2.before(date)) {
                    timeTextview2.requestFocus();
                    YoYo.with(Techniques.Tada)
                            .duration(300)
                            .repeat(2)
                            .playOn(timeTextview2);
                    YoYo.with(Techniques.Tada)
                            .duration(300)
                            .repeat(2)
                            .playOn(timeTextview);
                    isclick = false;
                    return;
                }


                listener = (getdataFromFragment) getActivity();
                listener.onlicknext(date,date2);

            dismissss();

            }

        });


        return v;
    }

private void dismissss()
{
    dismiss();
}
}