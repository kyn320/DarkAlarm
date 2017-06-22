package com.example.com.darckalram;


/**
 * Created by com on 2017-05-09.
 */

//리스트뷰 아이템 클래스
public class ListViewItem {
    private int ID;
    private String sun, name, days;
    private  int hour,min;
    private boolean isWork;
    private boolean[] weeks;

    public String getSun() {
        return sun;
    }

    public void setSun(String sun) {
        this.sun = sun;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int _hour) {
        hour = _hour;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int _min) {
        min = _min;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public boolean getWork() {
        return isWork;
    }

    public void setWork(boolean work) {
        isWork = work;
    }

    public int getID() {
        return ID;
    }

    public void setID(int id) {
        ID = id;
    }

    public void setWeeks(boolean[] week) {
        weeks = week;
    }

    public boolean[] getWeeks() {
        return weeks;
    }

    public boolean getWeeks(int day) {
        if (weeks != null
                && weeks.length - 1 >= day)
            return weeks[day];
        else
            return false;
    }

}
