package com.example.svava.planguin.Utils;

import com.example.svava.planguin.Entities.Date;
import com.example.svava.planguin.Entities.Group;
import com.example.svava.planguin.Entities.Schedule;
import com.example.svava.planguin.Entities.ScheduleItem;
import com.example.svava.planguin.Entities.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.*;

/**
 * Created by Svava on 27.03.17.
 */

public class JSONparser {

    public User parseUser(JSONObject json) throws JSONException {
        User user = new User();
        JSONObject userjson = json.optJSONObject("user");
        if (!json.isNull("user")) {
            user.setUserId(userjson.optInt("userId"));
            user.setUsername(userjson.optString("username"));
            user.setPassword(userjson.optString("password"));
            user.setPhoto(userjson.optString("photo"));
            user.setSchool(userjson.optString("school"));
        }
        return user;
    }

    public Schedule parseSchedule(JSONObject jsonSched) throws JSONException {
        Schedule schedule = new Schedule();

        //parse the user
        User user = parseUser(jsonSched.getJSONObject("user"));
        schedule.setUser(user);

        JSONArray jsonItems = jsonSched.getJSONArray("items");
        // parse the scheduleitems and add to schedule
        for (int i=0; i<jsonItems.length(); i++) {
            schedule.addItem(parseItem(jsonItems.getJSONObject(i)));
        }

        return schedule;
    }

    public ScheduleItem parseItem(JSONObject jsonitem) throws JSONException {
        ScheduleItem item = new ScheduleItem();

        // Parse string and int fields
        item.setId(jsonitem.optInt("id"));
        item.setColor(jsonitem.optInt("color"));
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

        // Parse tagged users
        List<String> tagged = new ArrayList<>();
        JSONArray jsontagged = jsonitem.optJSONArray("taggedUsers");
        for(int i=0; i<jsontagged.length(); i++) {
            tagged.add(jsontagged.optString(i));
        }
        item.setTaggedUsers(tagged);

        item.setStartTime(parseDate(jsonitem.getJSONObject("startTime")));
        item.setEndTime((parseDate(jsonitem.getJSONObject("endTime"))));

        return item;
    }

    public Date parseDate(JSONObject jsondate){
        int year = jsondate.optInt("year");
        int month = jsondate.optInt("month");
        int day = jsondate.optInt("dayOfMonth");
        int hour = jsondate.optInt("hour");
        int minute = jsondate.optInt("minute");
        return new Date(year,month,day,hour,minute);
    }

    public Group parseGroup(JSONObject jsongroup) {
        Group group = new Group();

        group.setGrpId(jsongroup.optInt("grpId"));

        group.setGrpName(jsongroup.optString("grpName"));

        // Parse member list
        JSONArray jsonmembers = jsongroup.optJSONArray("members");
        for(int i=0; i<jsonmembers.length(); i++) {
            group.addMember(jsonmembers.optString(i));
        }
        return group;
    }
}
