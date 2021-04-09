package com.example.testdoan.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.testdoan.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Form_add_category#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Form_add_category extends BottomSheetDialogFragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String UserID = Category_Manage.user.getId();
    private Button save;

    private FirebaseFirestore db ;
    private String mParam1;
    private int mParam3;
    private boolean mParam2;
    private EditText name,note;

    public Form_add_category() {
        // Required empty public constructor
    }


    public static Form_add_category newInstance(Bundle bundle,boolean iscreatenew) {
        Form_add_category fragment = new Form_add_category();
        Bundle args = new Bundle();
        args.putBundle("category", bundle);
        args.putBoolean("isCreateNew",iscreatenew);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getBundle("category").getString("id");
            mParam2 = getArguments().getBoolean("isCreateNew");
            Bundle b = getArguments().getBundle("category");
            mParam3 = b.getInt("incomeOrexpense");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_form_add_category, container, false);
        name = v.findViewById(R.id.category_form_name);
        note =v.findViewById(R.id.category_form_note);
        save = v.findViewById(R.id.form_category_save);

        if(mParam2==false)
        {
            name.setText(  getArguments().getBundle("category").getString("name"));
            note.setText( getArguments().getBundle("category").getString("note"));
        }
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = name.getText().toString();
                String textnote = note.getText().toString();

                if(text.equals(null) || text.equalsIgnoreCase("")) {
                    name.requestFocus();
                    YoYo.with(Techniques.Tada)
                            .duration(300)
                            .repeat(2)
                            .playOn(name);
                    //amountTextview.setText("Enter your amount !!!");
                    //amountTextview.setBackgroundColor(android.R.color.darker_gray);
                    return;
                }




               db=  FirebaseFirestore.getInstance();
                DocumentReference dff =  db
                        .collection("users")
                        .document(UserID);
                    if (mParam2==true)
                          dff=  dff.collection("category").document();
                    else
                       dff= dff.collection("category").document(mParam1);

                Map<String, Object> data = new HashMap<>();
                data.put("name", text);
                data.put("note", textnote);
                data.put("expen", mParam3== 0 ? false : true);
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
        Toast.makeText(getContext(), String.valueOf(mParam2), Toast.LENGTH_SHORT).show();
        return v ;
    }


}