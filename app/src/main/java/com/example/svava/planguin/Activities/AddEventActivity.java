package com.example.svava.planguin.Activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
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
import java.util.Calendar;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

public class AddEventActivity extends AppCompatActivity {

    public TimePicker simpleTimePicker_Start;
    public TimePicker simpleTimePicker_End;

    public Button startDateButton;
    public Button endDateButton;
    public TextView startDateText;
    public TextView endDateText;
    //public DatePicker simpleDatePicker_Start;
    //public DatePicker simpleDatePicker_End;
    int year_start;
    int month_start;
    int day_start;

    int year_end;
    int month_end;
    int day_end;


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

        startDateButton = (Button) findViewById(R.id.startDate_button);
        startDateText = (TextView) findViewById(R.id.startDate_text);
        endDateButton = (Button) findViewById(R.id.endDate_button);
        endDateText = (TextView) findViewById(R.id.endDate_text);

        Calendar today = Calendar.getInstance();
        year_start = today.get(Calendar.YEAR);
        month_start = today.get(Calendar.MONTH);
        day_start = today.get(Calendar.DAY_OF_MONTH);

        year_end = today.get(Calendar.YEAR);
        month_end = today.get(Calendar.MONTH);
        day_end = today.get(Calendar.DAY_OF_MONTH);

        showDateStart(year_start,month_start,day_start);
        showDateEnd(year_end, month_end, day_end);


        startDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(999);
            }
        });

        endDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(998);
            }
        });

        /*simpleDatePicker_Start = (DatePicker)findViewById(R.id.simpleDatePicker_Start); // initiate a date picker
        simpleDatePicker_Start.setSpinnersShown(false);

        simpleDatePicker_End = (DatePicker)findViewById(R.id.simpleDatePicker_End); // initiate a date picker
        simpleDatePicker_End.setSpinnersShown(false);*/

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

    private void showDateStart(int year, int month, int day) {
        startDateText.setText(new StringBuilder().append(day).append("/")
                .append(month+1).append("/").append(year));
    }

    private void showDateEnd(int year, int month, int day) {
        endDateText.setText(new StringBuilder().append(day).append("/")
                .append(month+1).append("/").append(year));
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this,
                    myStartDateListener, year_start, month_start, day_start);
        }
        if (id==998) {
            return new DatePickerDialog(this, myEndDateListener, year_end, month_end, day_end);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myStartDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    year_start = arg1;
                    month_start = arg2;
                    day_start = arg3;
                    showDateStart(arg1, arg2+1, arg3);
                }
            };

    private DatePickerDialog.OnDateSetListener myEndDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    year_end = arg1;
                    month_end = arg2;
                    day_end = arg3;
                    showDateEnd(arg1, arg2+1, arg3);
                }
            };

    private void createScheduleItem(String loggedInUser,String title,Date startTime,Date endTime  ){
        ScheduleItem scheduleItem = new ScheduleItem();
        scheduleItem.setTitle(title);
        startTime.setMonth(startTime.getMonth()+1);
        scheduleItem.setStartTime(startTime);
        endTime.setMonth(endTime.getMonth()+1);
        scheduleItem.setEndTime(endTime);

        Gson gson = new Gson();
        String json = gson.toJson(scheduleItem);

        StringEntity se = new StringEntity(json, "UTF-8");
        se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json;charset=UTF-8"));

        PlanguinRestClient.post("createItem/"+loggedInUser,se, "application/json;charset=UTF-8", new JsonHttpResponseHandler(){
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
        int year = year_start;
        int month = month_start;
        int day = day_start;

        System.out.println("start: "+day+"/"+month+"/"+year);
        Date start = new Date(year, month, day, hours_Start, minutes_Start);

        return start;
    }

    public Date getEndTime(){

        int hours_End =simpleTimePicker_End.getCurrentHour();
        int minutes_End = simpleTimePicker_End.getCurrentMinute();
        int year = year_end;
        int month = month_end;
        int day = day_end;

        System.out.println("end: "+day+"/"+month+"/"+year);
        Date end = new Date(year, month, day, hours_End, minutes_End);

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
