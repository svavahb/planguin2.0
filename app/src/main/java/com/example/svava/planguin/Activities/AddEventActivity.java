package com.example.svava.planguin.Activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.annotation.ColorInt;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.pes.androidmaterialcolorpickerdialog.ColorPicker;

import com.example.svava.planguin.Entities.Date;
import com.example.svava.planguin.Entities.ScheduleItem;
import com.example.svava.planguin.Managers.ScheduleManager;
import com.example.svava.planguin.R;
import com.example.svava.planguin.Utils.PlanguinRestClient;
import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.pes.androidmaterialcolorpickerdialog.ColorPickerCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.StringTokenizer;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

public class AddEventActivity extends AppCompatActivity {

    public Button startTimeButton;
    public Button endTimeButton;
    public Button colorPickButton;
    public Button addEventButton;

    public TextView startTimeText;
    public TextView endTimeText;
    public EditText filtersText;

    private Spinner FiltersSpinner;

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
    boolean isEdit;
    int eventId;
    int editColor;
    String editTitle;
    String editDescription;
    String checkboxUrl = "createItem";

    List<String> filters = new ArrayList<>();

    EditText myEventName;
    EditText myEventDesc;
    String  stringEventName;
    String stringEventDescription;
    String loggedInUser;
    ScheduleManager scheduleManager;
    CheckBox checkboxRepeat;


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

        isEdit = getIntent().getBooleanExtra("isEdit",false);
        eventId = getIntent().getIntExtra("eventId",0);
        editColor = getIntent().getIntExtra("color",0);
        editTitle = getIntent().getStringExtra("title");
        editDescription = getIntent().getStringExtra("description");

        colorPickButton = (Button) findViewById(R.id.color_pick_button);

        checkboxRepeat =(CheckBox) findViewById(R.id.repeat_checkBox);

        addEventButton = (Button) findViewById(R.id.submitevent_button);
        addEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the filters, trim spaces
                String filtersString = filtersText.getText().toString();
                StringTokenizer tokenizer = new StringTokenizer(filtersString, ",");
                while (tokenizer.hasMoreTokens()){
                    filters.add(tokenizer.nextToken().trim());
                }

                if(checkboxRepeat.isChecked()){
                    checkboxUrl = "createMultipleItems";
                    System.out.println("checked");
                }
                else{
                    checkboxUrl = "createItem";
                    System.out.println("Not checked");
                }

                if (validateItem()) {
                    stringEventName = myEventName.getText().toString();
                    if(isEdit) {
                        System.out.println("HALLOOOOO");
                        editScheduleItem(loggedInUser, stringEventName, getStartTime(), getEndTime(), eventId);
                    }
                    else {
                        createScheduleItem(loggedInUser, stringEventName, getStartTime(), getEndTime(),checkboxUrl);
                    }
                } else {
                    myEventName.setError("End time must be after start time!");
                }
            }
        });

        // Get start time from intent
        long startMillis = getIntent().getLongExtra("startTime",0);

        // Get end time from intent
        long endMillis = getIntent().getLongExtra("endTime",0);

        filtersText = (EditText) findViewById(R.id.filters_text);

        scheduleManager = new ScheduleManager();

        // Find buttons and text views
        myEventName =(EditText)findViewById(R.id.eventName);
        myEventDesc =(EditText)findViewById(R.id.eventDesc);

        startTimeButton = (Button) findViewById(R.id.startTime_button);
        startTimeText = (TextView) findViewById(R.id.startTime_text);

        endTimeButton = (Button) findViewById(R.id.endTime_button);
        endTimeText = (TextView) findViewById(R.id.endTime_text);




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

        int red = 250;
        int green = 250;
        int blue = 250;

        if(isEdit) {
            System.out.println("HALLÓ");
            myEventName.setText(editTitle);
            //myEventDescription.setText(editDescription);
            addEventButton.setText(R.string.edit_event_button);
            red = Color.red(editColor);
            green = Color.green(editColor);
            blue = Color.blue(editColor);
        }

        final ColorPicker cp = new ColorPicker(AddEventActivity.this, red, green, blue);

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

        FiltersSpinner = (Spinner) findViewById(R.id.filter_spinner_add);
        getFilters(loggedInUser);

        FiltersSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selItem = parent.getItemAtPosition(position).toString();
                if(!selItem.equals("")) {
                    String text = filtersText.getText().toString();
                    if (text.equals("")) {
                        filtersText.setText(new StringBuilder().append(selItem));
                    }
                    else {
                        filtersText.setText(new StringBuilder().append(text).append(",").append(selItem));
                    }

                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // do nothing
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
                    showTimeStart(hour_start, minute_start, arg3, arg2, arg1);
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
                    showTimeEnd(hour_end, minute_end, arg3, arg2, arg1);
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


    public void getFilters(String loggedInUser) {
        PlanguinRestClient.get("getFilters/"+loggedInUser, new RequestParams(), new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                List<String> filters = new ArrayList<>();
                filters.add("");
                for (int i=0; i<json.length(); i++) {
                    if(!filters.contains(json.optString(i))) {
                        filters.add(json.optString(i));
                    }
                }
                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(AddEventActivity.this, android.R.layout.simple_spinner_item, filters);
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                FiltersSpinner.setAdapter(spinnerAdapter);
                spinnerAdapter.notifyDataSetChanged();
            }
        });
    }

    private void editScheduleItem(String loggedInUser, String title, Date startTime, Date endTime, int id) {
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

        PlanguinRestClient.post("schedule/edit/"+id,se, "application/json;charset=UTF-8", new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject result){
                        System.out.println(result);

                        Intent intent = new Intent(AddEventActivity.this, ScheduleActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        Toast.makeText(AddEventActivity.this, "event edited!", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject s) {
                        System.out.println(s+" "+e);
                    }
                }
        );
    }

    private void createScheduleItem(String loggedInUser,String title,Date startTime,Date endTime ,String url ){
        ScheduleItem scheduleItem = new ScheduleItem();
        scheduleItem.setTitle(title);
        startTime.setMonth(startTime.getMonth()+1);
        scheduleItem.setStartTime(startTime);
        endTime.setMonth(endTime.getMonth()+1);
        scheduleItem.setEndTime(endTime);
        scheduleItem.setColor(color);
        scheduleItem.setFilters(filters);
        stringEventDescription = myEventDesc.getText().toString();
        scheduleItem.setDescription(stringEventDescription);

        Gson gson = new Gson();
        String json = gson.toJson(scheduleItem);

        StringEntity se = new StringEntity(json, "UTF-8");
        se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json;charset=UTF-8"));

        PlanguinRestClient.post(url+"/"+loggedInUser,se, "application/json;charset=UTF-8", new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject result){
                        System.out.println(result);

                        Intent intent = new Intent(AddEventActivity.this, ScheduleActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        Toast.makeText(AddEventActivity.this, "event created!", Toast.LENGTH_SHORT).show();
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

    public boolean validateItem() {
        if (year_end<year_start) return false;
        else if (month_end<month_start) return false;
        else if (day_end<day_start) return false;
        else if (hour_end<hour_start) return false;
        else if (minute_end<minute_start) return false;
        else return true;
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
