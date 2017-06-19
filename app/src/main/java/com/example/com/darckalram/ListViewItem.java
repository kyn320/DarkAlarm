package com.example.com.darckalram;


/**
 * Created by com on 2017-05-09.
 */

//리스트뷰 아이템 클래스
public class ListViewItem {
    private  int ID;
    private String sun, time, name, days;
    private  boolean isWork;
    private  boolean[] weeks;

    public String getSun() {
        return sun;
    }

    public void setSun(String sun) {
        this.sun = sun;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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

    public void setWeeks(boolean[] week){
        weeks = week;
    }

    public boolean[] getWeeks(){
        return weeks;
    }

}
