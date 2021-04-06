package com.example.testdoan.view;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.testdoan.R;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        getSupportFragmentManager().beginTransaction().add(R.id.login_container,LoginFragment.newInstance("sfda","Sdfas")).commit();
    }


    @Override
    public void onBackPressed(){
       super.onBackPressed();
    }
}