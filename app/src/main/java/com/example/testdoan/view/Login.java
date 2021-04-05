package com.example.testdoan.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.testdoan.R;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        getSupportFragmentManager().beginTransaction().add(R.id.login_container,LoginFragment.newInstance("sfda","Sdfas")).commit();
    }
}