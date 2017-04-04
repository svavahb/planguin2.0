package com.example.svava.planguin.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.alamkanak.weekview.WeekViewEvent;
import com.example.svava.planguin.Entities.Date;
import com.example.svava.planguin.Entities.ScheduleItem;
import com.example.svava.planguin.Managers.ScheduleManager;
import com.example.svava.planguin.R;
import com.example.svava.planguin.Utils.JSONparser;
import com.example.svava.planguin.Utils.PlanguinRestClient;
import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

public class AddEventActivity extends AppCompatActivity {

    public TimePicker simpleTimePicker_Start;
    public TimePicker simpleTimePicker_End;
    public DatePicker simpleDatePicker_Start;
    public DatePicker simpleDatePicker_End;


    EditText myEventName;
    String  stringEventName;
    JSONparser jsonparser = new JSONparser();
    SharedPreferences mySharedPreferences;
    String loggedInUser;
    ScheduleManager scheduleManager;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(AddEventActivity.this);
        loggedInUser = sharedPreferences.getString("username","");

        if(loggedInUser.isEmpty()){
            Intent i = new Intent(AddEventActivity.this, WelcomeActivity.class);
            startActivity(i);
        }

        scheduleManager = new ScheduleManager();

        myEventName =(EditText)findViewById(R.id.eventName);


        simpleDatePicker_Start = (DatePicker)findViewById(R.id.simpleDatePicker_Start); // initiate a date picker
        simpleDatePicker_Start.setSpinnersShown(false);

        simpleDatePicker_End = (DatePicker)findViewById(R.id.simpleDatePicker_End); // initiate a date picker
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

    private void createScheduleItem(String loggedInUser,String title,Date startTime,Date endTime  ){
        ScheduleItem scheduleItem = new ScheduleItem();
        scheduleItem.setTitle(title);
        startTime.setMonth(startTime.getMonth()+1);
        scheduleItem.setStartTime(startTime);
        endTime.setMonth(endTime.getMonth()+1);
        scheduleItem.setEndTime(endTime);

        Gson gson = new Gson();
        String json = gson.toJson(scheduleItem);

        StringEntity se = null;
        try {
            se = new StringEntity(json);
        } catch (UnsupportedEncodingException e) {
            // handle exceptions properly!
        }
        se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

        PlanguinRestClient.post("createItem/"+loggedInUser,se, "application/json", new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject result){
                System.out.println(result);

                Intent intent = new Intent(AddEventActivity.this, ScheduleActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject s) {
                System.out.println(s+" "+e);
            }
        }
        );
    }

    public Date getStartTime(){
        int hours_Start =simpleTimePicker_Start.getCurrentHour();
        int minutes_Start = simpleTimePicker_Start.getCurrentMinute();
        int year_start = simpleDatePicker_Start.getYear();
        int month_start = simpleDatePicker_Start.getMonth();
        int day_start = simpleDatePicker_Start.getDayOfMonth();

        Date start = new Date(year_start, month_start, day_start, hours_Start, minutes_Start);

        return start;
    }

    public Date getEndTime(){

        int hours_End =simpleTimePicker_Start.getCurrentHour();
        int minutes_End = simpleTimePicker_Start.getCurrentMinute();
        int year_End = simpleDatePicker_End.getYear();
        int month_End = simpleDatePicker_End.getMonth();
        int day_End =simpleDatePicker_End.getDayOfMonth();

        Date end = new Date(year_End, month_End, day_End, hours_End, minutes_End);

        return end;
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
            case R.id.submitevent_button:
                //i = new Intent(this, ScheduleActivity.class);
                stringEventName= myEventName.getText().toString();
                createScheduleItem(loggedInUser,stringEventName, getStartTime(),getEndTime());
                break;
            default:
                i = new Intent(this, ScheduleActivity.class);
                startActivity(i);
                break;
        }
    }
}
