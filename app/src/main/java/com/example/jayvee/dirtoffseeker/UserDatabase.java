package com.example.jayvee.dirtoffseeker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import java.util.ArrayList;

public class UserDatabase extends SQLiteOpenHelper {


    static String DATABASE = "db_seeker";
    static String TBL_USER = "tbl_user";
    static String TBL_WORKER = "tbl_worker";
    static String TBL_AVAILABILITY_MONTHLY = "tbl_availability_monthly";
    static String TBL_AVAILABILITY_WEEKLY = "tbl_availability_weekly";
    static String TBL_WALLET_CASH_IN = "tbl_wallet_cash_in";
    static String TBL_WALLET_PAYOUT = "tbl_wallet_payout";
    static String TBL_FEEDBACK = "tbl_feedback";
    static String TBL_BOOKING = "tbl_booking";
    static String TBL_HISTORY_BOOKING = "tbl_history_booking";

    public UserDatabase(Context context) {
        super(context, DATABASE, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase arg0) {
        String sql = "CREATE TABLE " +TBL_USER+ "(laundSeeker_cnum varchar(15), laundSeeker_email varchar(50), laundSeeker_fbid varchar(20) primary key, laundSeeker_fn varchar(50), laundSeeker_gender varchar(10), laundSeeker_ln varchar(50), laundSeeker_link varchar(50), laundSeeker_pic varchar(100), laundSeeker_status varchar(10), laundSeeker_totalbal varchar(30))";
        arg0.execSQL(sql);
        String sql1 = "CREATE TABLE " +TBL_AVAILABILITY_MONTHLY+ "(date varchar(20), endTime varchar(50), id string primary key, startMonth varchar(20), startTime varchar(50))";
        arg0.execSQL(sql1);
        String sql2 = "CREATE TABLE " +TBL_AVAILABILITY_WEEKLY+ "(dayOfTheWeek varchar(15), endTime varchar(50), id string primary key, monthOf varchar(20), startTime varchar(50))";
        arg0.execSQL(sql2);
        String sql3 = "CREATE TABLE " +TBL_WORKER+ "(laundWorker_address varchar(100), laundWorker_bdate varchar(15), laundWorker_cnum varchar(11), laundWorker_dateApplied varchar(15), laundWorker_email varchar(50), laundWorker_fbid varchar(20) primary key, laundWorker_fn varchar(50), laundWorker_ln varchar(50), laundWorker_mn varchar(50), laundWorker_pic varchar(100), laundWorker_status varchar(50))";
        arg0.execSQL(sql3);
        String sql4 = "CREATE TABLE " +TBL_WALLET_CASH_IN+ "(cashin_balance varchar(30), cashin_date varchar(12), cashin_id varchar(20) primary key, cashin_time varchar(15))";
        arg0.execSQL(sql4);
        String sql5 = "CREATE TABLE " +TBL_WALLET_PAYOUT+ "(booking_id varchar(30), payout_date varchar(15), payout_fee varchar(12), payout_id varchar(15) primary key)";
        arg0.execSQL(sql5);
        String sql6 = "CREATE TABLE " +TBL_FEEDBACK+ "(feedback_comment varchar(150), feedback_id varchar(20) primary key, feedback_rating varchar(10), feedback_seekerId varchar(20), feedback_workerId varchar(20))";
        arg0.execSQL(sql6);
        String sql7 = "CREATE TABLE " +TBL_BOOKING+ "(id integer primary key autoincrement, booking_date varchar(15), booking_id varchar(20), booking_status varchar(10), booking_time varchar(15), laundWorker_fn varchar(20), laundWorker_fbid varchar(20), laundWorker_ln varchar(20), laundWorker_mn varchar(20), laundWorker_pic varchar(100), laundSeeker_fbid varchar(20), booking_service varchar(20), booking_fee varchar(20))";
        arg0.execSQL(sql7);
        String sql8 = "CREATE TABLE " +TBL_HISTORY_BOOKING+ "(booking_date varchar(15), booking_id varchar(20) primary key, booking_status varchar(10), booking_time varchar(15), laundWorker_fn varchar(20), laundWorker_fbid varchar(20), laundWorker_ln varchar(20), laundWorker_mn varchar(20), laundWorker_pic varchar(100), laundSeeker_fbid varchar(20), booking_service varchar(20), booking_fee varchar(15))";
        arg0.execSQL(sql8);
    }

    public long addUser(String laundSeeker_cnum, String laundSeeker_email, String laundSeeker_fbid, String laundSeeker_fn, String laundSeeker_gender, String laundSeeker_ln, String laundSeeker_link, String laundSeeker_pic, String laundSeeker_status, String laundSeeker_totalbal) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = 0;
        ContentValues cv = new ContentValues();

        cv.put("laundSeeker_cnum", laundSeeker_cnum);
        cv.put("laundSeeker_email", laundSeeker_email);
        cv.put("laundSeeker_fbid", laundSeeker_fbid);
        cv.put("laundSeeker_fn", laundSeeker_fn);
        cv.put("laundSeeker_gender", laundSeeker_gender);
        cv.put("laundSeeker_ln", laundSeeker_ln);
        cv.put("laundSeeker_link", laundSeeker_link);
        cv.put("laundSeeker_pic", laundSeeker_pic);
        cv.put("laundSeeker_status", laundSeeker_status);
        cv.put("laundSeeker_totalbal", laundSeeker_totalbal);
        result = db.insert(TBL_USER, null, cv);

        db.close();
        return result;
    }

    public ArrayList<User> getAllUser() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<User> list = new ArrayList<>();
        Cursor c = db.query(TBL_USER, null, null, null, null, null, "laundSeeker_fbid");
        list.clear();

        for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            String laundSeeker_cnum = c.getString(c.getColumnIndex("laundSeeker_cnum"));
            String laundSeeker_email = c.getString(c.getColumnIndex("laundSeeker_email"));
            String laundSeeker_fbid = c.getString(c.getColumnIndex("laundSeeker_fbid"));
            String laundSeeker_fn = c.getString(c.getColumnIndex("laundSeeker_fn"));
            String laundSeeker_gender = c.getString(c.getColumnIndex("laundSeeker_gender"));
            String laundSeeker_ln = c.getString(c.getColumnIndex("laundSeeker_ln"));
            String laundSeeker_link = c.getString(c.getColumnIndex("laundSeeker_link"));
            String laundSeeker_pic = c.getString(c.getColumnIndex("laundSeeker_pic"));
            String laundSeeker_status = c.getString(c.getColumnIndex("laundSeeker_status"));
            String laundSeeker_totalbal = c.getString(c.getColumnIndex("laundSeeker_totalbal"));

            list.add(new User(laundSeeker_cnum, laundSeeker_email, laundSeeker_fbid, laundSeeker_fn, laundSeeker_gender, laundSeeker_ln, laundSeeker_link, laundSeeker_pic, laundSeeker_status, laundSeeker_totalbal));
        }

        c.close();
        db.close();
        return list;
    }

    public void deleteAllUser() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TBL_USER, null, null);

        db.close();
    }

    public long addWorker(String address, String bdate, String cnum, String dateApplied, String email, String fbid, String fname, String lname, String mname, String image, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = 0;
        ContentValues cv = new ContentValues();

        cv.put("laundWorker_address", address);
        cv.put("laundWorker_bdate", bdate);
        cv.put("laundWorker_cnum", cnum);
        cv.put("laundWorker_dateApplied", dateApplied);
        cv.put("laundWorker_email", email);
        cv.put("laundWorker_fbid", fbid);
        cv.put("laundWorker_fn", fname);
        cv.put("laundWorker_ln", lname);
        cv.put("laundWorker_mn", mname);
        cv.put("laundWorker_pic", image);
        cv.put("laundWorker_status", status);

        result = db.insert(TBL_WORKER, null, cv);

        db.close();
        return result;
    }

    public ArrayList<Worker> getAllWorker() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Worker> list = new ArrayList<>();
        Cursor c = db.query(TBL_WORKER, null, null, null, null, null, "laundWorker_fbid");
        list.clear();

        for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            String laundWorker_address = c.getString(c.getColumnIndex("laundWorker_address"));
            String laundWorker_bdate = c.getString(c.getColumnIndex("laundWorker_bdate"));
            String laundWorker_cnum = c.getString(c.getColumnIndex("laundWorker_cnum"));
            String laundWorker_dateApplied = c.getString(c.getColumnIndex("laundWorker_dateApplied"));
            String laundWorker_email = c.getString(c.getColumnIndex("laundWorker_email"));
            String laundWorker_fbid = c.getString(c.getColumnIndex("laundWorker_fbid"));
            String laundWorker_fn = c.getString(c.getColumnIndex("laundWorker_fn"));
            String laundWorker_ln = c.getString(c.getColumnIndex("laundWorker_ln"));
            String laundWorker_mn = c.getString(c.getColumnIndex("laundWorker_mn"));
            String laundWorker_pic = c.getString(c.getColumnIndex("laundWorker_pic"));
            String laundWorker_status = c.getString(c.getColumnIndex("laundWorker_status"));

            list.add(new Worker(laundWorker_address, laundWorker_bdate, laundWorker_cnum, laundWorker_dateApplied, laundWorker_email, laundWorker_fbid, laundWorker_fn, laundWorker_ln, laundWorker_mn, laundWorker_pic, laundWorker_status));
        }

        c.close();
        db.close();
        return list;
    }

    public void deleteAllWorker() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TBL_WORKER, null, null);

        db.close();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // WALLET PAYOUT
    public long addWalletPayout(String booking_id, String payout_date, String payout_fee, String payout_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result;
        ContentValues cv = new ContentValues();

        cv.put("booking_id", booking_id);
        cv.put("payout_date", payout_date);
        cv.put("payout_fee", payout_fee);
        cv.put("payout_id", payout_id);
        result = db.insert(TBL_WALLET_PAYOUT, null, cv);

        db.close();
        return result;
    }

    public ArrayList<WalletPayout> getAllWalletPayout() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<WalletPayout> list = new ArrayList<>();
        Cursor c = db.query(TBL_WALLET_PAYOUT, null, null, null, null, null, "payout_id");
        list.clear();

        for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            String booking_id = c.getString(c.getColumnIndex("booking_id"));
            String payout_date = c.getString(c.getColumnIndex("payout_date"));
            String payout_fee = c.getString(c.getColumnIndex("payout_fee"));
            String payout_id = c.getString(c.getColumnIndex("payout_id"));

            list.add(new WalletPayout(booking_id, payout_date, payout_fee, payout_id));
        }

        c.close();
        db.close();
        return list;
    }

    public int deleteWalletPayout(String payout_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.query(TBL_WALLET_PAYOUT, null, null, null, null, null, "payout_id");
        int id = 0;

        for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            if(payout_id.equals(c.getString(c.getColumnIndex("payout_id")))) {
                id = db.delete(TBL_WALLET_PAYOUT, "payout_id=?", new String[]{payout_id});
            }
        }

        c.close();
        db.close();
        return id;
    }

    public void deleteAllPayout() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TBL_WALLET_PAYOUT, null, null);

        db.close();
    }
    /////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // WALLET CASH IN
    public long addWalletCashIn(String cashin_balance, String cashin_date, String cashin_id, String cashin_time) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result;
        ContentValues cv = new ContentValues();

        cv.put("cashin_balance", cashin_balance);
        cv.put("cashin_date", cashin_date);
        cv.put("cashin_id", cashin_id);
        cv.put("cashin_time", cashin_time);
        result = db.insert(TBL_WALLET_CASH_IN, null, cv);

        db.close();
        return result;
    }

    public ArrayList<WalletCashIn> getAllWalletCashIn() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<WalletCashIn> list = new ArrayList<>();
        Cursor c = db.query(TBL_WALLET_CASH_IN, null, null, null, null, null, "cashin_id");
        list.clear();

        for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            String cashin_balance = c.getString(c.getColumnIndex("cashin_balance"));
            String cashin_date = c.getString(c.getColumnIndex("cashin_date"));
            String cashin_id = c.getString(c.getColumnIndex("cashin_id"));
            String cashin_time = c.getString(c.getColumnIndex("cashin_time"));

            list.add(new WalletCashIn(cashin_balance, cashin_date, cashin_id, cashin_time));
        }

        c.close();
        db.close();
        return list;
    }

    public int deleteWalletCashIn(String cashin_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.query(TBL_WALLET_CASH_IN, null, null, null, null, null, "cashin_id");
        int id = 0;

        for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            if(cashin_id.equals(c.getString(c.getColumnIndex("cashin_id")))) {
                id = db.delete(TBL_WALLET_CASH_IN, "cashin_id=?", new String[]{cashin_id});
            }
        }

        c.close();
        db.close();
        return id;
    }

    public void deleteAllCashIn() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TBL_WALLET_CASH_IN, null, null);

        db.close();
    }
    /////////////////////////////////////////////////////////////////////////

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
        arg0.execSQL("DROP IF TABLE EXISTS " +TBL_USER);
        arg0.execSQL("DROP IF TABLE EXIST " +TBL_WORKER);
        arg0.execSQL("DROP IF TABLE EXIST " +TBL_AVAILABILITY_MONTHLY);
        arg0.execSQL("DROP IF TABLE EXIST " +TBL_AVAILABILITY_WEEKLY);
        arg0.execSQL("DROP IF TABLE EXIST " +TBL_WALLET_CASH_IN);
        arg0.execSQL("DROP IF TABLE EXIST " +TBL_WALLET_PAYOUT);
        arg0.execSQL("DROP IF TABLE EXIST " +TBL_BOOKING);
        arg0.execSQL("DROP IF TABLE EXIST " +TBL_HISTORY_BOOKING);

        onCreate(arg0);
    }

    ///////////////////////////////////////////////////////////////////////////
    // MONTHLY
    public long addAvailabilityMonthly(String date, String endTime, String id, String startMonth, String startTime) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result;
        ContentValues cv = new ContentValues();

        cv.put("date", date);
        cv.put("endTime", endTime);
        cv.put("id", id);
        cv.put("startMonth", startMonth);
        cv.put("startTime", startTime);
        result = db.insert(TBL_AVAILABILITY_MONTHLY, null, cv);

        db.close();
        return result;
    }

    public ArrayList<MyAvailabilityMonthly> getAllAvailabilityMonthly() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<MyAvailabilityMonthly> list = new ArrayList<>();
        Cursor c = db.query(TBL_AVAILABILITY_MONTHLY, null, null, null, null, null, "id");
        list.clear();

        for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            String date = c.getString(c.getColumnIndex("date"));
            String endTime = c.getString(c.getColumnIndex("endTime"));
            String id = c.getString(c.getColumnIndex("id"));
            String startMonth = c.getString(c.getColumnIndex("startMonth"));
            String startTime = c.getString(c.getColumnIndex("startTime"));

            list.add(new MyAvailabilityMonthly(date, endTime, id, startMonth, startTime));
        }

        c.close();
        db.close();
        return list;
    }

    public int deleteAvailabilityMonthly(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.query(TBL_AVAILABILITY_MONTHLY, null, null, null, null, null, "id");
        int time = 0;

        for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            if(id.equals(c.getString(c.getColumnIndex("id")))) {
                time = db.delete(TBL_AVAILABILITY_MONTHLY, "id=?", new String[]{id});
            }
        }

        c.close();
        db.close();
        return time;
    }

    public void deleteAllAvailabilityMonthly() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TBL_AVAILABILITY_MONTHLY, null, null);

        db.close();
    }

    ///////////////////////////////////////////////////////////////////////////
    // WEEKLY
    public long addAvailabilityWeekly(String dayOfTheWeek, String endTime, String id, String monthOf, String startTime) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result;
        ContentValues cv = new ContentValues();

        cv.put("dayOfTheWeek", dayOfTheWeek);
        cv.put("endTime", endTime);
        cv.put("id", id);
        cv.put("monthOf", monthOf);
        cv.put("startTime", startTime);
        result = db.insert(TBL_AVAILABILITY_WEEKLY, null, cv);

        db.close();
        return result;
    }

    public ArrayList<MyAvailabilityWeekly> getAllAvailabilityWeekly() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<MyAvailabilityWeekly> list = new ArrayList<>();
        Cursor c = db.query(TBL_AVAILABILITY_WEEKLY, null, null, null, null, null, "id");
        list.clear();

        for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            String dayOfTheWeek = c.getString(c.getColumnIndex("dayOfTheWeek"));
            String endTime = c.getString(c.getColumnIndex("endTime"));
            String id = c.getString(c.getColumnIndex("id"));
            String monthOf = c.getString(c.getColumnIndex("monthOf"));
            String startTime = c.getString(c.getColumnIndex("startTime"));

            list.add(new MyAvailabilityWeekly(dayOfTheWeek, endTime, id, monthOf, startTime));
        }

        c.close();
        db.close();
        return list;
    }

    public int deleteAvailabilityWeekly(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.query(TBL_AVAILABILITY_WEEKLY, null, null, null, null, null, "id");
        int time = 0;

        for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            if(id.equals(c.getString(c.getColumnIndex("id")))) {
                time = db.delete(TBL_AVAILABILITY_WEEKLY, "id=?", new String[]{id});
            }
        }

        c.close();
        db.close();
        return time;
    }

    public void deleteAllAvailabilityWeekly() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TBL_AVAILABILITY_WEEKLY, null, null);

        db.close();
    }

    ///////////////////////////////////////////////////////////////////////////
    // FEEDBACK
    public long addFeedback(String feedback_comment, String feedback_id, String feedback_rating, String feedback_seekerId, String feedback_workerId) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result;
        ContentValues cv = new ContentValues();

        cv.put("feedback_comment", feedback_comment);
        cv.put("feedback_id", feedback_id);
        cv.put("feedback_rating", feedback_rating);
        cv.put("feedback_seekerId", feedback_seekerId);
        cv.put("feedback_workerId", feedback_workerId);
        result = db.insert(TBL_FEEDBACK, null, cv);

        db.close();
        return result;
    }

    public ArrayList<Feedback> getAllFeedback() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Feedback> list = new ArrayList<>();
        Cursor c = db.query(TBL_FEEDBACK, null, null, null, null, null, "feedback_id");
        list.clear();

        for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            String feedback_comment = c.getString(c.getColumnIndex("feedback_comment"));
            String feedback_id = c.getString(c.getColumnIndex("feedback_id"));
            String feedback_rating = c.getString(c.getColumnIndex("feedback_rating"));
            String feedback_seekerId = c.getString(c.getColumnIndex("feedback_seekerId"));
            String feedback_workerId = c.getString(c.getColumnIndex("feedback_workerId"));

            list.add(new Feedback(feedback_comment, feedback_id, feedback_rating, feedback_seekerId, feedback_workerId));
        }

        c.close();
        db.close();
        return list;
    }

    public int deleteFeedback(String feedback_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.query(TBL_FEEDBACK, null, null, null, null, null, "feedback_id");
        int time = 0;

        for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            if(feedback_id.equals(c.getString(c.getColumnIndex("feedback_id")))) {
                time = db.delete(TBL_FEEDBACK, "feedback_id=?", new String[]{feedback_id});
            }
        }

        c.close();
        db.close();
        return time;
    }

    public void deleteAllFeedback() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TBL_FEEDBACK, null, null);

        db.close();
    }

    ///////////////////////////////////////////////////////////////////////////
    // BOOKING
    public long addBooking(String booking_date, String booking_id, String booking_status, String booking_time, String laundWorker_fn, String laundWorker_fbid, String laundWorker_ln, String laundWorker_mn, String laundWorker_pic, String laundSeeker_fbid, String booking_service, String booking_fee) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result;
        ContentValues cv = new ContentValues();

        cv.put("booking_date", booking_date);
        cv.put("booking_id", booking_id);
        cv.put("booking_status", booking_status);
        cv.put("booking_time", booking_time);
        cv.put("laundWorker_fn", laundWorker_fn);
        cv.put("laundWorker_fbid", laundWorker_fbid);
        cv.put("laundWorker_ln", laundWorker_ln);
        cv.put("laundWorker_mn", laundWorker_mn);
        cv.put("laundWorker_pic", laundWorker_pic);
        cv.put("laundSeeker_fbid", laundSeeker_fbid);
        cv.put("booking_service", booking_service);
        cv.put("booking_fee", booking_fee);
        result = db.insert(TBL_BOOKING, null, cv);

        db.close();
        return result;
    }

    public ArrayList<Booking> getAllBooking() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Booking> list = new ArrayList<>();
        Cursor c = db.query(TBL_BOOKING, null, null, null, null, null, "booking_id");
        list.clear();

        for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            String booking_date = c.getString(c.getColumnIndex("booking_date"));
            String booking_id = c.getString(c.getColumnIndex("booking_id"));
            String booking_status = c.getString(c.getColumnIndex("booking_status"));
            String booking_time = c.getString(c.getColumnIndex("booking_time"));
            String laundWorker_fn = c.getString(c.getColumnIndex("laundWorker_fn"));
            String laundWorker_fbid = c.getString(c.getColumnIndex("laundWorker_fbid"));
            String laundWorker_ln = c.getString(c.getColumnIndex("laundWorker_ln"));
            String laundWorker_mn = c.getString(c.getColumnIndex("laundWorker_mn"));
            String laundWorker_pic = c.getString(c.getColumnIndex("laundWorker_pic"));
            String laundSeeker_fbid = c.getString(c.getColumnIndex("laundSeeker_fbid"));
            String booking_service = c.getString(c.getColumnIndex("booking_service"));
            String booking_fee = c.getString(c.getColumnIndex("booking_fee"));

            list.add(new Booking(booking_date, booking_id, booking_status, booking_time, laundWorker_fn, laundWorker_fbid, laundWorker_ln, laundWorker_mn, laundWorker_pic, laundSeeker_fbid, booking_service, booking_fee));
        }

        c.close();
        db.close();
        return list;
    }

    public int deleteBooking(String booking_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.query(TBL_BOOKING, null, null, null, null, null, "booking_id");
        int time = 0;

        for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            if(booking_id.equals(c.getString(c.getColumnIndex("booking_id")))) {
                time = db.delete(TBL_BOOKING, "booking_id=?", new String[]{booking_id});
            }
        }

        c.close();
        db.close();
        return time;
    }

    public void deleteAllBooking() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TBL_BOOKING, null, null);

        db.close();
    }

    ///////////////////////////////////////////////////////////////////////////
    // HISTORY BOOKING
    public long addHistoryBooking(String booking_date, String booking_id, String booking_status, String booking_time, String laundWorker_fn, String laundWorker_fbid, String laundWorker_ln, String laundWorker_mn, String laundWorker_pic, String laundSeeker_fbid, String booking_service, String booking_fee) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result;
        ContentValues cv = new ContentValues();

        cv.put("booking_date", booking_date);
        cv.put("booking_id", booking_id);
        cv.put("booking_status", booking_status);
        cv.put("booking_time", booking_time);
        cv.put("laundWorker_fn", laundWorker_fn);
        cv.put("laundWorker_fbid", laundWorker_fbid);
        cv.put("laundWorker_ln", laundWorker_ln);
        cv.put("laundWorker_mn", laundWorker_mn);
        cv.put("laundWorker_pic", laundWorker_pic);
        cv.put("laundSeeker_fbid", laundSeeker_fbid);
        cv.put("booking_service", booking_service);
        cv.put("booking_fee", booking_fee);
        result = db.insert(TBL_HISTORY_BOOKING, null, cv);

        db.close();
        return result;
    }

    public ArrayList<HistoryBooking> getAllHistoryBooking() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<HistoryBooking> list = new ArrayList<>();
        Cursor c = db.query(TBL_HISTORY_BOOKING, null, null, null, null, null, "booking_id");
        list.clear();

        for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            String booking_date = c.getString(c.getColumnIndex("booking_date"));
            String booking_id = c.getString(c.getColumnIndex("booking_id"));
            String booking_status = c.getString(c.getColumnIndex("booking_status"));
            String booking_time = c.getString(c.getColumnIndex("booking_time"));
            String laundWorker_fn = c.getString(c.getColumnIndex("laundWorker_fn"));
            String laundWorker_fbid = c.getString(c.getColumnIndex("laundWorker_fbid"));
            String laundWorker_ln = c.getString(c.getColumnIndex("laundWorker_ln"));
            String laundWorker_mn = c.getString(c.getColumnIndex("laundWorker_mn"));
            String laundWorker_pic = c.getString(c.getColumnIndex("laundWorker_pic"));
            String laundSeeker_fbid = c.getString(c.getColumnIndex("laundSeeker_fbid"));
            String booking_service = c.getString(c.getColumnIndex("booking_service"));
            String booking_fee = c.getString(c.getColumnIndex("booking_fee"));

            list.add(new HistoryBooking(booking_date, booking_id, booking_status, booking_time, laundWorker_fn, laundWorker_fbid, laundWorker_ln, laundWorker_mn, laundWorker_pic, laundSeeker_fbid, booking_service, booking_fee));
        }

        c.close();
        db.close();
        return list;
    }

    public int deleteHistoryBooking(String booking_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.query(TBL_HISTORY_BOOKING, null, null, null, null, null, "booking_id");
        int time = 0;

        for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            if(booking_id.equals(c.getString(c.getColumnIndex("booking_id")))) {
                time = db.delete(TBL_HISTORY_BOOKING, "booking_id=?", new String[]{booking_id});
            }
        }

        c.close();
        db.close();
        return time;
    }

    public void deleteAllHistoryBooking() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TBL_HISTORY_BOOKING, null, null);

        db.close();
    }
}