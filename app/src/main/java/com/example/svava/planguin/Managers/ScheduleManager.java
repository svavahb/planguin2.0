package com.example.svava.planguin.Managers;

import com.alamkanak.weekview.WeekViewEvent;
import com.example.svava.planguin.Entities.Schedule;
import com.example.svava.planguin.Entities.ScheduleItem;
import com.example.svava.planguin.Entities.User;

import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
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
        startTime.set(Calendar.MONTH, item.getStartTime().getMonth()-1);
        startTime.set(Calendar.DAY_OF_MONTH, item.getStartTime().getDayOfMonth());
        startTime.set(Calendar.HOUR_OF_DAY, item.getStartTime().getHour());
        startTime.set(Calendar.MINUTE, item.getStartTime().getMinute());

        Calendar endTime = (Calendar) startTime.clone();
        endTime.set(Calendar.YEAR, item.getEndTime().getYear());
        endTime.set(Calendar.MONTH, item.getEndTime().getMonth()-1);
        endTime.set(Calendar.DAY_OF_MONTH, item.getEndTime().getDayOfMonth());
        endTime.set(Calendar.HOUR_OF_DAY, item.getEndTime().getHour());
        endTime.set(Calendar.MINUTE, item.getEndTime().getMinute());

        WeekViewEvent event = new WeekViewEvent(item.getId(), item.getTitle(), startTime, endTime);
        event.setColor(item.getColor());

        return event;
    }

    public LocalDateTime parseTimeToDateTime(int startHour, int startMinute, int year, int month, int dayOfMonth){
        LocalDateTime newLocalDateTime = new LocalDateTime(year, month, dayOfMonth, startHour, startMinute);
        return newLocalDateTime;
    }


}
