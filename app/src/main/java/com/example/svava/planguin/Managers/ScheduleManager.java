package com.example.svava.planguin.Managers;

import com.alamkanak.weekview.WeekViewEvent;
import com.example.svava.planguin.Entities.Schedule;
import com.example.svava.planguin.Entities.ScheduleItem;
import com.example.svava.planguin.Entities.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Svava on 14.03.17.
 */

public class ScheduleManager {

    public WeekViewEvent parseItemToEvent(ScheduleItem item) {

        Calendar startTime = Calendar.getInstance();
        startTime.set(Calendar.YEAR, item.getStartTime().getYear());
        startTime.set(Calendar.MONTH, item.getStartTime().getMonthOfYear()-1);
        startTime.set(Calendar.DAY_OF_MONTH, item.getStartTime().getDayOfMonth());
        startTime.set(Calendar.HOUR_OF_DAY, item.getStartTime().getHourOfDay());
        startTime.set(Calendar.MINUTE, item.getStartTime().getMinuteOfHour());

        Calendar endTime = (Calendar) startTime.clone();
        endTime.set(Calendar.YEAR, item.getEndTime().getYear());
        endTime.set(Calendar.MONTH, item.getEndTime().getMonthOfYear()-1);
        endTime.set(Calendar.DAY_OF_MONTH, item.getEndTime().getDayOfMonth());
        endTime.set(Calendar.HOUR_OF_DAY, item.getEndTime().getHourOfDay());
        endTime.set(Calendar.MINUTE, item.getEndTime().getMinuteOfHour());

        WeekViewEvent event = new WeekViewEvent(item.getId(), item.getTitle(), startTime, endTime);

        return event;
    }
}
