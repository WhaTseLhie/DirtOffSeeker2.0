package com.example.jayvee.dirtoffseeker;

public class MyAvailabilityWeekly {

    String availability_dayOfTheWeek, availability_endTime, availability_key, availability_monthOf, availability_startTime;

    public MyAvailabilityWeekly(String availability_dayOfTheWeek, String availability_endTime, String availability_key, String availability_monthOf, String availability_startTime) {
        this.availability_dayOfTheWeek = availability_dayOfTheWeek;
        this.availability_endTime = availability_endTime;
        this.availability_key = availability_key;
        this.availability_monthOf = availability_monthOf;
        this.availability_startTime = availability_startTime;
    }

    public MyAvailabilityWeekly() {
        // Empty Constructor
    }

    public String getAvailability_dayOfTheWeek() {
        return availability_dayOfTheWeek;
    }

    public void setAvailability_dayOfTheWeek(String availability_dayOfTheWeek) {
        this.availability_dayOfTheWeek = availability_dayOfTheWeek;
    }

    public String getAvailability_endTime() {
        return availability_endTime;
    }

    public void setAvailability_endTime(String availability_endTime) {
        this.availability_endTime = availability_endTime;
    }

    public String getAvailability_key() {
        return availability_key;
    }

    public void setAvailability_key(String availability_key) {
        this.availability_key = availability_key;
    }

    public String getAvailability_monthOf() {
        return availability_monthOf;
    }

    public void setAvailability_monthOf(String availability_monthOf) {
        this.availability_monthOf = availability_monthOf;
    }

    public String getAvailability_startTime() {
        return availability_startTime;
    }

    public void setAvailability_startTime(String availability_startTime) {
        this.availability_startTime = availability_startTime;
    }
}