package com.example.svava.planguin.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.svava.planguin.Managers.SearchManager;
import com.example.svava.planguin.R;

import org.json.JSONException;

public class SearchActivity extends AppCompatActivity {
    SearchManager searchManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        try {
            searchManager.search("svava");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
