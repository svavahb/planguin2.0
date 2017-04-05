package com.example.svava.planguin.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.example.svava.planguin.Entities.Schedule;
import com.example.svava.planguin.Entities.ScheduleItem;
import com.example.svava.planguin.Managers.CompareManager;
import com.example.svava.planguin.Managers.ScheduleManager;
import com.example.svava.planguin.R;
import com.example.svava.planguin.Utils.JSONparser;
import com.example.svava.planguin.Utils.PlanguinRestClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class CompareActivity extends AppCompatActivity implements MonthLoader.MonthChangeListener {

    CompareManager compareManager = new CompareManager();
    JSONparser jsonparser = new JSONparser();

    private static final int TYPE_DAY_VIEW = 1;
    private static final int TYPE_THREE_DAY_VIEW = 2;
    private static final int TYPE_WEEK_VIEW = 3;
    private int mWeekViewType = TYPE_THREE_DAY_VIEW;
    protected WeekView mWeekView;
    WeekView.EventClickListener mEventClickListener;
    WeekView.EventLongPressListener mEventLongPressListener;
    List<WeekViewEvent> allEvents;
    String loggedInUser;

    Spinner friendSpin;
    Spinner groupSpin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare);

        // get the logged in user's name
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(CompareActivity.this);
        loggedInUser = sharedPreferences.getString("username","");

        if(loggedInUser.isEmpty()){
            Intent i = new Intent(CompareActivity.this, WelcomeActivity.class);
            startActivity(i);
        }



        // Get the spinners
        friendSpin = (Spinner) findViewById(R.id.spinner_friend);
        groupSpin = (Spinner) findViewById(R.id.spinner_group);

        // Get buttons and set onClick listeners
        Button compareFriend = (Button) findViewById(R.id.compare_friend_button);
        compareFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allEvents.clear();
                mWeekView.notifyDatasetChanged();
                onCompareFriend(loggedInUser, friendSpin.getSelectedItem().toString());
            }
        });

        Button compareGroup = (Button) findViewById(R.id.compare_group_button);
        compareGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCompareGroup(loggedInUser, groupSpin.getSelectedItem().toString());
            }
        });

        allEvents = new ArrayList<>();

        // Get a reference for the week view in the layout.
        mWeekView = (WeekView) findViewById(R.id.weekView_compare);

        // Set an action when any event is clicked.
        mWeekView.setOnEventClickListener(mEventClickListener);

        // The week view has infinite scrolling horizontally. We have to provide the events of a
        // month every time the month changes on the week view.
        mWeekView.setMonthChangeListener(this);

        // Set long press listener for events.
        mWeekView.setEventLongPressListener(mEventLongPressListener);

        // Get a list of the user's friends and update the friend spinner
        PlanguinRestClient.get("getFriends/"+loggedInUser, new RequestParams(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray jsonfriends){
                List<String> friends = new ArrayList<String>();
                for(int i=0; i<jsonfriends.length(); i++) {
                    String friend = jsonfriends.optString(i);
                    friends.add(friend);
                }
                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(CompareActivity.this, android.R.layout.simple_spinner_item, friends);
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                friendSpin.setAdapter(spinnerAdapter);
                spinnerAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONArray errorResponse) {
                System.out.println(statusCode+" "+e);
            }
        });

        // Get a list of all the user's groups and update the group spinner
        PlanguinRestClient.get("getGroups/"+loggedInUser, new RequestParams(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray jsongroups){
                List<String> groups = new ArrayList<String>();
                for(int i=0; i<jsongroups.length(); i++) {
                    try {
                        String group = jsongroups.getJSONObject(i).optString("grpName");
                        groups.add(group);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(CompareActivity.this, android.R.layout.simple_spinner_item, groups);
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                groupSpin.setAdapter(spinnerAdapter);
                spinnerAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONArray errorResponse) {
                System.out.println(statusCode+" "+e);
            }
        });
    }


    public void onCompareFriend(String loggedInUser, String friend) {
        PlanguinRestClient.get("compareFriends/"+loggedInUser+"/"+friend, new RequestParams(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonresult) {
                allEvents.clear();
                try {
                    Schedule schedule = jsonparser.parseSchedule(jsonresult);
                    List<ScheduleItem> items = schedule.getItems();
                    for(int i=0; i<items.size(); i++) {
                        WeekViewEvent event = compareManager.parseItemToEvent(items.get(i));
                        allEvents.add(event);
                    }
                    mWeekView.notifyDatasetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject jsonerror) {
                System.out.println(e+" "+jsonerror);
            }
        });
    }


    public void onCompareGroup(String loggedInUser, String groupname) {
        PlanguinRestClient.get("compareGroup/"+loggedInUser+"/"+groupname, new RequestParams(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonresult) {
                allEvents.clear();
                try {
                    Schedule schedule = jsonparser.parseSchedule(jsonresult);
                    List<ScheduleItem> items = schedule.getItems();
                    for(int i=0; i<items.size(); i++) {
                        WeekViewEvent event = compareManager.parseItemToEvent(items.get(i));
                        allEvents.add(event);
                    }
                    mWeekView.notifyDatasetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject jsonerror) {
                System.out.println(e+" "+jsonerror);
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
                Calendar start = allEvents.get(i).getStartTime();
            }
        }

        return events;

    }

    private static final String EXTRA_COMPARE_BUTTON = "compareButton";

    public static Intent newIntent(Context packageContext, boolean compareButton){
        Intent i = new Intent(packageContext, CompareActivity.class);
        i.putExtra(EXTRA_COMPARE_BUTTON, compareButton);
        return i;
    }

    public void onClick(View v) {

        Intent i;
        int id = v.getId();

        switch(id){
            case R.id.schedule_button:
                i = new Intent(this, ScheduleActivity.class);
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
            case R.id.find_friends_button:
                i = new Intent(this, SearchActivity.class);
                startActivity(i);
                overridePendingTransition(0, 0);
                break;
            default:
                i = new Intent(this, CompareActivity.class);
                startActivity(i);
                break;
        }
    }
}
