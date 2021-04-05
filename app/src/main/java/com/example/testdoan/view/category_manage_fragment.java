package com.example.testdoan.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.testdoan.R;
import com.example.testdoan.externalView.Iteam_category_adapter;
import com.example.testdoan.model.Category;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.firestore.Query;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link category_manage_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class category_manage_fragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Iteam_category_adapter adapter;
    private RecyclerView recyclerView;
    private String USERID;

    private String mParam1;
    private String mParam2;

    public category_manage_fragment() {

    }
    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();

    }

    public static category_manage_fragment newInstance(String param1, String param2) {
        category_manage_fragment fragment = new category_manage_fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        USERID = Category_Manage.user.getId();
        Query query = MainActivity.db
                .collection("users")
                .document(USERID)
                .collection(mParam1=="income"? "category_income" : "category_expense");
        FirestoreRecyclerOptions<Category> options = new FirestoreRecyclerOptions.Builder<com.example.testdoan.model.Category>()
                .setQuery(query, com.example.testdoan.model.Category.class)
                .build();
        adapter = new Iteam_category_adapter(options,getContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_category_manage_fragment, container, false);
        recyclerView = v.findViewById(R.id.recycleview_categoryIteam);
        recyclerView.addItemDecoration(new SpacesItemDecoration(50));
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        // do whatever
                       String data=  adapter.getItem(position).getName();
                       Intent t = new Intent();
                       t.putExtra("data",data );
                       t.putExtra("expen", Category_Manage.viewPager2.getCurrentItem()==0 ? false : true);
                        getActivity().setResult(Form_add_expense.requestcodeForcategory, t);
                        getActivity().finish();
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }


    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}