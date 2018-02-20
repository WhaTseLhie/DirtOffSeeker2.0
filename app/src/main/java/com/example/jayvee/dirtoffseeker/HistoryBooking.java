package com.example.jayvee.dirtoffseeker;

public class HistoryBooking {

    String booking_date, booking_id, booking_status, booking_time, laundWorker_fn, laundWorker_fbid, laundWorker_ln, laundWorker_mn, laundWorker_pic, laundSeeker_fbid, booking_service, booking_fee;

    public HistoryBooking(String booking_date, String booking_id, String booking_status, String booking_time, String laundWorker_fn, String laundWorker_fbid, String laundWorker_ln, String laundWorker_mn, String laundWorker_pic, String laundSeeker_fbid, String booking_service, String booking_fee) {
        this.booking_date = booking_date;
        this.booking_id = booking_id;
        this.booking_status = booking_status;
        this.booking_time = booking_time;
        this.laundWorker_fn = laundWorker_fn;
        this.laundWorker_fbid = laundWorker_fbid;
        this.laundWorker_ln = laundWorker_ln;
        this.laundWorker_mn = laundWorker_mn;
        this.laundWorker_pic = laundWorker_pic;
        this.laundSeeker_fbid = laundSeeker_fbid;
        this.booking_service = booking_service;
        this.booking_fee = booking_fee;
    }

    public HistoryBooking() {
        // Empty Constructor
    }

    public String getBooking_date() {
        return booking_date;
    }

    public void setBooking_date(String booking_date) {
        this.booking_date = booking_date;
    }

    public String getBooking_id() {
        return booking_id;
    }

    public void setBooking_id(String booking_id) {
        this.booking_id = booking_id;
    }

    public String getBooking_status() {
        return booking_status;
    }

    public void setBooking_status(String booking_status) {
        this.booking_status = booking_status;
    }

    public String getBooking_time() {
        return booking_time;
    }

    public void setBooking_time(String booking_time) {
        this.booking_time = booking_time;
    }

    public String getLaundWorker_fn() {
        return laundWorker_fn;
    }

    public void setLaundWorker_fn(String laundWorker_fn) {
        this.laundWorker_fn = laundWorker_fn;
    }

    public String getLaundWorker_fbid() {
        return laundWorker_fbid;
    }

    public void setLaundWorker_fbid(String laundWorker_id) {
        this.laundWorker_fbid = laundWorker_id;
    }

    public String getLaundWorker_ln() {
        return laundWorker_ln;
    }

    public void setLaundWorker_ln(String laundWorker_ln) {
        this.laundWorker_ln = laundWorker_ln;
    }

    public String getLaundWorker_mn() {
        return laundWorker_mn;
    }

    public void setLaundWorker_mn(String laundWorker_mn) {
        this.laundWorker_mn = laundWorker_mn;
    }

    public String getLaundWorker_pic() {
        return laundWorker_pic;
    }

    public void setLaundWorker_pic(String laundWorker_pic) {
        this.laundWorker_pic = laundWorker_pic;
    }

    public String getLaundSeeker_fbid() {
        return laundSeeker_fbid;
    }

    public void setLaundSeeker_fbid(String laundSeeker_fbid) {
        this.laundSeeker_fbid = laundSeeker_fbid;
    }

    public String getBooking_service() {
        return booking_service;
    }

    public void setBooking_service(String booking_service) {
        this.booking_service = booking_service;
    }

    public String getBooking_fee() {
        return booking_fee;
    }

    public void setBooking_fee(String booking_fee) {
        this.booking_fee = booking_fee;
    }
}