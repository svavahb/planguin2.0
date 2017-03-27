package com.example.svava.planguin.Utils;

import com.example.svava.planguin.Entities.Group;
import com.example.svava.planguin.Entities.Schedule;
import com.example.svava.planguin.Entities.ScheduleItem;
import com.example.svava.planguin.Entities.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Svava on 27.03.17.
 */

public class JSONparser {

    public static User parseUser(JSONObject json) throws JSONException {
        User user = new User();
        JSONObject userjson = json.getJSONObject("user");
        user.setUserId(userjson.optInt("userId"));
        user.setUsername(userjson.optString("username"));
        user.setPassword(userjson.optString("password"));
        user.setPhoto(userjson.optString("photo"));
        user.setSchool(userjson.optString("school"));

        return user;
    }

    public static Schedule parseSchedule(JSONObject jsonSched) throws JSONException {
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

    public static ScheduleItem parseItem(JSONObject jsonitem) {
        ScheduleItem item = new ScheduleItem();

        // Parse string and int fields
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

        // Parse tagged users
        List<String> tagged = new ArrayList<>();
        JSONArray jsontagged = jsonitem.optJSONArray("taggedUsers");
        for(int i=0; i<jsontagged.length(); i++) {
            tagged.add(jsontagged.optString(i));
        }
        item.setTaggedUsers(tagged);

        // PARSA RESTINA
        // dates: starttime, endtime
        // array: taggedusers

        return item;
    }

    public static Group parseGroup(JSONObject jsongroup) {
        Group group = new Group();

        group.setGrpId(jsongroup.optInt("grpId"));

        // Parse member list
        JSONArray jsonmembers = jsongroup.optJSONArray("members");
        for(int i=0; i<jsonmembers.length(); i++) {
            group.addMember(jsonmembers.optString(i));
        }
        return group;
    }
}
