package com.example.testdoan.view;

import android.app.DatePickerDialog;
import android.content.Intent;
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


public class Form_add_expense extends BottomSheetDialogFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static Bundle data = null;
    private static final String ARG_PARAM2 = "param2";
    public static final int requestcodeForcategory = 1;

    private String UserID ;
    private String mParam1;
    private TextView editText_time;
    private TextView editText_category;
    private boolean type;
    double amountbefore =-1;
    boolean typeBefore;

    public Form_add_expense() {
        // Required empty public constructor
    }

    public static Form_add_expense newInstance(Bundle bundle) {
        Form_add_expense fragment = new Form_add_expense();
        Bundle args = new Bundle();
        args.putBundle("expense", bundle);
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
        View v = inflater.inflate(R.layout.fragment_form_add_expense, container, false);
        editText_category=v.findViewById(R.id.form_expense_category);
        UserID =MainActivity.user.getId();
        editText_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userid = MainActivity.user.getId();
                Intent t = new Intent(getActivity(),Category_Manage.class);
               t.putExtra("idcuauser",userid);
                startActivityForResult(t,requestcodeForcategory);
            }
        });

        editText_time = v.findViewById(R.id.form_expense_time);
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
        TextView categoryTextview =  (TextView)v.findViewById(R.id.form_expense_category);
        EditText amountTextview =  v.findViewById(R.id.form_expense_amount);
        TextView timeTextview =  (TextView)v.findViewById(R.id.form_expense_time);
        EditText NoteTextview =  v.findViewById(R.id.form_expense_note);
        Button save = v.findViewById(R.id.form_expense_save);
        Bundle b = null;

        if(data!=null)
        {
           b = getArguments().getBundle("expense");
            mParam1=b.getString("id");
            amountbefore = Double.valueOf(b.getString("amount"));
            typeBefore = b.getString("type").equalsIgnoreCase("income") ? true :false;
            categoryTextview.setText(b.getString("category"));
            categoryTextview.setEnabled(false);
            amountTextview.setText(b.getString("amount"));
            SimpleDateFormat format = new SimpleDateFormat("EEE, dd/MMM/yyyy");
            timeTextview.setText(format.format(Date.parse(b.getString("time"))));
            NoteTextview.setText(b.getString("note"));
        }


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryTextview.setEnabled(true);
                String Category =categoryTextview.getText().toString();
                double amount = Double.valueOf(amountTextview.getText().toString());
                String time =timeTextview.getText().toString();
                String note =NoteTextview.getText().toString();
                SimpleDateFormat formatter =   new SimpleDateFormat("EEE, dd/MMM/yyyy");
                Date date = new Date();
                try {
                    date = formatter.parse(time);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    Log.d("loi ngay", "loi ngay");
                }

                Timestamp timestamp = new Timestamp( date );

                Map<String, Object> data = new HashMap<>();

                data.put("amount", amount);
                data.put("category", Category);
                data.put("expen", type);
                data.put("note", note);
                data.put("timeCreated", timestamp);

               DocumentReference dff =  MainActivity.db
                        .collection("users")
                        .document(UserID)
                        .collection("expense").document();
               if(getArguments().getBundle("expense")!=null)
               {
                   dff =  MainActivity.db
                           .collection("users")
                           .document(UserID)
                           .collection("expense").document(mParam1);
               }

               dff.set(data, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getContext(), "success", Toast.LENGTH_SHORT).show();
                        if(getArguments().getBundle("expense")!=null)
                            if(typeBefore==type)
                            {
                                if(amount!=amountbefore)
                                    Budgetmodify.modify(amount-amountbefore, (Boolean) data.get("expen"));
                                dismiss();

                            }
                            else
                            {
                                if(amount==amountbefore)
                                    Budgetmodify.modify(amount*2, (Boolean) data.get("expen"));
                                else if(typeBefore ==true && type ==false)
                                    Budgetmodify.modify(amount+amountbefore, (Boolean) data.get("expen"));
                                else if(typeBefore==false && type==true)
                                    Budgetmodify.modify(amount+amountbefore, (Boolean) data.get("expen"));
                                dismiss();

                            }
                        else
                        Budgetmodify.modify(amount, (Boolean) data.get("expen"));
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
            TextView v = getView().findViewById(R.id.form_expense_category);
            v.setText(data.getStringExtra("data"));
            type = data.getBooleanExtra("expen", true);
            // v.setText(data.getStringExtra("category"));
        }
    }


}