package com.exam.timetable.ui.timetableview;

import com.exam.timetable.model.data.InteractionData;
import com.exam.timetable.model.data.MemoDBInfo;
import com.exam.timetable.model.data.MemoData;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

public class Schedule implements Serializable {
    static final int MON = 0;
    static final int TUE = 1;
    static final int WED = 2;
    static final int THU = 3;
    static final int FRI = 4;
    static final int SAT = 5;
    static final int SUN = 6;

    String classTitle="";
    String classPlace="";
    String professorName="";
    private int day = 0;
    private Time startTime;
    private Time endTime;
    private InteractionData interactionData;
    private ArrayList<MemoDBInfo> memoDatas;
    private String key;
    private String color;
    public Schedule() {
        this.startTime = new Time();
        this.endTime = new Time();

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Schedule schedule = (Schedule) o;
        return day == schedule.day &&
                classTitle.equals(schedule.classTitle) &&
                classPlace.equals(schedule.classPlace) &&
                professorName.equals(schedule.professorName) &&
                startTime.equals(schedule.startTime) &&
                endTime.equals(schedule.endTime) &&
                interactionData.equals(schedule.interactionData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(classTitle, classPlace, professorName, day, startTime, endTime, interactionData);
    }

    public void setMemoDatas(ArrayList<MemoDBInfo> memoDatas) { this.memoDatas = memoDatas; }

    public ArrayList<MemoDBInfo> getMemoDatas() { return memoDatas; }

    public void setKey(String key) { this.key = key ;}

    public String getKey() { return key; }

    public void setColor(String color) { this.color = color ;}

    public String getColor() { return color; }


    public InteractionData getInteractionData() { return interactionData; }

    public void setInteractionData(InteractionData interactionData) { this.interactionData = interactionData; }

    public String getProfessorName() {
        return professorName;
    }

    public void setProfessorName(String professorName) {
        this.professorName = professorName;
    }

    public String getClassTitle() {
        return classTitle;
    }

    public void setClassTitle(String classTitle) {
        this.classTitle = classTitle;
    }

    public String getClassPlace() {
        return classPlace;
    }

    public void setClassPlace(String classPlace) {
        this.classPlace = classPlace;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public Time getStartTime() {
        return startTime;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public Time getEndTime() {
        return endTime;
    }

    public void setEndTime(Time endTime) {
        this.endTime = endTime;
    }
}
