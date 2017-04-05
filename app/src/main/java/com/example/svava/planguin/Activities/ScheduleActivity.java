package com.example.svava.planguin.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.alamkanak.weekview.WeekView;
import com.example.svava.planguin.Entities.Group;
import com.example.svava.planguin.Entities.Schedule;
import com.example.svava.planguin.Entities.ScheduleItem;
import com.example.svava.planguin.Entities.User;
import com.example.svava.planguin.Managers.ScheduleManager;
import com.example.svava.planguin.R;
import com.example.svava.planguin.Activities.ToolbarActivity;
import com.example.svava.planguin.Utils.JSONparser;
import com.example.svava.planguin.Utils.PlanguinRestClient;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import com.alamkanak.weekview.MonthLoader;
//import com.alamkanak.weekview.R;
import com.alamkanak.weekview.DateTimeInterpreter;
import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ScheduleActivity extends AppCompatActivity implements MonthLoader.MonthChangeListener {

    ScheduleManager scheduleManager;
    JSONparser jsonparser = new JSONparser();

    private Button AddEventButton;



    private static final int TYPE_DAY_VIEW = 1;
    private static final int TYPE_THREE_DAY_VIEW = 2;
    private static final int TYPE_WEEK_VIEW = 3;
    private int mWeekViewType = TYPE_THREE_DAY_VIEW;
    protected WeekView mWeekView;
    WeekView.EventClickListener mEventClickListener;
    WeekView.EventLongPressListener mEventLongPressListener;
    List<WeekViewEvent> allEvents;
    String loggedInUser;


    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ScheduleActivity.this);
        loggedInUser = sharedPreferences.getString("username","");
        System.out.println("í schedule: "+loggedInUser);

        if(loggedInUser.isEmpty()){
            Intent i = new Intent(ScheduleActivity.this, WelcomeActivity.class);
            startActivity(i);
        }

        scheduleManager = new ScheduleManager();

        allEvents = new ArrayList<>();

        // Get a reference for the week view in the layout.
        mWeekView = (WeekView) findViewById(R.id.weekView);

        // Set an action when any event is clicked.
        mWeekView.setOnEventClickListener(mEventClickListener);

        // The week view has infinite scrolling horizontally. We have to provide the events of a
        // month every time the month changes on the week view.
        mWeekView.setMonthChangeListener(this);

        // Set long press listener for events.
        mWeekView.setEventLongPressListener(mEventLongPressListener);

        AddEventButton = (Button) findViewById(R.id.add_event_button);
        AddEventButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(ScheduleActivity.this, AddEventActivity.class);
                startActivity(i);
                overridePendingTransition(0, 0);
            }
        });

        PlanguinRestClient.get("home/"+4+"/"+2017+"?loggedInUser="+loggedInUser,new RequestParams(), new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonSchedule) {
                try {
                    allEvents.clear();
                    Schedule schedule = jsonparser.parseSchedule(jsonSchedule);
                    List<ScheduleItem> scheduleItems = schedule.getItems();
                    for (int i = 0; i < scheduleItems.size(); i++) {
                        WeekViewEvent event = scheduleManager.parseItemToEvent(scheduleItems.get(i));
                        allEvents.add(event);
                    }
                    mWeekView.notifyDatasetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {
        // Populate the week view with some events
        List<WeekViewEvent> events = new ArrayList<>();

        for (int i = 0; i < allEvents.size(); i++) {
            if((allEvents.get(i).getStartTime().get(Calendar.MONTH) == newMonth)&&(allEvents.get(i).getStartTime().get(Calendar.YEAR) == newYear)) {
                events.add(allEvents.get(i));
            }
        }
        for(int i=0; i<events.size(); i++) {
            System.out.println(events.get(i).getName());
        }

        return events;
     }

    private static final String EXTRA_SCHEDULE_BUTTON = "scheduleButton";

    public static Intent newIntent(Context packageContext, boolean scheduleButton) {
        Intent i = new Intent(packageContext, ScheduleActivity.class);
        i.putExtra(EXTRA_SCHEDULE_BUTTON, scheduleButton);
        return i;
    }

    public void onClick(View v) {

        Intent i;
        int id = v.getId();

        switch(id){
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
