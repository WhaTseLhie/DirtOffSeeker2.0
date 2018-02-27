package com.example.jayvee.dirtoffseeker;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class WorkerScheduleFragment extends Fragment implements DatePickerDialog.OnDateSetListener {

    UserDatabase userDatabase;
    DatabaseReference mDatabase;
    ArrayList<Worker> workerList = new ArrayList<>();
    ArrayList<HistoryBooking> historyList = new ArrayList<>();
    ListView lvWeekly;
    ArrayList<MyAvailabilityWeekly> weekList = new ArrayList<>();
    MyAvailabilityWeeklyAdapter weeklyAdapter;
    TextView txtBulk;
    String booking_id, booking_date, booking_time, laundWorker_fn, laundWorker_fbid, laundWorker_ln, laundWorker_mn, laundWorker_pic, laundSeeker_fbid, booking_service = "";
    Calendar calendar;
    String weekDays = "", status = "";
    int day = 10;
    private String d = "";
    private Context mContext;

    public WorkerScheduleFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_worker_schedule, container, false);

        userDatabase = new UserDatabase(getActivity());
        historyList = userDatabase.getAllHistoryBooking();
        txtBulk = view.findViewById(R.id.textView);
        mContext = getActivity();

        calendar = Calendar.getInstance();
        txtBulk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(getActivity(), WorkerScheduleFragment.this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                dialog.show();

                ArrayList<User> userList = userDatabase.getAllUser();
                booking_time = "12am-11:59pm";
                laundWorker_fn = workerList.get(0).getLaundWorker_fn();
                laundWorker_fbid = workerList.get(0).getLaundWorker_fbid();
                laundWorker_ln = workerList.get(0).getLaundWorker_ln();
                laundWorker_mn = workerList.get(0).getLaundWorker_mn();
                laundWorker_pic = workerList.get(0).getLaundWorker_pic();
                laundSeeker_fbid = userList.get(0).getLaundSeeker_fbid();
            }
        });

        lvWeekly = view.findViewById(R.id.listView1);
        weeklyAdapter = new MyAvailabilityWeeklyAdapter(getActivity(), weekList);
        lvWeekly.setAdapter(weeklyAdapter);

        userDatabase = new UserDatabase(getActivity());
        workerList = userDatabase.getAllWorker();
        lvWeekly.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ArrayList<User> userList = userDatabase.getAllUser();
                booking_id = weekList.get(i).getAvailability_key();
                booking_date = weekList.get(i).getAvailability_dayOfTheWeek();
                booking_time = weekList.get(i).getAvailability_startTime() + " - " + weekList.get(i).getAvailability_endTime();
                laundWorker_fn = workerList.get(0).getLaundWorker_fn();
                laundWorker_fbid = workerList.get(0).getLaundWorker_fbid();
                laundWorker_ln = workerList.get(0).getLaundWorker_ln();
                laundWorker_mn = workerList.get(0).getLaundWorker_mn();
                laundWorker_pic = workerList.get(0).getLaundWorker_pic();
                laundSeeker_fbid = userList.get(0).getLaundSeeker_fbid();

                DatabaseReference bookingReference = FirebaseDatabase.getInstance().getReference().child("bookingList").child(laundWorker_fbid).child("weeklyBook");
                bookingReference.child(booking_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()) {
                            status = dataSnapshot.child("booking_status").getValue(String.class);
                        } else {
                            status = "DONE";
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                if(status.equalsIgnoreCase("DONE") || status.equalsIgnoreCase("CANCELED")) {
                    LaundryBasketActivity.start(getActivity(), new Booking(booking_date,
                            booking_id, "", booking_time, laundWorker_fn, laundWorker_fbid,
                            laundWorker_ln, laundWorker_mn, laundWorker_pic, laundSeeker_fbid, "", "0"));
                } else {
                    Toast.makeText(mContext, "Schedule is already booked", Toast.LENGTH_LONG).show();
                }
            }
        });

        mDatabase = FirebaseDatabase.getInstance().getReference().child("laundrySchedule").child(workerList.get(0).getLaundWorker_fbid()).child("modeWeekly");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    try {
                        MyAvailabilityWeekly weekly;
                        weekList.clear();
                        weekDays = "";
                        userDatabase.deleteAllAvailabilityWeekly();

                        for(DataSnapshot child : dataSnapshot.getChildren()) {
                            weekly = child.getValue(MyAvailabilityWeekly.class);
                            weekList.add(weekly);
                            if(weekly != null) {
                                userDatabase.addAvailabilityWeekly(weekly.getAvailability_dayOfTheWeek(), weekly.getAvailability_endTime(), weekly.getAvailability_key(), weekly.getAvailability_monthOf(), weekly.getAvailability_startTime());
                                weekDays += weekly.getAvailability_dayOfTheWeek().toLowerCase();
                            }
                        }

                        weeklyAdapter.notifyDataSetChanged();
                    } catch(Exception e) {
                        Toast.makeText(getActivity(), "Error" +e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        calendar.set(Calendar.YEAR, i);
        calendar.set(Calendar.MONTH, (i1+1));
        calendar.set(Calendar.DAY_OF_MONTH, i2);
        day = calendar.get(Calendar.DAY_OF_WEEK);

        switch(day) {
            case Calendar.SUNDAY:
                d = "Sunday";
                break;
            case Calendar.MONDAY:
                d = "Monday";
                break;
            case Calendar.TUESDAY:
                d = "Tuesday";
                break;
            case Calendar.WEDNESDAY:
                d = "Wednesday";
                break;
            case Calendar.THURSDAY:
                d = "Thursday";
                break;
            case Calendar.FRIDAY:
                d = "Friday";
                break;
            case Calendar.SATURDAY:
                d = "Saturday";
                break;
        }
        updateLabel();
    }

    private void updateLabel() {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
        booking_date = sdf.format(calendar.getTime());
        String cDate = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH).format(new Date());
        Date currentDate = null;
        Date bookDate = null;

        try {
            currentDate = sdf.parse(cDate);
            bookDate = sdf.parse(booking_date);
        } catch(Exception e) {
            Toast.makeText(getActivity(), "Error" +e.getMessage(), Toast.LENGTH_LONG).show();
        }

        if(bookDate != null && bookDate.after(currentDate)) {
            if(!weekDays.toLowerCase().contains(d.toLowerCase())) {
                LaundryBasketBulkyActivity.start(getActivity(), new Booking(booking_date,
                        "", "", booking_time, laundWorker_fn, laundWorker_fbid,
                        laundWorker_ln, laundWorker_mn, laundWorker_pic, laundSeeker_fbid, "", "0"));
            } else {
                Toast.makeText(getContext(), "Dates with weekly schedules are not allowed", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getContext(), "Past dates are not allowed", Toast.LENGTH_LONG).show();
        }
    }

                            /*LaundryBasketActivity.start(getActivity(), new Booking(booking_date,
                                    booking_id, "", booking_time, laundWorker_fn, laundWorker_fbid,
                                    laundWorker_ln, laundWorker_mn, laundWorker_pic, laundSeeker_fbid, "", "0"));*/



    /////////////
        /*Toast.makeText(getContext(), "currentdate: " +currentDate, Toast.LENGTH_LONG).show();
        Toast.makeText(getContext(), "bookDate: " +bookDate, Toast.LENGTH_LONG).show();
        Toast.makeText(getContext(), "cDate: " +cDate, Toast.LENGTH_LONG).show();
        Toast.makeText(getContext(), "booking_date: " +booking_date, Toast.LENGTH_LONG).show();
        Toast.makeText(getContext(), "weekdays: " +weekDays.toLowerCase(), Toast.LENGTH_LONG).show();
        Toast.makeText(getContext(), "d: " +d.toLowerCase(), Toast.LENGTH_LONG).show();*/
    /////////////
}