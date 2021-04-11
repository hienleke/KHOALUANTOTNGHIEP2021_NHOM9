package com.example.testdoan.view;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.text.DecimalFormat;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.testdoan.R;
import com.example.testdoan.repository.Budgetmodify;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.SetOptions;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@RequiresApi(api = Build.VERSION_CODES.N)
public class form_add_Planning extends BottomSheetDialogFragment {
    private static Bundle data = null;
    private static final String ARG_PARAM2 = "param2";
    public static final int requestcodeForcategory = 1;
    private boolean isclick = false;
    private String UserID ;
    private String mParam1;
    private TextView editText_time;
    private TextView editText_category;
    private boolean type;
    double amountbefore =-1;
    boolean typeBefore;
    DecimalFormat decimalFormat = new DecimalFormat("0.0");
    private double amount;
    int q=0;
    public form_add_Planning() {
        // Required empty public constructor
    }

    public static form_add_Planning newInstance(Bundle bundle) {
        form_add_Planning fragment = new form_add_Planning();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            data = getArguments().getBundle("expense");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_form_add__planning, container, false);
        UserID =MainActivity.user.getId();

        editText_time = v.findViewById(R.id.planningtime);
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

        EditText amountTextview =  v.findViewById(R.id.form_planning_amount);
        TextView timeTextview =  (TextView)v.findViewById(R.id.planningtime);

        Button save = v.findViewById(R.id.form_planning_save_period);
        Bundle b = null;

        if(data!=null)
        {
            b = getArguments().getBundle("expense");
            q=1;
            mParam1=b.getString("id");
            amountTextview.setText(decimalFormat.format(Double.valueOf(b.getString("amount"))));
            SimpleDateFormat format = new SimpleDateFormat("EEE, dd/MMM/yyyy");
            timeTextview.setText(format.format(Date.parse(b.getString("time"))));
        }



        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isclick==true)
                    return;
                isclick=true;
                amount = 0;
                try {
                    amount = Double.valueOf(amountTextview.getText().toString().replace(",", "."));
                }
                catch (Exception e)
                {
                    amountTextview.requestFocus();
                    YoYo.with(Techniques.Tada)
                            .duration(300)
                            .repeat(2)
                            .playOn(amountTextview);
                    isclick=false;
                    return;
                }
                String time =timeTextview.getText().toString();
                SimpleDateFormat formatter =   new SimpleDateFormat("EEE, dd/MMM/yyyy");
                Date date = new Date();
                try {
                    date = formatter.parse(time);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    Log.d("loi ngay", "loi ngay");
                    isclick=false;
                }

                Timestamp timestamp = new Timestamp( date );
                Map<String, Object> data = new HashMap<>();
                data.put("amount", amount);
                data.put("timeEnd", timestamp);
                data.put("timeStart", Timestamp.now());

                DocumentReference dff =  MainActivity.db
                        .collection("users")
                        .document(UserID)
                        .collection("planning").document("planning");

                dff.set(data, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getContext(), "success", Toast.LENGTH_SHORT).show();
                        dismiss();
                    }

                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "fail", Toast.LENGTH_SHORT).show();
                        dismiss();
                    }
                });
            }
        });
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==Form_add_expense.requestcodeForcategory && data !=null)
        {
            TextView v = getView().findViewById(R.id.form_expense_category_period);
            v.setText(data.getStringExtra("data"));
            type = data.getBooleanExtra("expen", true);
            // v.setText(data.getStringExtra("category"));
        }
    }


}