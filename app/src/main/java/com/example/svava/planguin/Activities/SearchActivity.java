package com.example.svava.planguin.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.svava.planguin.Managers.SearchManager;
import com.example.svava.planguin.R;

public class SearchActivity extends AppCompatActivity {
    SearchManager searchManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
    }
}
