package com.example.yzbkaka.things.db;

import android.util.Log;

import org.litepal.crud.LitePalSupport;
import java.util.Date;

public class UserRecord extends LitePalSupport {
    int id;
    private String category;
    private String actionDescription;
    private double duration;
    String year;
    String month;
    String Day;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getActionDescription() {
        return actionDescription;
    }

    public void setActionDescription(String actionDescription) {
        this.actionDescription = actionDescription;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDay() {
        return Day;
    }

    public void setDay(String Day) {
        this.Day = Day;
    }

}
