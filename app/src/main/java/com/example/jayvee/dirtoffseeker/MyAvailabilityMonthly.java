package com.example.jayvee.dirtoffseeker;

public class MyAvailabilityMonthly {

    String availability_date, availability_endTime, availability_key, availability_startMonth, availability_startTime;

    public MyAvailabilityMonthly(String availability_date, String availability_endTime, String availability_key, String availability_startMonth, String availability_startTime) {
        this.availability_date = availability_date;
        this.availability_endTime = availability_endTime;
        this.availability_key = availability_key;
        this.availability_startMonth = availability_startMonth;
        this.availability_startTime = availability_startTime;
    }

    public MyAvailabilityMonthly() {
        // Empty Constructor
    }

    public String getAvailability_date() {
        return availability_date;
    }

    public void setAvailability_date(String availability_date) {
        this.availability_date = availability_date;
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

    public String getAvailability_startMonth() {
        return availability_startMonth;
    }

    public void setAvailability_startMonth(String availability_startMonth) {
        this.availability_startMonth = availability_startMonth;
    }

    public String getAvailability_startTime() {
        return availability_startTime;
    }

    public void setAvailability_startTime(String availability_startTime) {
        this.availability_startTime = availability_startTime;
    }
}