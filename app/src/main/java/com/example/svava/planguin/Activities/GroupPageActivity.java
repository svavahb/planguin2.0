package com.example.svava.planguin.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.svava.planguin.Managers.GroupManager;
import com.example.svava.planguin.R;

public class GroupPageActivity extends AppCompatActivity {

    GroupManager groupManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_page);
    }
}
