package com.example.testdoan.view;

import android.content.DialogInterface;
import android.icu.text.DecimalFormat;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.testdoan.R;
import com.example.testdoan.repository.Budgetmodify;
import com.example.testdoan.viewmodel.PlanningViewModel;

@RequiresApi(api = Build.VERSION_CODES.N)
public class Planning extends Fragment {

    private PlanningViewModel mViewModel;
    private Button btnSave;
    private ListView lv;
    DecimalFormat decimalFormat = new DecimalFormat("0.0");
    public static Planning newInstance() {
        return new Planning();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.planning_fragment, container, false);
        EditText txtBudget = v.findViewById(R.id.txtbudget);
        txtBudget.setText(String.valueOf(decimalFormat.format(MainActivity.budget)));
        btnSave = v.findViewById(R.id.updateBudget);
        lv = v.findViewById(R.id.planing_setting_menu);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Do you make sure update Budget ?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Budgetmodify.init(Double.valueOf(txtBudget.getText().toString()));

                    }
                })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();



            }
        });


        return v ;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(PlanningViewModel.class);

    }

}