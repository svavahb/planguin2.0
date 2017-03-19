package com.example.svava.planguin.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.svava.planguin.Managers.CompareManager;
import com.example.svava.planguin.R;

public class CompareActivity extends AppCompatActivity {

    CompareManager compareManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare);
    }

    private static final String EXTRA_COMPARE_BUTTON = "compareButton";

    public static Intent newIntent(Context packageContext, boolean compareButton){
        Intent i = new Intent(packageContext, CompareActivity.class);
        i.putExtra(EXTRA_COMPARE_BUTTON, compareButton);
        return i;
    }
}
