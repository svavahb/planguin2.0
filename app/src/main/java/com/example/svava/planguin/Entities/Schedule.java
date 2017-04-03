package com.example.svava.planguin.Entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Svava on 14.03.17.
 */

public class Schedule {

    private User user;
    private List<ScheduleItem> items = new ArrayList<>();

    public List<ScheduleItem> getItems(){return items;}
    public void addItem(ScheduleItem item){items.add(item);}
    public void removeItem(ScheduleItem item){items.remove(item);}

    public void setUser(User user){this.user=user;}
    public User getUser() {return user;}
}
