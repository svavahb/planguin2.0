package com.example.svava.planguin.Activities;

import android.content.Context;
import android.content.Intent;
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
    JSONparser jsonparser;

    private Button InvitationButton;
    private Button ScheduleButton;
    private Button CompareButton;
    private Button FriendListButton;
    private Button GroupListButton;
    private Button ProfileButton;

    boolean invitationButton;
    boolean scheduleButton;
    boolean compareButton;
    boolean friendlistButton;
    boolean groupListButton;
    boolean profileButton;

    private static final int TYPE_DAY_VIEW = 1;
    private static final int TYPE_THREE_DAY_VIEW = 2;
    private static final int TYPE_WEEK_VIEW = 3;
    private int mWeekViewType = TYPE_THREE_DAY_VIEW;
    protected WeekView mWeekView;
    WeekView.EventClickListener mEventClickListener;
    WeekView.EventLongPressListener mEventLongPressListener;
    List<WeekViewEvent> allEvents;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

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


        PlanguinRestClient.get("home?loggedInUser=svava",new RequestParams(), new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonSchedule) {
                try {
                    Schedule schedule = jsonparser.parseSchedule(jsonSchedule);
                    List<ScheduleItem> scheduleItems = schedule.getItems();
                    for (int i = 0; i < scheduleItems.size(); i++) {
                        System.out.println(scheduleItems.get(i));

                    }
                    allEvents.clear();
                    Calendar startTime = Calendar.getInstance();
                    startTime.set(Calendar.HOUR_OF_DAY, 5);
                    startTime.set(Calendar.MINUTE, 0);
                    /*startTime.set(Calendar.DAY_OF_MONTH, 3);
                    startTime.set(Calendar.MONTH, 4);
                    startTime.set(Calendar.YEAR, 2017);
                    startTime.add(Calendar.DATE, 1);*/
                    Calendar endTime = (Calendar) startTime.clone();
                    endTime.add(Calendar.HOUR_OF_DAY, 6);
                    //endTime.set(Calendar.MONTH, 3);
                    WeekViewEvent event = new WeekViewEvent(3, "test0", startTime, endTime);
                    allEvents.add(event);
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
            System.out.println(allEvents.get(i).getStartTime().get(Calendar.MONTH)+" "+newMonth);
            if((allEvents.get(i).getStartTime().get(Calendar.MONTH) == newMonth)&&(allEvents.get(i).getStartTime().get(Calendar.YEAR) == newYear)) {
                events.add(allEvents.get(i));
            }
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

        /*InvitationButton = (Button) findViewById(R.id.invitation_button);
        InvitationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(ScheduleActivity.this, "Button Clicked!", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(ScheduleActivity.this, InvitationActivity.class);

                i.putExtra("activity_invitation_button", invitationButton);
                startActivity(i);
            }
        });

        ScheduleButton = (Button) findViewById(R.id.schedule_button);
        ScheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ScheduleActivity.this, ScheduleActivity.class);

                i.putExtra("activity_schedule_button", scheduleButton);
                startActivity(i);
            }
        });

        CompareButton = (Button) findViewById(R.id.compare_button);
        CompareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ScheduleActivity.this, CompareActivity.class);

                i.putExtra("activity_compare_button", compareButton);
                startActivity(i);
            }
        });

        FriendListButton = (Button) findViewById(R.id.friendlist_button);
        FriendListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ScheduleActivity.this, FriendListActivity.class);

                i.putExtra("activity_friendlist_button", friendlistButton);
                startActivity(i);
            }
        });

        GroupListButton = (Button) findViewById(R.id.grouplist_button);
        GroupListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ScheduleActivity.this, GroupListActivity.class);

                i.putExtra("activity_grouplist_button", groupListButton);
                startActivity(i);
            }
        });

        /*ProfileButton = (Button) findViewById(R.id.profile_button);
        ProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ScheduleActivity.this, ProfileActivity.class);

                i.putExtra("activity_profile_button", profileButton);
                startActivity(i);
            }
        });*/
