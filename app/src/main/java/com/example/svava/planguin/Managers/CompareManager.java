package com.example.svava.planguin.Managers;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Shader;

import com.alamkanak.weekview.WeekViewEvent;
import com.example.svava.planguin.Entities.ScheduleItem;
import com.example.svava.planguin.R;

import java.util.Calendar;

/**
 * Created by Svava on 14.03.17.
 */

public class CompareManager {

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

        WeekViewEvent event = new WeekViewEvent(item.getId(), " ", startTime, endTime);

        event.setColor(Color.rgb(245,138,138));

        return event;
    }
}
