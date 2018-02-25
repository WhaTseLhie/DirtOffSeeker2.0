package com.example.jayvee.dirtoffseeker;


import android.app.DatePickerDialog;
import android.app.Dialog;
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
    //ArrayList<Booking> bookingList = new ArrayList<>();
    ArrayList<HistoryBooking> historyList = new ArrayList<>();
    ListView lvWeekly, lvMonthly;
    //ArrayList<MyAvailabilityMonthly> monthList = new ArrayList<>();
    ArrayList<MyAvailabilityWeekly> weekList = new ArrayList<>();
    //MyAvailabilityMonthlyAdapter monthlyAdapter;
    MyAvailabilityWeeklyAdapter weeklyAdapter;
    TextView txtBulk;
    String booking_id, booking_date, booking_time, laundWorker_fn, laundWorker_fbid, laundWorker_ln, laundWorker_mn, laundWorker_pic, laundSeeker_fbid, booking_service = "";
    Calendar calendar;
    //String bday = "";
    String weekDays = "";
    int day = 10;
    private String d = "";

    public WorkerScheduleFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_worker_schedule, container, false);

        userDatabase = new UserDatabase(getActivity());
        //bookingList = userDatabase.getAllBooking();
        historyList = userDatabase.getAllHistoryBooking();
        txtBulk = view.findViewById(R.id.textView);

        calendar = Calendar.getInstance();
        txtBulk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(getActivity(), WorkerScheduleFragment.this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                dialog.show();

                ArrayList<User> userList = userDatabase.getAllUser();
                //booking_date = bday;
                booking_time = "12am-11:59pm";
                laundWorker_fn = workerList.get(0).getLaundWorker_fn();
                laundWorker_fbid = workerList.get(0).getLaundWorker_fbid();
                laundWorker_ln = workerList.get(0).getLaundWorker_ln();
                laundWorker_mn = workerList.get(0).getLaundWorker_mn();
                laundWorker_pic = workerList.get(0).getLaundWorker_pic();
                laundSeeker_fbid = userList.get(0).getLaundSeeker_fbid();
            }
        });

        //lvMonthly = view.findViewById(R.id.listView);
        //monthlyAdapter = new MyAvailabilityMonthlyAdapter(getActivity(), monthList);
        //lvMonthly.setAdapter(monthlyAdapter);

        lvWeekly = view.findViewById(R.id.listView1);
        weeklyAdapter = new MyAvailabilityWeeklyAdapter(getActivity(), weekList);
        lvWeekly.setAdapter(weeklyAdapter);

        /*lvMonthly.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ArrayList<User> userList = userDatabase.getAllUser();
                booking_date = monthList.get(i).getAvailability_date();
                booking_time = monthList.get(i).getAvailability_startTime() + " - " + monthList.get(i).getAvailability_endTime();
                laundWorker_fn = workerList.get(i).getLaundWorker_fn();
                laundWorker_fbid = workerList.get(i).getLaundWorker_fbid();
                laundWorker_ln = workerList.get(i).getLaundWorker_ln();
                laundWorker_mn = workerList.get(i).getLaundWorker_mn();
                laundWorker_pic = workerList.get(i).getLaundWorker_pic();
                laundSeeker_fbid = userList.get(i).getLaundSeeker_fbid();


                ////////////////////////////////////////////////////////
                final Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.dialog_select_service);

                TextView txtWash = dialog.findViewById(R.id.textView);
                TextView txtFold = dialog.findViewById(R.id.textView1);
                TextView txtIron = dialog.findViewById(R.id.textView2);

                txtWash.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        booking_service = "Wash";
                        dialog.dismiss();
                        showBookingInformation();
                    }
                });

                txtFold.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        booking_service = "Fold";
                        dialog.dismiss();
                        showBookingInformation();
                    }
                });

                txtIron.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        booking_service = "Iron";
                        dialog.dismiss();
                        showBookingInformation();
                    }
                });

                dialog.show();
                ////////////////////////////////////////////////////////
            }
        });*/

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

                LaundryBasketActivity.start(getContext(), new Booking(booking_date,
                        booking_id, "", booking_time, laundWorker_fn, laundWorker_fbid,
                        laundWorker_ln, laundWorker_mn, laundWorker_pic, laundSeeker_fbid, "", "0"));
                /*AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Confirmation");
                builder.setMessage("Are you sure you want to book this worker's schedule?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getContext(), "Booking Successful", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getContext(), "Booking Canceled", Toast.LENGTH_SHORT).show();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();*/
            }
        });

        /*mDatabase = FirebaseDatabase.getInstance().getReference().child("laundrySchedule").child(workerList.get(0).getLaundWorker_fbid()).child("modeMonthly");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    try {
                        MyAvailabilityMonthly monthly;
                        monthList.clear();
                        userDatabase.deleteAllAvailabilityMonthly();

                        for(DataSnapshot child : dataSnapshot.getChildren()) {
                            monthly = child.getValue(MyAvailabilityMonthly.class);
                            monthList.add(monthly);
                            if(monthly != null) {
                                userDatabase.addAvailabilityMonthly(monthly.getAvailability_endTime(), monthly.getAvailability_key(), monthly.getAvailability_date(), monthly.getAvailability_startMonth(), monthly.getAvailability_startTime());
                            }
                        }

                        monthlyAdapter.notifyDataSetChanged();
                    } catch(Exception e) {
                        Toast.makeText(getActivity(), "Error" +e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/

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

        /////////////
        /*Toast.makeText(getContext(), "currentdate: " +currentDate, Toast.LENGTH_LONG).show();
        Toast.makeText(getContext(), "bookDate: " +bookDate, Toast.LENGTH_LONG).show();
        Toast.makeText(getContext(), "cDate: " +cDate, Toast.LENGTH_LONG).show();
        Toast.makeText(getContext(), "booking_date: " +booking_date, Toast.LENGTH_LONG).show();
        Toast.makeText(getContext(), "weekdays: " +weekDays.toLowerCase(), Toast.LENGTH_LONG).show();
        Toast.makeText(getContext(), "d: " +d.toLowerCase(), Toast.LENGTH_LONG).show();*/
        /////////////

        if(bookDate != null && bookDate.after(currentDate)) {
            if(!weekDays.toLowerCase().contains(d.toLowerCase())) {
                LaundryBasketBulkyActivity.start(getContext(), new Booking(booking_date,
                        "", "", booking_time, laundWorker_fn, laundWorker_fbid,
                        laundWorker_ln, laundWorker_mn, laundWorker_pic, laundSeeker_fbid, "", "0"));
            } else {
                Toast.makeText(getContext(), "Dates with weekly schedules are not allowed", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getContext(), "Past dates are not allowed", Toast.LENGTH_LONG).show();
        }
    }

    /*private void showBookingInformation() {
        Toast.makeText(getContext(), booking_service, Toast.LENGTH_LONG).show();
        if(!booking_service.equals("")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Confirmation");
            builder.setMessage("Are you sure you want to book this worker's schedule?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("bookingList");
                    HashMap<String, String> hashMap = new HashMap<>();

                    String key = mDatabase.push().getKey();
                    hashMap.put("booking_date", booking_date);
                    hashMap.put("booking_id", key);
                    hashMap.put("booking_status", "Pending");
                    hashMap.put("booking_time", booking_time);
                    hashMap.put("laundWorker_fn", laundWorker_fn);
                    hashMap.put("laundWorker_fbid", laundWorker_fbid);
                    hashMap.put("laundWorker_ln", laundWorker_ln);
                    hashMap.put("laundWorker_mn", laundWorker_mn);
                    hashMap.put("laundWorker_pic", laundWorker_pic);
                    hashMap.put("laundSeeker_fbid", laundSeeker_fbid);
                    hashMap.put("booking_service", booking_service);
                    hashMap.put("booking_fee", "0.0");
                    mDatabase.child(key).setValue(hashMap);

                    userDatabase.deleteAllBooking();
                    userDatabase.addBooking(booking_date, key, "Pending", booking_time, laundWorker_fn, laundWorker_fbid, laundWorker_ln, laundWorker_mn, laundWorker_pic, laundSeeker_fbid, booking_service, "0.0");

                    ///////////
                    DatabaseReference mDb = FirebaseDatabase.getInstance().getReference().child("bookingListHistory");
                    HashMap<String, String> hash = new HashMap<>();

                    String key1 = mDb.push().getKey();
                    hash.put("booking_date", booking_date);
                    hash.put("booking_id", key1);
                    hash.put("booking_status", "Pending");
                    hash.put("booking_time", booking_time);
                    hash.put("laundWorker_fn", laundWorker_fn);
                    hash.put("laundWorker_fbid", laundWorker_fbid);
                    hash.put("laundWorker_ln", laundWorker_ln);
                    hash.put("laundWorker_mn", laundWorker_mn);
                    hash.put("laundWorker_pic", laundWorker_pic);
                    hash.put("laundSeeker_fbid", laundSeeker_fbid);
                    hash.put("booking_service", booking_service);
                    hashMap.put("booking_fee", "0.0");
                    mDb.child(key1).setValue(hash);

                    userDatabase.addHistoryBooking(booking_date, key1, "Pending", booking_time, laundWorker_fn, laundWorker_fbid, laundWorker_ln, laundWorker_mn, laundWorker_pic, laundSeeker_fbid, booking_service, "0.0");
                    Toast.makeText(getContext(), "Booking Successful", Toast.LENGTH_SHORT).show();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText(getContext(), "Booking Canceled", Toast.LENGTH_SHORT).show();
                }
            });

            AlertDialog dialog1 = builder.create();
            dialog1.show();
        }
    }*/
}