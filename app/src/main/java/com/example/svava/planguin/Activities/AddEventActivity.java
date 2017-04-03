package com.example.svava.planguin.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.example.svava.planguin.R;

public class AddEventActivity extends AppCompatActivity {

    private TimePicker simpleTimePicker_Start;
    private TimePicker simpleTimePicker_End;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        DatePicker simpleDatePicker_Start = (DatePicker)findViewById(R.id.simpleDatePicker_Start); // initiate a date picker
        simpleDatePicker_Start.setSpinnersShown(false);

        DatePicker simpleDatePicker_End = (DatePicker)findViewById(R.id.simpleDatePicker_End); // initiate a date picker
        simpleDatePicker_End.setSpinnersShown(false);

        simpleTimePicker_Start = (TimePicker)findViewById(R.id.simpleTimePicker_Start); // initiate a time picker
        simpleTimePicker_Start.setIs24HourView(true);
        int hours_Start =simpleTimePicker_Start.getCurrentHour();
        int minutes_Start = simpleTimePicker_Start.getCurrentMinute();
        System.out.println(hours_Start);

        simpleTimePicker_End = (TimePicker)findViewById(R.id.simpleTimePicker_End); // initiate a time picker
        simpleTimePicker_End.setIs24HourView(true);
        int hours_End =simpleTimePicker_End.getCurrentHour();
        int minutes_End = simpleTimePicker_End.getCurrentMinute();
        System.out.println(hours_End);
    }

    public void onClick(View v) {

        Intent i;
        int id = v.getId();

        switch (id) {
            case R.id.compare_button:
                i = new Intent(this, CompareActivity.class);
                startActivity(i);
                overridePendingTransition(0, 0);
                break;
            case R.id.friendlist_button:
                i = new Intent(this, FriendListActivity.class);
                startActivity(i);
                overridePendingTransition(0, 0);
                break;
            case R.id.grouplist_button:
                i = new Intent(this, GroupListActivity.class);
                startActivity(i);
                overridePendingTransition(0, 0);
                break;
            case R.id.settings_button:
                i = new Intent(this, SettingsActivity.class);
                startActivity(i);
                overridePendingTransition(0, 0);
                break;
            case R.id.invitation_button:
                i = new Intent(this, InvitationActivity.class);
                startActivity(i);
                overridePendingTransition(0, 0);
                break;
            default:
                i = new Intent(this, ScheduleActivity.class);
                startActivity(i);
                break;
        }
    }
}
