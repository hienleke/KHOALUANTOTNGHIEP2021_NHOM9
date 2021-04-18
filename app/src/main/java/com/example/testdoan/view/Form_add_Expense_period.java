package com.example.testdoan.view;

import android.content.Intent;
import android.icu.text.DecimalFormat;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import java.util.HashMap;
import java.util.Map;


@RequiresApi(api = Build.VERSION_CODES.N)
public class Form_add_Expense_period extends BottomSheetDialogFragment {
    private static Bundle data = null;
    private static final String ARG_PARAM2 = "param2";
    public static final int requestcodeForcategory = 1;
    private boolean isclick = false;
    private String UserID ;
    private String mParam1;
    private TextView editText_time;
    private TextView editText_category;
    private boolean type;
    private static String period;
    double amountbefore =-1;
    boolean typeBefore;
    DecimalFormat decimalFormat = new DecimalFormat("##.0");
    private double amount;
    int q=0;
    public Form_add_Expense_period() {
        // Required empty public constructor
    }

    public static Form_add_Expense_period newInstance(Bundle bundle) {
        Form_add_Expense_period fragment = new Form_add_Expense_period();
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
            this.period=data.getString("period");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_form_add__expense_period, container, false);
        editText_category=v.findViewById(R.id.form_expense_category_period);
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

        TextView categoryTextview =  (TextView)v.findViewById(R.id.form_expense_category_period);
        EditText amountTextview =  v.findViewById(R.id.form_expense_amount_period);

        EditText NoteTextview =  v.findViewById(R.id.form_expense_note_period);
        Button save = v.findViewById(R.id.form_expense_save_period);
        Bundle b = null;

        if(data!=null && data.getString("id")!=null)
        {
            b = getArguments().getBundle("expense");
           q=1;
            mParam1=b.getString("id");
            amountbefore = Double.valueOf(b.getString("amount"));
            String vcl = b.getString("type");
            typeBefore = b.getString("type").equalsIgnoreCase("income") ? true : false;
            type=typeBefore;
            categoryTextview.setText(b.getString("category"));
            categoryTextview.setEnabled(false);
            amountTextview.setText(decimalFormat.format(Double.valueOf(b.getString("amount"))));
            SimpleDateFormat format = new SimpleDateFormat("EEE, dd/MMM/yyyy");
            period = b.getString("period");
            NoteTextview.setText(b.getString("note"));
        }



        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isclick==true)
                    return;
                String Category =categoryTextview.getText().toString();
                isclick=true;
                amount = 0;
                try {
                    amount = Double.valueOf(amountTextview.getText().toString().replace(",", "."));
                    if(amount==0)
                    {
                        amountTextview.requestFocus();
                        YoYo.with(Techniques.Tada)
                                .duration(300)
                                .repeat(2)
                                .playOn(amountTextview);
                        isclick=false;
                        return;
                    }
                    if(Category.equalsIgnoreCase("None"))
                    {

                        //categoryTextview.requestFocus();
                        YoYo.with(Techniques.Tada)
                                .duration(300)
                                .repeat(2)
                                .playOn(categoryTextview);
                        isclick=false;
                        return;
                    }


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
                String note =NoteTextview.getText().toString();
                SimpleDateFormat formatter =   new SimpleDateFormat("EEE, dd/MMM/yyyy");



                Map<String, Object> data = new HashMap<>();
                data.put("amount", amount);
                data.put("category", Category);
                data.put("expen", type);
                data.put("note", note);
                data.put("timeCreated", Timestamp.now());
                data.put("enable", q==1 ? getArguments().getBundle("expense").getBoolean("enable"):true);
                data.put("period", period);


                DocumentReference dff =  MainActivity.db
                        .collection("users")
                        .document(UserID)
                        .collection("expensePeriodic").document();
                if(getArguments().getBundle("expense")!=null && getArguments().getBundle("expense").getString("id")!=null)
                {
                    dff =  MainActivity.db
                            .collection("users")
                            .document(UserID)
                            .collection("expensePeriodic").document(mParam1);
                }

                dff.set(data, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getContext(), "success", Toast.LENGTH_SHORT).show();
                        if(getArguments().getBundle("expense")!=null)
                            if(typeBefore==type)
                            {

                                isclick=false;
                                dismiss();
                            }
                            else
                            {
//                                if(amount==amountbefore)
//                                    Budgetmodify.modify(amount*2, (Boolean) data.get("expen"));
//                                else if(typeBefore ==true && type ==false)
//                                    Budgetmodify.modify(amount+amountbefore, (Boolean) data.get("expen"));
//                                else if(typeBefore==false && type==true)
//                                    Budgetmodify.modify(amount+amountbefore, (Boolean) data.get("expen"));
                                dismiss();
                            }
                        else
                            Budgetmodify.modify(amount, (Boolean) data.get("expen"));

                        categoryTextview.setEnabled(true);
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