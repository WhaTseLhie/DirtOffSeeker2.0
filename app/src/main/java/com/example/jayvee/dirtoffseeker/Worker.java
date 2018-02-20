package com.example.jayvee.dirtoffseeker;

import android.support.annotation.Keep;


public class Worker {

    public String laundWorker_address, laundWorker_bdate, laundWorker_cnum, laundWorker_dateApplied, laundWorker_email, laundWorker_fbid, laundWorker_fn, laundWorker_ln, laundWorker_mn, laundWorker_pic, laundWorker_status;

    @Keep
    public Worker() {
        // Default constructor
    }

    public Worker(String laundWorker_address, String laundWorker_bdate, String laundWorker_cnum, String laundWorker_dateApplied, String laundWorker_email, String laundWorker_fbid, String laundWorker_fn, String laundWorker_ln, String laundWorker_mn, String laundWorker_pic, String laundWorker_status) {
        this.laundWorker_address = laundWorker_address;
        this.laundWorker_bdate = laundWorker_bdate;
        this.laundWorker_cnum = laundWorker_cnum;
        this.laundWorker_dateApplied = laundWorker_dateApplied;
        this.laundWorker_email = laundWorker_email;
        this.laundWorker_fbid = laundWorker_fbid;
        this.laundWorker_fn = laundWorker_fn;
        this.laundWorker_ln = laundWorker_ln;
        this.laundWorker_mn = laundWorker_mn;
        this.laundWorker_pic = laundWorker_pic;
        this.laundWorker_status = laundWorker_status;
    }

    public String getLaundWorker_address() {
        return laundWorker_address;
    }

    public void setLaundWorker_address(String laundWorker_address) {
        this.laundWorker_address = laundWorker_address;
    }

    public String getLaundWorker_bdate() {
        return laundWorker_bdate;
    }

    public void setLaundWorker_bdate(String laundWorker_bdate) {
        this.laundWorker_bdate = laundWorker_bdate;
    }

    public String getLaundWorker_cnum() {
        return laundWorker_cnum;
    }

    public void setLaundWorker_cnum(String laundWorker_cnum) {
        this.laundWorker_cnum = laundWorker_cnum;
    }

    public String getLaundWorker_dateApplied() {
        return laundWorker_dateApplied;
    }

    public void setLaundWorker_dateApplied(String laundWorker_dateApplied) {
        this.laundWorker_dateApplied = laundWorker_dateApplied;
    }

    public String getLaundWorker_email() {
        return laundWorker_email;
    }

    public void setLaundWorker_email(String laundWorker_email) {
        this.laundWorker_email = laundWorker_email;
    }

    public String getLaundWorker_fbid() {
        return laundWorker_fbid;
    }

    public void setLaundWorker_fbid(String laundWorker_fbid) {
        this.laundWorker_fbid = laundWorker_fbid;
    }

    public String getLaundWorker_fn() {
        return laundWorker_fn;
    }

    public void setLaundWorker_fn(String laundWorker_fn) {
        this.laundWorker_fn = laundWorker_fn;
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

    public String getLaundWorker_status() {
        return laundWorker_status;
    }

    public void setLaundWorker_status(String laundWorker_status) {
        this.laundWorker_status = laundWorker_status;
    }
}

/*
public class Worker {

    String firstname, middlename, lastname, address, services, time;

    public Worker(String firstname, String middlename, String lastname, String address, String services, String time) {
        //this.imageUri = imageUri;
        this.firstname = firstname;
        this.middlename = middlename;
        this.lastname = lastname;
        this.address = address;
        this.services = services;
        this.time = time;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getMiddlename() {
        return middlename;
    }

    public void setMiddlename(String middlename) {
        this.middlename = middlename;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getServices() {
        return services;
    }

    public void setServices(String services) {
        this.services = services;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
*/