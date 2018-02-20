package com.example.jayvee.dirtoffseeker;

public class WalletPayout {

    String booking_id, payout_date, payout_fee, payout_id;

    public WalletPayout(String booking_id, String payout_date, String payout_fee, String payout_id) {
        this.booking_id = booking_id;
        this.payout_date = payout_date;
        this.payout_fee = payout_fee;
        this.payout_id = payout_id;
    }

    public WalletPayout() {
        // Empty Constructor
    }

    public String getPayout_id() {
        return payout_id;
    }

    public void setPayout_id(String payout_id) {
        this.payout_id = payout_id;
    }

    public String getBooking_id() {
        return booking_id;
    }

    public void setBooking_id(String booking_id) {
        this.booking_id = booking_id;
    }

    public String getPayout_fee() {
        return payout_fee;
    }

    public void setPayout_fee(String payout_fee) {
        this.payout_fee = payout_fee;
    }

    public String getPayout_date() {
        return payout_date;
    }

    public void setPayout_date(String payout_date) {
        this.payout_date = payout_date;
    }
}
