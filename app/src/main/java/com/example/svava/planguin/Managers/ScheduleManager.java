package com.example.svava.planguin.Managers;

import com.example.svava.planguin.Entities.Schedule;
import com.example.svava.planguin.Entities.ScheduleItem;
import com.example.svava.planguin.Entities.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Svava on 14.03.17.
 */

public class ScheduleManager {

    public Schedule currentSchedule;

    public static void createItem(){
        //do something
    }

    public static Schedule parseJSONSchedule(JSONObject jsonSched) throws JSONException {
        Schedule schedule = new Schedule();

        //parse the user
        User user = new User();
        JSONObject userjson;
        userjson = jsonSched.getJSONObject("user");
        user.setUserId(userjson.optInt("userId"));
        user.setUsername(userjson.optString("username"));
        user.setPassword(userjson.optString("password"));
        user.setPhoto(userjson.optString("photo"));
        user.setSchool(userjson.optString("school"));

        JSONArray jsonItems = jsonSched.getJSONArray("items");
        // parse the scheduleitems and add to schedule
        for (int i=0; i<jsonItems.length(); i++) {
            schedule.addItem(parseItem(jsonItems.getJSONObject(i)));
        }
        return schedule;
    }

    public static ScheduleItem parseItem(JSONObject jsonitem) {
        ScheduleItem item = new ScheduleItem();

        item.setId(jsonitem.optInt("id"));
        item.setColor(jsonitem.optString("color"));
        item.setDescription(jsonitem.optString("description"));
        item.setLocation(jsonitem.optString("location"));
        item.setTitle(jsonitem.optString("title"));
        item.setUserid(jsonitem.optInt("userid"));
        item.setWeekNo(jsonitem.optInt("weekNo"));
        item.setYear(jsonitem.optInt("year"));

        // Parse filter array
        List<String> filters = new ArrayList<>();
        JSONArray jsonfilters = jsonitem.optJSONArray("filters");
        for(int i=0; i<jsonfilters.length(); i++) {
            filters.add(jsonfilters.optString(i));
        }
        item.setFilters(filters);

        // PARSA RESTINA
        // dates: starttime, endtime
        // array: taggedusers

        return item;
    }
}
