package com.example.svava.planguin.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.svava.planguin.Managers.UserManager;
import com.example.svava.planguin.R;

public class LaunchActivity extends AppCompatActivity {

    UserManager userManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
    }
}
