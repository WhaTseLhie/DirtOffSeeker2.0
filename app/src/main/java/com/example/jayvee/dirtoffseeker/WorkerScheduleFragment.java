package com.example.jayvee.dirtoffseeker;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class WorkerScheduleFragment extends Fragment {

    UserDatabase userDatabase;
    DatabaseReference mDatabase;
    ArrayList<Worker> workerList = new ArrayList<>();
    //ArrayList<Booking> bookingList = new ArrayList<>();
    ArrayList<HistoryBooking> historyList = new ArrayList<>();
    ListView lvWeekly, lvMonthly;
    ArrayList<MyAvailabilityMonthly> monthList = new ArrayList<>();
    ArrayList<MyAvailabilityWeekly> weekList = new ArrayList<>();
    MyAvailabilityMonthlyAdapter monthlyAdapter;
    MyAvailabilityWeeklyAdapter weeklyAdapter;
    String booking_date, booking_time, laundWorker_fn, laundWorker_fbid, laundWorker_ln, laundWorker_mn, laundWorker_pic, laundSeeker_fbid, booking_service = "";

    public WorkerScheduleFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_worker_schedule, container, false);

        userDatabase = new UserDatabase(getActivity());
        //bookingList = userDatabase.getAllBooking();
        historyList = userDatabase.getAllHistoryBooking();

        lvMonthly = view.findViewById(R.id.listView);
        monthlyAdapter = new MyAvailabilityMonthlyAdapter(getActivity(), monthList);
        lvMonthly.setAdapter(monthlyAdapter);

        lvWeekly = view.findViewById(R.id.listView1);
        weeklyAdapter = new MyAvailabilityWeeklyAdapter(getActivity(), weekList);
        lvWeekly.setAdapter(weeklyAdapter);

        lvMonthly.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
            }
        });

        lvWeekly.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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
                dialog.show();
            }
        });

        userDatabase = new UserDatabase(getActivity());
        workerList = userDatabase.getAllWorker();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("laundrySchedule").child(workerList.get(0).getLaundWorker_fbid()).child("modeMonthly");
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
        });

        mDatabase = FirebaseDatabase.getInstance().getReference().child("laundrySchedule").child(workerList.get(0).getLaundWorker_fbid()).child("modeWeekly");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    try {
                        MyAvailabilityWeekly weekly;
                        weekList.clear();
                        userDatabase.deleteAllAvailabilityMonthly();

                        for(DataSnapshot child : dataSnapshot.getChildren()) {
                            weekly = child.getValue(MyAvailabilityWeekly.class);
                            weekList.add(weekly);
                            if(weekly != null) {
                                userDatabase.addAvailabilityWeekly(weekly.getAvailability_dayOfTheWeek(), weekly.getAvailability_endTime(), weekly.getAvailability_key(), weekly.getAvailability_monthOf(), weekly.getAvailability_startTime());
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

    private void showBookingInformation() {
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
    }
}