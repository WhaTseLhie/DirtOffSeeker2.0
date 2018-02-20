package com.example.jayvee.dirtoffseeker;

public class WalletCashIn {

    String cashin_balance, cashin_date, cashin_id, cashin_time;

    public WalletCashIn(String cashin_balance, String cashin_date, String cashin_id, String cashin_time) {
        this.cashin_balance = cashin_balance;
        this.cashin_date = cashin_date;
        this.cashin_id = cashin_id;
        this.cashin_time = cashin_time;
    }

    public WalletCashIn() {
        // Empty Constructor
    }

    public String getCashin_balance() {
        return cashin_balance;
    }

    public void setCashin_balance(String cashin_balance) {
        this.cashin_balance = cashin_balance;
    }

    public String getCashin_date() {
        return cashin_date;
    }

    public void setCashin_date(String cashin_date) {
        this.cashin_date = cashin_date;
    }

    public String getCashin_id() {
        return cashin_id;
    }

    public void setCashin_id(String cashin_id) {
        this.cashin_id = cashin_id;
    }

    public String getCashin_time() {
        return cashin_time;
    }

    public void setCashin_time(String cashin_time) {
        this.cashin_time = cashin_time;
    }
}
