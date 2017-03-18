package com.example.svava.planguin.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.svava.planguin.Managers.ScheduleManager;
import com.example.svava.planguin.R;

public class ScheduleActivity extends AppCompatActivity {

    ScheduleManager scheduleManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
    }
}
