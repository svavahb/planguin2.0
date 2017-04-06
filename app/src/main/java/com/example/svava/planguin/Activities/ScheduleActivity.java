package com.example.svava.planguin.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.RectF;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Spinner;
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

import static com.example.svava.planguin.R.string.filters;

public class ScheduleActivity extends AppCompatActivity implements MonthLoader.MonthChangeListener {

    ScheduleManager scheduleManager;
    JSONparser jsonparser = new JSONparser();

    private Button AddEventButton;
    private Button FilterButton;
    private Spinner FiltersSpinner;


    private static final int TYPE_DAY_VIEW = 1;
    private static final int TYPE_THREE_DAY_VIEW = 2;
    private static final int TYPE_WEEK_VIEW = 3;
    private int mWeekViewType = TYPE_THREE_DAY_VIEW;
    List<ScheduleItem> scheduleItems;
    protected WeekView mWeekView;
    WeekView.EventClickListener mEventClickListener;
    WeekView.EventLongPressListener mEventLongPressListener;
    List<WeekViewEvent> allEvents;
    String loggedInUser;
    String currentFilter;


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
        mWeekView.goToHour(7);

        // Set action when new event is added
        mWeekView.setAddEventClickListener(new WeekView.AddEventClickListener() {
            @Override
            public void onAddEventClicked(Calendar startTime, Calendar endTime) {
                Intent i = new Intent(ScheduleActivity.this, AddEventActivity.class);
                i.putExtra("startTime",startTime.getTimeInMillis());
                i.putExtra("endTime", endTime.getTimeInMillis());
                startActivity(i);
                overridePendingTransition(0, 0);
            }
        });

        // Set an action when any event is clicked.
        mWeekView.setOnEventClickListener(mEventClickListener);

        // The week view has infinite scrolling horizontally. We have to provide the events of a
        // month every time the month changes on the week view.
        mWeekView.setMonthChangeListener(this);

        // Set long press listener for events.
        mWeekView.setEventLongPressListener(new WeekView.EventLongPressListener() {
            @Override
            public void onEventLongPress(final WeekViewEvent event, RectF eventRect) {
                new AlertDialog.Builder(ScheduleActivity.this)
                        .setTitle(event.getName())
                        .setMessage("Do you want to edit or delete your item?")
                        .setPositiveButton(R.string.delete_event, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                onDeleteEvent(event);
                                allEvents.remove(event);
                            }
                        })
                        .setNegativeButton(R.string.edit_event, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO edit event??
                                Intent i = new Intent(ScheduleActivity.this, AddEventActivity.class);
                                i.putExtra("isEdit",true);
                                i.putExtra("eventId",event.getId());
                                i.putExtra("startTime",event.getStartTime().getTimeInMillis());
                                i.putExtra("endTime",event.getEndTime().getTimeInMillis());
                                i.putExtra("title",event.getName());
                                i.putExtra("description",event.getLocation());
                                i.putExtra("color",event.getColor());
                                startActivity(i);
                                overridePendingTransition(0,0);
                            }
                        })
                        .setNeutralButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .show();
            }
        });

        AddEventButton = (Button) findViewById(R.id.add_event_button);
        AddEventButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(ScheduleActivity.this, AddEventActivity.class);
                startActivity(i);
                overridePendingTransition(0, 0);
            }
        });

        currentFilter = "no filter";

        FilterButton = (Button) findViewById(R.id.filter_button);
        FilterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentFilter = FiltersSpinner.getSelectedItem().toString();
            }
        });

        FiltersSpinner = (Spinner) findViewById(R.id.filter_spinner);
        getFilters(loggedInUser);

        Calendar now = Calendar.getInstance();
        onLoadEvents(now.get(Calendar.MONTH)+1,now.get(Calendar.YEAR));
    }

    @Override
    public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {
        // Populate the week view with some events
        if(allEvents.size()!=0) {
            if(!containsEvents(allEvents, newYear, newMonth)) {
                onLoadEvents(newMonth, newYear);
            }
        }

        List<WeekViewEvent> events = new ArrayList<>();

        for (int i = 0; i < allEvents.size(); i++) {
            if((allEvents.get(i).getStartTime().get(Calendar.MONTH) == newMonth)&&(allEvents.get(i).getStartTime().get(Calendar.YEAR) == newYear)) {
                events.add(allEvents.get(i));
            }
        }
        return events;
     }

     public boolean containsEvents(List<WeekViewEvent> events, int year, int month) {
         Calendar time = events.get(0).getStartTime();
         boolean returnvalue = false;

         if(time.get(Calendar.YEAR)==year && time.get(Calendar.MONTH)==month) {
             returnvalue = true;
         }
         else if (time.get(Calendar.YEAR)==year && time.get(Calendar.MONTH)+1==month) {
             returnvalue = true;
         }
         else if (time.get(Calendar.YEAR)==year && time.get(Calendar.MONTH)-1==month) {
             returnvalue = true;
         }
         return returnvalue;
     }

     // Loads events of month, and of the months before and after
     public void onLoadEvents(int month, int year) {
         PlanguinRestClient.get("home/"+month+"/"+year+"?loggedInUser="+loggedInUser,new RequestParams(), new JsonHttpResponseHandler(){
             @Override
             public void onSuccess(int statusCode, Header[] headers, JSONObject jsonSchedule) {
                 try {
                     allEvents.clear();
                     Schedule schedule = jsonparser.parseSchedule(jsonSchedule);
                     scheduleItems = schedule.getItems();
                     for (int i = 0; i < scheduleItems.size(); i++) {
                         if(currentFilter.equals("no filter")) {
                             WeekViewEvent event = scheduleManager.parseItemToEvent(scheduleItems.get(i));
                             allEvents.add(event);
                         }
                         else if (scheduleItems.get(i).getFilters().contains(currentFilter)) {
                             WeekViewEvent event = scheduleManager.parseItemToEvent(scheduleItems.get(i));
                             allEvents.add(event);
                         }
                     }
                     mWeekView.notifyDatasetChanged();

                 } catch (JSONException e) {
                     e.printStackTrace();
                 }
             }
         });
     }

    public void onDeleteEvent(final WeekViewEvent event) {
        PlanguinRestClient.get("deleteItem?itemId="+event.getId(), new RequestParams(), new JsonHttpResponseHandler(){
             @Override
             public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                 System.out.println("success?");
                 allEvents.remove(event);
                 Toast.makeText(ScheduleActivity.this, "event deleted!", Toast.LENGTH_SHORT).show();
                 mWeekView.notifyDatasetChanged();
             }
         });
    }

    public void getFilters(String loggedInUser) {
        PlanguinRestClient.get("getFilters/"+loggedInUser, new RequestParams(), new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                List<String> filters = new ArrayList<>();
                filters.add("no filter");
                for (int i=0; i<json.length(); i++) {
                    if(!filters.contains(json.optString(i))) {
                        filters.add(json.optString(i));
                    }
                }
                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(ScheduleActivity.this, android.R.layout.simple_spinner_item, filters);
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                FiltersSpinner.setAdapter(spinnerAdapter);
                spinnerAdapter.notifyDataSetChanged();
            }
        });
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
            case R.id.find_friends_button:
                i = new Intent(this, SearchActivity.class);
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
