package com.example.jayvee.dirtoffseeker;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BookingFragment extends Fragment {

    ListView lv;
    BookingListAdapter adapter;
    ArrayList<Booking> bookingList = new ArrayList<>();
    ArrayList<Worker> workerList = new ArrayList<>();
    UserDatabase userDatabase;
    SwipeRefreshLayout refreshLayout;
    private Context mContext;

    public BookingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_booking, container, false);

        getActivity().setTitle("Booking List");
        mContext = getActivity();
        userDatabase = new UserDatabase(mContext);
        bookingList = userDatabase.getAllBooking();

        refreshLayout = view.findViewById(R.id.refreshLayout);
        lv = view.findViewById(R.id.listView);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshItems();
                refreshLayout.setRefreshing(false);
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent bookingIntent = new Intent(getActivity(), BookingInformationActivity.class);
                bookingIntent.putExtra("index", i);
                bookingIntent.putExtra("booking", bookingList.get(i));
                startActivity(bookingIntent);
            }
        });

        return view;
    }

    private void refreshItems() {
        onStart();

        refreshLayout.setRefreshing(false);
    }

    @Override
    public void onStart() {
        super.onStart();

        workerList = userDatabase.getAllWorker();
        if(!workerList.isEmpty()) {
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("bookingList").child(workerList.get(0).getLaundWorker_fbid()).child("weeklyBook");
            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        bookingList.clear();

                        try {
                            Booking booking;
                            userDatabase.deleteAllBooking();

                            for (DataSnapshot child : dataSnapshot.getChildren()) {
                                booking = child.getValue(Booking.class);

                                if (booking != null) {
                                    bookingList.add(booking);
                                    userDatabase.addBooking(booking.getBooking_date(), booking.getBooking_id(), booking.getBooking_status(), booking.getBooking_time(), booking.getLaundWorker_fn(), booking.getLaundWorker_fbid(), booking.getLaundWorker_ln(), booking.getLaundWorker_mn(), booking.getLaundWorker_pic(), booking.getLaundSeeker_fbid(), booking.getBooking_service(), booking.getBooking_fee());
                                }
                            }

                            adapter = new BookingListAdapter(mContext, bookingList);
                            lv.setAdapter(adapter);
                        } catch (Exception e) {
                            Toast.makeText(mContext, "Error ", Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }
}