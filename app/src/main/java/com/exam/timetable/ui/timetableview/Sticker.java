package com.exam.timetable.ui.timetableview;

import android.view.View;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;

public class Sticker implements Serializable {
    private ArrayList<View> view;
    private ArrayList<Schedule> schedules;

    public Sticker() {
        this.view = new ArrayList<View>();
        this.schedules = new ArrayList<Schedule>();
    }

    public void addTextView(View v){
        view.add(v);
    }

    public void addSchedule(Schedule schedule){
        schedules.add(schedule);
    }

    public ArrayList<View> getView() {
        return view;
    }

    public ArrayList<Schedule> getSchedules() {
        return schedules;
    }
}
