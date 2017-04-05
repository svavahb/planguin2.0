package com.example.svava.planguin.Activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.ColorInt;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.pes.androidmaterialcolorpickerdialog.ColorPicker;

import com.example.svava.planguin.Entities.Date;
import com.example.svava.planguin.Entities.ScheduleItem;
import com.example.svava.planguin.Managers.ScheduleManager;
import com.example.svava.planguin.R;
import com.example.svava.planguin.Utils.PlanguinRestClient;
import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.pes.androidmaterialcolorpickerdialog.ColorPickerCallback;

import org.json.JSONObject;

import java.util.Calendar;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

public class AddEventActivity extends AppCompatActivity {

    public Button startTimeButton;
    public Button endTimeButton;
    public Button colorPickButton;

    public TextView startTimeText;
    public TextView endTimeText;

    int year_start;
    int month_start;
    int day_start;
    int hour_start;
    int minute_start;

    int year_end;
    int month_end;
    int day_end;
    int hour_end;
    int minute_end;

    int color;

    EditText myEventName;
    String  stringEventName;
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
        // Get start time from intent
        long startMillis = getIntent().getLongExtra("startTime",0);

        // Get end time from intent
        long endMillis = getIntent().getLongExtra("endTime",0);

        scheduleManager = new ScheduleManager();

        // Find buttons and text views
        myEventName =(EditText)findViewById(R.id.eventName);

        startTimeButton = (Button) findViewById(R.id.startTime_button);
        startTimeText = (TextView) findViewById(R.id.startTime_text);

        endTimeButton = (Button) findViewById(R.id.endTime_button);
        endTimeText = (TextView) findViewById(R.id.endTime_text);

        colorPickButton = (Button) findViewById(R.id.color_pick_button);

        // Initialize the text views to today or the time gotten from the intent
        if (startMillis==0) {
            Calendar today = Calendar.getInstance();
            year_start = today.get(Calendar.YEAR);
            month_start = today.get(Calendar.MONTH);
            day_start = today.get(Calendar.DAY_OF_MONTH);
            hour_start = today.get(Calendar.HOUR_OF_DAY);
            minute_start = today.get(Calendar.MINUTE);
        }
        else {
            Calendar start = Calendar.getInstance();
            start.setTimeInMillis(startMillis);
            year_start = start.get(Calendar.YEAR);
            month_start = start.get(Calendar.MONTH);
            day_start = start.get(Calendar.DAY_OF_MONTH);
            hour_start = start.get(Calendar.HOUR_OF_DAY);
            minute_start = start.get(Calendar.MINUTE);
        }

        if (endMillis==0) {
            Calendar today = Calendar.getInstance();
            year_end = today.get(Calendar.YEAR);
            month_end = today.get(Calendar.MONTH);
            day_end = today.get(Calendar.DAY_OF_MONTH);
            hour_end = today.get(Calendar.HOUR_OF_DAY);
            minute_end = today.get(Calendar.MINUTE);
        }
        else {
            Calendar end = Calendar.getInstance();
            end.setTimeInMillis(endMillis);
            year_end = end.get(Calendar.YEAR);
            month_end = end.get(Calendar.MONTH);
            day_end = end.get(Calendar.DAY_OF_MONTH);
            hour_end = end.get(Calendar.HOUR_OF_DAY);
            minute_end = end.get(Calendar.MINUTE);
        }

        showTimeStart(hour_start, minute_start, day_start, month_start, year_start);
        showTimeEnd(hour_end, minute_end, day_end, month_end, year_end);

        startTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(997);
            }
        });

        endTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(996);
            }
        });

        final ColorPicker cp = new ColorPicker(AddEventActivity.this, 250, 250, 250);

        colorPickButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cp.show();
            }
        });

        cp.setCallback(new ColorPickerCallback() {
            @Override
            public void onColorChosen(@ColorInt int selectedColor) {
                color = selectedColor;
                cp.hide();

            }
        });
    }

    private void showTimeStart(int hour, int minute, int day, int month, int year) {
        String hourText = ""+hour;
        String minuteText = ""+minute;
        if (hour<10) {
            hourText = "0"+hourText;
        }
        if (minute<10) {
            minuteText = "0"+minuteText;
        }

        startTimeText.setText(new StringBuilder().append(hourText).append(":").append(minuteText).append(" ").append(day).append("/")
                .append(month+1).append("/").append(year));
    }

    private void showTimeEnd(int hour, int minute, int day, int month, int year) {
        String hourText = ""+hour;
        String minuteText = ""+minute;
        if (hour<10) {
            hourText = "0"+hourText;
        }
        if (minute<10) {
            minuteText = "0"+minuteText;
        }

        endTimeText.setText(new StringBuilder().append(hourText).append(":").append(minuteText).append(" ").append(day).append("/")
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
        if (id==997) {
            return new TimePickerDialog(this, myStartTimeListener, hour_start, minute_start, true);
        }
        if (id==996) {
            return new TimePickerDialog(this, myEndTimeListener, hour_end, minute_end, true);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myStartDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    year_start = arg1;
                    month_start = arg2;
                    day_start = arg3;
                    showTimeStart(hour_start, minute_start, arg1, arg2+1, arg3);
                }
            };

    private DatePickerDialog.OnDateSetListener myEndDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    year_end = arg1;
                    month_end = arg2;
                    day_end = arg3;
                    showTimeEnd(hour_end, minute_end, arg1, arg2+1, arg3);
                }
            };

    private TimePickerDialog.OnTimeSetListener myStartTimeListener = new
            TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker arg0,
                                      int arg1, int arg2) {
                    hour_start = arg1;
                    minute_start = arg2;
                    showDialog(999);
                }
            };

    private TimePickerDialog.OnTimeSetListener myEndTimeListener = new
            TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker arg0,
                                      int arg1, int arg2) {
                    hour_end = arg1;
                    minute_end = arg2;
                    showDialog(998);
                }
            };

    private void createScheduleItem(String loggedInUser,String title,Date startTime,Date endTime  ){
        ScheduleItem scheduleItem = new ScheduleItem();
        scheduleItem.setTitle(title);
        startTime.setMonth(startTime.getMonth()+1);
        scheduleItem.setStartTime(startTime);
        endTime.setMonth(endTime.getMonth()+1);
        scheduleItem.setEndTime(endTime);
        scheduleItem.setColor(color);

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
                        Toast.makeText(AddEventActivity.this, "event created", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject s) {
                        System.out.println(s+" "+e);
                    }
                }
        );
    }

    public Date getStartTime(){
        int hours = hour_start;
        int minutes = hour_start;
        int year = year_start;
        int month = month_start;
        int day = day_start;

        System.out.println("start: "+day+"/"+month+"/"+year);
        Date start = new Date(year, month, day, hours, minutes);

        return start;
    }

    public Date getEndTime(){

        int hours = hour_end;
        int minutes = minute_end;
        int year = year_end;
        int month = month_end;
        int day = day_end;

        System.out.println("end: "+day+"/"+month+"/"+year);
        Date end = new Date(year, month, day, hours, minutes);

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
